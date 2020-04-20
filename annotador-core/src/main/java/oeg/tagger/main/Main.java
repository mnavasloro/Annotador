package oeg.tagger.main;

import com.itextpdf.layout.hyphenation.Hyphenator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import oeg.tagger.core.time.tictag.Annotador;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import edu.stanford.nlp.util.logging.RedwoodConfiguration;
import eu.fbk.dh.tint.readability.es.SpanishReadabilityModel;
import java.io.OutputStream;
import java.io.PrintStream;
import oeg.tagger.core.time.aidCoreNLP.BasicAnnotator;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.varia.NullAppender;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.slf4j.LoggerFactory;

/**
 * Main class of the jar.
 *
 * @author vroddon
 * @author mnavas
 */
public class Main {

    static Logger logger = null;
    static boolean logs = false;
    static String lang = "es";
    static String date = "";
    static String format = "timeml";
    static String outpfilename = null;

    public static void main(String[] args) {
        
        BasicConfigurator.configure();

        // We do this to avoid the Ixa-Pipes debugging messages...
        PrintStream systemerr = System.err;

        init(args);

        if (args.length != 0) {
            String res = parsear(args);
            if (!res.isEmpty()) {
                System.out.println(res);
            }
        }
        
        System.setErr(systemerr);
    }

