package oeg.tagger.corpus;


import static edu.stanford.nlp.pipeline.StanfordCoreNLP.OutputFormat.JSON;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import oeg.tagger.core.time.tictag.AnnotadorStandard;
import oeg.tagger.core.tutorial.annotador.TutorialAnnotadorSyntES;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mnavas
 */
public class translateSUTIME {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();

        try {
            File finput = new File("../annotador-core/src/main/resources/rules/test_input/");
            String fjson = "../annotador-core/src/main/resources/rules/sutime_test_output/";
            String foutput = "../annotador-core/src/main/resources/rules/sutime_test_output_timex/";
            File[] listF = finput.listFiles();
            String total = "";
            for (File f : listF){
                try {
                    String txt = FileUtils.readFileToString(f, "UTF-8");
                    String txt2 = FileUtils.readFileToString(new File(fjson + f.getName()), "UTF-8");
//                    System.out.println("\n************************");
//                    System.out.println(txt + "\n");
                    String outp = translate(txt2, txt);
//                    System.out.println(outp);
                    
                    total = total + outp;
                    
                    FileOutputStream fos1 = new FileOutputStream(foutput + f.getName().replaceFirst("\\.txt", "_sol\\.txt"));
                    OutputStreamWriter w = new OutputStreamWriter(fos1, "UTF-8");
                    BufferedWriter bw = new BufferedWriter(w);
                    bw.write(outp);
                    bw.flush();
                    bw.close();
                    
                } catch (IOException ex) {
                    Logger.getLogger(TutorialAnnotadorSyntES.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        
        } catch (Exception ex) {
            Logger.getLogger(TutorialAnnotadorSyntES.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        

    
}


public static String translate(String outjson, String orig){
 
    String orTxt = orig;
//             String txt = inp;
             int offset = 0;
try {
    
    JSONParser parser = new JSONParser();
    JSONObject json = (JSONObject) parser.parse(outjson);
    JSONArray sentjs = (JSONArray) json.get("sentences");
    for(Object sent : sentjs){
        JSONObject sentj = (JSONObject) sent;
        JSONArray entjs = (JSONArray) sentj.get("entitymentions");
        for(Object ent : entjs){
            JSONObject entj = (JSONObject) ent;
            if(entj.containsKey("timex")){
                int ini = ((Long) entj.get("characterOffsetBegin")).intValue();
                int end = ((Long) entj.get("characterOffsetEnd")).intValue();
                JSONObject timex = (JSONObject) entj.get("timex");
                String tid = (String) timex.get("tid");
                String type = (String) timex.get("type");
                String value = (String) timex.get("value");
                String iniS = "<TIMEX3 tid=\"" + tid + "\" type=\"" + type + "\" value=\"" + value + "\">";
                String endS = "</TIMEX>";
                orTxt = orTxt.substring(0, offset + ini) + iniS + orTxt.substring(offset + ini, offset + end) + endS + orTxt.substring(offset + end);
                offset = offset + iniS.length() + endS.length();
            }
        }
    }
//    System.out.println("is: " + IOUtils.toString(process.getInputStream()));
    
    
    
    
    
//System.out.println("es: " + IOUtils.toString(process.getErrorStream()));
} catch (Throwable cause) {
    // process cause
}
    
return orTxt;
}


}