package oeg.annotador.core.time.tictag;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import oeg.annotador.core.data.FileTempEval3;
import oeg.annotador.core.data.FileTimeBank;
import oeg.annotador.core.data.ManagerTempEval3;
import oeg.annotador.core.data.ManagerTimeBank;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mnavas
 */
public class TicTagRules {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(TicTagRules.class);

    Map<String, String> map = new HashMap<String, String>();
    Map<TicTagRule, Double> ruleSet = new LinkedHashMap<TicTagRule, Double>();

    /**
     * Obtains a list of people in a certain String.
     *
     * @param sentence A text
     * @return Array of strings with people
     */
    public TicTagRules() {
        init();
    }

    /*    public void init(){
//        map.put("monthL","(?:" + deict + "|(?<=in |\\.|,|\\. |, ))"+ monthL);
        map.put("monthL","\\b(" + deict + " )?" + monthL + "\\b");
        map.put("dayWeekL","\\b(" + deict + " )?"+ dayWeekL + "\\b");
//        map.put("dayMonthN","(?:01|02|03|04|05|06|07|08|09|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31)");
//        map.put("monthN","(?:1|2|3|4|5|6|7|8|9|01|02|03|04|05|06|07|08|09|10|11|12)");
        map.put("yearN", "\\b" + yearN + "\\b");
//        map.put("yearN","(?<=in |\\.|,|\\. |, )" + yearN);

        map.put("DGranularity", "\\b(((" + deict + "|" + numberL + "|" + ordinalL + "|" + randomNumber + ")[ |-])+" + DGranularity + "s?|\\b" + DGranularity + "\\b)");
        map.put("TGranularity", "\\b(((" + deict + "|" + numberL + "|" + ordinalL + "|" + randomNumber + ")[ |-])+" + TGranularity + "s?|\\b" + TGranularity + "\\b)");
        map.put("PDGranularity","\\b(((" + deict + "|" + numberL + "|" + ordinalL + "|" + randomNumber + ")[ |-])+" + PDGranularity + "s?|\\b" + PDGranularity + "\\b)");
        map.put("presRef","\\b(?:[N|n]ow|[C|c]urrently|[N|n]owadays)\\b");
        map.put("pastRef","\\b(?:[I|i]n the past)\\b");
//        map.put("divisor","[/|-]");
        map.put("anchored","\\b" + anchored + "\\b");
//        map.put("deict","(next|last|following|that|this)");

        map.put("fullDateNumberEU","(" + dayMonthN + divisor + monthN + divisor + yearN + ")");
        map.put("fullDateNumberUSA","(" + monthN + divisor + dayMonthN + divisor + yearN + ")");
        map.put("fullDateNumberUSA2","(" + divisor + yearN + monthN + divisor + dayMonthN + ")");
//        map.put("longDate", "(?:01|02|03|04|05|06|07|08|09|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31)\\s(?:, |of\\s)(?:January|February|March|April|May|June|July|August|September|October|November|December)\\s?(?:(of\\s)?(?:[1|2]\\d\\d\\d))?");
        map.put("longDate", "(" + dayMonthN + "(?:, | of )" + monthL + " ?(?:(of )?" + yearN + ")?)");
        map.put("period", "(" + period + "|" + periodAdv + ")");

//        map.put("numberL","((twenty|thirty|forty|fifty|sixty|seventy|eighty|ninety)(-(?:one|two|three|four|five|six|seven|eight|nine))?|(ten|eleven|twelve|thierteen|fourteen|fifteen|sixteen|seventeen|eighteen|nineteen)|(one|two|three|four|five|six|seven|eight|nine))");
//        map.put("ordinalL","(?:[F|f]irst|[S|s]econd|[T|t]hird)");
    }*/
    public void init() {
        TicTagRule r = new TicTagRule("fullDateNumberEU", "(" + dayMonthN + divisor + monthN + divisor + yearN + ")", "DATE", "", "DD-MM-YYYY");
        ruleSet.put(r, 1.0);
        r = new TicTagRule("fullDateNumberUSA", "(" + monthN + divisor + dayMonthN + divisor + yearN + ")", "DATE", "", "MM-DD-YYYY");
        ruleSet.put(r, 1.0);
        r = new TicTagRule("fullDateNumberUSA2", "(" + yearN + divisor + monthN + divisor + dayMonthN + ")", "DATE", "", "YYYY-MM-DD");
        ruleSet.put(r, 1.0);
        r = new TicTagRule("longDate", "(" + dayMonthN + "(?:, | of )" + monthL + " ?(?:(of )?" + yearN + ")?)", "DATE", "", "2 of April 2000 - 2, April 2000");
        ruleSet.put(r, 1.0);
        r = new TicTagRule("longDate2", "(" + monthL + " (?: " + dayMonthN + "(?:, )" + yearN + "))", "DATE", "", "April 26, 2000");
        ruleSet.put(r, 1.0);
        r = new TicTagRule("monthYear", "(" + monthL + " (?:(, |of )?" + yearN + ")?)", "DATE", "", "April of 2000 - April 2000 - April, 2000");
        ruleSet.put(r, 2.0);
        r = new TicTagRule("period", "(" + period + "|" + periodAdv + ")", "SET", "", "monthly, daily...");
        ruleSet.put(r, 2.0);
        r = new TicTagRule("ComplexGranularity", "(?:(" + complexGranularity + "+" + "( of)? )*" + complexGranularity + ")", "TIME", "", "Friday afternoon, the morning of Monday");
        ruleSet.put(r, 3.0);
        r = new TicTagRule("DGranularity", complexDGranularity, "DURATION", "", "those days, next months, during 3 years...");
        ruleSet.put(r, 4.0);
        r = new TicTagRule("TGranularity", complexTGranularity, "DURATION", "", "those hours, during one minute...");
        ruleSet.put(r, 4.0);
        r = new TicTagRule("PDGranularity", complexPDGranularity, "DURATION", "", "those mornings, next mornings...");    // weird     
        ruleSet.put(r, 4.0);
        r = new TicTagRule("monthL", "\\b(" + deict + " )?" + monthL + "\\b", "DATE", "", "that January, next January...");
        ruleSet.put(r, 5.0);
        r = new TicTagRule("dayWeekL", "\\b(" + deict + " )?" + dayWeekL + "\\b", "DATE", "", "that Monday, next Monday...");
        ruleSet.put(r, 5.0);
        r = new TicTagRule("presRef", "\\b(?:[N|n]ow|[C|c]urrently|[N|n]owadays)\\b", "DATE", "", "refs to the present");
        ruleSet.put(r, 6.0);
        r = new TicTagRule("pastRef", "\\b(?:[I|i]n the past)\\b", "DATE", "", "refs to the past");
        ruleSet.put(r, 6.0);
        r = new TicTagRule("anchored", "\\b" + anchored + "\\b", "DATE", "", "today or tomorrow");
        ruleSet.put(r, 6.0);
        r = new TicTagRule("yearN", "\\b" + yearN + "\\b", "DATE", "", "Any number between 1000 and 2999 is considered a year");
        ruleSet.put(r, 6.0);

    }

