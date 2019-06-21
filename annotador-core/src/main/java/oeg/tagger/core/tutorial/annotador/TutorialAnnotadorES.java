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
//String txt = "un día después.";
//String txt = "el 2 de Mayo de 2020.";
//String txt = "cada dos años y tres meses y doce minutos.";    
//String txt = "algunos años.";
//String txt = "estuvo allí dos años, tres meses, cuatro semanas.";
//String txt = "ampliación de ocho a doce horas del periodo de re";
//String txt = "el déficit se puede reducir este mismo año 2000 al 0,5 % del PIB, todo un récord.";
//String txt = "Y aquí, mientras esperamos el derbi del Lunes.";
//String txt = "Meses excitantes.";
//String txt = "los locos años veinte.";
//String txt = "la década de los veinte.";
//String txt = "nueva ronda comercial esta semana.";
//String txt = " demandaron a Microsoft en 1998 presentaron.";
//String txt = " anoche fuimos a cenar.";
//String txt = "Son dos años y tres meses y dos minutos.";
//String txt = "participan del día 3 al 5";
//String txt = "el 2 de Mayo de 2020.";
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
//        String txt = "dos lustros";  
//String txt = "hace dos lustros";
//        String txt = "Últimamente estamos perdiendo incluso";
//String txt = "Los ministros de Defensa de la Unión Europea ( UE) celebrarán el próximo lunes en Bruselas una conferencia de \" generación de fuerzas \", en la que cada socio comunicará formalmente la aportación de su país a la Fuerza militar europea, que contará con más de 60.000 soldados.\n" +
//"El mismo día por la tarde,";
//String txt = "fue condenado hace varios días";
//String txt = "que \" el siglo XXI será el siglo de la ciencia y la tecnología .";
//String txt = "El primer trimestre del año";
//String txt = "Surgida en plena campaña para las elecciones municipales del próximo domingo, ";
//String txt = "Durante el cuatrimestre primero ocurrió ";

String txt = "La semana pasada el Departamento de Justicia y los g.";//String txt = "se fueron a las 10.";
//String txt = "El 4 de julio, por la mañana hizo bueno, pero por la tarde hizo malo.";//String txt = "se fueron a las 10.";
//String txt = "Durante el cuatrimestre primero ocurrió. ";//String txt = "se fueron a las 10.";
//String txt = "En un bienio";
//String txt = "Hoy, lunes, se armó la marimorena";
//String txt = "Hoy, 3 de marzo de 1991, se armó la marimorena";
//String txt = "el 20 antes de cristo.";
//String txt = "el 3 de octubre de 1932 antes de cristo.";
//String txt = "Este siglo.";
//String txt = "En el año mil novecientos cuarenta y ocho.";
//String txt = " giro de última hora.";
//String txt = " la desconfianza de cientos de miles de rumanos.";
//String txt = "la primera vuelta y que hace una semana.";
//String txt = " tuvo carácter musical y se cerró, por la noche, c .";
//String txt = "hasta finales del pasado mayo .";
//        String txt = "Al cumplirse hoy la segunda jornada de la protesta";
//        String txt = "madrileños aseguró que el siglo XXI será el siglo de la ciencia y la tecnología";
        Annotador tt = new Annotador("ES");
        String outp = tt.annotate(txt,"2019-12-20");
        System.out.println(outp);

    }

}
