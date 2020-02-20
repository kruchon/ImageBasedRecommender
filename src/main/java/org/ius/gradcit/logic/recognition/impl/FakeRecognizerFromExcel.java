package org.ius.gradcit.logic.recognition.impl;

import com.google.common.base.Preconditions;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.ius.gradcit.logic.recognition.Recognizer;
import org.ius.gradcit.prototype.Const;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class FakeRecognizerFromExcel implements Recognizer {

    private Sheet tagsSheet;

    @PostConstruct
    protected void openTagsSheet() throws IOException {
        File file = new File(Const.TAGS_PATH);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = new HSSFWorkbook(inputStream);
        tagsSheet = workbook.getSheet("Лист1");
    }

    @Override
    public Collection<RecognitionResult> recognize(String imageId) {
        Preconditions.checkNotNull(tagsSheet);
        Collection<RecognitionResult> recognitionResults = new ArrayList<>(50);
        for (int i = 1; i < tagsSheet.getLastRowNum(); i++) {
            Row row = tagsSheet.getRow(i);
            String photoID = row.getCell(0).getStringCellValue();
            if (imageId.equals(photoID)) {
                RecognitionResult recognitionResult = createResult(row);
                recognitionResults.add(recognitionResult);
            }
        }
        return recognitionResults;
    }

    private RecognitionResult createResult(Row row) {
        float probability = (float) row.getCell(1).getNumericCellValue();
        String thematics = row.getCell(2).getStringCellValue();
        return new RecognitionResult(thematics, probability);
    }
}
