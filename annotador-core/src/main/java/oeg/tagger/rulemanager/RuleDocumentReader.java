/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.tagger.rulemanager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 *
 * @author mnavas
 */
public class RuleDocumentReader {
    
    static String lan = "ES";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            File inputFile;
            Directory memoryIndex = new RAMDirectory();
            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            IndexWriter writter = new IndexWriter(memoryIndex, indexWriterConfig);
            
            
            File header = new File("../annotador-core/src/main/resources/rules/rules" + lan + "-header.txt");
            String outp = FileUtils.readFileToString(header, "UTF-8");
            
            
//            if(args != null){
//                inputFile = new File(args[0]);
//            }
//            else{
                inputFile = new File("../annotador-core/src/main/resources/rules/rules" + lan + "-rules.txt");
//            }
            String test = FileUtils.readFileToString(inputFile, "UTF-8");
            ReaderCoreNLP rcnlp = new ReaderCoreNLP();
            rcnlp.reader(test);
            
            Document document1 = new Document();
            for(RuleCoreNLP rul : rcnlp.lista){
                System.out.println("\n+++++++++\n" + rul.toString() + "\n+++++++++\n");
                outp = outp + rul.toString();
                document1 = rul.toLuceneDoc();
                writter.addDocument(document1);                
            }
            
            // lucene generation
            
            writter.close();
            
            // TXT generation
            FileOutputStream fos = new FileOutputStream("../annotador-core/src/main/resources/rules/rules" + lan + "-test.txt");
            OutputStreamWriter w = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(w);
            bw.write(outp);
            bw.flush();
            bw.close();
            
        } catch (IOException ex) {
            Logger.getLogger(RuleDocumentReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
