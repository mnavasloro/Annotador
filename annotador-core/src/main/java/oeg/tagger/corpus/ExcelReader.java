/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.tagger.corpus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONObject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Maria
 */
public class ExcelReader {

    List<AnnotationExcel> annotations = new ArrayList<AnnotationExcel>();

    public List<AnnotationExcel> read(String filename) throws IOException {
        File excelFile = new File(filename);
        FileInputStream fis = new FileInputStream(excelFile);

        // we create an XSSF Workbook object for our XLSX Excel File
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        // we get first sheet
        XSSFSheet sheet = workbook.getSheetAt(0);

        // we iterate on rows
        Iterator<Row> rowIt = sheet.iterator();
        Row rowini = rowIt.next();
        
        while (rowIt.hasNext()) {
            int i = 0;
            Row row = rowIt.next();
            Cell cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);

            // Order of the columns: TEXTO	INICIO	FIN	VALOR	TYPE	OTHER

            AnnotationExcel annEx = new AnnotationExcel();
            if (cell != null) {
                annEx.Text = JSONObject.escape(cell.toString());
            }
            i++;
            
            cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
            if (cell != null) {
                annEx.Begin = JSONObject.escape(cell.toString());
            }
            i++;
            
            cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
            if (cell != null) {
                annEx.End = JSONObject.escape(cell.toString());
            }
            i++;
            
            cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
            if (cell != null) {
                annEx.Value = JSONObject.escape(cell.toString());
            }
            i++;
            
            cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
                if (cell != null) {
                    annEx.Type = JSONObject.escape(cell.toString());
                }
            i++;
            
            cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
                if (cell != null) {
                    annEx.Info = JSONObject.escape(cell.toString());
                }
            i++;
            
            annEx.tid=i;

//            System.out.println(annEx.toString() + "\n------------");

            annotations.add(annEx);
        }

        workbook.close();
        fis.close();
        return annotations;
    }

}
