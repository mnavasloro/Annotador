package oeg.core.tagger.servlets;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oeg.tagger.core.time.tictag.AnnotadorStandard;
import oeg.tagger.core.servlets.Main;
import oeg.tagger.core.servlets.Salida;
import oeg.tagger.core.time.tictag.Annotador;
import oeg.tagger.core.time.tictag.AnnotadorLegal;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

/**
 * Servlet that returns the javascript needed for the BRAT visualization of the
 * webpage
 *
 * @author mnavas
 */
public class annotateDoc extends HttpServlet {

    static Annotador annotadorS;
    static Annotador annotadorL;
    static String pathpos;
    static String pathlemma;
    static String pathrules;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Takes a text as an input. Offers the same text: a) either
     * removing the legal references b)
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "X-PINGOTHER,Content-Type,X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
            response.addHeader("Access-Control-Expose-Headers", "xsrf-token");

        // We get the parameters
        request.setCharacterEncoding("UTF-8");
//        String input = request.getParameter("inputText");
//        String inputDate = request.getParameter("inputDate");
//        String domain = request.getParameter("domain");

String jsonString = IOUtils.toString(request.getInputStream(), "UTF-8");


        JSONObject json = new JSONObject(jsonString);
        String input = (String) json.get("inputText");
        String inputDate = (String) json.get("inputDate");
        String domain = (String) json.get("domain");

//        try {
//            inputDate = URLDecoder.decode(inputDate, StandardCharsets.UTF_8.name());
//            input = URLDecoder.decode(input, StandardCharsets.UTF_8.name());
//        } catch (Exception ex) {
//            Logger.getLogger(annotateDoc.class.getName()).log(Level.SEVERE, null, ex);
//        }


        response.setStatus(200);
        response.setContentType("text/html;charset=UTF-8");

        ServletContext context = getServletContext();

        pathpos = context.getResource("/WEB-INF/classes/ixa-pipes/morph-models-1.5.0/es/es-pos-perceptron-autodict01-ancora-2.0.bin").getPath();
        pathlemma = context.getResource("/WEB-INF/classes/ixa-pipes/morph-models-1.5.0/es/es-lemma-perceptron-ancora-2.0.bin").getPath();
        if(domain.equalsIgnoreCase("standard")){
            pathrules = context.getResource("/WEB-INF/classes/rules/rulesES-standard.txt").getPath();
        } else{
            pathrules = context.getResource("/WEB-INF/classes/rules/rulesES.txt").getPath();
        }

        // We call the tagger and return its output
        System.out.println("----------\n" + input);
        String salida = parseAndTag(input, inputDate, domain);
        response.setContentType("text/plain");
        response.getWriter().println(salida);

    }

    /**
     * Function that calls the temporal tagger
     *
     * @param txt to annotate
     * @param date anchor date provided
     * @return String with the javascript code for visualization
     */   
