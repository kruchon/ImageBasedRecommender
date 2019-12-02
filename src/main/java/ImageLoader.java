import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class ImageLoader {

    public static void main(String args[]) throws IOException {
        File file = new File(Const.IMAGES_PATH);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = new HSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheet("Лист1");
        int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            for (int i = 0; i < rowCount + 1; i++) {
                Row row = sheet.getRow(i);
                String item = row.getCell(Const.CURRENT_COLUMN).getStringCellValue();
                HtmlPage page = webClient.getPage("https://www.google.com/search?q=" + item + "&sxsrf=ACYBGNQAgoapFNHzDcqiwhfHxz525r9jUQ:1573991209326&source=lnms&tbm=isch&sa=X&ved=0ahUKEwiM_qSXlvHlAhUMmYsKHZ9MApAQ_AUIEigB&cshid=1573991239120766&biw=1666&bih=953");
                Thread.sleep(1000);
                List<Object> images = page.getByXPath("/html/body/div[6]/div[3]/div[3]/div[2]/div/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[*]/a[*]/img");
                for (int k = 1; k < (images.size() < 15 ? images.size() : 15); k++) {
                    ImageReader imageReader = ((HtmlImage) images.get(k)).getImageReader();
                    BufferedImage image = imageReader.read(0);
                    File outputfile = new File(Const.DATASET_PATH + "\\" + i + "_" + k + ".png");
                    ImageIO.write(image, "png", outputfile);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}