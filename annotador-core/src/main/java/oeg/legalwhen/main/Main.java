package oeg.annotador.main;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import oeg.annotador.core.time.tictag.Annotador;
import oeg.annotador.core.time.tictag.TicTag;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

/**
 * Main class of the jar.
 *
 * @author vroddon
 * @author mnavas
 */
public class Main {

    /// El logger se inicializa más tarde porque a estas alturas todavía no sabemos el nombre del archivo de logs.
    static Logger logger = null;
    static boolean logs = false;
    static String lang = "es";
    static String date = "";

    public static void main(String[] args) {
        
        BasicConfigurator.configure();

        init(args);

        if (args.length != 0) {
            String res = parsear(args);
            if (!res.isEmpty()) {
                System.out.println(res);
            }
        }
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
            options.addOption("lang", true, "OPTION to change language (Spanish by default)");
            options.addOption("date", true, "OPTION to add an anchor date (none by default)");
            options.addOption("text", true, "COMMAND to parse a text ");
            options.addOption("f", true, "COMMAND to parse a file ");
            options.addOption("help", false, "COMMAND to show help (Help)");
            parser = new BasicParser();
            cmd = parser.parse(options, args);

            if (cmd.hasOption("help") || args.length == 0) {
                new HelpFormatter().printHelp(Main.class.getCanonicalName(), options);
            }
            if (cmd.hasOption("lang")) {
                lang = cmd.getOptionValue("lang");
            }
            if (cmd.hasOption("date")) {
                date = cmd.getOptionValue("date");
            }
            if (cmd.hasOption("f")) {
                String filename = cmd.getOptionValue("f");
                logger.info("Trying to parse the file " + filename);
                parse(filename);
            }
            if (cmd.hasOption("text")) {
                String text = cmd.getOptionValue("text");
                logger.info("Trying to process the text " + text);
                parseText(text);
            }


        } catch (Exception e) {

        }

        return res.toString();
    }

    public static void parse(String filename) {        
        try {
            File f = new File(filename);
            logger.debug("parsing the folder " + filename);
            String input = FileUtils.readFileToString(f, "UTF-8");
            parseText(input);
                
        } catch (Exception e) {
            logger.error("error opening file");
            return;
        }
        logger.info("Parsing correct\n\n");
    }
    
    public static void parseText(String txt) {
        String res = "";
        
        try{
            if (!date.matches("\\d\\d\\d\\d-(1[012]|0\\d)-(3[01]|[012]\\d)")) // Is it valid?
        {
            date = null; // If not, we use no date (so anchor values will not be normalized)
        }
        
        if(lang.equalsIgnoreCase("ES")){
            Annotador annotador = new Annotador("es");   // We innitialize the tagger in Spanish
            res = annotador.annotate(txt, date);
        }
        else if(lang.equalsIgnoreCase("EN")){
            TicTag tt = new TicTag("EN");
            res = tt.annotate(txt);
        }
        else{
            logger.error("error in language; for now, just available ES and EN");
        }
        
        } catch (Exception e) {
            logger.info(res);
            logger.error("error processing text");
            return;
        }
       logger.info(res);
        logger.info("Parsing correct\n\n");
        
        System.out.println(res);
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
        
        logger.setLevel(Level.ERROR);

    }

    /**
     * Si se desean logs, lo que se hace es: - INFO en consola - DEBUG en
     * archivo de logs logs.txt
     * http://stackoverflow.com/questions/8965946/configuring-log4j-loggers-programmatically
     */
    private static void initLoggerDebug() {
        //Empezamos limpiando los loggers de mierda que se nos cuelan. Aquí mandamos nosotros cojones ya.
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

}
