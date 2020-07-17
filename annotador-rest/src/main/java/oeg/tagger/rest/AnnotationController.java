package oeg.tagger.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Tag(name = "Añotador Annotation REST API", description = "Test services implemented about annotation")
public class AnnotationController {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

//    @Operation(summary = "Annotates every possible temporal entity", description = "", tags = "annotation")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "Successfully annotated"),
//        @ApiResponse(responseCode = "401", description = "Internal error")})
//    @RequestMapping(value = "/annotate/temporal", method = RequestMethod.POST, produces = {"text/turtle","text/plain"}, consumes = {"text/turtle"})//, "application/xml"})
//    public String temporalNIF2NIF(
//            @Parameter(description = "Text where temporal expressions are going to be found") @RequestBody String txtinput,
//            @Parameter(name = "language", required = true, schema = @Schema(description = "Language", type = "string", defaultValue = "en", allowableValues = {"en", "es"})) @RequestParam("language") String lang,
//            @Parameter(name = "dct", required = false, schema = @Schema(description = "Document Creation Time", type = "string", defaultValue = "dd/MM/yyyy")) @RequestParam(value = "dct", required = false) String sdate,
//            @Parameter(name = "domain", required = true, schema = @Schema(description = "Domain", type = "string", defaultValue = "standard", allowableValues = {"standard", "legal"})) @RequestParam(value = "domain") String domain,
//            @Parameter(name = "format", required = true, schema = @Schema(description = "format", type = "string", defaultValue = "TIMEX", allowableValues = {"TIMEX", "JSON", "NIF"})) @RequestParam(value = "format") String format
//    ) {
//        return annotate(sdate, domain, txtinput, lang, "nif", format);
//    }

    @Operation(summary = "Annotates every possible temporal entity", description = "", tags = "annotation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully annotated"),
        @ApiResponse(responseCode = "401", description = "Internal error")})
    @RequestMapping(value = "/annotate/temporal", method = RequestMethod.POST, produces = {"text/plain"}, consumes = {"text/plain"})//, "application/xml"})
    public String temporalTXT2NIF(
            @Parameter(description = "Text where temporal expressions are going to be found") @RequestBody String txtinput,
            @Parameter(name = "language", required = true, schema = @Schema(description = "Language", type = "string", defaultValue = "en", allowableValues = {"en", "es"})) @RequestParam("language") String lang,
            @Parameter(name = "dct", required = false, schema = @Schema(description = "Document Creation Time", type = "string", defaultValue = "dd/MM/yyyy")) @RequestParam(value = "dct", required = false) String sdate,
            @Parameter(name = "domain", required = true, schema = @Schema(description = "Domain", type = "string", defaultValue = "standard", allowableValues = {"standard", "legal"})) @RequestParam(value = "domain") String domain,
            @Parameter(name = "format", required = true, schema = @Schema(description = "format", type = "string", defaultValue = "TIMEX", allowableValues = {"TIMEX", "JSON"})) @RequestParam(value = "format") String format
    ) {
        return annotate(sdate, domain, txtinput, lang, "text", format);
    }

