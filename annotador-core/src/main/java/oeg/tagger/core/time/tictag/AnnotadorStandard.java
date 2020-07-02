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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import oeg.tagger.core.servlets.Salida;
import org.joda.time.DateTime;

/**
 * Annotador core class, where the rules are applied and the normalization algorithm is.
 *
 * @author mnavas
 */
public class AnnotadorStandard  extends AnnotadorLegal  implements Annotador {

    private static final Logger logger = Logger.getLogger(AnnotadorStandard.class.getName());


    /**
     * Initializes a instance of the tagger
     *
     * @param lang language (ES - Spanish, EN - English)
     * @return an instance of the tagger
     */
    public AnnotadorStandard() {
        init();
    }

    public AnnotadorStandard(String language) {
        lang = language;
        init();
    }

    public AnnotadorStandard(String pos, String lemma, String rul, String language) {
        posModel = pos;
        lemmaModel = lemma;
        rules = rul;
        lang = language;
        init();
    }

    public AnnotadorStandard(String rul, String language) {
        rules = rul;
        lang = language;
        init();
    }

    @Override
    public void init() {

        if (lang.equalsIgnoreCase("ES")) {
            if (rules == null) {
                rules = "./src/main/resources/rules/rulesES-standard.txt";
            }

//        out = new PrintWriter(System.out);
            properties = StringUtils.argsToProperties(new String[]{"-props", "StanfordCoreNLP-spanish.properties"});

            if (posModel == null) {
                posModel = "./src/main/resources/ixa-pipes/morph-models-1.5.0/es/es-pos-perceptron-autodict01-ancora-2.0.bin";
            }
            if (lemmaModel == null) {
                lemmaModel = "./src/main/resources/ixa-pipes/morph-models-1.5.0/es/es-lemma-perceptron-ancora-2.0.bin";
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
        } else if (lang.equalsIgnoreCase("EN")) {
            if (rules == null) {
                rules = "./src/main/resources/rules/rulesEN-standard.txt";
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


    @Override
    public String annotate(String input, String anchorDate) {
        
        try{
            DateTime a = new DateTime(anchorDate);
        } catch(Exception e){
            Date dct = Calendar.getInstance().getTime();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            anchorDate = df.format(dct);
        }
        
        Pattern pAnchor = Pattern.compile("anchor\\((\\w+),(.),([^\\)]+)\\)");
        String lastfullDATE = anchorDate; // Where we keep the last full date, in case we have to normalize
        String backupAnchor = anchorDate;
        String lastDATE = anchorDate; // Where we keep the last date, in case we have to normalize
//        Pattern pAnchor = Pattern.compile("anchor\\((\\w+),([+-]?\\d+),(\\w+)\\)");
        try {
            String inp2 = input;
            int flagRN = 0;

            inp2 = inp2.replaceAll("\\r\\n", "\n");
//            inp2 = inp2.replaceAll("\\n", "\n\n\n\n");
//            inp2 = inp2.replaceAll("([^\\n])\\n([^\\n])", "$1\n\n\n\n$2");
            inp2 = inp2.replaceAll("([^\\n])\\n([^\\n])", "$1\nxyyz128945shb\n$2");

            int offsetdelay = 0;
            int numval = 0;
            Annotation annotation = new Annotation(inp2);

            pipeline.annotate(annotation);

            // An Annotation is a Map and you can get and use the various analyses individually.
//            out.println();
            // The toString() method on an Annotation just prints the text of the Annotation
            // But you can see what is in it with other methods like toShorterString()
//            out.println("The top level annotation");
//            System.out.println(annotation.toShorterString());

            List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
                lastfullDATE = backupAnchor;
            for (CoreMap sentence : sentences) {
                lastDATE = backupAnchor;
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

                    logger.info(typ + " | " + val + " | " + freq + " | " + rul);

                    // TO DO: el get? poner los values!
                    numval++;
                    int ini = cm.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
                    String text = cm.get(CoreAnnotations.TextAnnotation.class);
//        out.println(matched.getText() + " - " + matched.getCharOffsets());

                    // To adapt to TE3 format - news mode
                    if ((typ.equalsIgnoreCase("DATE") || typ.equalsIgnoreCase("TIME")) && val.startsWith("XXXX-XX") && anchorDate != null) {
                        DateTime dt = new DateTime(lastfullDATE);
//                        DateTime dt = new DateTime(anchorDate);
                        int month = dt.getMonthOfYear();
                        int year = dt.getYear();
                        val = year + "-" + String.format("%02d", month) + val.substring(7, val.length());
                    } else if ((typ.equalsIgnoreCase("DATE") || typ.equalsIgnoreCase("TIME")) && val.startsWith("XXXX") && anchorDate != null) {
                        DateTime dt = new DateTime(lastfullDATE);
//                        DateTime dt = new DateTime(anchorDate);
                        int year = dt.getYear();
                        val = year + val.substring(4, val.length());
                    }

                    // To adapt to TE3 format
                    val = val.replaceAll("-X+", "");

                    // TODO: also, use the dependency parsing to find modifiers
                    // TODO: the ref can be other day...
                    if (val.startsWith("Danchor(+,") && anchorDate != null) {
                        String refDate = val.substring(10, val.length() - 1);
                        val = getNextDate(anchorDate, refDate);
                    } else if (val.startsWith("Danchor(-,") && anchorDate != null) {
                        String refDate = val.substring(10, val.length() - 1);
                        val = getLastDate(anchorDate, refDate);
                    } else if (val.startsWith("Sanchor(+,") && anchorDate != null) {
                        String refDate = val.substring(10, val.length() - 1);
                        val = getNextSeason(anchorDate, refDate);
                    } else if (val.startsWith("Sanchor(-,") && anchorDate != null) {
                        String refDate = val.substring(10, val.length() - 1);
                        val = getLastSeason(anchorDate, refDate);
                    } else if (val.startsWith("Ranchor(+,") && anchorDate != null) {
                        String gran = val.substring(10, val.length() - 1);
                        DateTime dat = new DateTime(anchorDate);
                        if(gran.equalsIgnoreCase("M")){
                            int day = dat.getDayOfMonth();
                            int maxM = dat.dayOfMonth().getMaximumValue();
                            val = (maxM - day) + "D";
                        } else if(gran.equalsIgnoreCase("Y")){
                            int day = dat.getDayOfMonth();
                            int maxM = dat.dayOfMonth().getMaximumValue();
                            if(dat.getMonthOfYear() != 12){
                            val = (12 - dat.getMonthOfYear()) + "M" + (maxM - day) + "D";
                            } else{
                                val = (maxM - day) + "D";
                            }
                        }                       
                    } else if (val.startsWith("Ranchor(-,") && anchorDate != null) {
                        String gran = val.substring(10, val.length() - 1);
                        DateTime dat = new DateTime(anchorDate);
                        if(gran.equalsIgnoreCase("M")){
                            int day = dat.getDayOfMonth();
                            val = day + "D";
                        } else if(gran.equalsIgnoreCase("Y")){
                            int day = dat.getDayOfMonth();                            
                           if(dat.getMonthOfYear() != 1){
                                if(day == 1){
                                    val = (dat.getMonthOfYear()-1) + "M";
                                }
                                else{
                                    val = (dat.getMonthOfYear()-1) + "M" + (day-1) + "D";
                                }
                            } else{
                                val = (day -1) + "D";
                            }
                        }
                    } else if (val.startsWith("DWanchor(+,") && anchorDate != null) {
                        String refDate = val.substring(11, val.length() - 1);
                        val = getNextMonthS(new DateTime(anchorDate), refDate);
                    } else if (val.startsWith("DWanchor(-,") && anchorDate != null) {
                        String refDate = val.substring(11, val.length() - 1);
                        val = getLastMonthS(new DateTime(anchorDate), refDate);
                    } else if (val.startsWith("anchor") && anchorDate != null) {
                        DateTime dt = new DateTime(anchorDate);

                        Matcher m = pAnchor.matcher(val);
                        m.find();
                        String ref = m.group(1);
                        String plus = m.group(2);
                        String duration = m.group(3);

                        LinkedHashMap<String, String> durations = new LinkedHashMap<String, String>();
                        // If it is an anchor for a date (eg, "this month")
                        if (plus.equalsIgnoreCase("x")) {
                            durations.put(duration, "0");
                        } else {
                            durations = parseDuration(duration);
                        }

                        Set<String> durString = durations.keySet();

                        for (String gran : durString) {

                            int plusI = Integer.valueOf(durations.get(gran));

                            // Needs to be more general, check if today, proceed otherwise if not
                            if (gran.equalsIgnoreCase("D")) {
                                if (plus.equalsIgnoreCase("+")) {
                                    dt = dt.plusDays(plusI);
                                } else if (plus.equalsIgnoreCase("-")) {
                                    dt = dt.minusDays(plusI);
                                } else {
                                    dt = new DateTime(lastfullDATE);
                                    val = dt.toString("YYYY-MM-dd") + val.substring(val.lastIndexOf(")") + 1);

                                }
                            } else if (gran.equalsIgnoreCase("M")) {
                                if (plus.equalsIgnoreCase("+")) {
                                    dt = dt.plusMonths(plusI);
                                } else if (plus.equalsIgnoreCase("-")) {
                                    dt = dt.minusMonths(plusI);
                                } else {
                                    dt = new DateTime(lastfullDATE);
                                    val = dt.toString("YYYY-MM");
                                }
                            } else if (gran.equalsIgnoreCase("Y")) {
                                if (plus.equalsIgnoreCase("+")) {
                                    dt = dt.plusYears(plusI);
                                } else if (plus.equalsIgnoreCase("-")) {
                                    dt = dt.minusYears(plusI);
                                } else {
                                    dt = new DateTime(lastfullDATE);
                                    val = dt.toString("YYYY");
                                }
                            } else if (gran.equalsIgnoreCase("CENT")) {
                                if (plus.equalsIgnoreCase("+")) {
                                    dt = dt.plusYears(plusI * 100);
                                } else if (plus.equalsIgnoreCase("-")) {
                                    dt = dt.minusYears(plusI * 100);
                                } else {
                                    val = (dt.plusYears(100)).toString("YYYY");
                                    if (val.length() == 4) {
                                        val = val.substring(0, 2);
                                    } else if (val.length() == 3) {
                                        val = "0" + val.substring(0, 1);
                                    } else {
                                        val = "00";
                                    }
                                }
                            } else if (gran.equalsIgnoreCase("W")) {
                                if (plus.equalsIgnoreCase("+")) {
                                    dt = dt.plusWeeks(plusI);
                                } else if (plus.equalsIgnoreCase("-")) {
                                    dt = dt.minusWeeks(plusI);
                                } else {
                                    val = dt.toString("YYYY") + "-W" + String.format("%02d",dt.getWeekOfWeekyear());
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

                            } else if (gran.equalsIgnoreCase("DAYW")) {
                                if (plus.equalsIgnoreCase("+")) {
                                    dt = getNextDayWeek(dt, plusI);
                                } else if (plus.equalsIgnoreCase("-")) {
                                    dt = getLastDayWeek(dt, plusI);
                                } else if (plus.equalsIgnoreCase("z")) {
                                    int current = dt.getDayOfWeek();
                                    if (plusI <= current) {
                                        dt = dt.minusDays(current - plusI);
                                    } else {
                                        dt = dt.plusDays(plusI - current);
                                    }
                                }
                            } else if (gran.startsWith("Q")) {
                                if (plus.equalsIgnoreCase("x") && plus.matches("Q\\d+")) {
                                    val = dt.toString("YYYY") + "-" + gran;
                                } else {
                                    if (plus.equalsIgnoreCase("+")) {
                                        dt = dt.plusMonths(3 * plusI);
                                    } else if (plus.equalsIgnoreCase("-")) {
                                        dt = dt.minusMonths(3 * plusI);
                                    }
                                    if (dt.getMonthOfYear() < 4) {
                                        val = dt.toString("YYYY") + "-Q1";
                                    } else if (dt.getMonthOfYear() < 7) {
                                        val = dt.toString("YYYY") + "-Q2";
                                    } else if (dt.getMonthOfYear() < 10) {
                                        val = dt.toString("YYYY") + "-Q3";
                                    } else {
                                        val = dt.toString("YYYY") + "-Q4";
                                    }
                                }
                            } else if (gran.startsWith("HALF")) {
                                if (plus.equalsIgnoreCase("x") && plus.matches("HALF\\d+")) {
                                    val = dt.toString("YYYY") + "-" + gran.replaceFirst("ALF", "");
                                } else {
                                    if (plus.equalsIgnoreCase("+")) {
                                        dt = dt.plusMonths(6 * plusI);
                                    } else if (plus.equalsIgnoreCase("-")) {
                                        dt = dt.minusMonths(6 * plusI);
                                    }
                                    if (dt.getMonthOfYear() < 7) {
                                        val = dt.toString("YYYY") + "-H1";
                                    } else {
                                        val = dt.toString("YYYY") + "-H2";
                                    }
                                }
                            } else if (gran.startsWith("T")) {
                                if (plus.equalsIgnoreCase("x") && plus.matches("T\\d+")) {
                                    val = dt.toString("YYYY") + "-" + gran;
                                } else {
                                    if (plus.equalsIgnoreCase("+")) {
                                        dt = dt.plusMonths(4 * plusI);
                                    } else if (plus.equalsIgnoreCase("-")) {
                                        dt = dt.minusMonths(4 * plusI);
                                    }
                                    if (dt.getMonthOfYear() < 5) {
                                        val = dt.toString("YYYY") + "-T1";
                                    } else if (dt.getMonthOfYear() < 9) {
                                        val = dt.toString("YYYY") + "-T2";
                                    } else {
                                        val = dt.toString("YYYY") + "-T3";
                                    }
                                }
                            } else if (gran.equalsIgnoreCase("MONTHS")) {
                                if (plus.equalsIgnoreCase("+")) {
                                    dt = getNextMonth(dt, plusI);
                                } else {
                                    dt = getLastMonth(dt, plusI);
                                }
                            }
                        }

                        if (val.matches("anchor\\([A-Z]+,.,.*(\\d+)W\\)")) {
                            val = dt.getYear() + "-W" + String.format("%02d",dt.getWeekOfWeekyear());
                        } else if (val.matches("anchor\\([A-Z]+,.,.*(\\d+)Y\\)")) {
                            val = dt.toString("YYYY");
                        } else if (val.matches("anchor\\([A-Z]+,.,.*(\\d+)M\\)")) {
                            val = dt.toString("YYYY-MM");
                        } else if (val.matches("\\d{0,4}-[H|T|Q]\\d")) {
                        } else if (!plus.equalsIgnoreCase("x")) {
                            val = dt.toString("YYYY-MM-dd") + val.substring(val.lastIndexOf(")") + 1);
                        } else {

                        }
                    }

                    if ((typ.equalsIgnoreCase("DURATION") || typ.equalsIgnoreCase("SET"))) {
                        LinkedHashMap<String, String> auxVal = parseDuration(val);
                        String auxfin = "P";
                        int flagT = 0;
                        int mins = 0;
                        Set<String> durString = auxVal.keySet();
                        for (String gran : durString) {
                            if ((gran.equalsIgnoreCase("AF") || gran.equalsIgnoreCase("MO") || gran.equalsIgnoreCase("MI") || gran.equalsIgnoreCase("EV") || gran.equalsIgnoreCase("NI")) && flagT == 0) {
                                flagT = 1;
                                auxfin = auxfin + "T" + auxVal.get(gran).replaceFirst("\\.0", "") + gran;
                            } else if (gran.equalsIgnoreCase("H") && flagT == 0) {
                                flagT = 1;
                                auxfin = auxfin + "T" + auxVal.get(gran).replaceFirst("\\.0", "") + gran;
                            } else if (gran.equalsIgnoreCase("MIN") && flagT == 0) {
                                flagT = 1;
                                auxfin = auxfin + "T" + auxVal.get(gran).replaceFirst("\\.0", "") + "M";
                            } else if (gran.equalsIgnoreCase("HALF")) {
                                flagT = 1;
                                auxfin = auxfin + auxVal.get(gran).replaceFirst("\\.0", "") + "H";
                            } else if (gran.equalsIgnoreCase("S") && flagT == 0) {
                                flagT = 1;
                                auxfin = auxfin + "T" + auxVal.get(gran).replaceFirst("\\.0", "") + gran;
                            } else {
                                auxfin = auxfin + auxVal.get(gran).replaceFirst("\\.0", "") + gran;
                            }
                        }
                        val = auxfin;
                        val = val.replaceFirst("MIN", "M");
                        val = val.replaceFirst("HALF", "H");

                    }
                    if (typ.equalsIgnoreCase("TIME") && val.startsWith("T")) {
                        val = lastfullDATE + val;
                    }
                    if (typ.equalsIgnoreCase("TIME") && val.matches("....-..-..(Tanchor\\(.*,.*,.*\\))*.*")) { //for date + time anchorbug
                        val = val.replaceAll("(anchor\\(.*,.*,.*\\))", "");
                        val = val.replaceAll("T+", "T");
                    }

                    if (typ.equalsIgnoreCase("DATE") && val.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d")) {
                        lastfullDATE = val;
                    }
                    
                    if (typ.equalsIgnoreCase("TIME") && val.startsWith("\\d\\d\\d\\d-\\d\\d-\\d\\d")) {
                        lastfullDATE = val.substring(0,10);
                    }
                    
                    if (typ.equalsIgnoreCase("DATE")) {
                        lastDATE = val;
                    }
                    String addini = "<TIMEX3 tid=\"t" + numval + "\" type=\"" + typ + "\" value=\"" + val + "\">";
                    if (!freq.isEmpty()) {
                        addini = "<TIMEX3 tid=\"t" + numval + "\" type=\"" + typ + "\" value=\"" + val + "\" freq=\"" + freq + "\">";
                    }
                    String addfin = "</TIMEX3>";

                    
                    String toAdd = addini + text + addfin;
                    if(text.endsWith(" ,")){
                        toAdd = addini + text.substring(0, text.length()-2) + addfin + " ,";
                    }
                    else if(text.endsWith(",")){
                        toAdd = addini + text.substring(0, text.length()-1) + addfin + ",";
                    } else if(text.endsWith(" .")){
                        toAdd = addini + text.substring(0, text.length()-2) + addfin + " .";
                    }
                    else if(text.endsWith(".")){
                        toAdd = addini + text.substring(0, text.length()-1) + addfin + ".";
                    } else if(text.endsWith(" ;")){
                        toAdd = addini + text.substring(0, text.length()-2) + addfin + " ;";
                    }
                    else if(text.endsWith(";")){
                        toAdd = addini + text.substring(0, text.length()-1) + addfin + ";";
                    }
                    
                    inp2 = inp2.substring(0, ini + offsetdelay) + toAdd + inp2.substring(ini + text.length() + offsetdelay);

                    offsetdelay = offsetdelay + toAdd.length() - text.length();

                }
            }
//            if(flagRN==1){
//            inp2 = inp2.replaceAll("\\n\\n\\n\\n", "\n");
                        inp2 = inp2.replaceAll("\\nxyyz128945shb\\n", "\n");

            inp2 = inp2.replaceAll("\\n", "\r\n");
//            }

            inp2 = uniformOutp(inp2,input);
            return inp2;

        } catch (Exception ex) {
            Logger.getLogger(AnnotadorStandard.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
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
            Logger.getLogger(AnnotadorStandard.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private String uniformOutp(String cuerpoAnnotated, String cuerpo) {
        String cuerpoMerge = cuerpo;
        try{
        if (cuerpoAnnotated != null && cuerpo != null) {
            int j = 0; //to iterate in cuerpoAnnotated
            int i = 0; //to iterate in cuerpoMerge
            while (i < cuerpoMerge.length()) {
                while (i < cuerpoMerge.length() && (cuerpoMerge.charAt(i) == '\n' || cuerpoMerge.charAt(i) == '\r')){
                    i++;
                }
                while (j < cuerpoAnnotated.length() && (cuerpoAnnotated.charAt(j) == '\n' || cuerpoAnnotated.charAt(j) == '\r')){
                    j++;
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
                        
                        // Add duplicates
                        String intimexchanged = cuerpoMerge.substring(i, i + k2);
//                        if(intimexchanged.contains("<")){
//                            pretimex = pretimex.replaceFirst(">", " duplicated=\"true\">");                            
//                            intimexchanged = intimexchanged.replaceAll("(<[^>]+>)", "</TIMEX3>" + "$1" + pretimex);
//                        }
                        
                        
                        //
                        
                        cuerpoMerge = cuerpoMerge.substring(0, i) + pretimex + intimexchanged + "</TIMEX3>" + cuerpoMerge.substring(i + k2);
//                        cuerpoMerge = cuerpoMerge.substring(0,i) + cuerpoAnnotated.substring(j, j2)+ cuerpoMerge.substring(i+k2);

//                        i = i + k2 + pretimex.length() + "</TIMEX3>".length();
                        i = i + intimexchanged.length() + pretimex.length() + "</TIMEX3>".length();
//                        i = i + j2 - j;
                        j = j2;
                    } else {

                        cuerpoMerge = cuerpoMerge.substring(0, i) + cuerpoAnnotated.substring(j, j2) + cuerpoMerge.substring(i + lengtnew);
                        i = i + j2 - j;
                        j = j2;

                    }
//                    if (cuerpoMerge.charAt(i) == '<') {
                        i--;
                        j--;
//                    }
                } else if (j < cuerpoAnnotated.length() && cuerpoAnnotated.charAt(j) == '<' && cuerpoAnnotated.substring(j + 1, j + 9).equalsIgnoreCase("INTERVAL")) {
                    int j1 = cuerpoAnnotated.indexOf(">", j) + 1;
                    int j2 = cuerpoAnnotated.indexOf("</INTERVAL>", j) + "</INTERVAL>".length();
                    int j3 = cuerpoAnnotated.indexOf("</INTERVAL>", j);
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
                        cuerpoMerge = cuerpoMerge.substring(0, i) + pretimex + cuerpoMerge.substring(i, i + k2) + "</INTERVAL>" + cuerpoMerge.substring(i + k2);
//                        cuerpoMerge = cuerpoMerge.substring(0,i) + cuerpoAnnotated.substring(j, j2)+ cuerpoMerge.substring(i+k2);

                        i = i + k2 + pretimex.length() + "</INTERVAL>".length();
//                        i = i + j2 - j;
                        j = j2;
                    } else {

                        cuerpoMerge = cuerpoMerge.substring(0, i) + cuerpoAnnotated.substring(j, j2) + cuerpoMerge.substring(i + lengtnew);
                        i = i + j2 - j;
                        j = j2;

                    }
//                    if (cuerpoMerge.charAt(i) == '<') {
                        i--;
                        j--;
//                    }
                }
                i++;
                j++;
            }
            return cuerpoMerge;
        }
        } catch(Exception e){
            System.err.println("Error while setting the text uniform");
            System.err.println(e.toString());
        }
        return cuerpoAnnotated;
    }

}
