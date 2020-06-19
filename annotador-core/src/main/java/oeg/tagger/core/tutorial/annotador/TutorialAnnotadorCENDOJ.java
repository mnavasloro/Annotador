/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.tagger.core.tutorial.annotador;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import oeg.tagger.core.data.FileCENDOJ;
import oeg.tagger.core.time.tictag.Annotador;
import oeg.tagger.core.time.tictag.TicTag;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author mnavas
 */
public class TutorialAnnotadorCENDOJ {

    /**
     * Annotation of the files from CENDOJ
     */
    public static void main(String[] args) {

         FileOutputStream fos = null;
        try {
//            File finput = new File("C:\\Users\\mnavas\\Nextcloud\\OTROS\\COLABORACIONES\\CENDOJ\\FASE_1\\XML-originales");
//            String foutput = "C:\\Users\\mnavas\\Nextcloud\\OTROS\\COLABORACIONES\\CENDOJ\\FASE_1\\output\\";
            File finput = new File("C:\\Users\\mnavas\\Nextcloud\\OTROS\\COLABORACIONES\\CENDOJ\\FASE_2\\SentenciasPilotoL2");
            String foutput = "C:\\Users\\mnavas\\Nextcloud\\OTROS\\COLABORACIONES\\CENDOJ\\FASE_2\\output\\";
//            String foutputHTML = "../annotador-core/src/main/resources/rules/output.html";
            File[] listF = finput.listFiles();
            Annotador tt = new Annotador("ES");
            for (File f : listF){
                    FileCENDOJ fc = new FileCENDOJ(f);
                    String cuerpo = fc.getCuerpo();
                    String cuerpoClean = fc.getCuerpoClean();
                
                    String date = f.getName().substring(10, 14);
                    String outp = tt.annotate(cuerpoClean,date);

                    System.out.println(outp);
                    
                    fc.cuerpoAnnotated = outp;
                    
                    outp = fc.mergeXML();

                    FileOutputStream fos1 = new FileOutputStream(foutput + f.getName());
                    OutputStreamWriter w = new OutputStreamWriter(fos1, "ISO-8859-1");
                    BufferedWriter bw = new BufferedWriter(w);
                    bw.write(outp);
                    bw.flush();
                    bw.close();
            }
                    
                } catch (IOException ex) {
                    Logger.getLogger(TutorialAnnotadorTempEval2.class.getName()).log(Level.SEVERE, null, ex);
                }

    }

}
