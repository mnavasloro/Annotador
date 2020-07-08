/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.tagger.core.data;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author mnavas
 */
public class FileCENDOJ {

    public String cuerpo = null;
    public String cuerpoClean = null;
    public String cuerpoAnnotated = null;
    public String cuerpoMerge = null;
    public String input = null;
    public String output = null;
    public File file = null;

    public FileCENDOJ(File filename) {
        file = filename;
    }

    /**
     * Returns the CUERPO of the input file
     *
     * @return the CUERPO of the input file
     */
    public String getCuerpo() {
        try {
            input = FileUtils.readFileToString(file, "ISO-8859-1");
            String textRegex = "<cuerpo>([\\s\\S]*)<\\/cuerpo>";
            Pattern pText = Pattern.compile(textRegex);
            Matcher mText = pText.matcher(input);
            if (mText.find()) {
                cuerpo = mText.group(1);
                return cuerpo;
            } else {
                return null;
            }
        } catch (IOException ex) {
            Logger.getLogger(FileSoco.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Returns the CUERPO of the input file and deletes all the xml tags in it
     *
     * @return the CUERPO of the input file wothout xml tags
     */
    public String getCuerpoClean() {
        try {
            if (cuerpo == null) {
                getCuerpo();
            }
            if (cuerpo != null) {
                cuerpoClean = cuerpo.replaceAll("<vinculo([^>]*)>[\\s\\S]*?(?=(<\\/vinculo[^>]*>))<\\/vinculo([^>]*)>", "LEYREF");
                cuerpoClean = cuerpoClean.replaceAll("<\\/([^>]*)>", "");
                cuerpoClean = cuerpoClean.replaceAll("<([^>]*)>", "");
                return cuerpoClean;
            }
        } catch (Exception ex) {
            Logger.getLogger(FileCENDOJ.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String setOutput() {
        try {
            output = input.replaceFirst("<cuerpo>([\\s\\S]*)<\\/cuerpo>", "<cuerpo>" + cuerpoMerge + "<\\/cuerpo>");
            return output;
        } catch (Exception ex) {
            Logger.getLogger(FileSoco.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String mergeXML() {
        cuerpoMerge = cuerpo;
        if (cuerpoMerge != null && cuerpoAnnotated != null && input != null) {
//            int len = cuerpoMerge.length();
            int j = 0; //to iterate in cuerpoAnnotated
            int i = 0; //to iterate in cuerpoMerge
            while (i < cuerpoMerge.length()) {
                if((cuerpoMerge.charAt(i) != cuerpoAnnotated.charAt(j)) && (cuerpoMerge.charAt(i) != '<')  && (!cuerpoAnnotated.substring(j,j+7).equalsIgnoreCase("<TIMEX3"))){
                    System.out.println("EH.\n IN: merge: " + (int) cuerpoMerge.charAt(i) + "\t annotated: " + (int) cuerpoAnnotated.charAt(j));
                    System.out.println("Estamos en i " + i);
                    System.out.println("EH.\n PRE: merge: " + (int) cuerpoMerge.charAt(i-1) + "\t annotated: " + (int) cuerpoAnnotated.charAt(j-1));
                    
                }
                if (cuerpoMerge.charAt(i) == '<') {
                    while (cuerpoMerge.charAt(i) == '<') {
                        if (cuerpoMerge.charAt(i) == '<' && cuerpoMerge.length() > i + 23 && cuerpoMerge.substring(i, i + 23).equalsIgnoreCase("<vinculo_jurisprudencia")) {
                            int i2 = cuerpoMerge.indexOf(">", i) + 1;
                            int i3 = cuerpoMerge.indexOf("</vinculo_jurisprudencia>", i);
                            int i4 = i3 + "</vinculo_jurisprudencia>".length();
                            i = i4 - 1;
                            if (cuerpoAnnotated.substring(j, j + 6).equalsIgnoreCase("LEYREF")) {
                                j = j + 5;
                            } else {
                                System.out.println("ERROR WHILE MERGING THE FILES");
                                return cuerpoMerge;
                            }
//                    if(cuerpoAnnotated.substring(j, j + i3 - i2 + "TIMEX3".length()).contains("TIMEX3")){
//                        j = j + i3 - i2 + cuerpoAnnotated.indexOf(">", j) + 1 - cuerpoAnnotated.indexOf("<TIMEX3", j) + "</TIMEX3>".length();
//                    } else{
//                        String inVinc = cuerpoMerge.substring(i2, i3);
//                        inVinc = inVinc.replaceAll("<[^>]*>", "");
//                        j = j + inVinc.length();
//                    }
                        } else if (cuerpoMerge.indexOf(">", i) + 1 < cuerpoMerge.length()) {
                            i = cuerpoMerge.indexOf(">", i);
                            j--;
                        } else {
                            System.out.println("ERROR WHILE MERGING THE FILES");
                            return cuerpoMerge;
                        }
                    }
                }
                if (j < cuerpoAnnotated.length() && cuerpoAnnotated.charAt(j) == '<' && cuerpoAnnotated.substring(j + 1, j + 7).equalsIgnoreCase("TIMEX3")) {
                    int j1 = cuerpoAnnotated.indexOf(">", j) + 1;
                    int j2 = cuerpoAnnotated.indexOf("</TIMEX3>", j) + "</TIMEX3>".length();
                    int j3 = cuerpoAnnotated.indexOf("</TIMEX3>", j);
                    int lengtnew = j3 - j1;
                    // in TIMEX3
                    String intimex = cuerpoAnnotated.substring(j1, j3);
                    String pretimex = cuerpoAnnotated.substring(j, j1);
                    if (!cuerpoMerge.substring(i, i + lengtnew).equalsIgnoreCase(intimex)) {
                        int lengthint = intimex.length();
                        int k = 0;
                        int k2 = 0;
                        while (k < lengthint) {
                            while (cuerpoMerge.charAt(i + k2) != intimex.charAt(k)) {
                                k2++;
                            }
                            k++;
                            k2++;
                        }
                        cuerpoMerge = cuerpoMerge.substring(0, i) + pretimex + cuerpoMerge.substring(i, i + k2) + "</TIMEX3>" + cuerpoMerge.substring(i + k2);
//                        cuerpoMerge = cuerpoMerge.substring(0,i) + cuerpoAnnotated.substring(j, j2)+ cuerpoMerge.substring(i+k2);

                        i = i + k2 + pretimex.length() + "</TIMEX3>".length();
//                        i = i + j2 - j;
                        j = j2;
                    } else {

                        cuerpoMerge = cuerpoMerge.substring(0, i) + cuerpoAnnotated.substring(j, j2) + cuerpoMerge.substring(i + lengtnew);
                        i = i + j2 - j;
                        j = j2;

                    }
                    if (cuerpoMerge.charAt(i) == '<') {
                        i--;
                        j--;
                    }
                }
                i++;
                j++;
            }
            return setOutput();
        }

        return null;
    }

}
