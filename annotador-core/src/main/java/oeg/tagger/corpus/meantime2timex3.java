/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.tagger.corpus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import oeg.tagger.core.tutorial.annotador.TutorialAnnotadorSyntES;
import static oeg.tagger.corpus.curlSUTIME.callCURL;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author mnavas
 */
public class meantime2timex3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        try {
////            File finput = new File("C:\\Users\\mnavas\\DATA\\meantime_newsreader_spanish_nov15\\intra-doc_annotation\\");
//            File finput = new File("C:\\Users\\mnavas\\DATA\\meantime_newsreader_spanish_nov15\\intra-doc_annotation\\");
//            String foutputP = "C:\\Users\\mnavas\\DATA\\meantime_newsreader_spanish_nov15\\plainOutput\\";
//            String foutputA = "C:\\Users\\mnavas\\DATA\\meantime_newsreader_spanish_nov15\\annotatedOutput\\";
//            File[] listFolders = finput.listFiles();
//            
//            String total = "";
//            for (File fold : listFolders){
//                File[] listF = fold.listFiles();
//            for (File f : listF){
//                try {
//                    String txt = FileUtils.readFileToString(f, "UTF-8");
////                    System.out.println("\n************************");
////                    System.out.println(txt + "\n");
//                    String outp = callCURL(txt, (new File(foutput + f.getName())).getCanonicalPath());
////                    System.out.println(outp);
//                    
//                    total = total + outp;
//                    
//                    FileOutputStream fos1 = new FileOutputStream(foutput + f.getName().replaceFirst("\\.txt", "_sol\\.txt"));
//                    OutputStreamWriter w = new OutputStreamWriter(fos1, "UTF-8");
//                    BufferedWriter bw = new BufferedWriter(w);
//                    bw.write(outp);
//                    bw.flush();
//                    bw.close();
//                    
//                } catch (IOException ex) {
//                    Logger.getLogger(TutorialAnnotadorSyntES.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }}
//        
//        } catch (Exception ex) {
//            Logger.getLogger(TutorialAnnotadorSyntES.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
}
