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
import oeg.tagger.core.time.tictag.Annotador;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author mnavas
 */
public class TempCourtEval {
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String[] taggers = {"ANNOTADOR"};
        String[] corpora = {"ECHR", "ECJ", "USC"};

        Annotador ann = new Annotador("EN");
        for (String corpus : corpora) {
            System.out.println("***************************************************************");
            System.out.println("STARTING CORPUS " + corpus);
            System.out.println("***************************************************************");
            for (String tagger : taggers) {
                System.out.println("_____________________________________________");
                System.out.println("STARTING TAGGER " + tagger + " for corpus" + corpus);
                System.out.println("_____________________________________________");
//        String tagger = "CLEARTK";
//        String corpus = "ECHR";
                File folder = new File("C:\\Users\\mnavas\\DATA\\TempCourt\\TIMEML\\PlainTimemL\\" + corpus);
//                if(tagger.equals("TARSQI")){
//                    cleanDocs(folder);
//                }
                File[] listFiles = folder.listFiles();


                File folderTagger = new File("C:\\Users\\mnavas\\DATA\\TempCourt\\TempCourt_output_corpus\\" + corpus + "\\" + tagger);
                folderTagger.mkdir();
                
                int i = 0;
                for (File f : listFiles) {
                    i = i + 1;
                    System.out.println(corpus + "|" + tagger + ": " + "Processing file " + i + "...");
                    File foutput = new File("C:\\Users\\mnavas\\DATA\\TempCourt\\TempCourt_output_corpus\\" + corpus + "\\" + tagger + "\\" + f.getName().replaceFirst("tml", "xml"));
                    FileTempCourt filetc = new FileTempCourt(f, f, foutput);
                    try{
                        String outp = ann.annotate(filetc.getTextInput(), filetc.getDCT(corpus));
                        if(filetc.writeOutputFile(outp)){                            
                            System.out.println(corpus + "|" + tagger + ": " + "File " + i + " done!");
                        } else{
                            System.out.println("ERROR WHILE WRITING");
                        }
                    }catch(Exception ex){
                    System.err.println(corpus + "|" + tagger + ": " + "Error while processing file " + i);
                    System.err.println(ex.toString());
                }
                    
                        System.out.println("------------------------");
                    
                }
            }
        }
        
                    
                    
    }
    
   
}