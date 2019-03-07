package oeg.annotador.core.time.annotationHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.LoggerFactory;

/**
 * Class that converts a TIMEX annotation into a NIF annotation
 *
 * @author mnavas
 */
public class TIMEX2NIF {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(TIMEX2NIF.class);

    List<NIFAnnotation> listAnnotations = new ArrayList<NIFAnnotation>();
    String web = "lynxURL";
    String number = "RFC5147";

    /**
     * Initializes a instance of the converter
     *
     * @return an instance of the converter
     */
    public TIMEX2NIF() {
        init();
    }

    public void init() {

    }

    String prefixNIF1 = "@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\n"
            + "@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\n"
            + "\n\n";

    /**
     * Converts a sentence @intput in TIMEX format into NIF
     *
     * @param input String in TIMEX format
     * @return String in NIF format
     */
    public String translateSentence(String input) {
        try {
            String inp2 = input;

            while (!inp2.isEmpty()) {
                String pattern = "<TIMEX3 [^>]*>([^<]*)<\\/TIMEX3>";
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(inp2);
                StringBuffer sb = new StringBuffer(inp2.length());
                if (m.find()) {
                    NIFAnnotation ann = new NIFAnnotation();
                    int end = (m.start() + m.group(1).length());
                    ann.beginIndex = "        nif:beginIndex  \"" + m.start() + "\"^^xsd:nonNegativeInteger ;\n";
                    ann.endIndex = "        nif:endIndex    \"" + end + "\"^^xsd:nonNegativeInteger ;\n";
                    ann.isString = "        nif:anchorOf    \"" + m.group(1) + "\"^^xsd:string .\n";
                    ann.header = "<" + web + "/#char=" + m.start() + "," + end + ">\n";
                    ann.a = "        a                     nif:" + number + "String , nif:String , nif:Phrase ;\n";
                    ann.referenceContext = "";

                    listAnnotations.add(ann);
                    m.appendReplacement(sb, m.group(1));
                    m.appendTail(sb);
                    inp2 = sb.toString();
                } else {
                    NIFAnnotation ann = new NIFAnnotation();
                    ann.header = "<" + web + "/#char=0," + inp2.length() + ">\n";
                    ann.a = "        a             nif:" + number + "String , nif:String , nif:Context ;\n";
                    ann.isString = "        nif:isString    \"" + inp2 + "\"^^xsd:string .\n";

                    String outputNIF = prefixNIF1 + ann.toString();

                    for (NIFAnnotation a : listAnnotations) {
                        a.referenceContext = "        nif:referenceContext   " + ann.header.substring(0, ann.header.length() - 1) + " ;";
                        outputNIF = outputNIF + a.toString();
                    }

                    return outputNIF;
                }
            }
            return "ERROR\n" + input;

        } catch (Exception ex) {
            Logger.getLogger(TIMEX2NIF.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