//    @Operation(summary = "Annotates every possible temporal entity", description = "", tags = "annotation")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "Successfully annotated"),
//        @ApiResponse(responseCode = "401", description = "Internal error")})
//    @RequestMapping(value = "/annotate/temporal", method = RequestMethod.POST, produces = {"text/plain"}, consumes = {"text/turtle"})//, "application/xml"})
//    public String temporalNIF2TXT(
//            @Parameter(description = "Text where temporal expressions are going to be found") @RequestBody String txtinput,
//            @Parameter(name = "language", required = true, schema = @Schema(description = "Language", type = "string", defaultValue = "en", allowableValues = {"en", "es"})) @RequestParam("language") String lang,
//            @Parameter(name = "dct", required = false, schema = @Schema(description = "Document Creation Time", type = "string", defaultValue = "dd/MM/yyyy")) @RequestParam(value = "dct", required = false) String sdate,
//            @Parameter(name = "domain", required = true, schema = @Schema(description = "Domain", type = "string", defaultValue = "standard", allowableValues = {"standard", "legal"})) @RequestParam(value = "domain") String domain
//    ) {
//        return annotate(sdate, domain, txtinput, lang, "nif", "text");
//    }
//
//    //ADD
//    @Operation(summary = "Annotates every possible temporal entity", description = "", tags = "annotation")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "Successfully annotated"),
//        @ApiResponse(responseCode = "401", description = "Internal error")})
//    @RequestMapping(value = "/annotate/temporal", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"text/plain"})//, "application/xml"})
//    public String temporalTXT2JSON(
//            @Parameter(description = "Text where temporal expressions are going to be found") @RequestBody String txtinput,
//           @Parameter(name = "language", required = true, schema = @Schema(description = "Language", type = "string", defaultValue = "en", allowableValues = {"en", "es"})) @RequestParam("language") String lang,
//            @Parameter(name = "dct", required = false, schema = @Schema(description = "Document Creation Time", type = "string", defaultValue = "dd/MM/yyyy")) @RequestParam(value = "dct", required = false) String sdate,
//            @Parameter(name = "domain", required = true, schema = @Schema(description = "Domain", type = "string", defaultValue = "standard", allowableValues = {"standard", "legal"})) @RequestParam(value = "domain") String domain
//    ) {
//        return annotate(sdate, domain, txtinput, lang, "text", "json");
//    }
//
//    //ADD
//    @Operation(summary = "Annotates every possible temporal entity", description = "", tags = "annotation")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "Successfully annotated"),
//        @ApiResponse(responseCode = "401", description = "Internal error")})
//    @RequestMapping(value = "/annotate/temporal", method = RequestMethod.POST, produces = {"text/plain"}, consumes = {"text/plain"})//, "application/xml"})
//    public String temporalTXT2TXT(
//            @Parameter(description = "Text where temporal expressions are going to be found") @RequestBody String txtinput,
//            @Parameter(name = "language", required = true, schema = @Schema(description = "Language", type = "string", defaultValue = "en", allowableValues = {"en", "es"})) @RequestParam("language") String lang,
//            @Parameter(name = "dct", required = false, schema = @Schema(description = "Document Creation Time", type = "string", defaultValue = "dd/MM/yyyy")) @RequestParam(value = "dct", required = false) String sdate,
//            @Parameter(name = "domain", required = true, schema = @Schema(description = "Domain", type = "string", defaultValue = "standard", allowableValues = {"standard", "legal"})) @RequestParam(value = "domain") String domain
//    ) {
//
//        return annotate(sdate, domain, txtinput, lang, "text", "text");
//    }

    @Operation(summary = "Test method to show info about the deployed version", tags = "internal")
    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String index() {
        log.debug("This is a debug message");
        log.info("This is an info message");
        log.warn("This is a warn message");
        log.error("This is an error message");
        return "Service up and running by UPM-OEG.";
    }

    @Operation(summary = "Internal operations", description = "Not documented.", tags = "internal")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "403", description = "Not authorized"),
        @ApiResponse(responseCode = "500", description = "Internal error")})
    @RequestMapping(value = "/internal", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public String internal(@Parameter(description = "Joker parameter", required = false, schema = @Schema(defaultValue = "valor")) @RequestParam("param") String param) {
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

            if (formatoutput.equals("NIF") && !formatinput.equals("nif")) {
                TIMEX2NIF toNIF = new TIMEX2NIF();
                res = toNIF.toNIF(res, reference, lang);
            } else if (formatoutput.equals("NIF") && formatinput.equals("nif")) {
                TIMEX2NIF toNIF = new TIMEX2NIF();
                res = toNIF.insert2ExistingNIF(res, reference, originalNIF);
            } else if (formatoutput.equals("JSON")) {
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




//package oeg.tagger.rest;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.media.Schema;
//import java.text.DateFormat;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import oeg.tagger.core.time.tictag.AnnotadorStandard;
//import oeg.tagger.core.time.tictag.AnnotadorLegal;
//import oeg.tagger.core.time.annotationHandler.*;
//import oeg.tagger.core.time.tictag.Annotador;
//
//import org.slf4j.LoggerFactory;
//
//@RestController
//@Tag(name = "Añotador Annotation REST API", description = "Test services implemented about annotation")
//public class AnnotationController {
//
//    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());
//
//    @Operation(summary = "Annotates every possible temporal entity", description = "", tags = "annotation")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "Successfully annotated"),
//        @ApiResponse(responseCode = "401", description = "Internal error")})
//    @RequestMapping(value = "/annotate", method = RequestMethod.POST, produces = {"text/turtle","text/plain"}, consumes = {"application/json"})//, "application/xml"})
//    public String temporal(@Parameter(description = "Text where temporal expressions are going to be found") @RequestBody String txtinput
//
//                ) {
//        return annotate("", "", "", "", "nif", "nif");
//    }
//
//
//    @Operation(summary = "Test method to show info about the deployed version", tags = "internal")
//    @RequestMapping(value = "/about", method = RequestMethod.GET)
//    public String index() {
//        log.debug("This is a debug message");
//        log.info("This is an info message");
//        log.warn("This is a warn message");
//        log.error("This is an error message");
//        return "Service up and running by UPM-OEG.";
//    }
//
//    @Operation(summary = "Internal operations", description = "Not documented.", tags = "internal")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "Success"),
//        @ApiResponse(responseCode = "403", description = "Not authorized"),
//        @ApiResponse(responseCode = "500", description = "Internal error")})
//    @RequestMapping(value = "/internal", method = RequestMethod.GET)
//    @PreAuthorize("hasRole('ADMIN')")
//    public String internal(@Parameter(description = "Joker parameter", required = false, schema = @Schema(defaultValue = "valor")) @RequestParam("param") String param) {
//        log.debug("This is a debug message");
//        log.info("This is an info message");
//        log.warn("This is a warn message");
//        log.error("This is an error message");
//        return "Internal information";
//    }
//
//    private String annotate(String sdate, String domain, String txtinput, String lang, String formatinput, String formatoutput) {
//
//        Date dct = null;
//
//
//        /* DATE HANDLING */
//        try {
//            if (sdate == null || sdate.isEmpty()) {
//                dct = Calendar.getInstance().getTime();
//            } else {
//                dct = new SimpleDateFormat("dd/MM/yyyy").parse(sdate);
//
//            }
//        } catch (Exception ex) {
//            dct = new Date();
//        }
//
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        String date = df.format(dct);
//        String res = "";
//
//        try {
//            if (!date.matches("\\d\\d\\d\\d-(1[012]|0\\d)-(3[01]|[012]\\d)")) // Is it valid?
//            {
//                date = null; // If not, we use no date (so anchor values will not be normalized)
//            }
//
//            /* DATE HANDLING ENDS */
// /* TEXT HANDLING */
//            String txt = "";
//            String reference = "http://annotador.oeg.fi.upm.es/";
//            String originalNIF = txtinput;
//
//            // If it is NIF, we read it and keep the text and the reference URL
//            if (formatinput.equalsIgnoreCase("nif")) {
//                NIFReader nr = new NIFReader();
//                NIFText nf = nr.readNIF(txtinput);
//                if (nf == null) {
//                    System.out.println("ERROR WHILE READING NIF: Malformed");
//                }
//                txtinput = nf.text;
//                reference = nf.reference;
//            }
//
//            // ADDING replace for \r\n
////            String input = txtinput;
////            txtinput = txtinput.replaceAll("\\r\\n", "\\\\n");
//
//            txt = txtinput;
//
//            /* TEXT HANDLING */
//            Annotador upm_timex;
//            /* ANNOTATION */
//            // We annotate the text with Annotador (EN,ES)
//            if(domain.equalsIgnoreCase("standard")){
//            if (lang.equalsIgnoreCase("es")) {
//                upm_timex = new AnnotadorStandard("es");   // We initialize the tagger in Spanish
//                res = upm_timex.annotate(txt, date);
//            } else if (lang.equalsIgnoreCase("en")) {
//
//                upm_timex = new AnnotadorStandard("en");   // We initialize the tagger in English
//                res = upm_timex.annotate(txt, date);
//
//            } else {
//                System.out.println("ERROW WITH LANGUAGES\n");
//            }
//            } else  if(domain.equalsIgnoreCase("legal")){
//            if (lang.equalsIgnoreCase("es")) {
//                upm_timex = new AnnotadorLegal("es");   // We initialize the tagger in Spanish
//                res = upm_timex.annotate(txt, date);
//            } else if (lang.equalsIgnoreCase("en")) {
//
//                upm_timex = new AnnotadorLegal("en");   // We initialize the tagger in English
//                res = upm_timex.annotate(txt, date);
//
//            } else {
//                System.out.println("ERROW WITH DOMAIN\n");
//            }
//            }
//            /* ANNOTATION */
//            /* RETURN FORMAT */
////            if (!input.equals(txtinput)) {
////                res = res.replaceAll("\\\\n", "\r\n");
////            }
//
//            if (formatoutput.equals("nif") && !formatinput.equals("nif")) {
//                TIMEX2NIF toNIF = new TIMEX2NIF();
//                res = toNIF.toNIF(res, reference, lang);
//            } else if (formatoutput.equals("nif") && formatinput.equals("nif")) {
//                TIMEX2NIF toNIF = new TIMEX2NIF();
//                res = toNIF.insert2ExistingNIF(res, reference, originalNIF);
//            } else if (formatoutput.equals("json")) {
//                TIMEX2JSON toJSON = new TIMEX2JSON();
//                res = toJSON.translateSentence(res);
//            }
//            System.out.println("OUT: " + res);
//
//            /* RETURN FORMAT */
//        } catch (Exception e) {
//            System.out.println("ERROR: ");
//            e.printStackTrace();
//        }
//
//        System.out.println(res);
//
//        return res;
//
//    }
//
//}











//package oeg.tagger.rest;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.media.Schema;
//import java.text.DateFormat;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import oeg.tagger.core.time.tictag.AnnotadorStandard;
//import oeg.tagger.core.time.tictag.AnnotadorLegal;
//import oeg.tagger.core.time.annotationHandler.*;
//import oeg.tagger.core.time.tictag.Annotador;
//
//import org.slf4j.LoggerFactory;
//
//@RestController
//@Tag(name = "Añotador Annotation REST API", description = "Test services implemented about annotation")
//public class AnnotationController {
//
//    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());
//
//    @Operation(summary = "Annotates every possible temporal entity", description = "", tags = "annotation")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "Successfully annotated"),
//        @ApiResponse(responseCode = "401", description = "Internal error")})
//    @RequestMapping(value = "/annotate/temporal", method = RequestMethod.POST, produces = {"text/turtle"}, consumes = {"text/turtle"})//, "application/xml"})
//    public String temporalNIF2NIF(
//            @Parameter(description = "Text where temporal expressions are going to be found") @RequestBody String txtinput,
//            @Parameter(name = "language", required = true, schema = @Schema(description = "Language", type = "string", defaultValue = "en", allowableValues = {"en", "es"})) @RequestParam("language") String lang,
//            @Parameter(name = "dct", required = false, schema = @Schema(description = "Document Creation Time", type = "string", defaultValue = "dd/MM/yyyy")) @RequestParam(value = "dct", required = false) String sdate,
//            @Parameter(name = "domain", required = true, schema = @Schema(description = "Domain", type = "string", defaultValue = "standard", allowableValues = {"standard", "legal"})) @RequestParam(value = "domain") String domain
//    ) {
//        return annotate(sdate, domain, txtinput, lang, "nif", "nif");
//    }
//
//    @Operation(summary = "Annotates every possible temporal entity", description = "", tags = "annotation")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "Successfully annotated"),
//        @ApiResponse(responseCode = "401", description = "Internal error")})
//    @RequestMapping(value = "/annotate/temporal", method = RequestMethod.POST, produces = {"text/turtle"}, consumes = {"text/plain"})//, "application/xml"})
//    public String temporalTXT2NIF(
//            @Parameter(description = "Text where temporal expressions are going to be found") @RequestBody String txtinput,
//            @Parameter(name = "language", required = true, schema = @Schema(description = "Language", type = "string", defaultValue = "en", allowableValues = {"en", "es"})) @RequestParam("language") String lang,
//            @Parameter(name = "dct", required = false, schema = @Schema(description = "Document Creation Time", type = "string", defaultValue = "dd/MM/yyyy")) @RequestParam(value = "dct", required = false) String sdate,
//            @Parameter(name = "domain", required = true, schema = @Schema(description = "Domain", type = "string", defaultValue = "standard", allowableValues = {"standard", "legal"})) @RequestParam(value = "domain") String domain
//    ) {
//        return annotate(sdate, domain, txtinput, lang, "text", "nif");
//    }
//
//    @Operation(summary = "Annotates every possible temporal entity", description = "", tags = "annotation")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "Successfully annotated"),
//        @ApiResponse(responseCode = "401", description = "Internal error")})
//    @RequestMapping(value = "/annotate/temporal", method = RequestMethod.POST, produces = {"text/plain"}, consumes = {"text/turtle"})//, "application/xml"})
//    public String temporalNIF2TXT(
//            @Parameter(description = "Text where temporal expressions are going to be found") @RequestBody String txtinput,
//            @Parameter(name = "language", required = true, schema = @Schema(description = "Language", type = "string", defaultValue = "en", allowableValues = {"en", "es"})) @RequestParam("language") String lang,
//            @Parameter(name = "dct", required = false, schema = @Schema(description = "Document Creation Time", type = "string", defaultValue = "dd/MM/yyyy")) @RequestParam(value = "dct", required = false) String sdate,
//            @Parameter(name = "domain", required = true, schema = @Schema(description = "Domain", type = "string", defaultValue = "standard", allowableValues = {"standard", "legal"})) @RequestParam(value = "domain") String domain
//    ) {
//        return annotate(sdate, domain, txtinput, lang, "nif", "text");
//    }
//
//    //ADD
//    @Operation(summary = "Annotates every possible temporal entity", description = "", tags = "annotation")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "Successfully annotated"),
//        @ApiResponse(responseCode = "401", description = "Internal error")})
//    @RequestMapping(value = "/annotate/temporal", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"text/plain"})//, "application/xml"})
//    public String temporalTXT2JSON(
//            @Parameter(description = "Text where temporal expressions are going to be found") @RequestBody String txtinput,
//           @Parameter(name = "language", required = true, schema = @Schema(description = "Language", type = "string", defaultValue = "en", allowableValues = {"en", "es"})) @RequestParam("language") String lang,
//            @Parameter(name = "dct", required = false, schema = @Schema(description = "Document Creation Time", type = "string", defaultValue = "dd/MM/yyyy")) @RequestParam(value = "dct", required = false) String sdate,
//            @Parameter(name = "domain", required = true, schema = @Schema(description = "Domain", type = "string", defaultValue = "standard", allowableValues = {"standard", "legal"})) @RequestParam(value = "domain") String domain
//    ) {
//        return annotate(sdate, domain, txtinput, lang, "text", "json");
//    }
//
//    //ADD
//    @Operation(summary = "Annotates every possible temporal entity", description = "", tags = "annotation")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "Successfully annotated"),
//        @ApiResponse(responseCode = "401", description = "Internal error")})
//    @RequestMapping(value = "/annotate/temporal", method = RequestMethod.POST, produces = {"text/plain"}, consumes = {"text/plain"})//, "application/xml"})
//    public String temporalTXT2TXT(
//            @Parameter(description = "Text where temporal expressions are going to be found") @RequestBody String txtinput,
//            @Parameter(name = "language", required = true, schema = @Schema(description = "Language", type = "string", defaultValue = "en", allowableValues = {"en", "es"})) @RequestParam("language") String lang,
//            @Parameter(name = "dct", required = false, schema = @Schema(description = "Document Creation Time", type = "string", defaultValue = "dd/MM/yyyy")) @RequestParam(value = "dct", required = false) String sdate,
//            @Parameter(name = "domain", required = true, schema = @Schema(description = "Domain", type = "string", defaultValue = "standard", allowableValues = {"standard", "legal"})) @RequestParam(value = "domain") String domain
//    ) {
//
//        return annotate(sdate, domain, txtinput, lang, "text", "text");
//    }
//
//    @Operation(summary = "Test method to show info about the deployed version", tags = "internal")
//    @RequestMapping(value = "/about", method = RequestMethod.GET)
//    public String index() {
//        log.debug("This is a debug message");
//        log.info("This is an info message");
//        log.warn("This is a warn message");
//        log.error("This is an error message");
//        return "Service up and running by UPM-OEG.";
//    }
//
//    @Operation(summary = "Internal operations", description = "Not documented.", tags = "internal")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "Success"),
//        @ApiResponse(responseCode = "403", description = "Not authorized"),
//        @ApiResponse(responseCode = "500", description = "Internal error")})
//    @RequestMapping(value = "/internal", method = RequestMethod.GET)
//    @PreAuthorize("hasRole('ADMIN')")
//    public String internal(@Parameter(description = "Joker parameter", required = false, schema = @Schema(defaultValue = "valor")) @RequestParam("param") String param) {
//        log.debug("This is a debug message");
//        log.info("This is an info message");
//        log.warn("This is a warn message");
//        log.error("This is an error message");
//        return "Internal information";
//    }
//
//    private String annotate(String sdate, String domain, String txtinput, String lang, String formatinput, String formatoutput) {
//
//        Date dct = null;
//
//
//        /* DATE HANDLING */
//        try {
//            if (sdate == null || sdate.isEmpty()) {
//                dct = Calendar.getInstance().getTime();
//            } else {
//                dct = new SimpleDateFormat("dd/MM/yyyy").parse(sdate);
//
//            }
//        } catch (Exception ex) {
//            dct = new Date();
//        }
//
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        String date = df.format(dct);
//        String res = "";
//
//        try {
//            if (!date.matches("\\d\\d\\d\\d-(1[012]|0\\d)-(3[01]|[012]\\d)")) // Is it valid?
//            {
//                date = null; // If not, we use no date (so anchor values will not be normalized)
//            }
//
//            /* DATE HANDLING ENDS */
// /* TEXT HANDLING */
//            String txt = "";
//            String reference = "http://annotador.oeg.fi.upm.es/";
//            String originalNIF = txtinput;
//
//            // If it is NIF, we read it and keep the text and the reference URL
//            if (formatinput.equalsIgnoreCase("nif")) {
//                NIFReader nr = new NIFReader();
//                NIFText nf = nr.readNIF(txtinput);
//                if (nf == null) {
//                    System.out.println("ERROR WHILE READING NIF: Malformed");
//                }
//                txtinput = nf.text;
//                reference = nf.reference;
//            }
//
//            // ADDING replace for \r\n
////            String input = txtinput;
////            txtinput = txtinput.replaceAll("\\r\\n", "\\\\n");
//
//            txt = txtinput;
//
//            /* TEXT HANDLING */
//            Annotador upm_timex;
//            /* ANNOTATION */
//            // We annotate the text with Annotador (EN,ES)
//            if(domain.equalsIgnoreCase("standard")){
//            if (lang.equalsIgnoreCase("es")) {
//                upm_timex = new AnnotadorStandard("es");   // We initialize the tagger in Spanish
//                res = upm_timex.annotate(txt, date);
//            } else if (lang.equalsIgnoreCase("en")) {
//
//                upm_timex = new AnnotadorStandard("en");   // We initialize the tagger in English
//                res = upm_timex.annotate(txt, date);
//
//            } else {
//                System.out.println("ERROW WITH LANGUAGES\n");
//            }
//            } else  if(domain.equalsIgnoreCase("legal")){
//            if (lang.equalsIgnoreCase("es")) {
//                upm_timex = new AnnotadorLegal("es");   // We initialize the tagger in Spanish
//                res = upm_timex.annotate(txt, date);
//            } else if (lang.equalsIgnoreCase("en")) {
//
//                upm_timex = new AnnotadorLegal("en");   // We initialize the tagger in English
//                res = upm_timex.annotate(txt, date);
//
//            } else {
//                System.out.println("ERROW WITH DOMAIN\n");
//            }
//            }
//            /* ANNOTATION */
//            /* RETURN FORMAT */
////            if (!input.equals(txtinput)) {
////                res = res.replaceAll("\\\\n", "\r\n");
////            }
//
//            if (formatoutput.equals("nif") && !formatinput.equals("nif")) {
//                TIMEX2NIF toNIF = new TIMEX2NIF();
//                res = toNIF.toNIF(res, reference, lang);
//            } else if (formatoutput.equals("nif") && formatinput.equals("nif")) {
//                TIMEX2NIF toNIF = new TIMEX2NIF();
//                res = toNIF.insert2ExistingNIF(res, reference, originalNIF);
//            } else if (formatoutput.equals("json")) {
//                TIMEX2JSON toJSON = new TIMEX2JSON();
//                res = toJSON.translateSentence(res);
//            }
//            System.out.println("OUT: " + res);
//
//            /* RETURN FORMAT */
//        } catch (Exception e) {
//            System.out.println("ERROR: ");
//            e.printStackTrace();
//        }
//
//        System.out.println(res);
//
//        return res;
//
//    }
//
//}
