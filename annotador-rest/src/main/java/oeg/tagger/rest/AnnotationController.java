package oeg.tagger.rest;

import com.google.gson.Gson;
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
import javax.servlet.http.HttpServletResponse;
import oeg.tagger.core.time.tictag.Annotador;
import oeg.tagger.core.time.annotationHandler.TIMEX2NIF;
import oeg.tagger.core.time.tictag.TicTag;
import org.slf4j.LoggerFactory;

@RestController
@Api(value = "annotador Annotation REST API", description = "Test services implemented about annotation")
public class AnnotationController {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @ApiOperation(value = "Annotates every possible temporal entity", response = String.class, tags = "annotation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully annotated")
        ,
    @ApiResponse(code = 401, message = "Internal error")})
    @RequestMapping(value = "/annotate/temporal", method = RequestMethod.POST, produces = {"plain/text"})//, "application/xml"})
    public String temporal(
            @ApiParam(value = "Text where temporal expressions are going to be found") @RequestBody String txt,
            @ApiParam(value = "Format of the output", required = true, defaultValue = "timex", allowableValues = "nif, timex", allowMultiple = false) @RequestParam("format") String format,
            @ApiParam(value = "Language", required = true, defaultValue = "en", allowableValues = "en, es, de, it", allowMultiple = false) @RequestParam("language") String lang,
//            @ApiParam(value = "Document type", required = false, defaultValue = "narratives", allowableValues = "narratives, news, colloquial, scientific", allowMultiple = false) @RequestParam(value = "doctype", required = false) String dt,
            @ApiParam(value = "Document Creation Time", required = false, defaultValue = "dd/MM/yyyy") @RequestParam(value = "dct", required = false) String sdate
    ) {
        System.out.println(format);
        Date dct = null;
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
        
        try{
            if (!date.matches("\\d\\d\\d\\d-(1[012]|0\\d)-(3[01]|[012]\\d)")) // Is it valid?
        {
            date = null; // If not, we use no date (so anchor values will not be normalized)
        }
        
        if(lang.equalsIgnoreCase("es")){
            Annotador annotador = new Annotador("es");   // We innitialize the tagger in Spanish
            res = annotador.annotate(txt, date);
        }
        else if(lang.equalsIgnoreCase("en")){
            TicTag tt = new TicTag("EN");
            res = tt.annotate(txt);
        }
        else{
//            logger.error("error in language; for now, just available ES and EN");
        }
        
        } catch (Exception e) {
//            logger.info(res);
//            logger.error("error opening file");
//            return;
        }
//       logger.info(res);
//        logger.info("Parsing correct\n\n");
        
        
        
        
        
        
//        TicTag tt = new TicTag(lan);
//        res = tt.annotate(text);
//        if(res==null){
//            res = ManagerSutime.annotate(text, sdate);
//        }
        
        
        if(format.equals("nif")){
            TIMEX2NIF toNIF = new TIMEX2NIF();
            res = toNIF.translateSentence(res);
            System.out.println("NIF: " + res);
        }
        
        
//        res = "{\"resultado\":\"" + res+ "\"}";
        System.out.println(res);
        
        return res;
            /*
        }
        if (format.equals("timex")) {
            Gson gson = new Gson();
            String jtimexes = gson.toJson(ManagerHeidelTime.getTimeML(text, lan, dt, dct));
            return jtimexes;
//            return "\"" + ManagerHeidelTime.getTimeML(text, lan, dt, dct) + "\"";
//            return ManagerHeidelTime.getTimeML(text, lan, dt, dct);
        } else {
            String timexes[] = ManagerHeidelTime.getTimex(text, lan, dt, dct);
            Gson gson = new Gson();
            String jtimexes = gson.toJson(timexes);
            return jtimexes;
        } */
    }

//    {
//        return "Service up and running by UPM-OEG. Temporal annotations are given";
//    }
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
        @ApiResponse(code = 200, message = "Success")
        ,
    @ApiResponse(code = 403, message = "Not authorized")
        ,
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

}

