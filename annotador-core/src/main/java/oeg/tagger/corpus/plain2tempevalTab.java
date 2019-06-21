/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.tagger.corpus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author mnavas
 */
public class plain2tempevalTab {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here     
        int flagen = 0;
        String txtExtent = "";
        String txtAttr = "";
        List<List<String>> records = new ArrayList<>();
        File foutput = new File("C:\\Users\\mnavas\\CODE\\OLD_CODE\\data\\datasets\\timeEval\\tempeval2\\tempeval2annotador\\");
        File[] flist = foutput.listFiles();
        try (BufferedReader br = new BufferedReader( new InputStreamReader(new FileInputStream("C:\\Users\\mnavas\\CODE\\OLD_CODE\\data\\datasets\\timeEval\\tempeval2\\newtab.txt"), "UTF8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\t");
                records.add(Arrays.asList(values));
            }
            
            for(File f: flist){
                String s = FileUtils.readFileToString(f, "UTF-8");
                JSONObject obj = new JSONObject(s);
                JSONArray jsonArray = obj.getJSONArray("annotations");
                int i = 0;
                while(i < jsonArray.length()){
                    JSONObject js = (JSONObject) jsonArray.get(i);
                    int beg = (int) js.get("beginIndex");
                    int end = (int) js.get("endIndex");
                    String type = (String) js.get("type");
                    String tid = (String) js.get("tid");
                    String value = (String) js.get("value");
                    
                    for(List<String> r : records){
                        if(r.get(0).equalsIgnoreCase(f.getName())){
                            if(Integer.valueOf(r.get(4))>=beg && Integer.valueOf(r.get(5))<=end){
                                String base = r.get(0) + "\t" + r.get(1) + "\t" + r.get(2) + "\t" + "timex3" + "\t" + tid + "\t" + "1" ; 
                                txtExtent = txtExtent + base + "\n";//System.getProperty("line.separator");
                                if(flagen == 0){
                                    txtAttr = txtAttr +  base + "\t" + "type" +  "\t" + type + "\n";//System.getProperty("line.separator");
                                    txtAttr = txtAttr +  base + "\t" + "val" +  "\t" + value + "\n";//System.getProperty("line.separator");
                                    flagen = 1;
                                }
                            }                            
                        }
                    }
                    flagen = 0;
                    i++;
                }
                System.out.println(f.getName() + "done -----\n" + txtAttr + "\n");
            }
            
            FileOutputStream fos1 = new FileOutputStream("C:\\Users\\mnavas\\CODE\\OLD_CODE\\data\\datasets\\timeEval\\tempeval2\\" + "timex-attributes.tab");
            OutputStreamWriter w = new OutputStreamWriter(fos1, "UTF-8");
            BufferedWriter bw = new BufferedWriter(w);
            bw.write(txtAttr);
            bw.flush();
            bw.close();
            
            fos1 = new FileOutputStream("C:\\Users\\mnavas\\CODE\\OLD_CODE\\data\\datasets\\timeEval\\tempeval2\\" + "timex-extent.tab");
            w = new OutputStreamWriter(fos1, "UTF-8");
            bw = new BufferedWriter(w);
            bw.write(txtExtent);
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(plain2tempevalTab.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
