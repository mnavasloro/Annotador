/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.tagger.core.tutorial.tictag;

import oeg.tagger.core.time.annotationHandler.TIMEX2NIF;
import oeg.tagger.core.time.tictag.TicTag;

/**
 * Tutorial of the TicTag tagger Tagging of one sentence with an output in NIF
 * format
 *
 * @author mnavas
 */
public class TutorialTicTag {

    /**
     * Annotation of the String txt, output in the file test.tml
     */
    public static void main(String[] args) {
//        String txt = "By judgment of 25 June 2010, the Rechtbank ’s-Gravenhage rejected the action brought by Ms S. against the decision of 16 November 2009 as unfounded.";
        String txt = "Mediante sentencia de 25 de junio de 2010, el Rechtbank ’s-Gravenhage declaró infundado el recurso interpuesto por la Sra. S. contra la resolución de 16 de noviembre de 2009.";
//        String txt = "Mañana iremos a comer patatas como antaño.";
//        String txt = "Welcome to Berlin in 2016. We will meet in 2020.";
//        String txt = "The flu season is winding down, and it has killed 105 children so far - about the average toll.\n"
//                + "\n"
//                + "The season started about a month earlier than usual, sparking concerns it might turn into the worst in a decade. It ended up being very hard on the elderly, but was moderately severe overall, according to the Centers for Disease Control and Prevention.\n"
//                + "\n"
//                + "Six of the pediatric deaths were reported in the last week, and it's possible there will be more, said the CDC's Dr. Michael Jhung said Friday.\n"
//                + "\n"
//                + "Roughly 100 children die in an average flu season. One exception was the swine flu pandemic of 2009-2010, when 348 children died.\n"
//                + "\n"
//                + "The CDC recommends that all children ages 6 months and older be vaccinated against flu each season, though only about half get a flu shot or nasal spray.\n"
//                + "\n"
//                + "All but four of the children who died were old enough to be vaccinated, but 90 percent of them did not get vaccinated, CDC officials said.\n"
//                + "\n"
//                + "This year's vaccine was considered effective in children, though it didn't work very well in older people. And the dominant flu strain early in the season was one that tends to cause more severe illness.\n"
//                + "\n"
//                + "The government only does a national flu death count for children. But it does track hospitalization rates for people 65 and older, and those statistics have been grim.\n"
//                + "\n"
//                + "In that group, 177 out of every 100,000 were hospitalized with flu-related illness in the past several months. That's more than 2 1/2 times higher than any other recent season.\n"
//                + "\n"
//                + "This flu season started in early December, a month earlier than usual, and peaked by the end of year. Since then, flu reports have been dropping off throughout the country.\n"
//                + "\n"
//                + "\"We appear to be getting close to the end of flu season,\" Jhung said..";

//        TicTag tt = new TicTag("EN");
        TicTag tt = new TicTag("ES");

        String out = tt.annotate(txt);
        System.out.println(out);

        tt.writeFile("<?xml version=\"1.0\" ?><TimeML xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://timeml.org/timeMLdocs/TimeML_1.2.1.xsd\">\n"
                + "\n"
                + "<DOCID>AP_20130322</DOCID>\n"
                + "\n"
                + "<DCT><TIMEX3 functionInDocument=\"CREATION_TIME\" temporalFunction=\"false\" tid=\"t0\" type=\"DATE\" value=\"2013-03-22\">March 22, 2013</TIMEX3></DCT>\n"
                + "\n"
                + "<TITLE>105 U.S. Kids Died From Flu, CDC Says</TITLE>\n"
                + "\n"
                + "\n"
                + "\n"
                + "<TEXT>\n\n" + out + "\n\n</TEXT>\n"
                + "\n"
                + "\n"
                + "</TimeML>", "test.tml");

        TIMEX2NIF toNIF = new TIMEX2NIF();
        String outNIF = toNIF.translateSentence(out);
        System.out.println("\n\n--------------\n\n" + outNIF);

    }

}
