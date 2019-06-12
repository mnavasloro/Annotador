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
public class TutorialAnnotadorESTXT {

    /**
     * Annotation of the files (in Spanish, just training) from TempEval3
     */
    public static void main(String[] args) { 
        File f = new File("../annotador-core/src/main/resources/rules/TEST.txt");
        File foutput = new File("../annotador-core/src/main/resources/rules/OUT-TEST.txt");
        String s;
        String total = "";
        try {
            s = FileUtils.readFileToString(f, "UTF-8");
            String[] lines = s.split(System.getProperty("line.separator"));
        Annotador tt = new Annotador("ES");
        for (String txtf : lines) {
            System.out.println("\n************************");
            String txt = txtf;
            if (txtf.length() > 1) {
                String[] aux = txtf.split("\t");
                txt = aux[0];
            }
            System.out.println(txt + "\n");
        String outp = tt.annotate(txt,"2019-12-20");
        System.out.println(outp);
        total = total + outp + "\n";
        
        }
        
        FileOutputStream fos = new FileOutputStream(foutput.getAbsolutePath());
            OutputStreamWriter w = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(w);
            bw.write(total);
            bw.flush();
            bw.close();
            
        } catch (IOException ex) {
            Logger.getLogger(TutorialAnnotadorESTXT.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
