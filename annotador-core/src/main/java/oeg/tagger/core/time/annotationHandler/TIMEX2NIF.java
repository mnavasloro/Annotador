package oeg.tagger.core.time.annotationHandler;

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

    String prefixNIF1 = "@prefix nif: <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\n" +
"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
"@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .\n" +
"@prefix lkg: <http://lkg.lynx-project.eu/def/>"
            + "\n\n";

    /**
     * Converts a sentence @intput in TIMEX format into NIF
     *
     * @param input String in TIMEX format
     * @return String in NIF format
     */
    public String translateSentence(String input, String reference) {
        try {
            String inp2 = input;

            while (!inp2.isEmpty()) {
                String pattern = "<TIMEX3 tid=\"([^\"]+)\" type=\"([^\"]+)\" value=\"([^\"]+)\"[^>]*>([^<]*)<\\/TIMEX3>";
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(inp2);
                StringBuffer sb = new StringBuffer(inp2.length());
                if (m.find()) {
                    NIFAnnotation ann = new NIFAnnotation();
                    int end = (m.start() + m.group(4).length());
                    ann.beginIndex = "        nif:beginIndex  " + m.start() + " ;\n";
                    ann.endIndex = "        nif:endIndex    " + end + " ;\n";
                    ann.isString = "        nif:anchorOf    \"" + m.group(4) + "\" .\n";                    
                    ann.annotationUnit = "        nif:annotationUnit [\n" +
"        a lkg:LynxDocument, nif:AnnotationUnit, nif:OffsetBasedString ;\n" +
"        itsrdf:taClassRef <http://www.w3.org/2006/time#TemporalEntity> ;\n" +
"        itsrdf:taConfidence 1.0 ;\n" +
"        itsrdf:taIdentRef lkg:" + m.group(2) + "\n" +
"        ] ;";
                    
                    
                    ann.header = "<" + reference + "#offset_" + m.start() + "_" + end + ">\n";
                    ann.a = "        a                     nif:Annotation ;\n";
                    ann.referenceContext = "";

//                               item.put("anchorOf", m.group(4) );
//                    item.put("tid", m.group(1) );
//                    item.put("type",  );
//                    item.put("value", m.group(3) );
//                    ann.value = "        nif:anchorOf    \"" + m.group(3) + " .\n";
//                    ann.isString = "        nif:anchorOf    \"" + m.group(4) + " .\n";
                    
                    
                    listAnnotations.add(ann);
                    m.appendReplacement(sb, m.group(3));
                    m.appendTail(sb);
                    inp2 = sb.toString();
                } else {
                    NIFAnnotation ann = new NIFAnnotation();
                    ann.header = "<" + reference + ">\n";
                    ann.a = "        a             lkg:LynxDocument, nif:Context ;\n";
                    ann.isString = "        nif:isString    \"" + inp2 + "\" .\n";

                    String outputNIF = prefixNIF1 + ann.toString();

                    for (NIFAnnotation a : listAnnotations) {
                        a.referenceContext = "        nif:referenceContext   " + ann.header.substring(0, ann.header.length() - 1) + " ;\n";
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


