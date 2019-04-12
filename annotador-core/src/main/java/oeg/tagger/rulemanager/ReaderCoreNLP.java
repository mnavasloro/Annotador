/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.tagger.rulemanager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author mnavas
 */
public class ReaderCoreNLP {

//    final String readerRegex = "(?<comm>#.*\\s*)*{(\\s*(?<field1>\\w+)\\s*:\\s*(?<value1>.*)\\s*,\\s*)(\\s*(?<field2>\\w+)\\s*:\\s*(?<value2>.*)\\s*,\\s*)(\\s*(?<field3>\\w+)\\s*:\\s*(?<value3>.*)\\s*,\\s*)(\\s*(?<field4>\\w+)\\s*:\\s*(?<value4>.*)\\s*,\\s*)*(\\s*(?<field5>\\w+)\\s*:\\s*(?<value5>.*)\\s*)}";
    final String readerRegex1 = "(?<comm>(#.*\\s*)*)?\\{(\\s*(?<field1>\\w+)\\s*:\\s*(?<value1>.*)\\s*,\\s*)+(\\s*(?<field5>\\w+)\\s*:\\s*(?<value5>.*)\\s*)\\}";
//    final String readerRegex1 = "((#.*\\s*)*)\\{(\\s*(?:\\w+)\\s*:\\s*(?:.*)\\s*,\\s*)+(\\s*(?:\\w+)\\s*:\\s*(?:.*)\\s*)\\}";
    final String readerRegex2 = "(\\s*(?<field2>\\w+)\\s*:\\s*(?<value2>.*)\\s*)[,}]";

    List<RuleCoreNLP> lista = new ArrayList<RuleCoreNLP>();

    public String reader(String input) {
        
        int num = 0;

        Pattern pattern = Pattern.compile(readerRegex1, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(input);

        Pattern pattern2 = Pattern.compile(readerRegex2, Pattern.MULTILINE);
        Matcher matcher2;

        while (matcher.find()) {
            String singlerule = matcher.group(0);
            System.out.println("Full rule match: " + singlerule);
            matcher2 = pattern2.matcher(singlerule);
            RuleCoreNLP rul = new RuleCoreNLP();
            rul.id = "id" + num;
            num++;
            if (matcher.group("comm") != null) {
                rul.comment = matcher.group("comm");
            }
            while (matcher2.find()) {
                String field = matcher2.group("field2");
                String value = matcher2.group("value2");
                System.out.println("\tField: " + field);
                System.out.println("\tValue: " + value);
                if (field.equalsIgnoreCase("ruleType")) {
                    rul.ruleT = value;
                } else if (field.equalsIgnoreCase("matchWithResults")) {
                    rul.match = value;
                } else if (field.equalsIgnoreCase("pattern")) {
                    rul.pattern = value;
                } else if (field.equalsIgnoreCase("action")) {
                    rul.action = value;
                } else if (field.equalsIgnoreCase("result")) {
                    rul.result = value;
                } else if (field.equalsIgnoreCase("stage")) {
                    rul.stage = value;
                } else if (field.equalsIgnoreCase("priority")) {
                    rul.priority = value;
                }
            }
            
            lista.add(rul);
        }

        return "";

    }
}
