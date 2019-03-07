/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.legalwhen.core.tutorial.annotador.Annotador;

import oeg.annotador.core.time.tictag.Annotador;
import oeg.annotador.core.time.tictag.TicTag;

/**
 *
 * @author mnavas
 */
public class TutorialAnnotadorTempEval3ES {

    /**
     * Annotation of the files (in Spanish, just training) from TempEval3
     */
    public static void main(String[] args) {
        Annotador tt = new Annotador("ES");
        tt.evaluateTE3ES();

    }

}