    String monthL = "(?:January|February|March|April|May|June|July|August|September|October|November|December)";
    String dayWeekL = "(?:Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday)";
    String dayMonthN = "(?:01|02|03|04|05|06|07|08|09|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31)";
    String monthN = "(?:1|2|3|4|5|6|7|8|9|01|02|03|04|05|06|07|08|09|10|11|12)";
    String yearN = "(?:[1|2]\\d\\d\\d)";
    String numberL = "((twenty|thirty|forty|fifty|sixty|seventy|eighty|ninety)(-(?:one|two|three|four|five|six|seven|eight|nine))?|(ten|eleven|twelve|thierteen|fourteen|fifteen|sixteen|seventeen|eighteen|nineteen)|(a|one|two|three|four|five|six|seven|eight|nine))";
    String ordinalL = "(?:[F|f]irst|[S|s]econd|[T|t]hird)";

    String DGranularity = "(?:day|weekend|week|fortnight|month|quarter|year|decade|century)"; //season?
    String TGranularity = "(?:second|minute|hour)";
    String PDGranularity = "(?:morning|afternoon|evening|night)";
    String presRef = "(?:now|currently|nowadays)";
    String pastRef = "(?:(?<=[I|i]n )the past|[A|t]t the time)";
    String divisor = "[/|-]";
    String anchored = "([T|t](oday|omorrow)|[Y|y]esterday)";
    String deictic2 = "(?:[N|n]ext|[L|l]ast|[F|f]ollowing|[P|p]revious)";
    String deictic2Dur = "(?:[S|s]everal|[A|a] couple of|[A|a] bunch of|[S|s]ome|[M|m]any|[F|f]ew|[r|R]ecent)";
    String deictic1 = "(?:[T|t]hat|[T|t]his|[T|t]he|[P|p]ast|[F|f]uture)";
    String deictic1Dur = "(?:[T|t]hose|[T|t]hese|[T|t]he|[P|p]ast|[F|f]uture)";
    String deict = "(" + deictic1 + "|" + deictic2 + "|" + deictic1 + " " + deictic2 + ")";
    String deictDur = "((during )?" + deictic1Dur + "|" + deictic2Dur + "|" + deictic1Dur + " " + deictic2Dur + ")";

