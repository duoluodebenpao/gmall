package org.frank.common.learn;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

public class LearnExcel {
    public static void main(String[] args) throws Exception {

        FileInputStream fileInputStream = new FileInputStream(new File("C:\\share\\divice.xlsx"));

        Workbook workbook = new XSSFWorkbook(fileInputStream);

        Iterator<Sheet> iterator = workbook.iterator();
        while (iterator.hasNext()) {
            Sheet sheet = iterator.next();
            String sheetName = sheet.getSheetName();
            System.out.println(sheetName);

            Iterator<Row> rows = sheet.iterator();
            while (rows.hasNext()) {
                Row row = rows.next();
                if (row.getRowNum() == 0) {
                    // 检查列头顺序
                    checkHeadInfo(row, 0, "lac");
                    checkHeadInfo(row, 1, "ci");
                    checkHeadInfo(row, 2, "维度");
                    checkHeadInfo(row, 3, "所属区县");
                    checkHeadInfo(row, 4, "运营商");
                    checkHeadInfo(row, 5, "制式");
                    checkHeadInfo(row, 6, "名称");
                } else {
                    System.out.println((long) row.getCell(0).getNumericCellValue());
                }
            }
        }
    }

    private static void checkHeadInfo(Row row, int index, String fieldName) {
        String headName = row.getCell(index).getStringCellValue();
        boolean flag = fieldName.equals(headName.trim().toLowerCase());
        if (!flag) {
            throw new RuntimeException("excel的头信息 [" + headName + "]不符合规范, excel正确的表头格式为 [ lac,ci,维度,所属区县,运营商,制式,名称 ]");
        }
    }
}
