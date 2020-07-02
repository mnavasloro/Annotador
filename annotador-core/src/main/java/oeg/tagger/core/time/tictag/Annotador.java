/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.tagger.core.time.tictag;


/**
 *
 * @author mnavas
 */
public interface Annotador {
    
    public String annotate(String input, String anchorDate);
    
    public String annotateNIF(String input, String anchorDate, String reference, String lang);
    
    public String annotateJSON(String input, String anchorDate);
    
    
    
}
