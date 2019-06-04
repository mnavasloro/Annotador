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
//        String txt = " el último jueves";
        String txt = "  Entre 1987 y 1998, la industria se desplomó";
//        String txt = "esto fue la última semana, hace tiempo.";
//        String txt = " algún mes, aquel bonito, largo y hermoso año";
//        String txt = "ya lo veremos pasado o al siguiente";
//        String txt = "Hace 2 horas y media. En 10 días, 2 años y seis meses. Nos vemos dentro de 4 meses, pero 7 años, 2 meses y tres minutos antes.";
//        String txt = "Hace un año y dos meses. Ayer fuimos al parque, hace dos siglos lo vimos.";
//        String txt = "mensualmente";
//        String txt = "una vez cada semana";
//        String txt = "a los 2029 capítulos";
//        String txt = "viene a finales de 2019";
        Annotador tt = new Annotador("ES");
        String outp = tt.annotate(txt,"2019-12-20");
        System.out.println(outp);

    }

}
