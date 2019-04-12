/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.tagger.rulemanager;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 *
 * @author mnavas
 */
public class RuleCoreNLP {

    String name = null;
    String id = null;
    String comment = null;
    String tag = null;
    String ruleT = null;
    String match = null;
    String pattern = null;
    String action = null;
    String result = null;
    String stage = null;
    String priority = null;

    @Override
    public String toString() {
        String out = "#####################\n";
        if(id!=null){
            out = out + "# id - " + id + "\n";
        }
        if(name!=null){
            out = out + "# name - " + name + "\n";
        }
        if(name!=null){
            out = out + "# tag - " + tag + "\n";
        }
        if(comment!=null){
            out = out + "# other comments \n" + comment;
        }
        out = out + "#####################\n{";
        
        
        if(ruleT!=null){
            out = out + "\n\t RuleType :" + ruleT;
        }
        if(priority!=null){
            out = out + ",\n\t priority :" + priority;
        }
        if(match!=null){
            out = out + ",\n\t matchedExpressionGroup :" + match;
        }
        if(ruleT!=null){
            out = out + ",\n\t pattern :" + pattern;
        }
        if(ruleT!=null){
            out = out + ",\n\t action :" + action;
        }
        if(ruleT!=null){
            out = out + ",\n\t result :" + result;
        }
        if(stage!=null){
            out = out + ",\n\t stage :" + stage;
        }
        
        return out + "\n}\n\n";
        
    }
    
    
    public Document toLuceneDoc() {
        Document document1 = null;
        try {
            

            document1 = new Document();
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

            
        } catch (Exception ex) {
            Logger.getLogger(RuleCoreNLP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            return document1;
    }

    
    
    
    
}
