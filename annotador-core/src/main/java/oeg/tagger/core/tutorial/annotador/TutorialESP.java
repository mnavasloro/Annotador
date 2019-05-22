package oeg.tagger.core.tutorial.annotador;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;
import oeg.tagger.core.time.annotation.myNER;
import oeg.tagger.core.time.annotation.temporal;
import oeg.tagger.core.time.annotation.timex;

/**
 * Tutorial of the Annotador tagger Tagging of a sentence with detailed output
 * in the stdout
 *
 * @author mnavas
 */
public class TutorialESP {

    public static void main(String[] args) throws IOException {
        PrintWriter out;

        String rules = "../annotador-core/src/main/resources/rules/rulesES.txt";

        out = new PrintWriter(System.out);

        Properties properties = StringUtils.argsToProperties(new String[]{"-props", "StanfordCoreNLP-spanish.properties"});

        String posModel = "../annotador-core/src/main/resources/ixa-pipes/morph-models-1.5.0/es/es-pos-perceptron-autodict01-ancora-2.0.bin";
        String lemmaModel = "../annotador-core/src/main/resources/ixa-pipes/morph-models-1.5.0/es/es-lemma-perceptron-ancora-2.0.bin";

        properties.setProperty("annotators", "tokenize,ssplit,spanish,readability,ner,tokensregexdemo");
//    properties.setProperty("ner.useSUTime", "false");
        properties.setProperty("spanish.posModel", posModel);
        properties.setProperty("spanish.lemmaModel", lemmaModel);
        properties.setProperty("readability.language", "es");

        properties.setProperty("customAnnotatorClass.spanish", "oeg.tagger.core.time.aidCoreNLP.BasicAnnotator");
        properties.setProperty("customAnnotatorClass.readability", "eu.fbk.dh.tint.readability.ReadabilityAnnotator");

        properties.setProperty("customAnnotatorClass.tokensregexdemo", "edu.stanford.nlp.pipeline.TokensRegexAnnotator");
        properties.setProperty("tokensregexdemo.rules", rules);
        StanfordCoreNLP pipeline = new StanfordCoreNLP(properties);

//        String txt = "Si tuvieran cumplidos sesenta y cinco años en la fecha de entrada en vigor del presente Real Decreto, el importe de las cuotas a ingresar deberá ser el equivalente a los diez años de cotización necesarios para que se reconozca el derecho a la pensión de jubilación.\n" +
//"Dos. Para los menores de sesenta y cinco años y mayores de sesenta años, el ingreso se efectuará conforme a la cantidad que resulte de la aplicación de la siguiente escala:\n" +
//"Sesenta y cuatro años, nueve años de cotización.\n" +
//"Sesenta y tres años, ocho años de cotización.\n" +
//"Sesenta y dos años, siete años de cotización.\n" +
//"Sesenta y un años, seis años de cotización.\n" +
//"Sesenta años, cinco años de cotización.\n" +
//"Tres. Para los menores de sesenta años se";
//        String txt = "Hace una semana.";
String txt = "En 10 días, 2 años y seis meses nos v. Nos vemos dentro de 4 minutos, pero 7 años, 2 meses y tres días antes.";

//        String txt = "treinta de mayo de mil novecientos setenta y cuatro";
//        String txt = "mensualmente";
//        String txt = "a los 2029 capítulos";
//        String txt = "viene a finales de 2019";
//        String txt = "dos días a la semana";
//        String txt = "Cada dos meses";
//    String txt = "El 10 de Julio";

        /* Problemas con pasado */
//    Annotation annotation = new Annotation("pasado un tiempo");  //solo pasado no lo pilla bien... si no es "en el pasado"
//    Annotation annotation = new Annotation("ha pasado mucho tiempo");  //solo pasado no lo pilla bien... si no es "en el pasado"
//    Annotation annotation = new Annotation("esa persona hizo lo mismo en el pasado");  //solo pasado no lo pilla bien... si no es "en el pasado"
//    Annotation annotation = new Annotation("ya lo veremos pasado o al siguiente");  //solo pasado no lo pilla bien... si no es "en el pasado"
//    Annotation annotation = new Annotation("ya lo veremos pasado");  //solo pasado no lo pilla bien... si no es "en el pasado"
//    Annotation annotation = new Annotation("ya lo veremos pasado mañana");  //solo pasado no lo pilla bien... si no es "en el pasado"

        /* Varios */
//    Annotation annotation = new Annotation("en ese momento");
//    Annotation annotation = new Annotation("en el año mil");
//    Annotation annotation = new Annotation("El 10 de Junio de 1991.");
        Annotation annotation = new Annotation(txt);
//    Annotation annotation = new Annotation("Martes. Junio");
//    Annotation annotation = new Annotation("Todas las semanas.");
//    Annotation annotation = new Annotation("Cada dos meses. Cada mes. Lo hacían dos días cada dos meses.");
//    Annotation annotation = new Annotation("Lo hacían una vez cada semana.");
//    Annotation annotation = new Annotation("Lo hacían tres veces a la semana.");
//    Annotation annotation = new Annotation("Lo hacían tres veces al día.");
//    Annotation annotation = new Annotation("Lo hacían tres veces cada semana.");
//    Annotation annotation = new Annotation("Lo hacían un día cada semana.");
//    Annotation annotation = new Annotation("Lo hacían dos veces a la semana y 3 veces al mes.");
//    Annotation annotation = new Annotation("Lo hacían diariamente.");
//    Annotation annotation = new Annotation("La sentencia se hizo publica unos cuantos días después.");
//    Annotation annotation = new Annotation("Esta mañana, aquella tarde, en la mañana del día 10, esa mañana, lo hizo de buena mañana. Pasado mañana vamos a comer, porque pasado es un gran día. Una vez haya pasado mañana.");
//    Annotation annotation = new Annotation("Mañana lo veremos, en la mañana del día 10, esa mañana, lo hizo de buena mañana. Pasado mañana vamos a comer, porque pasado es un gran día. Una vez haya pasado mañana.");
//    Annotation annotation = new Annotation("muchos días después, y además dos días y una semana después.");

//    Annotation annotation = new Annotation("Ayer comí patatas.");
//    Annotation annotation = new Annotation("El tres de abril de 1991.");

        /* Problemas con segundos */
//    Annotation annotation = new Annotation("el segundo perro tardó dos segundos");

        /* Meses */
//    Annotation annotation = new Annotation("en mayo me gusta ir con Abril al campo, sobre todo los jueves y los domingos...");
//    Annotation annotation = new Annotation("el niño, este niño, esas personas, el pasado jueves, unos pocos días");  //solo pasado no lo pilla bien... si no es "en el pasado"
//    Annotation annotation = new Annotation("el futuro nos depara algo futuro igual que a su futuro hijo");  //solo pasado no lo pilla bien... si no es "en el pasado"
//    Annotation annotation = new Annotation("mañana comeré en la mañana, no la segunda mañana pero varias mañanas seguidas, pensando en el mañana, o en el día de mañana");  //solo pasado no lo pilla bien... si no es "en el pasado"
        pipeline.annotate(annotation);

        // An Annotation is a Map and you can get and use the various analyses individually.
        out.println();
        // The toString() method on an Annotation just prints the text of the Annotation
        // But you can see what is in it with other methods like toShorterString()
        out.println("The top level annotation");
        out.println(annotation.toShorterString());

//   Tree tree = annotation.get(CoreAnnotations.SentencesAnnotation.class).get(0).get(TreeCoreAnnotations.TreeAnnotation.class);
//    System.out.println("--------\n" + tree + "\n--------\n");
//    String timexVersion = txt;
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            // NOTE: Depending on what tokensregex rules are specified, there are other annotations
            //       that are of interest other than just the tokens and what we print out here

            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {

                Integer beginOff = token.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
                Integer endOff = token.get(CoreAnnotations.CharacterOffsetEndAnnotation.class);

                // Print out words, lemma, ne, and normalized ne
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                String myNe = token.get(myNER.MyNamedEntityTagAnnotation.class);
                String normalized = token.get(CoreAnnotations.NormalizedNamedEntityTagAnnotation.class);
                String myNeNormalized = token.get(myNER.MyNormalizedNamedEntityTagAnnotation.class);
                String tnormalized = token.get(temporal.MyNormalizedTemporalAnnotation.class);
                String myNum = token.get(myNER.MyNumTagAnnotation.class);
                String myTeNormalized = token.get(temporal.MyTemporalAnnotation.class);
//        System.out.println("dfgfg - " + token.get(temporal.MyValueAnnotation.class));
                Number myTeValue = token.get(temporal.MyValueAnnotation.class);
                String mySValue = token.get(temporal.MyStringValueAnnotation.class);
                String myTeType = token.get(temporal.MyTypeTemporalAnnotation.class);
                String rule = token.get(temporal.MyRuleAnnotation.class);

                String value = token.get(timex.Value.class);
                String type = token.get(timex.Type.class);
                String freq = token.get(timex.Freq.class);
//        out.println("token: " + "word="+word + ", myNe=" + myNe + ", myNenormalized=" + myNeNormalized + "\t\t" + token.value());
//        out.println("token: " + "TIMEX=(" + type + ", " + value + ") \t word=" +word + ",  \t lemma="+lemma + ",  \t pos=" + pos);
                out.println("token: " + "TIMEX=(" + type + ", " + value + ", " + freq + ", " + rule + ") \t word=" + word + ",  \t lemma=" + lemma + ",  \t pos=" + pos + ",  \t ne=" + ne + ",  \t normalized=" + normalized + ",  \t myNe=" + myNe + ",  \t myNenormalized=" + myNeNormalized + "\t myT=" + tnormalized + "\t  myTnormalized=" + myTeNormalized + "\t  myTValue=" + myTeValue + "\t  mySTValue=" + mySValue + "\t  myTType=" + myTeType + "\t  myNum=" + myNum);
//        out.println("token: " + "word="+word + ", lemma="+lemma + ", pos=" + pos + ", ne=" + ne + ", normalized=" + normalized + ", myNe=" + myNe + ", myNenormalized=" + myNeNormalized);
out.flush();
            }
        }
        out.flush();
    }

}
