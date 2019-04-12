/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.tagger.rulemanager;

/**
 *
 * @author mnavas
 */
public class ReaderTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String test = "{ ruleType:   \"tokens\",\n" +
"  pattern: ( (?$amoun[{myNUM:\"myNUMBER\"} & !{timexType:\"DURATION\"} & !{timexType:\"TIME\"}]+)  (?$gran [{lemma:\"quarter\"} & !{timexType:\"DURATION\"} & !{timexType:\"TIME\"}]) (?$plus [{lemma:\"and\"}] [{lemma:\"a\"}]? (?$added [{lemma:\"half\"} | {lemma:\"quarter\"} & !{timexType:\"DURATION\"} & !{timexType:\"TIME\"}] | ([{lemma:\"three\"} | {lemma:\"3\"}] [{lemma:\"quarter\"}])))? ),\n" +
"    action: ( Annotate($gran, \"myNERnormalized\", \"3_MONTH\"), Annotate($0, \"timexType\", \"DURATION\"), Annotate($0, \"myRule\", \"Rule$quarter+half\"), Annotate($0, \"timexValue\",  Concat(Multiply(DGRANULARITY_AMOUNT_MAP[$gran[0].myNERnormalized], $amoun[0].myTValue),DGRANULARITY_UNIT_MAP[$gran[0].myNERnormalized], :case{($plus != NIL) => :case{($added[0].lemma == \"half\") => HALF_DURATION_MAP[$gran[0].myNERnormalized], ($added[0].lemma == \"quarter\") => QUARTER_DURATION_MAP[$gran[0].myNERnormalized], :else => TQUARTER_DURATION_MAP[$gran[0].myNERnormalized]}, :else => \"\"})), Annotate($0, DURATION_MAP[$gran[0].myNERnormalized], $gran[0].timexValue), Annotate($0, \"Period\", PERIOD_MAP[$gran[0].myNERnormalized]) ) , \n" +
"  stage : 50  }\n" +
"\n" +
"\n" +
"\n" +
"  \n" +
"{ ruleType: \"composite\",\n" +
"priority: 2.0,\n" +
"  pattern: ( (?$month [{myNER:\"MONTHS\"}]) (?$day [{myNER:\"DAYMONTH\"} | {myNER:\"MONTHN\"} | ({myNUM:\"myNUMBER\"} & {mySTValue:/([1-2][0-9])|0?[1-9]|3[0-1]/})]) [{lemma:\",\"}]? (?$year [{myNER:\"YEAR\"}])?) ,\n" +
"  action: ( Annotate($0, \"myDateDay\", $day[0].myNERnormalized), Annotate($0, \"myDateMonth\", $month[0].myNERnormalized), Annotate($0, \"myDateYear\", :case{ ($$year != NIL) => $year[0].myNERnormalized, :else => \"XXXX\"}), Annotate($0, \"myRule\", \"Rule$DD_MM,YYYY\") ), \n" +
"  result: ( \"DATE\", Concat($0[0].myDateYear, \"-\", $0[0].myDateMonth, \"-\", $0[0].myDateDay), \"\", \"\", \"Rule$DD_MM,YYYY\")  ,\n" +
"  stage : 25  }  \n" +
"   \n" +
"{ ruleType: \"composite\",\n" +
"priority: 2.0,\n" +
"  pattern: ( (?: [{myNER:\"WEEKDAY\"}] /,/? [{lemma:\"on\"}]?[{lemma:\"the\"}]?)? [{lemma:\"day\"}]? ([{myNER:\"DAYMONTH\"} | {myNER:\"MONTHN\"} | ({myNUM:\"myNUMBER\"} & {mySTValue:/([1-2][0-9])|0?[1-9]|3[0-1]/})])? (?: [{lemma:\"of\"}])? [{lemma:\"of\"}]? [{lemma:\"the\"}]? [{lemma:\"month\"}]? [{lemma:\"of\"}]? ([{myNER:\"MONTHS\"}]) (?: [{lemma:\"of\"}]? [{lemma:\"the\"}]? /,/? [{lemma:\"year\"}]?)?  ([{myNER:\"YEAR\"}])?) ,\n" +
"  action: ( Annotate($0, \"timexType\",\"DATE\"), Annotate($0, \"myRule\",\"Rule$DDdeMMdeYYYY\"), Annotate($0, \"timexValue\", Concat(:case{ ($$3 != NIL) => $3[0].myNERnormalized, :else => \"XXXX\"}, \"-\", :case{ ($$2 != NIL) => $2[0].myNERnormalized, :else => \"XX\"}, \"-\", :case{ ($$1 != NIL) => $1[0].myNERnormalized, :else => \"XX\"})), :case{ ($$1 != NIL || $$3 != NIL  ) => (Annotate($0, \"myDateDay\", :case{ ($$1 != NIL) => $1[0].myNERnormalized, :else => \"XX\"}), Annotate($0, \"myDateMonth\", :case{ ($$2 != NIL) => $2[0].myNERnormalized, :else => \"XX\"}),Annotate($0, \"myDateYear\", :case{ ($$3 != NIL) => $3[0].myNERnormalized, :else => \"XXXX\"}));} ), \n" +
"  result: ( \"DATE\", $0[0].timexValue, \"\", \"\", \"Rule$DDdeMMdeYYYY\")  ,\n" +
"  stage : 25  } \n" +
"  \n" +
"{ ruleType: \"composite\",\n" +
"matchedExpressionGroup: 1,\n" +
"priority: 3.0,\n" +
"  pattern: ( (?: [{lemma:\"in\"}]?  [{lemma:\"the\"}]? [{lemma:\"year\"}]?)  [{lemma:\"of\"}]? ([{myNER:\"YEAR\"}]+) (?:[!{pos:/NN.*/}])) ,\n" +
"  action: ( Annotate($0, \"myDateYear\", :case{ ($$1 != NIL) => $1[0].myNERnormalized, :else => \"XXXX\"}) ),\n" +
"  result: ( \"DATE\", $1[0].myNERnormalized,  \"\", \"\", \"Rule$EnElAñoYYYY\")  ,\n" +
"  stage : 200  }  \n" +
"\n" +
"# cuidado con el entre\n" +
"# Sucesion de dias\n" +
"{ ruleType: \"tokens\",\n" +
"  pattern: ( (?: [{myNER:\"DAYMONTH\"} | {myNER:\"MONTHN\"} | ({myNUM:\"myNUMBER\"} & {mySTValue:/([1-2][0-9])|0?[1-9]|3[0-1]/})] (?: /,/ | [{lemma:\"and\"}]?))+  [{lemma:\"the\"}]? ([{myNER:\"DAYMONTH\"} | {myNER:\"MONTHN\"} | ({myNUM:\"myNUMBER\"} & {mySTValue:/([1-2][0-9])|0?[1-9]|3[0-1]/})]) ) ,\n" +
"  action: ( Annotate($0, \"myRule\", \"Rule$el2yel3deoctubre\"), Annotate($0, \"myDateMonth\", $1[0].myDateMonth), Annotate($0, \"myDateYear\", $1[0].myDateYear)),\n" +
"  stage : 100  }\n" +
"\n" +
"{ ruleType: \"composite\",\n" +
" matchedExpressionGroup: 1,\n" +
"  pattern: ( ( (?: [{myNER:\"WEEKDAY\"}] /,/?)? (?$dig [{myNER:\"DAYMONTH\"} | {myNER:\"MONTHN\"} | ({myNUM:\"myNUMBER\"} & {mySTValue:/([1-2][0-9])|0?[1-9]|3[0-1]/})])) (?: [{lemma:\"and\"}] | /,/)  [{lemma:\"the\"}]? ([{myRule:/Rule\\$DDdeMMdeYYYY|Rule\\$DDdeMMdeYYYYCom/}]) ) ,\n" +
"  action: ( Annotate($1, \"myRule\", \"Rule$DDdeMMdeYYYYCom\"), Annotate($1, \"timexType\", \"DATE\"), Annotate($1, \"myDateDay\", $dig[0].myNERnormalized)),\n" +
"  result: ( \"DATE\", Concat($dig[0].myDateYear, \"-\", $dig[0].myDateMonth, \"-\", $dig[0].myDateDay), \"\", \"\",\"Rule$DDdeMMdeYYYYCom\")  ,\n" +
"  stage : 100  }  \n" +
"\n" +
"\n" +
"# Sucesion de meses\n" +
"{ ruleType: \"tokens\",\n" +
"  pattern: ( (?:  [{myNER:\"MONTHS\"}] (?: [{lemma:\"and\"}] | /,/))+  ([{myNER:\"MONTHS\"}]) ) ,\n" +
"  action: ( Annotate($0, \"myRule\", \"Rule$octubreydiciembre\"), Annotate($0, \"myDateYear\", $1[0].myDateYear)),\n" +
"  stage : 100  }\n" +
"\n" +
"{ ruleType: \"composite\",\n" +
" matchedExpressionGroup: 1,\n" +
" priority: 1.0,\n" +
"  pattern: ( ([{myNER:\"MONTHS\"}]) (?: [{lemma:\"and\"}] | /,/) ([{myRule:/Rule\\$DDdeMMdeYYYY|Rule\\$DDdeMMdeYYYYCom/}]) ) ,\n" +
"  action: ( Annotate($1, \"myRule\", \"Rule$DDdeMMdeYYYYCom\"), Annotate($1, \"timexType\", \"DATE\"), Annotate($1, \"myDateMonth\", $1[0].myNERnormalized)),\n" +
"  result: ( \"DATE\", Concat($1[0].myDateYear, \"-\", $1[0].myDateMonth), \"\", \"\", \"Rule$DDdeMMdeYYYYCom\")  ,\n" +
"  stage : 100  } \n" +
"\n" +
"# Sucesion de duraciones\n" +
"{ ruleType: \"composite\",\n" +
" priority: 1.0,\n" +
"  pattern: ( [{timexType:\"DURATION\"}]* ([{timexType:\"DURATION\"}]) (?: [{lemma:\"and\"}] | /,/) [{timexType:\"DURATION\"}]* ([{timexType:\"DURATION\"}]) ) ,\n" +
"  action: ( Annotate($0, \"timexType\", \"DURATION\"), Annotate($0, \"myRule\", \"Rule$1añoy2meses\"), Annotate($0, DURATION_MAP[$1[0].myNERnormalized], $1[0].timexValue) , Annotate($0, DURATION_MAP[$2[0].myNERnormalized], $2[0].timexValue), Annotate($0, \"Period\", :case{($1[0].Period == \"PT\" || $2[0].Period == \"PT\") => \"PT\", :else => \"P\" }) ),\n" +
"  stage : 100  }\n" +
"  \n" +
"{ ruleType: \"composite\",\n" +
" priority: 1.0,\n" +
"  pattern: ( ([{myRule:\"Rule$1añoy2meses\"}]+) ) ,\n" +
"  result: ( \"DURATION\", \n" +
"  Concat($0[0].Period, :case {($0[1].myDurationDecade == NIL ) => \"\" , :else => $0[1].myDurationDecade}, :case {($0[1].myDurationYear == NIL ) => \"\" , :else => $0[1].myDurationYear}, :case {($0[1].myDurationMonth == NIL ) => \"\" , :else => $0[1].myDurationMonth}, :case {($0[1].myDurationWeek == NIL ) => \"\" , :else => $0[1].myDurationWeek}, :case {($0[1].myDurationWeekend == NIL ) => \"\" , :else => $0[1].myDurationWeekend}, :case {($0[1].myDurationDay == NIL ) => \"\" , :else => $0[1].myDurationDay}, :case {($0[1].myDurationHour == NIL ) => \"\" , :else => $0[1].myDurationHour}, :case {($0[1].myDurationMinute == NIL ) => \"\" , :else => $0[1].myDurationMinute}, :case {($0[1].myDurationSecond == NIL ) => \"\" , :else => $0[1].myDurationSecond}), \"\", \"\", \"Rule$1añoy2meses\")  ,\n" +
"  stage : 199  }\n" +
"  \n" +
"  \n" +
"{ ruleType: \"composite\",\n" +
" priority: 1.0,\n" +
"  pattern: ( (?$times [{timexType:\"TIME\"}]+) [{lemma:\",\"}]? [{lemma:\"at\"} || {lemma:\"of\"} || {lemma:\"on\"} || {lemma:\"in\"} || {lemma:\"during\"}]? [{lemma:\"the\"}]? (?$dates [{timexType:\"DATE\"}]+)) ,\n" +
"  action: ( Annotate($0, \"myRule\", \"Rule$TIME_DATE\"), Annotate($0, \"timexType\", \"TIME\"), Annotate($0, \"timexValue\", Concat($dates[0].timexValue, \"T\",  $times[0].timexValue)), Annotate($0, \"myDateDay\", $dates[0].myDateDay), Annotate($0, \"myDateMonth\", $dates[0].myDateMonth), Annotate($0, \"myDateYear\", $dates[0].myDateYear), :case{($times[0].myTimePartDay != NIL) => Annotate($0, \"myTimePartDay\", $times[0].myTimePartDay);}, :case{($times[0].myTimeMinute != NIL) => Annotate($0, \"myTimeMinute\", $times[0].myTimeMinute);}, :case{($times[0].myTimeHour != NIL) => Annotate($0, \"myTimeHour\", $times[0].myTimeHour);}), \n" +
"  result: ( \"TIME\", Concat($dates[0].timexValue,  \"T\", $times[0].timexValue), \"\", \"\", \"Rule$TIME_DATE\")  ,\n" +
"  stage : 150  }  \n" +
"  \n" +
"\n" +
"  \n" +
"  \n" +
"  \n" +
"####### ISOLATED\n" +
"  \n" +
"  # Weekday isolated\n" +
"{ ruleType:   \"composite\",\n" +
"  pattern: ( [{myNER:\"WEEKDAY\"}]  ),\n" +
"  result: ( \"DATE\", Concat($0[0].timexValue,\"\"), \"\", \"\", \"Rule$WEEKL\" ) ,\n" +
"  stage : 200  } \n" +
"  \n" +
"# Month isolated\n" +
"{ ruleType:   \"composite\",\n" +
"  pattern: ( [{myNER:\"MONTHS\"}]  ),\n" +
"  result: ( \"DATE\", Concat($0[0].timexValue,\"\"), \"\", \"\", \"Rule$MONTHS\" ) ,\n" +
"  stage : 200  } \n" +
"  \n" +
"  # Weekend isolated\n" +
"{ ruleType:   \"composite\",\n" +
"  pattern: ( [{myTType:\"finsemana\"}]+  ),\n" +
"  result: ( \"DATE\", Concat($0[0].timexValue,\"\"), \"\", \"\", \"Rule$finsemana\" ) ,\n" +
"  stage : 200  } \n" +
"  \n" +
"    # anchored isolated\n" +
"{ ruleType:   \"composite\",\n" +
"  pattern: ( [{timexValue:/anchor.*/}]+  ),\n" +
"  result: ( \"DATE\", Concat($0[0].timexValue,\"\"), \"\", \"\", $0[0].myRule ) ,\n" +
"  stage : 200  } \n" +
"  \n" +
"   # duration\n" +
" { ruleType:   \"composite\",\n" +
"   pattern: ( [{timexType:\"DURATION\"}]+  ),\n" +
"   result: ( \"DURATION\", Concat($0[1].Period ,$0[0].timexValue), \"\", \"\", $0[0].myRule ) ,\n" +
"   stage : 200  } \n" +
"  \n" +
"     # time\n" +
"{ ruleType:   \"composite\",\n" +
"  pattern: ( [{timexType:\"TIME\"}]+  ),\n" +
"  result: ( \"TIME\", Concat($0[1].timexValue,\"\"), \"\", \"\", $0[0].myRule ) ,\n" +
"  stage : 200  } \n" +
"  \n" +
"      # set\n" +
"{ ruleType:   \"composite\",\n" +
"  pattern: ( [{timexType:\"SET\"}]+  ),\n" +
"  result: ( \"SET\", Concat($0[1].timexValue,\"\"), :case{($0[0].timexFreq != NIL) => Concat($0[0].timexFreq, \"\"), :else => \"\"} , :case{($0[0].timexQuant != NIL) => Concat($0[0].timexQuant, \"\"), :else => \"\"}, $0[0].myRule ) ,\n" +
"  stage : 199  } \n" +
"  \n" +
"        # dateRefs\n" +
"{ ruleType:   \"composite\",\n" +
"  pattern: ( [{timexType:\"DATE\"} && {timexValue:/.+_REF/}]+  ),\n" +
"  result: ( \"DATE\", Concat($0[1].timexValue,\"\"), \"\", \"\", $0[0].myRule ) ,\n" +
"  stage : 200  } \n" +
"  \n" +
"        # date en el año\n" +
"{ ruleType:   \"composite\",\n" +
"  pattern: ( ([{timexType:\"DATE\"} && {myRule:\"Rule$EnElAñoMyNumber\"}]+)  ),\n" +
"  result: ( \"DATE\", Concat($1[0].timexValue,\"\"), \"\", \"\", \"Rule$EnElAñoMyNumber\" ) ,\n" +
"  stage : 200  } \n" +
"  \n" +
"  \n" +
"  # NOT USEFUL FOR THE LEGAL DOMAIN\n" +
"  \n" +
"  # adj proximo\n" +
"{ ruleType:   \"composite\",\n" +
"  pattern: ( [{pos:/RB.*/} && {lemma:/next|future/}]+  ),\n" +
"  result: ( \"DATE\", \"FUTURE_REF\", \"\", \"\", \"Rule$AdjProximo\" ) ,\n" +
"  stage : 200  }   \n" +
"\n" +
"  # adj reciente\n" +
"{ ruleType:   \"composite\",\n" +
"  pattern: ( [{pos:/RB.*/} && {lemma:/recent|past|ancient/}]+  ),\n" +
"  result: ( \"DATE\", \"PAST_REF\", \"\", \"\", \"Rule$AdjReciente\" ) ,\n" +
"  stage : 200  }     \n" +
"\n" +
"\n" +
"# Intervalo\n" +
"  \n" +
"#";
        ReaderCoreNLP rcnlp = new ReaderCoreNLP();
        rcnlp.reader(test);
        
        for(RuleCoreNLP rul : rcnlp.lista){
            System.out.println("\n+++++++++\n" + rul.toString() + "\n+++++++++\n");
        }
        
    }
    
}
