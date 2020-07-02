/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.tagger.core.tutorial.annotador;

import oeg.tagger.core.time.tictag.AnnotadorStandard;
import oeg.tagger.core.time.tictag.TicTag;

/**
 *
 * @author mnavas
 */
public class TutorialAnnotadorEN {

    /**
     * Annotation of the files (in Spanish, just training) from TempEval3
     */
    public static void main(String[] args) {
        String txt = "Two days and a half" ; 
//        String txt = "una vez cada semana";
//        String txt = "a los 2029 capítulos";
//        String txt = "viene a finales de 2019";
        AnnotadorStandard tt = new AnnotadorStandard("EN");
        String outp = tt.annotate(txt,null);
        System.out.println(outp);

    }

}
