package oeg.tagger.core.tutorial.annotador;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import oeg.tagger.core.time.tictag.AnnotadorStandard;
import org.apache.commons.io.FileUtils;

/**
 * Tutorial of the AnnotadorStandard tagger Tagging the content of a TEST.txt file with
 detailed output in the TIMEX format
 *
 * @author mnavas
 */
public class TutorialESPfromTXTJSON {

    public static void main(String[] args) throws IOException {
        PrintWriter out;
        File f = new File("../annotador-core/src/main/resources/rules/TEST.txt");
        File outputFile = new File("../annotador-core/src/main/resources/rules/OUT-TEST.json");
        String input = FileUtils.readFileToString(f, "UTF-8");
        AnnotadorStandard tt = new AnnotadorStandard("ES");
        String outp = tt.annotateJSON(input, null);

        System.out.println(outp);
        FileOutputStream fos = new FileOutputStream(outputFile.getAbsolutePath());
        OutputStreamWriter w = new OutputStreamWriter(fos, "UTF-8");
        BufferedWriter bw = new BufferedWriter(w);
        bw.write(outp);
        bw.flush();
        bw.close();

    }

}