    String randomNumber = "\\d+";

    String Granularity = "(" + DGranularity + "|" + TGranularity + "|" + PDGranularity + ")";
    String period = "(?:each|every) " + Granularity;
    String periodAdv = Granularity + "ly";

    String fullDateNumberEU = "(" + dayMonthN + divisor + monthN + divisor + yearN + ")";
    String fullDateNumberUSA = "(" + monthN + divisor + dayMonthN + divisor + yearN + ")";
    String fullDateNumberUSA2 = "(" + divisor + yearN + monthN + divisor + dayMonthN + ")";

    String longDate = "(" + dayMonthN + " of " + monthL + "(?: (?:of )?" + yearN + "))";

    String complexDGranularity = "\\b(((" + deictDur + "|" + deict + "|" + numberL + "|" + ordinalL + "|" + randomNumber + ")[ |-])+" + DGranularity + "s?|\\b" + DGranularity + "\\b)";
    String complexTGranularity = "\\b(((" + deictDur + "|" + deict + "|" + numberL + "|" + ordinalL + "|" + randomNumber + ")[ |-])+" + TGranularity + "s?|\\b" + TGranularity + "\\b)";
    String complexPDGranularity = "\\b(((" + deictDur + "|" + deict + "|" + numberL + "|" + ordinalL + "|" + randomNumber + ")[ |-])+" + PDGranularity + "s?|\\b" + PDGranularity + "\\b)";
    String complexdayWeekL = "\\b(((" + deictDur + "|" + deict + "|" + numberL + "|" + ordinalL + "|" + randomNumber + ")[ |-])+" + dayWeekL + "s?|\\b" + dayWeekL + "\\b)";

    String complexGranularity = "(" + complexDGranularity + "|" + complexTGranularity + "|" + complexPDGranularity + "|" + complexdayWeekL + ")";

