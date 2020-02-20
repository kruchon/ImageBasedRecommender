package org.ius.gradcit.prototype.datapreparation;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.ius.gradcit.prototype.Const;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ImageConverter {

    private static int i = 0;

    public static void main(String args[]) throws Exception {
        Workbook tagsBook = new HSSFWorkbook();
        Sheet sheet = tagsBook.createSheet("Лист" + (Const.CURRENT_COLUMN + 1));
        final File folder = new File(Const.DATASET_PATH);
        File[] files = folder.listFiles();
        for (File imageFile : files) {
            String fileName = imageFile.getName();
            String id = fileName.split("\\.")[0];
            try {
                detectLabels(Const.DATASET_PATH + "\\" + fileName, id, sheet);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Iteration " + i);
        }
        FileOutputStream outputStream = new FileOutputStream(Const.TAGS_PATH);
        tagsBook.write(outputStream);
        outputStream.close();
    }

    private static void detectLabels(String s, String id, Sheet sheet) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(s));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    Row row = sheet.createRow(i);
                    Cell idCell = row.createCell(0);
                    Cell scoreCell = row.createCell(1);
                    Cell descriptionCell = row.createCell(2);
                    Cell midCell = row.createCell(3);
                    Cell topicallityCell = row.createCell(4);
                    scoreCell.setCellValue(annotation.getScore());
                    topicallityCell.setCellValue(annotation.getTopicality());
                    descriptionCell.setCellValue(annotation.getDescription());
                    midCell.setCellValue(annotation.getMid());
                    idCell.setCellValue(id);
                    i++;
                }
            }
        }
    }
}
