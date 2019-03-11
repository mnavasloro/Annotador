/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.tagger.core.tutorial.annotador;

import oeg.tagger.core.time.tictag.Annotador;
import oeg.tagger.core.time.tictag.TicTag;

/**
 *
 * @author mnavas
 */
public class TutorialAnnotadorES {

    /**
     * Annotation of the files (in Spanish, just training) from TempEval3
     */
    public static void main(String[] args) {
        String txt = "mensualmente";
//        String txt = "una vez cada semana";
//        String txt = "a los 2029 capítulos";
//        String txt = "viene a finales de 2019";
        Annotador tt = new Annotador("ES");
        String outp = tt.annotate(txt,null);
        System.out.println(outp);

    }

}
