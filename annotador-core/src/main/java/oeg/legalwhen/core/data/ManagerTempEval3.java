package oeg.annotador.core.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager of the corpus TempEval 3
 *
 * @author mnavas
 */
public class ManagerTempEval3 {

    File cleanDocs = new File("../data/datasets/timeEval/tempeval3/test/test-clean/"); // Path to the folder with the input (clean) files
    File testDocs = new File("../data/datasets/timeEval/tempeval3/test/test/"); // Path to the folder with the test files
    File outDocs = new File("../data/datasets/timeEval/tempeval3/test/output/t/"); // Path to the folder to store the output files

    public List<FileTempEval3> lista = new ArrayList<FileTempEval3>();

    public ManagerTempEval3(String fInput, String fTest, String fOut) {
        cleanDocs = new File(fInput);
        testDocs = new File(fTest);
        outDocs = new File(fOut);

        for (File f : cleanDocs.listFiles()) {
            FileTempEval3 ft3 = new FileTempEval3(f, testDocs, outDocs);
            lista.add(ft3);
        }
    }

    public ManagerTempEval3() {

        for (File f : cleanDocs.listFiles()) {
            FileTempEval3 ft3 = new FileTempEval3(f, testDocs, outDocs);
            lista.add(ft3);
        }
    }

    public void main() {

    }

}
