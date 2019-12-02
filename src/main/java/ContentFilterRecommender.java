import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class ContentFilterRecommender {

    private String[] userHistory = {
            "soccer",
            "football",
            "milan",
            "red",
            "milan"
    };

    private static class Item {
        private float probability;
        private String tag;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public float getProbability() {
            return probability;
        }

        public void setProbability(float probability) {
            this.probability = probability;
        }
    }

    private static List<Item> items = new LinkedList<>();

    public static void main(String args[]) throws IOException {
        InputStream is = new FileInputStream(new File(Const.TAGS_PATH));
        Workbook itemsBook = new HSSFWorkbook(is);
        Sheet itemsSheet = itemsBook.getSheet("Лист1");
        boolean skipFirst = true;
        for (Row row : itemsSheet) {
            if (skipFirst) {
                skipFirst = false;
                continue;
            }
            Item item = new Item();
            item.setTag(row.getCell(2).getStringCellValue());
            item.setProbability((float) row.getCell(1).getNumericCellValue());
            items.add(item);
        }
        is.close();
    }

}
