package oeg.annotador.core.tutorial.tictag;

import oeg.annotador.core.time.tictag.TicTag;

/**
 * Tutorial of the TicTag tagger Tagging of the Spanish TempEval3 corpus
 *
 * @author mnavas
 */
public class TutorialTicTagTempEval3ES {

    /**
     * Annotation of the files (in Spanish, just training) from TempEval3
     */
    public static void main(String[] args) {
        TicTag tt = new TicTag("ES");
        tt.evaluateTE3ES();

    }

}
