package oeg.tagger.core.tutorial.annotador;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.CoreMapExpressionExtractor;
import edu.stanford.nlp.ling.tokensregex.MatchedExpression;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;
import oeg.tagger.core.time.annotation.myNER;
import oeg.tagger.core.time.annotation.temporal;
import oeg.tagger.core.time.annotation.timex;
import oeg.tagger.core.time.annotation.duration;
import org.apache.commons.io.FileUtils;

/**
 * Tutorial of the Annotador tagger Tagging the content of a TEST.txt file with
 * detailed output in the stdout
 *
 * @author mnavas
 */
public class TutorialESPfromTXT {

    public static void main(String[] args) throws IOException {
        PrintWriter out;

//    String rules = "../annotador-core/src/main/resources/rules/rulesEScomposite.txt";
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
        properties.setProperty("tokenize.verbose", "false");
        properties.setProperty("TokensRegexNERAnnotator.verbose", "false");
//    properties.setProperty("regexner.verbose", "false");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(properties);

        File f = new File("../annotador-core/src/main/resources/rules/TEST.txt");
        String s = FileUtils.readFileToString(f, "UTF-8");
        String[] lines = s.split(System.getProperty("line.separator"));
        for (String txtf : lines) {
            System.out.println("\n************************");
            String txt = txtf;
            if (txtf.length() > 1) {
                String[] aux = txtf.split("\t");
                txt = aux[0];
            }
            System.out.println(txt + "\n");
            Annotation annotation = new Annotation(txt);

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
                    String myNum = token.get(myNER.MyNumTagAnnotation.class);
                    String normalized = token.get(CoreAnnotations.NormalizedNamedEntityTagAnnotation.class);
                    String myNeNormalized = token.get(myNER.MyNormalizedNamedEntityTagAnnotation.class);
                    String tnormalized = token.get(temporal.MyNormalizedTemporalAnnotation.class);
                    String myTeNormalized = token.get(temporal.MyTemporalAnnotation.class);
//        System.out.println("dfgfg - " + token.get(temporal.MyValueAnnotation.class));
                    String mySTeValue = token.get(temporal.MyStringValueAnnotation.class);
        Number myTeValue = token.get(temporal.MyValueAnnotation.class);
//        Number myTeValue = token.get(temporal.MyValueAnnotation.class);
                    String myTeType = token.get(temporal.MyTypeTemporalAnnotation.class);
                    String rule = token.get(temporal.MyRuleAnnotation.class);

                    String duryear = token.get(duration.MyYears.class);
                    String durday = token.get(duration.MyDays.class);
                    String durper = token.get(duration.Period.class);

                    String value = token.get(timex.Value.class);
                    String type = token.get(timex.Type.class);
                    String freq = token.get(timex.Freq.class);
//        out.println("token: " + "word="+word + ", myNe=" + myNe + ", myNenormalized=" + myNeNormalized + "\t\t" + token.value());
//        out.println("token: " + "TIMEX=(" + type + ", " + value + ") \t word=" +word + ",  \t lemma="+lemma + ",  \t pos=" + pos);
//                    out.println("token: " + "TIMEX=(" + type + ", " + value + ", " + freq + ") \t word=" + word);
                    out.println("token: " + "TIMEX=(" + type + ", " + value + ", " + freq + ", " + rule + ") \t word=" + word + ",  \t lemma=" + lemma + ",  \t pos=" + pos + ",  \t ne=" + ne + ",  \t normalized=" + normalized + ",  \t myNe=" + myNe + ",  \t myNum=" + myNum + ",  \t myNenormalized=" + myNeNormalized + "\t myT=" + tnormalized + "\t  myTnormalized=" + myTeNormalized + "\t  myTValue=" + myTeValue + "\t  mySTValue=" + mySTeValue + "\t  myTType=" + myTeType);
                    out.println("\t " + "DUR=(" + duryear + ", " + durday + " - " + durper + ")");
                    out.println("\t " + "SET=(" + freq + ")");
//        out.println("token: " + "word="+word + ", lemma="+lemma + ", pos=" + pos + ", ne=" + ne + ", normalized=" + normalized + ", myNe=" + myNe + ", myNenormalized=" + myNeNormalized);

                }
                CoreMapExpressionExtractor<MatchedExpression> extractor = CoreMapExpressionExtractor
                        .createExtractorFromFiles(TokenSequencePattern.getNewEnv(), rules);
                List<MatchedExpression> matchedExpressions = extractor.extractExpressions(sentence);
                out.println("Matched expressions\n----------\n");
                for (MatchedExpression matched : matchedExpressions) {
                    // Print out matched text and value
                    out.println("Matched expression: " + matched.getText() + " with value " + matched.getValue());
                    // Print out token information
                    CoreMap cm = matched.getAnnotation();

                    for (CoreLabel token : cm.get(CoreAnnotations.TokensAnnotation.class)) {
                        String word = token.get(CoreAnnotations.TextAnnotation.class);
                        String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
                        String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                        String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                        out.println("  Matched token: " + "word=" + word + ", lemma=" + lemma + ", pos=" + pos + ", ne=" + ne);
                    }
                }

            }

            out.flush();
        }
    }

}
