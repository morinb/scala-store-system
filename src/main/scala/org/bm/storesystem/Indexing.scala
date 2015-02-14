package org.bm.storesystem

import java.io.File

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.{Document, Field, TextField}
import org.apache.lucene.index.{DirectoryReader, IndexWriter, IndexWriterConfig}
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.{IndexSearcher, Query, ScoreDoc, TopScoreDocCollector}
import org.apache.lucene.store.{Directory, FSDirectory}
import org.apache.lucene.util.Version.LUCENE_4_9

import scala.io.Source

/**
 * .
 * @author Baptiste Morin
 */
trait Indexing {
  val analyzer: StandardAnalyzer = new StandardAnalyzer(LUCENE_4_9)
  val config: IndexWriterConfig = new IndexWriterConfig(LUCENE_4_9, analyzer)
  val indexDirectory: Directory = FSDirectory.open(new File("."))

  def index(filename: String) = {
    val doc: Document = new Document()
    doc.add(new TextField("filename", filename, Field.Store.YES))

    val writer = new IndexWriter(indexDirectory, config)

    writer.addDocument(doc)

    writer.close()

  }

  val HITS_PER_PAGE: Int = 10

  def search(queryString: String): Array[ScoreDoc] = {
    require(queryString.length > 0, "Query must not be empty")

    val query: Query = new QueryParser(LUCENE_4_9, "filename", analyzer).parse(queryString)
    val reader: DirectoryReader = DirectoryReader.open(indexDirectory)
    val searcher: IndexSearcher = new IndexSearcher(reader)
    val collector: TopScoreDocCollector = TopScoreDocCollector.create(HITS_PER_PAGE, true)
    searcher.search(query, collector)

    collector.topDocs().scoreDocs
  }


}
