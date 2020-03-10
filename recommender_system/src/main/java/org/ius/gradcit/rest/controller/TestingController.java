package org.ius.gradcit.rest.controller;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.ius.gradcit.database.repository.UserRepository;
import org.ius.gradcit.logic.image.ImageService;
import org.ius.gradcit.logic.recommender.Recommender;
import org.ius.gradcit.logic.user.UserService;
import org.ius.gradcit.rest.entity.ContentRecommendation;
import org.ius.gradcit.rest.entity.UserAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class TestingController {

    private final ImageService imageService;
    private final Map<String, Object> testUsers;
    private final UserRepository userRepository;
    private final Recommender recommender;
    private final UserService userService;

    private static final int MAX_DEPTH = 10;
    private static final String RESULTS_PATH = "C:\\gradcit\\recommender\\testdata\\results.xls";

    @Autowired
    public TestingController(ImageService imageService,
                             @Qualifier("testUsers") Map<String, Object> testUsers,
                             UserRepository userRepository,
                             Recommender recommender,
                             UserService userService) {
        this.imageService = imageService;
        this.testUsers = testUsers;
        this.userRepository = userRepository;
        this.recommender = recommender;
        this.userService = userService;
    }

    @GetMapping("saveImagesFromExcel")
    public void saveImagesFromExcel() {
        new Thread(() -> {
            List<String> imageIds = null;
            try {
                imageIds = Files.walk(Paths.get("C:\\gradcit\\recommender\\testdata\\dataset"))
                        .skip(1)
                        .map(s -> s.toString().replace("C:\\gradcit\\recommender\\testdata\\dataset\\", "")
                                .replaceAll("\\.png", ""))
                        .collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (String imageId : imageIds) {
                imageService.saveImage(imageId, "testUser");
                System.out.println(imageId);
            }
        }).start();
    }

    @PostMapping("testRecommender")
    @SuppressWarnings("unchecked")
    public void testRecommender() throws IOException {
        Workbook workbook = new HSSFWorkbook();
        Sheet precisionSheet = workbook.getSheet("Precision");
        Sheet recallSheet = workbook.getSheet("Recall");
        Sheet timeSheet = workbook.getSheet("Time");

        List<Map<String, Object>> users = (List<Map<String, Object>>) ((Map<String, Object>) testUsers.get("testUsers")).get("users");
        for (Map<String, Object> userJson : users) {
            recreateUser(userJson);
            String userId = (String) userJson.get("name");
            Row precisionHeaderRow = precisionSheet.getRow(0);
            Row recallHeaderRow = precisionSheet.getRow(0);
            DecimalFormat df = new DecimalFormat("#.##");
            int rowCount = 1;
            for (float q = 0.0f; q < 1.0f; q += 0.1f, rowCount++) {
                precisionHeaderRow.getCell(rowCount).setCellValue(df.format(q));
                recallHeaderRow.getCell(rowCount).setCellValue(df.format(q));
            }
            rowCount = 1;
            for (int searchDepth = 0; searchDepth < MAX_DEPTH; searchDepth++) {
                int columnCount = 1;
                Row precisionRow = precisionSheet.getRow(rowCount);
                Row recallRow = recallSheet.getRow(rowCount);
                precisionRow.getCell(0).setCellValue(searchDepth);
                recallRow.getCell(0).setCellValue(searchDepth);
                for (float q = 0.0f; q < 1.0f; q += 0.1f) {
                    Cell precisionCell = precisionRow.getCell(columnCount);
                    Cell recallCell = recallRow.getCell(columnCount);
                    List<ContentRecommendation> recommendations = recommender.generateContentRecommendation(q, userId, String.valueOf(searchDepth));
                    Set<String> expected = new HashSet<>((List<String>) userJson.get("expected"));
                    int right = 0;
                    for (ContentRecommendation recommendation : recommendations) {
                        if (expected.contains(recommendation.getImgId())) {
                            right++;
                        }
                    }
                    precisionCell.setCellValue(((double) right) / recommendations.size());
                    recallCell.setCellValue(((double) right) / expected.size());
                    columnCount++;
                }
                rowCount++;
            }
            Row header = timeSheet.getRow(0);
            Row res = timeSheet.getRow(1);
            for (int searchDepth = 0; searchDepth < MAX_DEPTH; searchDepth++) {
                header.getCell(searchDepth).setCellValue(searchDepth);
                recreateUser(userJson);
                long start = System.currentTimeMillis();
                List<ContentRecommendation> contentRecommendations = recommender.generateContentRecommendation(0.7f, userId, String.valueOf(searchDepth));
                long end = System.currentTimeMillis();
                System.out.println(contentRecommendations);
                res.getCell(searchDepth).setCellValue(((double) end - start) / 1000);
            }

            FileOutputStream outputStream = new FileOutputStream(RESULTS_PATH);
            workbook.write(outputStream);
            outputStream.close();
        }
    }

    private void recreateUser(Map<String, Object> userJson) {
        String userId = (String) userJson.get("name");
        userRepository.deleteByExternalId(userId);
        List<String> likes = (List<String>) userJson.get("like");
        for (String like : likes) {
            UserAction userAction = new UserAction();
            userAction.setActionType("LIKE");
            userAction.setImageId(like);
            userAction.setUserId(userId);
            userService.incInterest(userAction);
        }
    }
}
