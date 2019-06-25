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
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import static org.joda.time.format.ISODateTimeFormat.dateTime;
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

    String iniSP = "-03-20";
    String iniSU = "-06-21";
    String iniFA = "-09-22";
    String iniWI = "-12-21";

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
        } else if (lang.equalsIgnoreCase("EN")) {
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

    public DateTime getNextMonth(DateTime dt, int monthS) {
        int current = dt.getMonthOfYear();
        if (monthS <= current) {
            monthS += 12;
        }
        DateTime next = dt.plusMonths(monthS - current);
        return next;
    }

    public DateTime getLastMonth(DateTime dt, int monthS) {
        int current = dt.getMonthOfYear();
        if (monthS < current) {
            monthS = current - monthS;
        } else {
            monthS = 12 - monthS + current;
        }
        DateTime next = dt.minusMonths(monthS);
        return next;
    }

    public DateTime getNextDayWeek(DateTime dt, int dayW) {
        int current = dt.getDayOfWeek();
        if (dayW <= current) {
            dayW += 7;
        }
        DateTime next = dt.plusDays(dayW - current);
        return next;
    }

    public DateTime getLastDayWeek(DateTime dt, int dayW) {
        int current = dt.getDayOfWeek();
        if (dayW < current) {
            dayW = current - dayW;
        } else {
            dayW = 7 - dayW + current;
        }
        DateTime next = dt.minusDays(dayW);
        return next;
    }

    public String getNextMonthS(DateTime dt, String monthSS) {
        int current = dt.getMonthOfYear();
        String a = monthSS.replaceAll("MONTHS", "");
        int monthS = Integer.valueOf(a);
        String next;
        if (monthS <= current) {
            next = (dt.getYear() + 1) + "-" + String.format("%02d", monthS);
        } else {
            next = dt.getYear() + "-" + String.format("%02d", monthS);
        }
        return next;
    }

    public String getLastMonthS(DateTime dt, String monthSS) {
        int current = dt.getMonthOfYear();
        String a = monthSS.replaceAll("MONTHS", "");
        int monthS = Integer.valueOf(a);
        String next;
        if (monthS >= current) {
            next = (dt.getYear() + 1) + "-" + String.format("%02d", monthS);
        } else {
            next = dt.getYear() + "-" + String.format("%02d", monthS);
        }
        return next;
    }

    public String getNextDate(String dt, String refD) {
        DateTime dtDT = new DateTime(dt);
        if (refD.matches("\\d\\d\\d\\d-\\d\\d(-\\d\\d)?")) {
            return refD;
        } else if (refD.matches("XXXX-\\d\\d-\\d\\d")) {
            refD = refD.replaceAll("XXXX", dt.substring(0, 4));
            DateTime refDDT = new DateTime(refD);
            if (refDDT.isAfter(dtDT)) {
                return refD;
            } else {
                return refDDT.plusYears(1).toString("YYYY-MM-dd");
            }
        } else if (refD.matches("XXXX-XX-\\d\\d")) {
            refD = refD.replaceAll("XXXX", dt.substring(0, 4));
            refD = refD.replaceAll("XX", dt.substring(5, 7));
            DateTime refDDT = new DateTime(refD);
            if (refDDT.isAfter(dtDT)) {
                return refD;
            } else {
                return refDDT.plusMonths(1).toString("YYYY-MM-dd");
            }
        }
        return refD;
    }

    public String getLastDate(String dt, String refD) {
        DateTime dtDT = new DateTime(dt);
        if (refD.matches("\\d\\d\\d\\d-\\d\\d(-\\d\\d)?")) {
            return refD;
        } else if (refD.matches("XXXX-\\d\\d-\\d\\d")) {
            refD = refD.replaceAll("XXXX", dt.substring(0, 4));
            DateTime refDDT = new DateTime(refD);
            if (refDDT.isBefore(dtDT)) {
                return refD;
            } else {
                return refDDT.minusYears(1).toString("YYYY-MM-dd");
            }
        } else if (refD.matches("XXXX-XX-\\d\\d")) {
            refD = refD.replaceAll("XXXX", dt.substring(0, 4));
            refD = refD.replaceAll("XX", dt.substring(5, 7));
            DateTime refDDT = new DateTime(refD);
            if (refDDT.isBefore(dtDT)) {
                return refD;
            } else {
                return refDDT.minusMonths(1).toString("YYYY-MM-dd");
            }
        }

        return refD;
    }

    public String getNextSeason(String dt, String refD) {
        if (refD.matches("\\d\\d\\d\\d-[A-Z][A-Z]")) {
            return refD;
        }
        String year = dt.substring(0, 4);
        String season = refD.substring(4, 7);
        String seasondate = year;
        DateTime dtDT = new DateTime(dt);
        if (season.equalsIgnoreCase("-SU")) {
            seasondate = seasondate + iniSU;
        } else if (season.equalsIgnoreCase("-SP")) {
            seasondate = seasondate + iniSP;
        } else if (season.equalsIgnoreCase("-WI")) {
            seasondate = seasondate + iniWI;
        } else if (season.equalsIgnoreCase("-FA")) {
            seasondate = seasondate + iniFA;
        }
        DateTime refDDT = new DateTime(seasondate);

        if (refDDT.isAfter(dtDT)) {
            return year + season;
        } else {
            return refDDT.plusYears(1).toString("YYYY") + season;
        }
    }

    public String getLastSeason(String dt, String refD) {
        if (refD.matches("\\d\\d\\d\\d-[A-Z][A-Z]")) {
            return refD;
        }
        String year = dt.substring(0, 4);
        String season = refD.substring(4, 7);
        String seasondate = year;
        DateTime dtDT = new DateTime(dt);
        if (season.equalsIgnoreCase("-SU")) {
            seasondate = seasondate + iniSU;
        } else if (season.equalsIgnoreCase("-SP")) {
            seasondate = seasondate + iniSP;
        } else if (season.equalsIgnoreCase("-WI")) {
            seasondate = seasondate + iniWI;
        } else if (season.equalsIgnoreCase("-FA")) {
            seasondate = seasondate + iniFA;
        }
        DateTime refDDT = new DateTime(seasondate);

        if (refDDT.isBefore(dtDT)) {
            return year + season;
        } else {
            return refDDT.minusYears(1).toString("YYYY") + season;
        }
    }

    public LinkedHashMap<String, String> parseDuration(String input) {
        LinkedHashMap<String, String> durations = new LinkedHashMap<String, String>();
        Pattern pAnchor = Pattern.compile("(\\d*\\.?\\d+|X)([a-zA-Z]+)");

        Matcher m = pAnchor.matcher(input);
        while (m.find()) {
            String numb = m.group(1);
            String unit = m.group(2);
            durations.put(unit, numb);

        }
//        Pattern pAnchor = Pattern.compile("anchor\\((\\w+),([+-]?\\d+),(\\w+)\\)");

        return durations;
    }

    public String annotate(String input, String anchorDate) {
        Pattern pAnchor = Pattern.compile("anchor\\((\\w+),(.),([^\\)]+)\\)");
        String lastfullDATE = anchorDate; // Where we keep the last full date, in case we have to normalize
        String backupAnchor = anchorDate;
        String lastDATE = anchorDate; // Where we keep the last date, in case we have to normalize
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
                lastfullDATE = backupAnchor;
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

                    System.out.println(typ + " | " + val + " | " + freq + " | " + rul);

                    // TO DO: el get? poner los values!
                    numval++;
                    int ini = cm.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
                    String text = cm.get(CoreAnnotations.TextAnnotation.class);
//        out.println(matched.getText() + " - " + matched.getCharOffsets());

                    // To adapt to TE3 format - news mode
                    if (typ.equalsIgnoreCase("DATE") && val.startsWith("XXXX-XX") && anchorDate != null) {
                        DateTime dt = new DateTime(anchorDate);
                        int month = dt.getMonthOfYear();
                        int year = dt.getYear();
                        val = year + "-" + String.format("%02d", month) + val.substring(7, val.length());
                    } else if (typ.equalsIgnoreCase("DATE") && val.startsWith("XXXX") && anchorDate != null) {
                        DateTime dt = new DateTime(anchorDate);
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

                    if (typ.equalsIgnoreCase("DATE") && val.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d")) {
                        lastfullDATE = val;
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
            int tot = list.size();
            int i = 0;
            for (FileTempEval3ES f : list) {
                i++;
                System.out.println("--------> Doc num: " + i + "/" + tot);
                String input = f.getTextInput();
                String input2 = input.replaceAll("\\r\\n", "\\\\n");
                String output = annotate(input2, f.getDCTInput());
                if (!input.equals(input2)) {
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