    public static void init(String[] args) {
        logs = Arrays.asList(args).contains("-logs");
        initLogger(logs);
        
        
        //Welcome message
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(new FileReader("pom.xml"));
            String welcome = model.getArtifactId() + " " + model.getVersion() + "\n-----------------------------------------------------\n";
            logger.info(welcome);
        } catch (Exception e) {
        }

    }

    public static String parsear(String[] args) {
        ///Respuesta
        StringBuilder res = new StringBuilder();
        CommandLineParser parser = null;
        CommandLine cmd = null;
        try {

            Options options = new Options();
            options.addOption("nologs", false, "OPTION to disables logs");
            options.addOption("logs", false, "OPTION to enable logs");
            options.addOption("lang", true, "OPTION to change language [ES, EN] (Spanish by default)");
            options.addOption("date", true, "OPTION to add an anchor date in the format yyyy-mm-dd (today by default)");
            options.addOption("text", true, "COMMAND to parse a text");
            options.addOption("f", true, "COMMAND to parse a file");
            options.addOption("outf", true, "COMMAND to save the output to a file"); 
            options.addOption("format", true, "COMMAND to choose the output format [timeml,json,nif] (TimeML by default)"); 
            options.addOption("help", false, "COMMAND to show help (Help)");
            parser = new BasicParser();
            cmd = parser.parse(options, args);
            String outp = "";
            
            if (cmd.hasOption("help") || args.length == 0) {
                new HelpFormatter().printHelp(Main.class.getCanonicalName(), options);
            }
            if (cmd.hasOption("lang")) {
                lang = cmd.getOptionValue("lang");
            }
            if (!cmd.hasOption("logs")) {
                initLoggerDisabled();
            }
            if (cmd.hasOption("date")) {
                date = cmd.getOptionValue("date");
            }
            if (cmd.hasOption("format")) {
                format = cmd.getOptionValue("format");
            }
            if (cmd.hasOption("f")) {
                String filename = cmd.getOptionValue("f");
                logger.info("Trying to parse the file " + filename);
                outp = parse(filename);
            }
            if (cmd.hasOption("text")) {
                String text = cmd.getOptionValue("text");
                logger.info("Trying to process the text " + text);
                outp = parseText(text);
            }
            if(cmd.hasOption("outf")){
                outpfilename = cmd.getOptionValue("outf");
                if(!writeFile(outp, outpfilename)){
                    logger.error("Error while writing.");
                } else{
                    logger.info("Output correctly written to " + outpfilename);
                }
            }
            if(outp != null){                
                System.out.println("\n----------------\n");
                System.out.println(outp);
                System.out.println("\n----------------\n");
            }

        } catch (Exception e) {

        }

        return res.toString();
    }

    public static String parse(String filename) {   
        String res = "";
        try {
            File f = new File(filename);
            logger.debug("parsing the folder " + filename);
            String input = FileUtils.readFileToString(f, "UTF-8");
            res = parseText(input);
                
        } catch (Exception e) {
            logger.error("error opening file");
            return "";
        }
        logger.info("Parsing correct\n\n");
        return res;
    }
    
    public static String parseText(String txt) {
        String res = "";
        Date dct = null;
        try{
            if (!date.matches("\\d\\d\\d\\d-(1[012]|0\\d)-(3[01]|[012]\\d)")) // Is it valid?
        {
            logger.info("No correct date provided, ");
            dct = Calendar.getInstance().getTime();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            date = df.format(dct);
        }
        
        Annotador annotador;
        
        if(lang.equalsIgnoreCase("ES")){
               // We innitialize the tagger in Spanish        
               annotador = new Annotador("es");
        }
        else if(lang.equalsIgnoreCase("EN")){
            annotador = new Annotador("en");
        }
        else{
            logger.error("error in language; for now, just available ES and EN");
            return res;
        }
        
        if(format.equalsIgnoreCase("timeml")){
            res = annotador.annotate(txt, date);
        } else if(format.equalsIgnoreCase("nif")){
            res = annotador.annotateNIF(txt, date, "ref", lang);
        } else if(format.equalsIgnoreCase("json")){
            res = annotador.annotateJSON(txt, date);
        } else{
            logger.error("Incorrect format; TimeML will be used.");
            res = annotador.annotate(txt, date);
        }
        
        } catch (Exception e) {
            logger.info(res);
            logger.error("error processing text");
            return "";
        }
       logger.info(res);
        logger.info("Text processing correct\n\n");

        return res;
    }

    public static void initLogger(boolean logs) {
        if (logs) {
            initLoggerDebug();
        } else {
            initLoggerDisabled();
        }

    }

    /**
     * Silencia todos los loggers. Una vez invocada esta función, la función que
     * arranca los logs normalmente queda anulada. Detiene también los logs
     * ajenos (de terceras librerías etc.)
     */
    private static void initLoggerDisabled() {
        logger = Logger.getLogger(Main.class);
        List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
        loggers.add(LogManager.getRootLogger());
        for (Logger log : loggers) {
            log.setLevel(Level.OFF);
        }
        Logger.getRootLogger().setLevel(Level.OFF);
        
        // We do this to void IxaPipes messages...
        PrintStream falseerr = new PrintStream(new OutputStream(){
            public void write(int b) {
            }
        });
        System.setErr(falseerr);
        
        // We turn off CoreNLP logger
        RedwoodConfiguration.current().clear().apply();        
        
        // We turn off some inner IxaPipes loggers
//        ch.qos.logback.classic.Logger logger1 = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(SpanishReadabilityModel.class);
//        logger1.setLevel(ch.qos.logback.classic.Level.OFF);           
//        ch.qos.logback.classic.Logger logger2 = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Hyphenator.class);
//        logger2.setLevel(ch.qos.logback.classic.Level.OFF);
//        ch.qos.logback.classic.Logger logger3 = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(BasicAnnotator.class);
//        logger3.setLevel(ch.qos.logback.classic.Level.OFF);
        
        logger.setLevel(Level.OFF);

        Logger.getRootLogger().removeAllAppenders();
Logger.getRootLogger().addAppender(new NullAppender());
    }

    /**
     * Si se desean logs, lo que se hace es: - INFO en consola - DEBUG en
     * archivo de logs logs.txt
     */
    private static void initLoggerDebug() {

        Enumeration currentLoggers = LogManager.getCurrentLoggers();
        List<Logger> loggers = Collections.<Logger>list(currentLoggers);
        loggers.add(LogManager.getRootLogger());
        for (Logger log : loggers) {
            log.setLevel(Level.OFF);
        }

        Logger root = Logger.getRootLogger();
        root.setLevel((Level) Level.DEBUG);

        //APPENDER DE CONSOLA (INFO)%d{ABSOLUTE} 
        PatternLayout layout = new PatternLayout("%d{HH:mm:ss} [%5p] %13.13C{1}:%-4L %m%n");
        ConsoleAppender appenderconsole = new ConsoleAppender(); //create appender
        appenderconsole.setLayout(layout);
        appenderconsole.setThreshold(Level.INFO);
        appenderconsole.activateOptions();
        appenderconsole.setName("console");
        root.addAppender(appenderconsole);

        //APPENDER DE ARCHIVO (DEBUG)
        PatternLayout layout2 = new PatternLayout("%d{ISO8601} [%5p] %13.13C{1}:%-4L %m%n");
        FileAppender appenderfile = null;
        String filename = "./logs/logs.txt";
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(new FileReader("pom.xml"));
            filename = "./logs/" + model.getArtifactId() + ".txt";
        } catch (Exception e) {
        }
        try {
            appenderfile = new FileAppender(layout2, filename, false);
            appenderfile.setName("file");
            appenderfile.setThreshold(Level.DEBUG);
            appenderfile.activateOptions();
        } catch (Exception e) {
        }

        root.addAppender(appenderfile);


        logger = Logger.getLogger(Main.class);
    }
    public static boolean writeFile(String input, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter w = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(w);
            bw.write(input);
            bw.flush();
            bw.close();
            return true;
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Annotador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return false;
    }

}