    //MODS (faltan BEFORE, AFTER, ON OR BEFORE, ON OR AFTER)
    String modApprox = "(?:[A|a]bout|[N|n]early|[A|a]pproximately|[M|m]ore or less|[A|a]round|[T|t]owards)";
    String modLessThan = "(?:(?:[I|i]n )?([L|l]ess than|[M|m]inor than|[W|w]ithin|[A|a] period shorter than))";
    String modEqualOrLess = "(?:([I|i]n )?([N|n]o more than|[N|n]o major than|[N|n]o longer than|[N|n]o later than|[N|n]o in more than|[N|n]o in a period more than|[N|n]o in a period longer than))";
    String modMoreThan = "(?:([I|i]n )?([M|m]ore than|[M|m]ajor than|[A|a] period longer than))";
    String modEqualOrMore = "(?:([I|i]n )?([N|n]o less than|[N|n]o shorter than|[N|n]o minor than|[N|n]o sooner than|[N|n]o in less than|[N|n]o in a period less than|[N|n]o in a period shorter than))";
    String modStart = "(?:([E|e]arly|[A|a]t the beginning|[S|s]tart)( of)?)";
    String modMid = "(?:([M|m]id|[M|m]iddle)( of)?)";
    String modEnd = "(?:([E|e]nd|[L|l]ast part|[L|l]ate|[F|f]inal part)( of)?)";

    String mod = "(?:(?:" + modApprox + "|" + modLessThan + "|" + modEqualOrLess + "|" + modMoreThan + "|" + modEqualOrMore + "|" + modStart + "|" + modMid + "|" + modEnd + ")( on)?( the)?)";

