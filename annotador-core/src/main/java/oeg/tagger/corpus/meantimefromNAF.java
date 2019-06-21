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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author mnavas
 */
public class meantimefromNAF {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
//            File finput = new File("C:\\Users\\mnavas\\DATA\\meantime_newsreader_spanish_nov15\\intra-doc_annotation\\");
            File finput = new File("C:\\Users\\mnavas\\DATA\\meantime_newsreader_spanish_raw_NAF\\");
            String foutput = "C:\\Users\\mnavas\\DATA\\meantime_newsreader_spanish_nov15\\plainOutput\\";
//            String foutputA = "C:\\Users\\mnavas\\DATA\\meantime_newsreader_spanish_nov15\\annotatedOutput\\";
            File[] listFolders = finput.listFiles();
            
            String output = "";
            Pattern pText = Pattern.compile("<raw><!\\[CDATA\\[([\\w\\W]*)\\]\\]><\\/raw>");
            
            String total = "";
            for (File fold : listFolders){
                File dir = new File(foutput + fold.getName());
                dir.mkdir();
                File[] listF = fold.listFiles();
            for (File f : listF){
                try {
                    String txt = FileUtils.readFileToString(f, "UTF-8");
                    Matcher mText = pText.matcher(txt);
                    if (mText.find()) {
                         output = mText.group(1);
                    
                    FileOutputStream fos1 = new FileOutputStream(dir.getCanonicalPath() + "\\" + f.getName().replaceFirst("\\.naf", "\\.txt"));
                    OutputStreamWriter w = new OutputStreamWriter(fos1, "UTF-8");
                    BufferedWriter bw = new BufferedWriter(w);
                    bw.write(output);
                    bw.flush();
                    bw.close();
                    } else{
                        System.out.println("Error in file " + f.getName() + " from corpus " + fold );
                        System.out.println("\t text " + txt );
                        System.out.println("----------------");
                    }
                    
                } catch (IOException ex) {
                    Logger.getLogger(meantimefromNAF.class.getName()).log(Level.SEVERE, null, ex);
                }
            }}
        
        } catch (Exception ex) {
            Logger.getLogger(meantimefromNAF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
