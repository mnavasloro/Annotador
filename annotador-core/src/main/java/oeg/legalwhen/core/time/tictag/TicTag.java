package oeg.annotador.core.time.tictag;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import oeg.annotador.core.data.FileTempEval3;
import oeg.annotador.core.data.FileTempEval3ES;
import oeg.annotador.core.data.FileTimeBank;
import oeg.annotador.core.data.ManagerTempEval3;
import oeg.annotador.core.data.ManagerTempEval3ES;
import oeg.annotador.core.data.ManagerTimeBank;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @author mnavas
 */
public class TicTag {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(TicTag.class);

    Map<String, String> map = new HashMap<String, String>();
    Map<TicTagRule, Double> ruleSet = new LinkedHashMap<TicTagRule, Double>();

    String lang = "en";

    /**
     * Initializes a instance of the tagger
     *
     * @param lang language (ES - Spanish, EN - English)
     * @return an instance of the tagger
     */
    public TicTag() {
        init();
    }

    public TicTag(String language) {
        lang = language;
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
        TicTagRule r;
        if (lang.equalsIgnoreCase("ES")) {
            r = new TicTagRule("fullDateNumberEU", "(" + dayMonthNES + divisorES + monthNES + divisorES + yearNES + ")", "DATE", "", "DD-MM-YYYY");
            ruleSet.put(r, 1.0);
            r = new TicTagRule("fullDateNumberUSA", "(" + monthNES + divisorES + dayMonthNES + divisorES + yearNES + ")", "DATE", "", "MM-DD-YYYY");
            ruleSet.put(r, 1.0);
            r = new TicTagRule("fullDateNumberUSA2", "(" + yearNES + divisorES + monthNES + divisorES + dayMonthNES + ")", "DATE", "", "YYYY-MM-DD");
            ruleSet.put(r, 1.0);
            r = new TicTagRule("longDate", "(" + dayMonthNES + "(?:, | de | a | )" + monthLES + " ?(?:(de )?" + yearN + ")?)", "DATE", "", "2 of April 2000 - 2, April 2000");
            ruleSet.put(r, 1.0);
//                r = new TicTagRule("longDate3", longDateES, "DATE", "", "2 of April 2000 - 2, April 2000");        
//                ruleSet.put(r,1.0);
            r = new TicTagRule("longDate2", "(" + monthLES + " (?: " + dayMonthNES + "(?:, )" + yearNES + "))", "DATE", "", "April 26, 2000");
            ruleSet.put(r, 1.0);
            r = new TicTagRule("monthYear", "(" + monthLES + " (?:(, |de )?" + yearNES + ")?)", "DATE", "", "April of 2000 - April 2000 - April, 2000");
            ruleSet.put(r, 2.0);
            r = new TicTagRule("period", "(" + periodES + "|" + periodAdvES + ")", "SET", "", "monthly, daily...");
            ruleSet.put(r, 2.0);
            r = new TicTagRule("ComplexGranularity", "(?:(" + complexGranularityES + "+" + "( de)? )*" + complexGranularityES + ")", "TIME", "", "Friday afternoon, the morning of Monday");
            ruleSet.put(r, 3.0);
            r = new TicTagRule("yearLES", "\\b" + yearLES + "\\b", "DATE", "", "Any number between 1000 and 2999 is considered a year");
            ruleSet.put(r, 4.0);
            r = new TicTagRule("DGranularity", complexDGranularityES, "DURATION", "", "those days, next months, during 3 years...");
            ruleSet.put(r, 4.0);
            r = new TicTagRule("TGranularity", complexTGranularityES, "DURATION", "", "those hours, during one minute...");
            ruleSet.put(r, 4.0);
            r = new TicTagRule("PDGranularity", complexPDGranularityES, "DURATION", "", "those mornings, next mornings...");    // weird     
            ruleSet.put(r, 4.0);
            r = new TicTagRule("monthL", "\\b(" + deictES + " )?" + monthLES + "\\b", "DATE", "", "that January, next January...");
            ruleSet.put(r, 5.0);
            r = new TicTagRule("dayWeekL", "\\b(" + deictES + " )?" + dayWeekLES + "\\b", "DATE", "", "that Monday, next Monday...");
            ruleSet.put(r, 5.0);
            r = new TicTagRule("presRef", "\\b(?:[A|a]hora|[A|a]ctualmente|[E|e]n el presente)\\b", "DATE", "", "refs to the present");
            ruleSet.put(r, 6.0);
            r = new TicTagRule("pastRef", "\\b(?:[E|e]n el pasado)\\b", "DATE", "", "refs to the past");
            ruleSet.put(r, 6.0);
            r = new TicTagRule("anchored", "\\b" + anchoredES + "\\b", "DATE", "", "today or tomorrow");
            ruleSet.put(r, 6.0);
            r = new TicTagRule("yearN", "\\b" + yearNES + "\\b", "DATE", "", "Any number between 1000 and 2999 is considered a year");
            ruleSet.put(r, 6.0);
        } else {
            r = new TicTagRule("fullDateNumberEU", "(" + dayMonthN + divisor + monthN + divisor + yearN + ")", "DATE", "", "DD-MM-YYYY");
            ruleSet.put(r, 1.0);
            r = new TicTagRule("fullDateNumberUSA", "(" + monthN + divisor + dayMonthN + divisor + yearN + ")", "DATE", "", "MM-DD-YYYY");
            ruleSet.put(r, 1.0);
            r = new TicTagRule("fullDateNumberUSA2", "(" + yearN + divisor + monthN + divisor + dayMonthN + ")", "DATE", "", "YYYY-MM-DD");
            ruleSet.put(r, 1.0);
            r = new TicTagRule("longDate", "(" + dayMonthN + "(?:, | of | )" + monthL + " ?(?:(of )?" + yearN + ")?)", "DATE", "", "2 of April 2000 - 2, April 2000");
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

    }

    /* ENGLISH */
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

    String longDate = "(" + dayMonthN + "(?: (?:of )?" + monthL + "(?: (?:of )?" + yearN + "))";

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

    /* SPANISH*/
    String monthLES = "(?:[E|e]nero|[F|f]ebrero|[M|m]arzo|[A|a]bril|[M|m]ayo|[J|j]unio|[J|j]ulio|Agosto|[S|s]eptiembre|[O|o]ctubre|[N|n]oviembre|[D|d]iciembre)";
    String dayWeekLES = "(?:[l|L]unes|[M|m]artes|[M|m]i[é|e]rcoles|[J|j]ueves|[V|v]iernes|[S|s][á|a]bado|[D|d]omingo)";
    String dayMonthNES = "(?:01|02|03|04|05|06|07|08|09|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31)";
    String monthNES = "(?:1|2|3|4|5|6|7|8|9|01|02|03|04|05|06|07|08|09|10|11|12)";
    String yearNES = "(?:[1|2]\\d\\d\\d)";
    String numberLES = "((?:(dos )?mil)( (?:ciento|doscientos|trescientos|cuatrocientos|quinientos|seiscientos|setecientos|ochocientos|novecientos))?( (((treinta|cuarenta|cincuenta|sesenta|setenta|ochenta|noventa)( y (?:uno|dos|tres|cuatro|cinco|seis|siete|ocho|nueve))?)|(diez|once|doce|trece|catorce|quince|dieciseis|diecisiete|dieciocho|diecinueve|veinte)|((?:veinti)?(?:uno|dos|tres|cuatro|cinco|seis|siete|ocho|nueve)))?))";
    String yearLES = "((?:(dos )?mil)( (?:ciento|doscientos|trescientos|cuatrocientos|quinientos|seiscientos|setecientos|ochocientos|novecientos))?( (((treinta|cuarenta|cincuenta|sesenta|setenta|ochenta|noventa)( y (?:uno|dos|tres|cuatro|cinco|seis|siete|ocho|nueve))?)|(diez|once|doce|trece|catorce|quince|dieciseis|diecisiete|dieciocho|diecinueve|veinte)|((?:veinti)?(?:uno|dos|tres|cuatro|cinco|seis|siete|ocho|nueve)))?))";
    String ordinalLES = "(?:[P|p]rimer(o|a)?|[S|s]egund(o|a)?|[T|t]ercer(o|a)?)";

    String DGranularityES = "(?:[D|d][í|i]a|[F|f]in de semana|semana|quincena|mes|bimestre|trimestre|semestre|año|d[é|e]cada|lustro|siglo)"; //season?
    String TGranularityES = "(?:segundo|minuto|hora)";
    String PDGranularityES = "(?:madrugada|mañana|tarde|tardenoche|noche)";
    String presRefES = "(?:ahora|ahora mismo|actualmente)";
    String pastRefES = "(?:(?<=[E|e]n )el pasado|[E|e]n (ese|el) (momento|instante)|[A|a]ntiguamente|[A|a]ntaño|[H|h]ace tiempo)";
    String divisorES = "[/|-]";
    String anchoredES = "([H|h]oy|[M|m]añana|[A|a]yer|[P|p]asado( mañana)?|[A|a]nteayer|[A|a]ntes de ayer)";
    String deictic2ES = "((?:[P|p]r[ó|o]ximo|[S|s]iguiente|[Ú|u|U|ú]ltim[o|a]|[A|a]nterior|[P|p]revi[o|a]|[P|p]osterior)(es|s)?)";
    String deictic2DurES = "(?:[M|m]uch[a|o]s|[U|u]n par de|[U|u]n[o|a]s cuant[o|a]s|[A|a]lgun[a|o]s|([U|u]n[a|o]s )?[P|p]oc[a|o]s)";
    String deictic1ES = "(?:[E|e]l|[L|l]a|[L|l]os|[L|l]as|[E|e]s[e|a]|[E|e]st[e|a]|[E|e]s[o|a]s|[E|e]st[o|a]s|[A|a]quell[o|a]s|[A|a]quel|[P|p]asad[o|a](s)?|[F|f]utur[o|a](s)?)";
    //COMMENT
    String deictic1DurES = "(?:[E|e]l|[L|l]a|[L|l]os|[L|l]as|[E|e]s[e|a]|[E|e]st[e|a]|[E|e]s[o|a]s|[E|e]st[o|a]s|[A|a]quell[o|a]s|[A|a]quel|[P|p]asad[o|a](s)?|[F|f]utur[o|a](s)?)";
    String deictES = "(" + deictic1ES + "|" + deictic2ES + "|" + deictic1ES + " " + deictic2ES + ")";
    String deictDurES = "(((en el transcurso de|durante) )?" + deictic1DurES + "|" + deictic2DurES + "|" + deictic1DurES + " " + deictic2DurES + ")";

    String randomNumberES = "\\d+";

    String GranularityES = "(" + DGranularityES + "|" + TGranularityES + "|" + PDGranularityES + ")";
    String periodES = "(?:cada|tod[o|a]s l[o|a]s) " + GranularityES;
    String periodAdvES = GranularityES + "mente";

//    String fullDateNumberEU = "(" + daymonthNES + divisor + monthNES + divisor + yearNES + ")";
//    String fullDateNumberUSA = "(" + monthNES + divisor + daymonthNES + divisor + yearNES + ")";
//    String fullDateNumberUSA2 = "(" + divisor + yearNES + monthNES + divisor + daymonthNES + ")";
    String longDateES = "(" + dayMonthNES + " de " + monthLES + "(?: (?:de )?" + yearNES + "))";
//    String longDateES = "(" + dayMonthNES + " de " + monthLES + " de " + yearNES + "))";

    String complexDGranularityES = "\\b(((" + deictDurES + "|" + deictES + "|" + numberLES + "|" + ordinalLES + "|" + randomNumberES + ")[ |-])+" + DGranularityES + "s?|\\b" + DGranularityES + "\\b)";
    String complexTGranularityES = "\\b(((" + deictDurES + "|" + deictES + "|" + numberLES + "|" + ordinalLES + "|" + randomNumberES + ")[ |-])+" + TGranularityES + "s?|\\b" + TGranularityES + "\\b)";
    String complexPDGranularityES = "\\b(((" + deictDurES + "|" + deictES + "|" + numberLES + "|" + ordinalLES + "|" + randomNumberES + ")[ |-])+" + PDGranularityES + "s?|\\b" + PDGranularityES + "\\b)";
    String complexdayWeekLES = "\\b(((" + deictDurES + "|" + deictES + "|" + numberLES + "|" + ordinalLES + "|" + randomNumberES + ")[ |-])+" + dayWeekLES + "s?|\\b" + dayWeekLES + "\\b)";

    String complexGranularityES = "(" + complexDGranularityES + "|" + complexTGranularityES + "|" + complexPDGranularityES + "|" + complexdayWeekLES + ")";

    //MODS (faltan BEFORE, AFTER, ON OR BEFORE, ON OR AFTER)
    String modApproxES = "(?:[S|s]obre( [el|la])?|[C|c]erca de(l)?|[E|e]n torno a(l)?|[A|a]proximadamente|[M|m][á|a]s o menos|[A|a]lrededor|[H|h]acia)";
    String modLessThanES = "(?:(?:[E|e]n )?([M|m]enos (de|que|a)|[D|d]entro de (un|una) (periodo|lapso|intervalo|plazo|duraci[ó|o]n) (menor|inferior) (de|que|a)|[U|u](n|na) (periodo|lapso|intervalo|plazo|duraci[ó|o]n) menor que))";
    String modEqualOrLessES = "(?:([E|e]n )?([N|n]o m[á|a]s (de|que|a)|[N|n]o mayor (de|que|a)|[N|n]o superior (de|que|a)|[N|n]o (despu[é|e]s|m[á|a]s tarde) (de|que|a)|[N|n]o en m[á|a]s (de|que|a)|[N|n]o en [U|u](n|na) (periodo|lapso|intervalo|plazo|duraci[ó|o]n) (superior|mayor|m[á|a]s larg[o|a]) (de|que|a)))";
    String modMoreThanES = "(?:([E|e]n )?([M|m][á|a]s (de|que|a)|[M|m]ayor (de|que|a)|[U|u](n|na) (periodo|lapso|intervalo|plazo|duraci[ó|o]n) (superior|mayor|m[á|a]s larg[o|a]) (de|que|a)))";
    String modEqualOrMoreES = "(?:([E|e]n )?([N|n]o menos (de|que|a)|[N|n]o menor (de|que|a)|[N|n]o inferior (de|que|a)|[N|n]o antes (de|que|a)|[N|n]o en menos (de|que|a)|[N|n]o en (un|una) (periodo|lapso|intervalo|plazo|duraci[ó|o]n) (menor|inferior) (de|que|a)))";
    String modStartES = "(?:([T|t]emprano|([A|a]l )?(principio|inicio|comienzo))( de)?)";
    String modMidES = "(?:([M|m]edi[o|a]|[M|m]itad( de)?|[M|m]ediados)( de)?)";
    String modEndES = "(?:([F|f]in(al)?|[Ú|u|U|ú]ltim[o|a](s)? parte(s)?|[T|t]arde)( de)?)";

    String modES = "(?:(?:" + modApproxES + "|" + modLessThanES + "|" + modEqualOrLessES + "|" + modMoreThanES + "|" + modEqualOrMoreES + "|" + modStartES + "|" + modMidES + "|" + modEndES + ")( en)?( (el|la|los|las))?)";

    public String annotate(String input) {
        try {
            String inp2 = input;
            int i = 0;
            for (TicTagRule r : ruleSet.keySet()) {
//            System.out.println("regla: " + r.id + ": " + r.rule);
                String rule = "(?:([T|t]he )?(?=" + mod + " )?" + r.rule + ")(?!(.(?!<TIMEX3))*?<\\/TIMEX3>)";
                if (lang.equalsIgnoreCase("ES")) {
                    rule = "(?:(([E|e]l|[L|l](a|as|os)) )?(?=" + modES + " )*" + r.rule + ")(?!(.(?!<TIMEX3))*?<\\/TIMEX3>)";
                }

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
            Logger.getLogger(TicTag.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(TicTag.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(TicTag.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean evaluateTE3ES() {
        try {
            ManagerTempEval3ES mte3 = new ManagerTempEval3ES();
            List<FileTempEval3ES> list = mte3.lista;
            for (FileTempEval3ES f : list) {
                String input = f.getTextInput();
                String output = annotate(input);
                f.writeOutputFile(output);
            }
        } catch (Exception ex) {
            Logger.getLogger(TicTag.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(TicTag.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(TicTag.class.getName()).log(Level.SEVERE, null, ex);
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
