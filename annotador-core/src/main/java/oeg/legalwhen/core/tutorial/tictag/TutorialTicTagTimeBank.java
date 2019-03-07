package oeg.annotador.core.tutorial.tictag;

import oeg.annotador.core.time.tictag.TicTag;

/**
 * Tutorial of the TicTag tagger Tagging of the TimeBank corpus
 *
 * @author mnavas
 */
public class TutorialTicTagTimeBank {

    /**
     * Annotation of the files (in English) from TimeBank
     */
    public static void main(String[] args) {
        TicTag tt = new TicTag();
        tt.evaluateTimeBank();

    }

}
