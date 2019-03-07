package oeg.core.annotador.servlets;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oeg.annotador.core.time.tictag.Annotador;
import oeg.annotador.core.servlets.Main;
import oeg.annotador.core.servlets.Salida;

/**
 * Servlet that returns the javascript needed for the BRAT visualization of the
 * webpage
 *
 * @author mnavas
 */
public class annotateDoc extends HttpServlet {

    static Annotador annotador;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // We get the parameters
        request.setCharacterEncoding("UTF-8");
        String input = request.getParameter("inputText");
        String inputDate = request.getParameter("inputDate");

        response.setStatus(200);
        response.setContentType("text/html;charset=UTF-8");

        ServletContext context = getServletContext();

        pathpos = context.getResource("/WEB-INF/classes/ixa-pipes/morph-models-1.5.0/es/es-pos-perceptron-autodict01-ancora-2.0.bin").getPath();
        pathlemma = context.getResource("/WEB-INF/classes/ixa-pipes/morph-models-1.5.0/es/es-lemma-perceptron-ancora-2.0.bin").getPath();
        pathrules = context.getResource("/WEB-INF/classes/rules/rulesES.txt").getPath();

        // We call the tagger and return its output
        String salida = parseAndTag(input, inputDate);
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
    public static String parseAndTag(String txt, String date) {

        try {
            if (annotador == null) {
                annotador = new Annotador(pathpos, pathlemma, pathrules, "ES"); // We innitialize the tagger in Spanish
            }
            if (date != null && !date.matches("\\d\\d\\d\\d-(1[012]|0\\d)-(3[01]|[012]\\d)")) { // Is the date valid?
                date = null; // If not, we use no date (so anchor values will not be normalized)
            }
            Salida output = annotador.annotateBRAT(txt, date); // We annotate in BRAT format
            return output.txt + "\n\n" + output.format; // We return the javascript with the values to evaluate

        } catch (Exception ex) {
            System.err.print(ex.toString());
        }
        return "";
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
