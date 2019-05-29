/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.tagger.core.tutorial.annotador;

import oeg.tagger.core.time.tictag.Annotador;

/**
 *
 * @author mnavas
 */
public class TutorialAnnotadorSoco {

    /**
     * Annotation of the files (in Spanish, just training) from TempEval3
     */
    public static void main(String[] args) {
        Annotador tt = new Annotador("ES");
        tt.evaluateSoco();

    }

}