    public String annotate(String input) {
        try {
            String inp2 = input;
            int i = 0;
            for (TicTagRule r : ruleSet.keySet()) {
                String rule = "(?:([T|t]he )?(?:" + mod + " )?" + r.rule + ")(?!(.(?!<TIMEX3))*?<\\/TIMEX3>)";
                input = input.replaceAll(rule, "<TIMEX3 tid=\"t" + i + "\" type=\"" + r.type + "\" value=\"" + r.norm + "\" rule=\"" + r.id + "\" >$0</TIMEX3>");
//            input = input.replaceAll(r.rule,"<TIMEX3 tid=\"t" + i + "\" type=\"" + r.type + "\" value=\"" + r.norm + "\" rule=\"" + r.id + " - " + r.desc + "\" >$0</TIMEX3>");
                i++;
            }
            return input;
//        Pattern pText = Pattern.compile(k);
//        Matcher mText = pText.matcher(input);

//        while (mText.find()) {
//            mText.group(1);
//        }
        } catch (Exception ex) {
            Logger.getLogger(TicTagRules.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String normalize(String input) {
        try {
            String inp2 = input;
            int i = 0;
            for (String k : map.keySet()) {
//            System.out.println(k + " : " + map.get(k));
                input = input.replaceAll(map.get(k), "<TIMEX3 tid=\"" + i + "\" type=\"DATE\" value=\"" + k + "\" >$0</TIMEX3>");
            }
            return input;
//        Pattern pText = Pattern.compile(k);
//        Matcher mText = pText.matcher(input);

//        while (mText.find()) {
//            mText.group(1);
//        }
        } catch (Exception ex) {
            Logger.getLogger(TicTagRules.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public boolean evaluateTE3() {
        try {
            ManagerTempEval3 mte3 = new ManagerTempEval3();
            List<FileTempEval3> list = mte3.lista;
            for (FileTempEval3 f : list) {
                String input = f.getTextInput();
                String output = annotate(input);
                f.writeOutputFile(output);
            }
        } catch (Exception ex) {
            Logger.getLogger(TicTagRules.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean evaluateTimeBank() {
        try {
            ManagerTimeBank mtb = new ManagerTimeBank();
            List<FileTimeBank> list = mtb.lista;
            for (FileTimeBank f : list) {
                String input = f.getTextInput();
                String output = annotate(input);
                f.writeOutputFile(output);
            }
        } catch (Exception ex) {
            Logger.getLogger(TicTagRules.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(TicTagRules.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

//    public static String[] getTimex(String sentence, String lan, String dtype, Date dct2) {
//        log.info("Sample log message");
//        List<String> list = new ArrayList();
//        try {
//            init(lan, dtype);
//            if (heidelTime == null) {
//                return list.toArray(new String[0]);
//            }
//            String str;
//            if(dtype!=null && dtype.equals("news")){
//                Date dct = Calendar.getInstance().getTime();
//                if(dct2!=null)
//                    dct=dct2;
//                str = heidelTime.process(sentence, dct);
//            }
//            else{
//                if(dct2!=null)
//                    str = heidelTime.process(sentence, dct2);
//                else
//                    str = heidelTime.process(sentence);
//            }
//                    
////            System.out.print(str);
//            String pattern = "<TIMEX3 [^>]*type=\"([^\"]*)\" [^v]*value=\"([^\"]*)\">([^<]*)<\\/TIMEX3>";
//            Pattern p = Pattern.compile(pattern);
//            Matcher m = p.matcher(str);
//            while(m.find()){
//                list.add(m.group(3) + " (" + m.group(1) + " - " + m.group(2) + ")");
//            }
//            
//        } catch (Exception e) {
//        }
//        return list.toArray(new String[0]);
//    }
//    
//    public static String getTimeML(String sentence, String lan, String dtype, Date dct2) {
//        log.info("Sample log message");
//        try {
//            init(lan, dtype);
//            if (heidelTime == null) {
//                return "";
//            }
//            String str = "";
//            if(dtype!=null && dtype.equals("news")){
//                Date dct = Calendar.getInstance().getTime();
//                if(dct2!=null)
//                    dct=dct2;
//                str = heidelTime.process(sentence, dct);
//            }
//            else{
//                if(dct2!=null)
//                    str = heidelTime.process(sentence, dct2);
//                else
//                    str = heidelTime.process(sentence);
//            }
//            return str;
//        } catch (Exception e) {
//        }
//        return "";
//    }
    /**
     * ****************** PRIVATE METHODS
     * ******************************************
     */
//    private static HeidelTimeStandalone heidelTime = null;
//    private static void init() {
//        try {
//            
//            heidelTime  = new HeidelTimeStandalone(Language.ENGLISH, DocumentType.NARRATIVES, OutputType.TIMEML,
//                    "../lynx-rest/src/main/resources/config.props", POSTagger.NO, true);            
//            
//        } catch (Exception ex) {
//            Logger.getLogger(ManagerHeidelTime.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    private static void init(String lan, String dtype) {
//        try {
//            
////            File f = new File("../lynx-rest/src/main/resources/config.props");
//            File f = new File("");
//            String path = f.getCanonicalPath() + "/src/main/resources/config.props";
//            log.info("reading from: " + path);
//            log.info("-------------\n He leido: " + FileUtils.readFileToString(new File(path), "UTF-8") + "\n-------------\n");
//           
//            Language lang = Language.ENGLISH;
//            if(lan!=null){
//                if(lan.equals("es")){
//                    lang = Language.SPANISH;
//                }
//                else if(lan.equals("it")){
//                    lang = Language.ITALIAN;
//                }
//                else if(lan.equals("de")){
//                    lang = Language.GERMAN;
//                }
//            }
//            
//            DocumentType dt = DocumentType.NARRATIVES;
//            if(dtype!=null){
//                if(dtype.equals("news")){
//                    dt = DocumentType.NEWS;
//                    heidelTime  = new HeidelTimeStandalone(lang, dt, OutputType.TIMEML,
//                    path, POSTagger.NO, true);
//                }
//                else if(dtype.equals("colloquial")){
//                    dt = DocumentType.COLLOQUIAL;
//                }
//                else if(dtype.equals("scientific")){
//                    dt = DocumentType.SCIENTIFIC;
//                }
//            }
//            
//            log.info("-------------\n LANG: " + lang.getName());
//            log.info("\n-------------\n DT: " + dt.toString());
//            log.info("\n-------------");
//            heidelTime  = new HeidelTimeStandalone(lang, dt, OutputType.TIMEML,
//                    path , POSTagger.NO, true);            
//            
//        } catch (Exception ex) {
//            Logger.getLogger(TicTag.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
