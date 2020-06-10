/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.tagger.core.tutorial.annotador;

import oeg.tagger.core.time.tictag.*;

/**
 *
 * @author mnavas
 */
public class TutorialAnnotadorTempEval3ES {

    /**
     * Annotation of the files (in Spanish, just training) from TempEval3
     */
    public static void main(String[] args) {
        AnnotadorStandard tt = new AnnotadorStandard("ES");
        tt.evaluateTE3ES();

    }

}
