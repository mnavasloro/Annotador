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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import oeg.tagger.core.time.tictag.AnnotadorStandard;
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
            String foutputHTML = "../annotador-core/src/main/resources/rules/output.html";
        String s;
        String total = "";
        try {
            s = FileUtils.readFileToString(f, "UTF-8");
            String[] lines = s.split(System.getProperty("line.separator"));
        AnnotadorStandard tt = new AnnotadorStandard("ES");
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
        try {    
            String htmlS = createHighlights(total);
            FileOutputStream fos2 = new FileOutputStream(foutputHTML);
            OutputStreamWriter w2 = new OutputStreamWriter(fos2, "UTF-8");
            BufferedWriter bw2 = new BufferedWriter(w2);
            bw2.write(htmlS);
            bw2.flush();
            bw2.close();
        } catch (Exception ex) {
            Logger.getLogger(TutorialAnnotadorSyntES.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }}

    
    
     static public String createHighlights(String input2) {
//        input2 = input2.replaceFirst(Pattern.quote("<?xml version=\"1.0\"?>\n" + "<!DOCTYPE TimeML SYSTEM \"TimeML.dtd\">\n" + "<TimeML>"), "");
        input2 = input2.replaceFirst(Pattern.quote("</TimeML>"), "");
        input2 = input2.replaceAll("</TIMEX3>", "</span>");
        input2 = input2.replaceAll("\\r?\\n", "<br>");

        String pattern = "(<TIMEX3 ([^>]*)>)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(input2);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String color = "#7fa2ff";//"Orange";
//            String color = "rgba(255, 165, 0, 0.5)";//"Orange";
            String contetRegex = m.group(2);
            contetRegex = contetRegex.replaceAll("\"", "");
            contetRegex = contetRegex.replaceAll(" ", "\n");
            if (contetRegex.contains("SET")) {
                color = "#ccb3ff";//DodgerBlue";
//                color = "rgba(135, 206, 235, 0.5)";//DodgerBlue";
            } else if (contetRegex.contains("DURATION")) {
                color = "#99ffeb"; //Tomato
//                color = "hsla(9, 100%, 64%, 0.5)"; //Tomato
            } else if (contetRegex.contains("TIME")) {
                color = "#ffbb99";//"MediumSeaGreen";
//                color = "rgba(102, 205, 170, 0.5)";//"MediumSeaGreen";
            }

            String aux2 = m.group(0);
            aux2 = aux2.replace(">", "");

            m.appendReplacement(sb, aux2.replaceFirst(Pattern.quote(aux2), "<span style=\"background-color:"
                    + color + "\" title=\"" + contetRegex + "\">"));
        }
        m.appendTail(sb); // append the rest of the contents
        
        String saux = sb.toString();

        return saux;
    }

}
