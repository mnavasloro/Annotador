/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.tagger.rulemanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 *
 * @author mnavas
 */
public class luceneTest {

    private static final Logger logger = Logger.getLogger(luceneTest.class.getName());

    public luceneTest() {

    }

    public static void main(String[] args) {
        try {
            Directory memoryIndex = new RAMDirectory();
            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            IndexWriter writter = new IndexWriter(memoryIndex, indexWriterConfig);

            Document document1 = new Document();
            String name = "name";
            String id = "id";
            String comment = "com";
            String tag = "tag";
            String ruleT = "ruleType";
            String match = "matchWithResults";
            String pattern = "pattern";
            String action = "action";
            String result = "result";
            String stage = "stage";
            String priority = "priority";

            document1.add(new TextField("comment", comment, Field.Store.YES));
            document1.add(new TextField("name", name, Field.Store.YES));
            document1.add(new TextField("id", id, Field.Store.YES));
            document1.add(new TextField("tag", tag, Field.Store.YES));
            document1.add(new TextField("ruleType", ruleT, Field.Store.YES));
            document1.add(new TextField("matchWithResults", match, Field.Store.YES));
            document1.add(new TextField("pattern", pattern, Field.Store.YES));
            document1.add(new TextField("action", action, Field.Store.YES));
            document1.add(new TextField("result", result, Field.Store.YES));
            document1.add(new TextField("stage", stage, Field.Store.YES));
            document1.add(new TextField("priority", priority, Field.Store.YES));

            writter.addDocument(document1);

            Document document = new Document();
            String name0 = "name0";
            String id0 = "id0";
            String comment0 = "com0";
            String tag0 = "tag0";
            String ruleT0 = "ruleType0";
            String match0 = "matchWithResults0";
            String pattern0 = "pattern0";
            String action0 = "action0";
            String result0 = "result0";
            String stage0 = "stage0";
            String priority0 = "priority0";

            document.add(new TextField("comment", comment0, Field.Store.YES));
            document.add(new TextField("name", name0, Field.Store.YES));
            document.add(new TextField("id", id0, Field.Store.YES));
            document.add(new TextField("tag", tag0, Field.Store.YES));
            document.add(new TextField("ruleType", ruleT0, Field.Store.YES));
            document.add(new TextField("matchWithResults", match0, Field.Store.YES));
            document.add(new TextField("pattern", pattern0, Field.Store.YES));
            document.add(new TextField("action", action0, Field.Store.YES));
            document.add(new TextField("result", result0, Field.Store.YES));
            document.add(new TextField("stage", stage0, Field.Store.YES));
            document.add(new TextField("priority", priority0, Field.Store.YES));

            writter.addDocument(document);
            writter.close();

            /* Search */
            
            String inField = "id";
            String queryString = "id0";

            Query query = new QueryParser(inField, analyzer).parse(queryString);

            IndexReader indexReader = DirectoryReader.open(memoryIndex);
            IndexSearcher searcher = new IndexSearcher(indexReader);
            TopDocs topDocs = searcher.search(query, 10);
            List<Document> documents = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                documents.add(searcher.doc(scoreDoc.doc));
            }
            
            System.out.println(documents.get(0).toString());

        } catch (Exception ex) {
            Logger.getLogger(luceneTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
