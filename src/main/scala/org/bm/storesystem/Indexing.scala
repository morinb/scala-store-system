package org.bm.storesystem

import java.io.File
import java.nio.file.FileSystems

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.{Document, Field, TextField}
import org.apache.lucene.index.{IndexNotFoundException, DirectoryReader, IndexWriter, IndexWriterConfig}
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.{IndexSearcher, Query, ScoreDoc, TopScoreDocCollector}
import org.apache.lucene.store.{Directory, FSDirectory}
import org.apache.lucene.util.Version.LUCENE_4_9

/**
 * .
 * @author Baptiste Morin
 */
trait Indexing extends Config{
  val analyzer: StandardAnalyzer = new StandardAnalyzer(LUCENE_4_9)
  val indexConfig: IndexWriterConfig = new IndexWriterConfig(LUCENE_4_9, analyzer)
  val indexDirectory: Directory = FSDirectory.open(new File(ensureDirectoryExists(indexPath).toString))

  def index(filename: String, filepath: String) = {
    if(!isAlreadyIndexed(filename)) {
      val doc: Document = new Document()
      doc.add(new TextField("filename", filename, Field.Store.YES))
      doc.add(new TextField("filepath", filepath, Field.Store.YES))

      val writer = new IndexWriter(indexDirectory, indexConfig)

      writer.addDocument(doc)

      writer.close()
    }
  }

  /**
   * This method check if the filename is already indexed.
   * @param filename the filename to check
   * @return
   */
  private[this] def isAlreadyIndexed(filename: String): Boolean =
  try {
    search(filename).length == 1
  } catch {
    // No index yet, so no file indexed :)
    case e: IndexNotFoundException => false
  }


  val HITS_PER_PAGE: Int = 10

  def search(queryString: String): Array[ScoreDoc] = {
    require(queryString.length > 0, "Query must not be empty")

    val query: Query = new QueryParser(LUCENE_4_9, "filename", analyzer).parse(queryString)

    val collector: TopScoreDocCollector = TopScoreDocCollector.create(HITS_PER_PAGE, true)
    searcher.search(query, collector)

    collector.topDocs().scoreDocs
  }

  lazy val reader = DirectoryReader.open(indexDirectory)

  lazy val searcher = new IndexSearcher(reader)


}
