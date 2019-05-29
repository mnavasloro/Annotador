package oeg.tagger.corpus;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

/**
 * Main class of the ECJ visualizer demo application.
 *
 * @author vroddon
 * @author mnavas
 */
public class Main {

    /// El logger se inicializa más tarde porque a estas alturas todavía no sabemos el nombre del archivo de logs.
    static Logger logger = null;
    static boolean logs = false;

    public static void main(String[] args) {

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
            options.addOption("parse", true, "COMMAND to parse a file ");
            options.addOption("help", false, "COMMAND to show help (Help)");
            parser = new BasicParser();
            cmd = parser.parse(options, args);

            if (cmd.hasOption("help") || args.length == 0) {
                new HelpFormatter().printHelp(Main.class.getCanonicalName(), options);
            }
            if (cmd.hasOption("parse")) {
                String filename = cmd.getOptionValue("parse");
                logger.info("Trying to parse the file " + filename);
                parse(filename);
            }

        } catch (Exception e) {

        }

        return res.toString();
    }

    public static void parse(String filename) {
       String res = "";
        String txt = "";
        
//        ECJvisualizer cf = new ECJvisualizer();
        
        try {
            File f = new File(filename);
            if(!filename.endsWith("xslx")){
            logger.debug("parsing the folder " + filename);
//                res += cf.visualizeSeveral(filename);
            }
            else{
            logger.debug("parsing the file " + filename);
//                res += cf.visualizeSingle(filename);
            }
                
        } catch (Exception e) {
            logger.info(res);
            logger.error("error opening file");
            return;
        }
       logger.info(res);
        logger.info("Parsing correct\n\n");
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
