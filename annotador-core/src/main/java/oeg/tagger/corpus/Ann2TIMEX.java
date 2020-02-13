/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.tagger.corpus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.codehaus.plexus.util.FileUtils;

/**
 * Tool to visualize event annotations in Excel
 *
 * @author Maria
 */
public class Ann2TIMEX {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        int i = 0;


        String numJud = "62016CJ0569";
//        String numJud = "62016CJ0684"; // Not done
//        String numJud = "62016CJ0619";
//        String numJud = "62016CJ0214";
//        String numJud = "62015CJ0341";
//        String numJud = "62014CJ0408";
//        String numJud = "62014CJ0219";
//        String numJud = "62013CJ0328";
//        String numJud = "62013CJ0020";
        String filepath = "C:\\Users\\mnavas\\DATA\\CORPUS SOCO\\temporal\\temporal\\" + numJud + "(SB).xlsx";
        String filepathTXT = "C:\\Users\\mnavas\\DATA\\CORPUS SOCO\\temporal\\temporal\\" + numJud + ".txt";
        
//        String numJud = "Case C-13-16";
//        String numJud = "Case C-131-12";
//        String numJud = "Case C-191-15";
//        String numJud = "Case C-212-13";
//        String numJud = "Case C-219-12";
//        String numJud = "Case C-230-14";
//        String numJud = "Case C-434-16";
//        String numJud = "Case C-473-12";
//        String numJud = "Case C-486-12";
//        String numJud = "Case C-536-15";
//        String filepath = "C:\\Users\\mnavas\\DATA\\CORPUS SOCO\\temporal\\2nd\\" + numJud + "(SB).xlsx";
//        String filepathTXT = "C:\\Users\\mnavas\\DATA\\CORPUS SOCO\\temporal\\2nd\\" + numJud + ".txt";
        String txt = "";
        
        try {
            txt = org.apache.commons.io.FileUtils.readFileToString(new File(filepathTXT), "UTF-8");
        } catch (IOException ex) {
            Logger.getLogger(Ann2TIMEX.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        String txt2 = txt;
        String txt2 = txt.replaceAll("\uFEFF", "");
        txt2 = txt2.replaceAll("ï»¿","");
        txt2 = txt2.replaceAll(System.lineSeparator(), "\n");
        int ini = 0;
        int fin = 0;
        int offset = 0; // due to Soco's offset
        String aux1 = "";
        String aux2 = "</TIMEX3>";
        int off2 = aux2.length();
        String newT= "";
        
        ExcelReader xreader = new ExcelReader();
        List<AnnotationExcel> evs = xreader.read(filepath);
        
        
        for (AnnotationExcel ev : evs) {
            i++;
            // If it is a core info of the event
//            if (ev.Type.toLowerCase().isEmpty()) {
//                
//            }
//            else if (ev.Type.toLowerCase().contains("signal")) {
//                
//
//            } else if (ev.Info.toLowerCase().contains("what")) {
            
            aux1 = "<TIMEX3 tid=\"t" + i + "\" type=\"" + ev.Type + "\" value=\"" + ev.Value + "\" text=\"" + ev.Text  + "\" extra=\"" + ev.Info + "\">";
            ini = offset + Double.valueOf(ev.Begin).intValue();
            fin = offset + Double.valueOf(ev.End).intValue();
            
            System.out.println("XXX: ini: " + ini + "\tfin: " + fin);
            newT = aux1 + txt2.substring(ini, fin) + aux2;
            txt2 = txt2.substring(0, ini) + newT + txt2.substring(fin);
            
            offset = offset + aux1.length() + off2;
                        
            System.out.println("ANNOTATION " + i + " :\n annText: " + ev.Text + "\n subst: " + newT + "\n");
        }


        // Write tml
        txt2 = txt2.replaceAll("\n", System.lineSeparator());
        File tmlfile = new File(filepath.replace("xlsx", "tml"));
																											   
        String tmlOutput = "<?xml version=\"1.0\"?>\n" +
"<TimeML xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://timeml.org/timeMLdocs/TimeML_1.2.1.xsd\">\n" +                
 txt2               +
"\n" +
"</TimeML>";
//        FileUtils.fileWrite(tmlfile, "UTF-8", tmlOutput);

    }

}
