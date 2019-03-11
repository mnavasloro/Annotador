package oeg.tagger.core.tutorial.tictag;

import oeg.tagger.core.time.tictag.TicTag;

/**
 * Tutorial of the TicTag tagger Tagging of one sentence with an output via
 * stdout
 *
 * @author mnavas
 */
public class TutorialTicTagAnnotate {

    /**
     * Annotation of the String txt in Spanish
     */
    public static void main(String[] args) {

        TicTag tt = new TicTag("ES");

        String txt = "Transcurrido más de medio siglo desde que se promulgó en mil novecientos dos la vigente Ley de Caza, resulta obligado dejar constancia del acierto de los legisladores al enfrentarse con los difíciles problemas que ya entonces planteaba la armonización del aprovechamiento y conservación de la caza con el respeto debido a los derechos inherentes a la propiedad de la tierra, a la seguridad de las personas y a la adecuada protección de sus bienes y cultivos.";
        String out = tt.annotate(txt);
        System.out.println(out);

    }

}
