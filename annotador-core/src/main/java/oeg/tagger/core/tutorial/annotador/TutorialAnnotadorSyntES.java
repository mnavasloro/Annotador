/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.tagger.core.tutorial.annotador;

import edu.stanford.nlp.pipeline.Annotation;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import oeg.tagger.core.time.tictag.Annotador;
import oeg.tagger.core.time.tictag.TicTag;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author mnavas
 */
public class TutorialAnnotadorSyntES {

    /**
     * Annotation of the files (in Spanish, just training) from TempEval3
     */
    public static void main(String[] args) { 
        File finput = new File("../annotador-core/src/main/resources/rules/test_input/");
        String foutput = "../annotador-core/src/main/resources/rules/test_output/";
        File[] listF = finput.listFiles();
        Annotador tt = new Annotador("ES");
        for (File f : listF){
            try {
            String txt = FileUtils.readFileToString(f, "UTF-8");
            System.out.println("\n************************");
            System.out.println(txt + "\n");
        String outp = tt.annotate(txt,"2019-12-20");
        System.out.println(outp);
        
        FileOutputStream fos = new FileOutputStream(foutput + f.getName().replaceFirst("\\.txt", "_sol\\.txt"));
            OutputStreamWriter w = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(w);
            bw.write(outp);
            bw.flush();
            bw.close();
            
        } catch (IOException ex) {
            Logger.getLogger(TutorialAnnotadorSyntES.class.getName()).log(Level.SEVERE, null, ex);
        }
        }

    }

}
