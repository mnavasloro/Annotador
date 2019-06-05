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
// String txt = "Esto lo dirige en la actualidad Luis Gómez.";
// String txt = "está próximo a mi casa. Soy el próximo en la lista.";
//        String txt = "esto fue el próximo mayo, hace tiempo.";
//String txt = "El pasado 2 de junio.";
//String txt = "Nos vemos el próximo 3 de marzo.";
//String txt = "Es una pasada.";
//String txt = "El día 1 y 2.";
//String txt = " le vistamos durante años ";
//String txt = "cada día";
//String txt = "Mayo y Junio.";
String txt = "un día después.";//String txt = "el 2 de Mayo de 2020.";
//String txt = "ocurre durante la noche.";
//String txt = "El día 1 y 2.";
//String txt = "Los días 1, 2, 3 y 5 de abril.";
//String txt = "El próximo invierno.";
//String txt = "El próximo verano.";
//String txt = "El último verano.";
//String txt = "El verano de 1991.";
//String txt = "A PLANTILLA del cuerpo ha ido menguando desde 1997.";
//String txt = "Nos vemos el pasado día 21 de diciembre.";
//        String txt = " algún mes, aquel bonito, largo y hermoso año";
//        String txt = "ya lo veremos pasado o al siguiente";
//        String txt = "Hace 2 horas y media. En 10 días, 2 años y seis meses. Nos vemos dentro de 4 meses, pero 7 años, 2 meses y tres minutos antes.";
//        String txt = "Hace un año y dos meses. Ayer fuimos al parque, hace dos siglos lo vimos.";
//        String txt = "mensualmente";
//        String txt = "una vez cada semana";
//        String txt = "a los 2029 capítulos";
//        String txt = "viene a finales de 2019";
        Annotador tt = new Annotador("ES");
        String outp = tt.annotate(txt,"2002-02-02");
        System.out.println(outp);

    }

}