//    public static String parseAndTagBRAT(String txt, String date) {
//        try {
//            if (annotador == null) {
//                annotador = new asdad(pathpos, pathlemma, pathrules, "ES"); // We innitialize the tagger in Spanish
//            }   // We innitialize the tagger in Spanish
//            if (date != null && !date.matches("\\d\\d\\d\\d-(1[012]|0\\d)-(3[01]|[012]\\d)")) // Is it valid?
//            {
//                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                Date dct = Calendar.getInstance().getTime();
//                date = df.format(dct);
////                date = null; // If not, we use no date (so anchor values will not be normalized)
//            }
//            Salida output = annotador.annotateBRAT(txt, date); // We annotate in BRAT format
//            return output.txt + "\n\n" + output.format; // We return the javascript with the values to evaluate
//        } catch (Exception ex) {
//            System.err.print(ex.toString());
//        }
//        return "";
//    }
    
    public static String parseAndTag(String txt, String date, String domain) {

        Annotador annotador;
        
        System.out.println("To tag: " + txt);
        Date dct = null;
         try {
            if(domain.equalsIgnoreCase("standard")){
            if (annotadorS == null) {
                annotadorS = new AnnotadorStandard(pathpos, pathlemma, pathrules, "ES"); // We innitialize the tagger in Spanish
            }
            annotador = annotadorS;
        } else{
            if (annotadorL == null) {
                annotadorL = new AnnotadorLegal(pathpos, pathlemma, pathrules, "ES"); // We innitialize the tagger in Spanish
            }
            annotador = annotadorL;
        }
            if (date == null || date.isEmpty() || !date.matches("\\d\\d\\d\\d-(1[012]|0\\d)-(3[01]|[012]\\d)")) {
                dct = Calendar.getInstance().getTime();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                date = df.format(dct);
            }
            
            String output = annotador.annotate(txt, date); // We annotate in TIMEX format
            System.out.println(output);
            String out2 = createHighlights(output);
            System.out.println(out2);
            return out2; // We return the javascript with the values to evaluate
        } catch (Exception ex) {
            System.err.print(ex.toString());
            return "";
        }
    }
    
    static public String createHighlights(String input2) {
//        input2 = input2.replaceFirst(Pattern.quote("<?xml version=\"1.0\"?>\n" + "<!DOCTYPE TimeML SYSTEM \"TimeML.dtd\">\n" + "<TimeML>"), "");
        input2 = input2.replaceFirst(Pattern.quote("</TimeML>"), "");
        input2 = input2.replaceAll("</TIMEX3>", "</span>");
        input2 = input2.replaceAll("</INTERVAL>", "</span>");
        input2 = input2.replaceAll("\\r?\\n", "<br>");

        String pattern = "(<TIMEX3 ([^>]*)>)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(input2);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String color = "#7fa2ff";//"Orange";
//            String color = "rgba(255, 165, 0, 0.5)";//"Orange";
            String contetRegex = m.group(2);
            contetRegex = contetRegex.replaceAll("\"", "");
            contetRegex = contetRegex.replaceAll(" ", "\n");
            if (contetRegex.contains("SET")) {
                color = "#ccb3ff";//DodgerBlue";
//                color = "rgba(135, 206, 235, 0.5)";//DodgerBlue";
            } else if (contetRegex.contains("DURATION")) {
                color = "#99ffeb"; //Tomato
//                color = "hsla(9, 100%, 64%, 0.5)"; //Tomato
            } else if (contetRegex.contains("TIME")) {
                color = "#ffbb99";//"MediumSeaGreen";
//                color = "rgba(102, 205, 170, 0.5)";//"MediumSeaGreen";
            }

            String aux2 = m.group(0);
            aux2 = aux2.replace(">", "");

            m.appendReplacement(sb, aux2.replaceFirst(Pattern.quote(aux2), "<span style=\"background-color:"
                    + color + "\" title=\"" + contetRegex + "\">"));
        }
        m.appendTail(sb); // append the rest of the contents
        
        String saux = sb.toString();
        
        
        
        // INTERVAL
        
        pattern = "(<INTERVAL ([^>]*)>)";
        r = Pattern.compile(pattern);
        m = r.matcher(saux);
        sb = new StringBuffer();
        while (m.find()) {
            String color = "#9bd29b";//"Orange";
//            String color = "rgba(255, 165, 0, 0.5)";//"Orange";
            String contetRegex = m.group(2);
            contetRegex = contetRegex.replaceAll("\"", "");
            contetRegex = contetRegex.replaceAll(" ", "\n");            

            String aux2 = m.group(0);
            aux2 = aux2.replace(">", "");

            m.appendReplacement(sb, aux2.replaceFirst(Pattern.quote(aux2), "<span style=\"background-color:"
                    + color + "\" title=\"" + contetRegex + "\">"));
        }
        m.appendTail(sb); // append the rest of the contents
        
        saux = sb.toString();
       
        
        
        
        
        
        
        
        

        return saux;
    }
    
    
    
    
    
    
    
    
    

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
