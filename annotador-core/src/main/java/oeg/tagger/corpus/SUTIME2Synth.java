/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.tagger.corpus;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.time.*;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;

public class SUTIME2Synth {

  /** Example usage:
   *  java SUTimeDemo "Three interesting dates are 18 Feb 1997, the 20th of july and 4 days from today."
   *
   *  @param args Strings to interpret
   */
  public static void main(String[] args) {
    Properties props = StringUtils.argsToProperties(new String[]{"-props", "StanfordCoreNLP-spanish.properties"});
    AnnotationPipeline pipeline = new AnnotationPipeline();
    pipeline.addAnnotator(new TokenizerAnnotator(false));
    pipeline.addAnnotator(new WordsToSentencesAnnotator(false));
    pipeline.addAnnotator(new POSTaggerAnnotator(false));
    pipeline.addAnnotator(new TimeAnnotator("sutime", props));

    File finput = new File("../annotador-core/src/main/resources/rules/HOURGLASS_RESULTS/test_input_plain/");
            String foutput = "../annotador-core/src/main/resources/rules/HOURGLASS_RESULTS/test_SUTime/";
            File[] listF = finput.listFiles();
            String total = "";            
            for (File f : listF){
                try {
                    String text = FileUtils.readFileToString(f, "UTF-8");
                    String orTxt = text;
                    int offset = 0;
                    int i = 0;
//                    System.out.println("\n************************");
//                    System.out.println(txt + "\n");
                   
                    String date = "2019-12-20";
                    
      Annotation annotation = new Annotation(text);
      annotation.set(CoreAnnotations.DocDateAnnotation.class, date);
      pipeline.annotate(annotation);
      System.out.println(annotation.get(CoreAnnotations.TextAnnotation.class));
      List<CoreMap> timexAnnsAll = annotation.get(TimeAnnotations.TimexAnnotations.class);
      for (CoreMap cm : timexAnnsAll) {
          List<CoreLabel> tokens = cm.get(CoreAnnotations.TokensAnnotation.class);
                int ini = tokens.get(0).get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
                int end = tokens.get(tokens.size() - 1).get(CoreAnnotations.CharacterOffsetEndAnnotation.class);
//                String tid = "t0";
//                String tid = (String) timex.get("tid");
//                String type = (String) timex.get("type");
                SUTime.Temporal temporal = cm.get(TimeExpression.Annotation.class).getTemporal();
                String type = temporal.getTimexType().toString();
                String value = temporal.getTimexValue();
                String tid =  "t" + i;
                i++;
             
                String iniS = "<TIMEX3 tid=\"" + tid + "\" type=\"" + type + "\" value=\"" + value + "\">";
                String endS = "</TIMEX3>";
                orTxt = orTxt.substring(0, offset + ini) + iniS + orTxt.substring(offset + ini, offset + end) + endS + orTxt.substring(offset + end);
                offset = offset + iniS.length() + endS.length();
//            }
//        }
          
          
          
          
          
          
//          
//        List<CoreLabel> tokens = cm.get(CoreAnnotations.TokensAnnotation.class);
//        System.out.println(cm + " [from char offset " +
//            tokens.get(0).get(CoreAnnotations.CharacterOffsetBeginAnnotation.class) +
//            " to " + tokens.get(tokens.size() - 1).get(CoreAnnotations.CharacterOffsetEndAnnotation.class) + ']' +
//            " --> " + cm.get(TimeExpression.Annotation.class).getTemporal());
      }
      
      String finalTxt = "<?xml version=\"1.0\" ?>\n" +
"<TimeML xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://timeml.org/timeMLdocs/TimeML_1.2.1.xsd\">\n" +
"\n" +
                                                "\n" +
"\n" +
"<TEXT>" + orTxt + "</TEXT>\n" +
"\n" +
"</TimeML>";
      FileOutputStream fos1 = new FileOutputStream(foutput + f.getName().replaceFirst("\\.txt", "\\.tml"));
                    OutputStreamWriter w = new OutputStreamWriter(fos1, "UTF-8");
                    BufferedWriter bw = new BufferedWriter(w);
                    bw.write(finalTxt);
                    bw.flush();
                    bw.close();
                } catch (Exception ex) {
            Logger.getLogger(SUTIME2Synth.class.getName()).log(Level.SEVERE, null, ex);
        }
      System.out.println();
    }
            
            
  }

}