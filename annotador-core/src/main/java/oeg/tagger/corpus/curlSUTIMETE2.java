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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class curlSUTIMETE2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();

        try {
            File finput = new File("C:\\Users\\mnavas\\CODE\\OLD_CODE\\data\\datasets\\timeEval\\tempeval2\\tempeval2plain\\");
            String foutput = "C:\\Users\\mnavas\\CODE\\OLD_CODE\\data\\datasets\\timeEval\\tempeval2\\tempeval2SUTime";
            File[] listF = finput.listFiles();
            String total = "";
            Pattern pText = Pattern.compile("\\d+_(\\d{4})(\\d{2})(\\d{2})");
            for (File f : listF){
                try {
                    String txt = FileUtils.readFileToString(f, "UTF-8");
//                    System.out.println("\n************************");
//                    System.out.println(txt + "\n");
                    Matcher mText = pText.matcher(f.getName());
                    String date = "2019-12-20";
                    if (mText.find()) {
                        date = mText.group(1) + "-" + mText.group(2) + "-" + mText.group(3);
                    }
//                    System.out.println("\n************************");
//                    System.out.println(txt + "\n");
                    String outp = callCURL(txt, (new File(foutput + f.getName())).getCanonicalPath(), date);
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


public static String callCURL(String inp, String file, String date){
             Runtime runtime = Runtime.getRuntime();
             String orTxt = inp;
//             String txt = inp;
             int offset = 0;
try {
    String txt = URLEncoder.encode(inp, StandardCharsets.UTF_8.toString());
//    txt = txt + ":";
//    txt = txt.replaceAll("ñ", "\\%C3\\%B1");
    txt = txt.replaceAll("\\+", "%20");
    
    String mandar = "curl -X POST 'https://corenlp.run/?properties=%7B%22annotators%22%3A%20%22tokenize%2Cssplit%2Cpos%2Cner%2Cregexner%2Copenie%22%2C%20%22date%22%3A%20%22" + date + "%22%7D&pipelineLanguage=es' -d '" + txt + "' > '" + file + "'";
    System.out.println(mandar);System.out.println(mandar);
    
    Process p = Runtime.getRuntime().exec(mandar);
    p.waitFor();
    
    
    String outjson = IOUtils.toString(p.getInputStream());
    JSONParser parser = new JSONParser();
    JSONObject json = (JSONObject) parser.parse(outjson);
    JSONArray sentjs = (JSONArray) json.get("sentences");
    for(Object sent : sentjs){
        JSONObject sentj = (JSONObject) sent;
        JSONArray entjs = (JSONArray) sentj.get("entitymentions");
        for(Object ent : entjs){
            JSONObject entj = (JSONObject) ent;
            if(entj.containsKey("timex")){
                int ini = Integer.valueOf((String) entj.get("characterOffsetBegin"));
                int end = Integer.valueOf((String) entj.get("characterOffsetEnd"));
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