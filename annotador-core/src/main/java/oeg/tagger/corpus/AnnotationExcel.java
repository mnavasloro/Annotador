/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.tagger.corpus;

/**
 *
 * @author Maria
 */
public class AnnotationExcel {
    
    public String Type;
    public String Value;
    public int tid;
    public String Info;
    public String Text;
    public String Begin;
    public String End;
    
    @Override
    public String toString(){
        return "Text: " + Text + "\ntid: " + tid +  "\nValue: " + Value +  "\nType: " + Type +  "\nText: " + Text +  "\nBegin: " + Begin +  "\nEnd: " + End + "\nInfo: " + Info + "\n";
    }
    
}
