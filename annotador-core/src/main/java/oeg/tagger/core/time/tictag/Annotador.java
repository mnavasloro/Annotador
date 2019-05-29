package oeg.tagger.core.time.tictag;

import oeg.tagger.core.time.annotationHandler.BRATAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.tokensregex.CoreMapExpressionExtractor;
import edu.stanford.nlp.ling.tokensregex.MatchedExpression;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.ling.tokensregex.types.Value;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import oeg.tagger.core.data.FileSoco;
import oeg.tagger.core.data.FileTempEval3;
import oeg.tagger.core.data.FileTempEval3ES;
import oeg.tagger.core.data.FileTimeBank;
import oeg.tagger.core.data.ManagerSoco;
import oeg.tagger.core.data.ManagerTempEval3;
import oeg.tagger.core.data.ManagerTempEval3ES;
import oeg.tagger.core.data.ManagerTimeBank;
import oeg.tagger.core.servlets.Salida;
import oeg.tagger.core.time.annotationHandler.TIMEX2JSON;
import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @author mnavas
 */
public class Annotador {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Annotador.class);

//    PrintWriter out;
    String rules;
    Properties properties = new Properties();
    String posModel;
    String lemmaModel;
    StanfordCoreNLP pipeline;

    Map<String, String> map = new HashMap<String, String>();
    Map<TicTagRule, Double> ruleSet = new LinkedHashMap<TicTagRule, Double>();

    String lang = "es";

    /**
     * Initializes a instance of the tagger
     *
     * @param lang language (ES - Spanish, EN - English)
     * @return an instance of the tagger
     */
    public Annotador() {
        init();
    }

    public Annotador(String language) {
        lang = language;
        init();
    }

    public Annotador(String pos, String lemma, String rul, String language) {
        posModel = pos;
        lemmaModel = lemma;
        rules = rul;
        lang = language;
        init();
    }
    
    public Annotador(String rul, String language) {
        rules = rul;
        lang = language;
        init();
    }

    public void init() {

        if (lang.equalsIgnoreCase("ES")) {
            if (rules == null) {
                rules = "../annotador-core/src/main/resources/rules/rulesES.txt";
            }

//        out = new PrintWriter(System.out);
            properties = StringUtils.argsToProperties(new String[]{"-props", "StanfordCoreNLP-spanish.properties"});

            if (posModel == null) {
                posModel = "../annotador-core/src/main/resources/ixa-pipes/morph-models-1.5.0/es/es-pos-perceptron-autodict01-ancora-2.0.bin";
            }
            if (lemmaModel == null) {
                lemmaModel = "../annotador-core/src/main/resources/ixa-pipes/morph-models-1.5.0/es/es-lemma-perceptron-ancora-2.0.bin";
            }

            properties.setProperty("annotators", "tokenize,ssplit,spanish,readability,ner,tokensregexdemo");
//    properties.setProperty("ner.useSUTime", "false");
            properties.setProperty("spanish.posModel", posModel);
            properties.setProperty("spanish.lemmaModel", lemmaModel);
            properties.setProperty("readability.language", "es");

            properties.setProperty("customAnnotatorClass.spanish", "oeg.tagger.core.time.aidCoreNLP.BasicAnnotator");
            properties.setProperty("customAnnotatorClass.readability", "eu.fbk.dh.tint.readability.ReadabilityAnnotator");

            properties.setProperty("customAnnotatorClass.tokensregexdemo", "edu.stanford.nlp.pipeline.TokensRegexAnnotator");
            properties.setProperty("tokensregexdemo.rules", rules);
            properties.setProperty("tokenize.verbose", "false");
            properties.setProperty("TokensRegexNERAnnotator.verbose", "false");
//    properties.setProperty("regexner.verbose", "false");
        }
        
        else if (lang.equalsIgnoreCase("EN")) {
            if (rules == null) {
                rules = "../annotador-core/src/main/resources/rules/rulesEN.txt";
            }

//        out = new PrintWriter(System.out);
//            properties = StringUtils.argsToProperties(new String[]{"-props", "StanfordCoreNLP-spanish.properties"});
            properties = new Properties();

            properties.setProperty("annotators", "tokenize, ssplit, pos, lemma,ner,tokensregexdemo");
            properties.setProperty("ner.useSUTime", "false");
            

            properties.setProperty("customAnnotatorClass.tokensregexdemo", "edu.stanford.nlp.pipeline.TokensRegexAnnotator");
            properties.setProperty("tokensregexdemo.rules", rules);
            properties.setProperty("tokenize.verbose", "false");
            properties.setProperty("TokensRegexNERAnnotator.verbose", "false");
//    properties.setProperty("regexner.verbose", "false");
        }

        try {
            pipeline = new StanfordCoreNLP(properties);
        } catch (Exception ex) {
            System.out.println("Error: " + ex.toString());
        }

    }
    
    public Map<String,String> parseDuration(String input) {
        Map<String,String> durations = new HashMap<String,String>();
        Pattern pAnchor = Pattern.compile("(\\d+)([a-zA-Z])");
        
        Matcher m = pAnchor.matcher(input);
        while(m.find()){
            String numb = m.group(1);
            String unit = m.group(2);
            if(unit.equalsIgnoreCase("M") && input.startsWith("PT")){
                durations.put("MIN", numb);
            } else{
                durations.put(unit, numb);
            }
        }
//        Pattern pAnchor = Pattern.compile("anchor\\((\\w+),([+-]?\\d+),(\\w+)\\)");

        return durations;
    }

    public String annotate(String input, String anchorDate) {
        Pattern pAnchor = Pattern.compile("anchor\\((\\w+),([+-]),([^\\)]+)\\)");
//        Pattern pAnchor = Pattern.compile("anchor\\((\\w+),([+-]?\\d+),(\\w+)\\)");
        try {
            String inp2 = input;
            int flagRN = 0;
           
                inp2 = inp2.replaceAll("\\r\\n", "\\\\n");
            
            
                
            int offsetdelay = 0;
            int numval = 0;
            Annotation annotation = new Annotation(inp2);

            pipeline.annotate(annotation);

            // An Annotation is a Map and you can get and use the various analyses individually.
//            out.println();
            // The toString() method on an Annotation just prints the text of the Annotation
            // But you can see what is in it with other methods like toShorterString()
//            out.println("The top level annotation");
            System.out.println(annotation.toShorterString());
            
            
            List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
            for (CoreMap sentence : sentences) {
                CoreMapExpressionExtractor<MatchedExpression> extractor = CoreMapExpressionExtractor
                        .createExtractorFromFiles(TokenSequencePattern.getNewEnv(), rules);
                List<MatchedExpression> matchedExpressions = extractor.extractExpressions(sentence);
//      out.println("Matched expressions\n----------\n");

                for (MatchedExpression matched : matchedExpressions) {
//        out.println("Matched expression: " + matched.getText() + " with value " + matched.getValue());
                    CoreMap cm = matched.getAnnotation();

                    Value v = matched.getValue();

                    ArrayList<edu.stanford.nlp.ling.tokensregex.types.Expressions.PrimitiveValue> a = (ArrayList<edu.stanford.nlp.ling.tokensregex.types.Expressions.PrimitiveValue>) v.get();
                    String typ = (String) a.get(0).get();
                    String val = (String) a.get(1).get();
                    String freq = (String) a.get(2).get();
                    String rul = (String) a.get(4).get();

                    System.out.println(typ + " | " + val + " | " + freq + " | " + rul);

                    // TO DO: el get? poner los values!
                    numval++;
                    int ini = cm.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
                    String text = cm.get(CoreAnnotations.TextAnnotation.class);
//        out.println(matched.getText() + " - " + matched.getCharOffsets());

                    // To adapt to TE3 format - news mode
                    if (val.startsWith("XXXX-XX") && anchorDate != null) {
                        DateTime dt = new DateTime(anchorDate);
                        int month = dt.getMonthOfYear();
                        int year = dt.getYear();
                        val = year + "-" + String.format("%02d", month) + val.substring(7, val.length());
                    } else if (val.startsWith("XXXX") && anchorDate != null) {
                        DateTime dt = new DateTime(anchorDate);
                        int year = dt.getYear();
                        val = year + val.substring(4, val.length());
                    }

                    // To adapt to TE3 format
                    val = val.replaceAll("-X+", "");

                    // TODO: also, use the dependency parsing to find modifiers
                    // TODO: the ref can be other day...
                    
                    if (val.startsWith("anchor") && anchorDate != null) {     
                        DateTime dt = new DateTime(anchorDate);
                        
                        Matcher m = pAnchor.matcher(val);
                        m.find();
                        String ref = m.group(1);
                        String plus = m.group(2);
                        String duration = m.group(3);
                        
                        Map<String,String> durations = new HashMap<String,String>();
                        durations = parseDuration(duration);
                        Set<String> durString = durations.keySet();
                        for(String gran : durString){
        
                            int plusI = Integer.valueOf(durations.get(gran));

                            // Needs to be more general, check if today, proceed otherwise if not

                            if (gran.equalsIgnoreCase("D")) {
                                if (plus.equalsIgnoreCase("+")) {
                                    dt = dt.plusDays(plusI);
                                } else {
                                    dt = dt.minusDays(plusI);
                                }
                            } else if (gran.equalsIgnoreCase("M")) {
                                if (plus.equalsIgnoreCase("+")) {
                                    dt = dt.plusMonths(plusI);
                                } else {
                                    dt = dt.minusMonths(plusI);
                                }
                            } else if (gran.equalsIgnoreCase("Y")) {
                                if (plus.equalsIgnoreCase("+")) {
                                    dt = dt.plusYears(plusI);
                                } else {
                                    dt = dt.minusYears(plusI);
                                }
    //                        } else if (gran.equalsIgnoreCase("MIN")) {
    //                            if (plusI > 0) {
    //                                dt = dt.plusYears(plusI * 10);
    //                            } else {
    //                                dt = dt.minusYears(plusI * -10);
    //                            }
    //                        } else if (gran.equalsIgnoreCase("S")) {
    //                            if (plusI > 0) {
    //                                dt = dt.plusYears(plusI * 100);
    //                            } else {
    //                                dt = dt.minusYears(plusI * -100);
    //                            }
    //                        } else if (gran.equalsIgnoreCase("H")) {
    //                            if (plusI > 0) {
    //                                dt = dt.plusYears(plusI * 1000);
    //                            } else {
    //                                dt = dt.minusYears(plusI * -1000);
    //                            }
                            } else if (gran.equalsIgnoreCase("W")) {
                                if (plus.equalsIgnoreCase("+")) {
                                    dt = dt.plusWeeks(plusI);
                                } else {
                                    dt = dt.minusWeeks(plusI);
                                }
                            } else if (gran.equalsIgnoreCase("H")) {
                                if (plus.equalsIgnoreCase("+")) {
                                    dt = dt.plusHours(plusI);
                                } else {
                                    dt = dt.minusHours(plusI);
                                }
                            } else if (gran.equalsIgnoreCase("MIN")) {
                                if (plus.equalsIgnoreCase("+")) {
                                    dt = dt.plusMinutes(plusI);
                                } else {
                                    dt = dt.minusMinutes(plusI);
                                }
                            } else if (gran.equalsIgnoreCase("S")) {
                                if (plus.equalsIgnoreCase("+")) {
                                    dt = dt.plusSeconds(plusI);
                                } else {
                                    dt = dt.minusSeconds(plusI);
                                }

                            }
                        }

                        val = dt.toString("YYYY-MM-dd") + val.substring(val.lastIndexOf(")") + 1);
                    }
                    

                    String addini = "<TIMEX3 tid=\"t" + numval + "\" type=\"" + typ + "\" value=\"" + val + "\">";
                    if (!freq.isEmpty()) {
                        addini = "<TIMEX3 tid=\"t" + numval + "\" type=\"" + typ + "\" value=\"" + val + "\" freq=\"" + freq + "\">";
                    }
                    String addfin = "</TIMEX3>";

                    String toAdd = addini + text + addfin;

                    inp2 = inp2.substring(0, ini + offsetdelay) + toAdd + inp2.substring(ini + text.length() + offsetdelay);

                    offsetdelay = offsetdelay + toAdd.length() - text.length();

                }
            }
//            if(flagRN==1){
                inp2 = inp2.replaceAll("\\\\n", "\r\n");
//            }
            return inp2;

        } catch (Exception ex) {
            Logger.getLogger(Annotador.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public boolean evaluateTE3() {
        try {
            ManagerTempEval3 mte3 = new ManagerTempEval3();
            List<FileTempEval3> list = mte3.lista;
            for (FileTempEval3 f : list) {
                String input = f.getTextInput();
                String output = annotate(input, f.getDCTInput());
                f.writeOutputFile(output);
            }
        } catch (Exception ex) {
            Logger.getLogger(Annotador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    
    public boolean evaluateSoco() {
        try {
            ManagerSoco mte3 = new ManagerSoco();
            List<FileSoco> list = mte3.lista;
            for (FileSoco f : list) {
                String input = f.getTextInput();
                String output = annotate(input, null);
                f.writeOutputFile(output);
            }
        } catch (Exception ex) {
            Logger.getLogger(Annotador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    

    public boolean evaluateTE3ES() {
        try {
            ManagerTempEval3ES mte3 = new ManagerTempEval3ES();
            List<FileTempEval3ES> list = mte3.lista;
            for (FileTempEval3ES f : list) {
                String input = f.getTextInput();
                String input2 = input.replaceAll("\\r\\n", "\\\\n");
                String output = annotate(input2, f.getDCTInput());
                if(!input.equals(input2)){
                    output = output.replaceAll("\\\\n", "\r\n");
                }
                f.writeOutputFile(output);
            }
        } catch (Exception ex) {
            Logger.getLogger(Annotador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean evaluateTimeBank() {
        try {
            ManagerTimeBank mtb = new ManagerTimeBank();
            List<FileTimeBank> list = mtb.lista;
            for (FileTimeBank f : list) {
                String input = f.getTextInput();
                String output = annotate(input, f.getDCTInput());
                f.writeOutputFile(output);
            }
        } catch (Exception ex) {
            Logger.getLogger(Annotador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean writeFile(String input, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter w = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(w);
            bw.write(input);
            bw.flush();
            bw.close();
            return true;
        } catch (Exception ex) {
            Logger.getLogger(Annotador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public String annotateJSON(String input, String anchorDate) {
        
        String out = annotate(input, anchorDate);
        TIMEX2JSON t2j = new TIMEX2JSON();
        return t2j.translateSentence(out);        
    }
    
   /* DEPRECATED */ 
    
    
    // tb con anchordate
//   public String annotateBRAT(String input, String anchorDate) {
    public Salida annotateBRAT(String input, String anchorDate) {
        String outp = "var x = {\n"
                + "    'text': '" + input + "',\n"
                + "    'entities': [\n";
        String format = "var y = {\n"
                + "    entity_types: [";
        Pattern pAnchor = Pattern.compile("anchor\\((\\w+),([+-]?\\d+),(\\w+)\\)");
        try {
            String inp2 = input;
            int numval = 0;
            Annotation annotation = new Annotation(inp2);

            pipeline.annotate(annotation);

            // An Annotation is a Map and you can get and use the various analyses individually.
//            out.println();
            // The toString() method on an Annotation just prints the text of the Annotation
            // But you can see what is in it with other methods like toShorterString()
//            out.println("The top level annotation");
//            out.println(annotation.toShorterString());
            List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
            for (CoreMap sentence : sentences) {
                CoreMapExpressionExtractor<MatchedExpression> extractor = CoreMapExpressionExtractor
                        .createExtractorFromFiles(TokenSequencePattern.getNewEnv(), rules);
                List<MatchedExpression> matchedExpressions = extractor.extractExpressions(sentence);
//      out.println("Matched expressions\n----------\n");

                for (MatchedExpression matched : matchedExpressions) {
//        out.println("Matched expression: " + matched.getText() + " with value " + matched.getValue());
                    CoreMap cm = matched.getAnnotation();

                    Value v = matched.getValue();

                    ArrayList<edu.stanford.nlp.ling.tokensregex.types.Expressions.PrimitiveValue> a = (ArrayList<edu.stanford.nlp.ling.tokensregex.types.Expressions.PrimitiveValue>) v.get();
                    String typ = (String) a.get(0).get();
                    String val = (String) a.get(1).get();
                    String rul = (String) a.get(2).get();

                    // TO DO: el get? poner los values!
                    numval++;
                    int ini = cm.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
                    int fin = cm.get(CoreAnnotations.CharacterOffsetEndAnnotation.class);
                    String text = cm.get(CoreAnnotations.TextAnnotation.class);
//        out.println(matched.getText() + " - " + matched.getCharOffsets());

                    // To adapt to TE3 format - news mode
                    if (val.startsWith("XXXX-XX") && anchorDate != null) {
                        DateTime dt = new DateTime(anchorDate);
                        int month = dt.getMonthOfYear();
                        int year = dt.getYear();
                        val = year + "-" + String.format("%02d", month) + val.substring(7, val.length());
                    } else if (val.startsWith("XXXX") && anchorDate != null) {
                        DateTime dt = new DateTime(anchorDate);
                        int year = dt.getYear();
                        val = year + val.substring(4, val.length());
                    }

                    // To adapt to TE3 format
                    val = val.replaceAll("-X+", "");

                    // TODO: also, use the dependency parsing to find modifiers
                    // TODO: the ref can be other day...
                    if (val.startsWith("anchor") && anchorDate != null) {
                        Matcher m = pAnchor.matcher(val);
                        m.find();
                        String ref = m.group(1);
                        String plus = m.group(2);
                        String gran = m.group(3);
                        int plusI = Integer.valueOf(plus);

                        // Needs to be more general, check if today, proceed otherwise if not
                        DateTime dt = new DateTime(anchorDate);
                        if (gran.equalsIgnoreCase("DAY")) {
                            if (plusI > 0) {
                                dt = dt.plusDays(plusI);
                            } else {
                                dt = dt.minusDays(plusI * -1);
                            }
                        } else if (gran.equalsIgnoreCase("MONTH")) {
                            if (plusI > 0) {
                                dt = dt.plusMonths(plusI);
                            } else {
                                dt = dt.minusMonths(plusI * -1);
                            }
                        } else if (gran.equalsIgnoreCase("YEAR")) {
                            if (plusI > 0) {
                                dt = dt.plusYears(plusI);
                            } else {
                                dt = dt.minusYears(plusI * -1);
                            }
                        } else if (gran.equalsIgnoreCase("10_YEAR")) {
                            if (plusI > 0) {
                                dt = dt.plusYears(plusI * 10);
                            } else {
                                dt = dt.minusYears(plusI * -10);
                            }
                        } else if (gran.equalsIgnoreCase("100_YEAR")) {
                            if (plusI > 0) {
                                dt = dt.plusYears(plusI * 100);
                            } else {
                                dt = dt.minusYears(plusI * -100);
                            }
                        } else if (gran.equalsIgnoreCase("1000_YEAR")) {
                            if (plusI > 0) {
                                dt = dt.plusYears(plusI * 1000);
                            } else {
                                dt = dt.minusYears(plusI * -1000);
                            }
                        } else if (gran.equalsIgnoreCase("WEEK")) {
                            if (plusI > 0) {
                                dt = dt.plusWeeks(plusI);
                            } else {
                                dt = dt.minusWeeks(plusI);
                            }
                        } else if (gran.equalsIgnoreCase("HOUR")) {
                            if (plusI > 0) {
                                dt = dt.plusHours(plusI);
                            } else {
                                dt = dt.minusHours(plusI * -1);
                            }
                        } else if (gran.equalsIgnoreCase("MINUTE")) {
                            if (plusI > 0) {
                                dt = dt.plusMinutes(plusI);
                            } else {
                                dt = dt.minusMinutes(plusI * -1);
                            }
                        } else if (gran.equalsIgnoreCase("SECOND")) {
                            if (plusI > 0) {
                                dt = dt.plusSeconds(plusI);
                            } else {
                                dt = dt.minusSeconds(plusI * -1);
                            }

                        }

                        val = dt.toString("YYYY-MM-dd") + val.substring(val.lastIndexOf(")") + 1);
                    }

                    BRATAnnotation ba = new BRATAnnotation();
                    ba.id = String.valueOf(numval);
                    ba.beginIndex = String.valueOf(ini);
                    ba.endIndex = String.valueOf(fin);
                    ba.type = typ;
                    ba.value = val;

                    outp = outp + ba.toString();
                    format = format + ba.formatToString();

                }
            }

            Salida s = new Salida();
            s.txt = outp + "],\n}";
            s.format = format + "{\n"
                    + "            type   : 'DATE',\n"
                    + "            labels : ['DATE'],\n"
                    + "            bgColor: '#7fa2ff',\n"
                    + "            borderColor: 'darken'\n"
                    + "    },\n"
                    + "{\n"
                    + "            type   : 'TIME',\n"
                    + "            labels : ['TIME'],\n"
                    + "            bgColor: '#ffbb99',\n"
                    + "            borderColor: 'darken'\n"
                    + "    },\n"
                    + "{\n"
                    + "            type   : 'SET',\n"
                    + "            labels : ['SET'],\n"
                    + "            bgColor: '#ccb3ff',\n"
                    + "            borderColor: 'darken'\n"
                    + "    },\n"
                    + "{\n"
                    + "            type   : 'DURATION',\n"
                    + "            labels : ['DURATION'],\n"
                    + "            bgColor: '#99ffeb',\n"
                    + "            borderColor: 'darken'\n"
                    + "    }	]\n"
                    + "};";
            return s;

        } catch (Exception ex) {
            Logger.getLogger(Annotador.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
