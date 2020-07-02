package oeg.tagger.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.text.DateFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import oeg.tagger.core.time.tictag.AnnotadorStandard;
import oeg.tagger.core.time.tictag.AnnotadorLegal;
import oeg.tagger.core.time.annotationHandler.*;
import oeg.tagger.core.time.tictag.Annotador;

import org.slf4j.LoggerFactory;

@RestController
@Api(value = "Añotador Annotation REST API", description = "Test services implemented about annotation")
public class AnnotationController {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @ApiOperation(value = "Annotates every possible temporal entity", response = String.class, tags = "annotation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully annotated"),
        @ApiResponse(code = 401, message = "Internal error")})
    @RequestMapping(value = "/annotate/temporal", method = RequestMethod.POST, produces = {"text/turtle"}, consumes = {"text/turtle"})//, "application/xml"})
    public String temporalNIF2NIF(
            @ApiParam(value = "Text where temporal expressions are going to be found") @RequestBody String txtinput,
            @ApiParam(value = "Language", required = true, defaultValue = "en", allowableValues = "en, es", allowMultiple = false) @RequestParam("language") String lang,
            @ApiParam(value = "Document Creation Time", required = false, defaultValue = "dd/MM/yyyy") @RequestParam(value = "dct", required = false) String sdate,           
            @ApiParam(value = "Domain", required = true, defaultValue = "standard", allowableValues = "standard, legal", allowMultiple = false) @RequestParam(value = "domain") String domain
    ) {
        return annotate(sdate, domain, txtinput, lang, "nif", "nif");
    }

    @ApiOperation(value = "Annotates every possible temporal entity", response = String.class, tags = "annotation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully annotated"),
        @ApiResponse(code = 401, message = "Internal error")})
    @RequestMapping(value = "/annotate/temporal", method = RequestMethod.POST, produces = {"text/turtle"}, consumes = {"text/plain"})//, "application/xml"})
    public String temporalTXT2NIF(
            @ApiParam(value = "Text where temporal expressions are going to be found") @RequestBody String txtinput,
            @ApiParam(value = "Language", required = true, defaultValue = "en", allowableValues = "en, es", allowMultiple = false) @RequestParam("language") String lang,
            @ApiParam(value = "Document Creation Time", required = false, defaultValue = "dd/MM/yyyy") @RequestParam(value = "dct", required = false) String sdate,
            @ApiParam(value = "Domain", required = true, defaultValue = "standard", allowableValues = "standard, legal", allowMultiple = false) @RequestParam(value = "domain") String domain
    ) {
        return annotate(sdate, domain, txtinput, lang, "text", "nif");
    }

    @ApiOperation(value = "Annotates every possible temporal entity", response = String.class, tags = "annotation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully annotated"),
        @ApiResponse(code = 401, message = "Internal error")})
    @RequestMapping(value = "/annotate/temporal", method = RequestMethod.POST, produces = {"text/plain"}, consumes = {"text/turtle"})//, "application/xml"})
    public String temporalNIF2TXT(
            @ApiParam(value = "Text where temporal expressions are going to be found") @RequestBody String txtinput,
            @ApiParam(value = "Language", required = true, defaultValue = "en", allowableValues = "en, es", allowMultiple = false) @RequestParam("language") String lang,
            @ApiParam(value = "Document Creation Time", required = false, defaultValue = "dd/MM/yyyy") @RequestParam(value = "dct", required = false) String sdate,
            @ApiParam(value = "Domain", required = true, defaultValue = "standard", allowableValues = "standard, legal", allowMultiple = false) @RequestParam(value = "domain") String domain
    ) {
        return annotate(sdate, domain, txtinput, lang, "nif", "text");
    }

    //ADD
    @ApiOperation(value = "Annotates every possible temporal entity", response = String.class, tags = "annotation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully annotated"),
        @ApiResponse(code = 401, message = "Internal error")})
    @RequestMapping(value = "/annotate/temporal", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"text/plain"})//, "application/xml"})
    public String temporalTXT2JSON(
            @ApiParam(value = "Text where temporal expressions are going to be found") @RequestBody String txtinput,
            @ApiParam(value = "Language", required = true, defaultValue = "en", allowableValues = "en, es", allowMultiple = false) @RequestParam("language") String lang,
            @ApiParam(value = "Document Creation Time", required = false, defaultValue = "dd/MM/yyyy") @RequestParam(value = "dct", required = false) String sdate,
            @ApiParam(value = "Domain", required = true, defaultValue = "standard", allowableValues = "standard, legal", allowMultiple = false) @RequestParam(value = "domain") String domain
    ) {
        return annotate(sdate, domain, txtinput, lang, "text", "json");
    }

    //ADD
    @ApiOperation(value = "Annotates every possible temporal entity", response = String.class, tags = "annotation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully annotated"),
        @ApiResponse(code = 401, message = "Internal error")})
    @RequestMapping(value = "/annotate/temporal", method = RequestMethod.POST, produces = {"text/plain"}, consumes = {"text/plain"})//, "application/xml"})
    public String temporalTXT2TXT(
            @ApiParam(value = "Text where temporal expressions are going to be found") @RequestBody String txtinput,
            @ApiParam(value = "Language", required = true, defaultValue = "en", allowableValues = "en, es", allowMultiple = false) @RequestParam("language") String lang,
            @ApiParam(value = "Document Creation Time", required = false, defaultValue = "dd/MM/yyyy") @RequestParam(value = "dct", required = false) String sdate,
            @ApiParam(value = "Domain", required = true, defaultValue = "standard", allowableValues = "standard, legal", allowMultiple = false) @RequestParam(value = "domain") String domain
    ) {

        return annotate(sdate, domain, txtinput, lang, "text", "text");
    }

    @ApiOperation(value = "Test method to show info about the deployed version", tags = "internal")
    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String index() {
        log.debug("This is a debug message");
        log.info("This is an info message");
        log.warn("This is a warn message");
        log.error("This is an error message");
        return "Service up and running by UPM-OEG.";
    }

    @ApiOperation(value = "Internal operations", notes = "Not documented.", response = String.class, tags = "internal")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 403, message = "Not authorized"),
        @ApiResponse(code = 500, message = "Internal error")})
    @RequestMapping(value = "/internal", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public String internal(@ApiParam(value = "Joker parameter", required = false, defaultValue = "valor") @RequestParam("param") String param) {
        log.debug("This is a debug message");
        log.info("This is an info message");
        log.warn("This is a warn message");
        log.error("This is an error message");
        return "Internal information";
    }

    private String annotate(String sdate, String domain, String txtinput, String lang, String formatinput, String formatoutput) {

        Date dct = null;


        /* DATE HANDLING */
        try {
            if (sdate == null || sdate.isEmpty()) {
                dct = Calendar.getInstance().getTime();
            } else {
                dct = new SimpleDateFormat("dd/MM/yyyy").parse(sdate);

            }
        } catch (Exception ex) {
            dct = new Date();
        }

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(dct);
        String res = "";

        try {
            if (!date.matches("\\d\\d\\d\\d-(1[012]|0\\d)-(3[01]|[012]\\d)")) // Is it valid?
            {
                date = null; // If not, we use no date (so anchor values will not be normalized)
            }

            /* DATE HANDLING ENDS */
 /* TEXT HANDLING */
            String txt = "";
            String reference = "http://annotador.oeg.fi.upm.es/";
            String originalNIF = txtinput;

            // If it is NIF, we read it and keep the text and the reference URL
            if (formatinput.equalsIgnoreCase("nif")) {
                NIFReader nr = new NIFReader();
                NIFText nf = nr.readNIF(txtinput);
                if (nf == null) {
                    System.out.println("ERROR WHILE READING NIF: Malformed");
                }
                txtinput = nf.text;
                reference = nf.reference;
            }

            // ADDING replace for \r\n
//            String input = txtinput;
//            txtinput = txtinput.replaceAll("\\r\\n", "\\\\n");

            txt = txtinput;

            /* TEXT HANDLING */
            Annotador upm_timex;
            /* ANNOTATION */
            // We annotate the text with Annotador (EN,ES)
            if(domain.equalsIgnoreCase("standard")){
            if (lang.equalsIgnoreCase("es")) {
                upm_timex = new AnnotadorStandard("es");   // We initialize the tagger in Spanish
                res = upm_timex.annotate(txt, date);
            } else if (lang.equalsIgnoreCase("en")) {

                upm_timex = new AnnotadorStandard("en");   // We initialize the tagger in English
                res = upm_timex.annotate(txt, date);

            } else {
                System.out.println("ERROW WITH LANGUAGES\n");
            }
            } else  if(domain.equalsIgnoreCase("legal")){
            if (lang.equalsIgnoreCase("es")) {
                upm_timex = new AnnotadorLegal("es");   // We initialize the tagger in Spanish
                res = upm_timex.annotate(txt, date);
            } else if (lang.equalsIgnoreCase("en")) {

                upm_timex = new AnnotadorLegal("en");   // We initialize the tagger in English
                res = upm_timex.annotate(txt, date);

            } else {
                System.out.println("ERROW WITH DOMAIN\n");
            }
            }
            /* ANNOTATION */
            /* RETURN FORMAT */
//            if (!input.equals(txtinput)) {
//                res = res.replaceAll("\\\\n", "\r\n");
//            }

            if (formatoutput.equals("nif") && !formatinput.equals("nif")) {
                TIMEX2NIF toNIF = new TIMEX2NIF();
                res = toNIF.toNIF(res, reference, lang);
            } else if (formatoutput.equals("nif") && formatinput.equals("nif")) {
                TIMEX2NIF toNIF = new TIMEX2NIF();
                res = toNIF.insert2ExistingNIF(res, reference, originalNIF);
            } else if (formatoutput.equals("json")) {
                TIMEX2JSON toJSON = new TIMEX2JSON();
                res = toJSON.translateSentence(res);
            }
            System.out.println("OUT: " + res);

            /* RETURN FORMAT */
        } catch (Exception e) {
            System.out.println("ERROR: ");
            e.printStackTrace();
        }

        System.out.println(res);

        return res;

    }

}
