package oeg.tagger.core.tutorial.tictag;

import oeg.tagger.core.time.tictag.TicTag;

/**
 * Tutorial of the TicTag tagger Tagging of the TempEval3 corpus
 *
 * @author mnavas
 */
public class TutorialTicTagTempEval3EN {

    /**
     * Annotation of the files (in English) from TempEval3
     */
    public static void main(String[] args) {

        TicTag tt = new TicTag("EN");
        tt.evaluateTE3();

    }

}
