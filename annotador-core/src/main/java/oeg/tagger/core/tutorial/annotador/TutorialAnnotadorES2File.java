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
import oeg.tagger.core.time.tictag.Annotador;
import oeg.tagger.core.time.tictag.TicTag;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author mnavas
 */
public class TutorialAnnotadorES2File {

    /**
     * Annotation of the files (in Spanish, just training) from TempEval3
     */
    public static void main(String[] args) {

         FileOutputStream fos = null;
        try {
            File finput = new File("C:\\Users\\mnavas\\Desktop\\CENDOJ\\XML\\");
            String foutput = "C:\\Users\\mnavas\\Desktop\\CENDOJ\\output\\";
//            String foutputHTML = "../annotador-core/src/main/resources/rules/output.html";
            File[] listF = finput.listFiles();
            Annotador tt = new Annotador("ES");
            for (File f : listF){
                    String txt = FileUtils.readFileToString(f, "UTF-8");
                    String date = f.getName().substring(10, 14);
                    String outp = tt.annotate(txt,date);
                    System.out.println(outp);
        
        outp = "<?xml version=\"1.0\" ?>\n" +
"<TimeML xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://timeml.org/timeMLdocs/TimeML_1.2.1.xsd\">\n" +
"\n" +
                                                "\n" +
"\n" +
"<TEXT>" + outp + "</TEXT>\n" +
"\n" +
"</TimeML>";
                    FileOutputStream fos1 = new FileOutputStream(foutput + f.getName());
                    OutputStreamWriter w = new OutputStreamWriter(fos1, "UTF-8");
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
