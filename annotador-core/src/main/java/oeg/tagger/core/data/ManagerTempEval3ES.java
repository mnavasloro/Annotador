package oeg.tagger.core.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager of the Spanish corpus TempEval 3
 *
 * @author mnavas
 */
public class ManagerTempEval3ES {

    File cleanDocs = new File("../data/datasets/timeEval/tempeval3ES/test/test-clean/"); // Path to the folder with the input (clean) files
    File testDocs = new File("../data/datasets/timeEval/tempeval3ES/test/test/"); // Path to the folder with the test files
    File outDocs = new File("../data/datasets/timeEval/tempeval3ES/test/output/t/"); // Path to the folder to store the output files

    public List<FileTempEval3ES> lista = new ArrayList<FileTempEval3ES>();

    public ManagerTempEval3ES(String fInput, String fTest, String fOut) {
        cleanDocs = new File(fInput);
        testDocs = new File(fTest);
        outDocs = new File(fOut);

        for (File f : cleanDocs.listFiles()) {
            FileTempEval3ES ft3 = new FileTempEval3ES(f, testDocs, outDocs);
            lista.add(ft3);
        }
    }

    public ManagerTempEval3ES() {

        for (File f : cleanDocs.listFiles()) {
            FileTempEval3ES ft3 = new FileTempEval3ES(f, testDocs, outDocs);
            lista.add(ft3);
        }
    }

    public void main() {

    }

}
