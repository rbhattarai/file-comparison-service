package com.trp.file_comparison_service.service;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ExcelComparisonService {

    public List<Map<String, Object>> compareFiles(MultipartFile actualFile, MultipartFile expectedFile) throws IOException {
        Workbook actualWorkbook = WorkbookFactory.create(actualFile.getInputStream());
        Workbook expectedWorkbook = WorkbookFactory.create(expectedFile.getInputStream());

        List<Map<String, Object>> resultList = new ArrayList<>();

        int numberOfSheets = actualWorkbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet actualSheet = actualWorkbook.getSheetAt(i);
            Sheet expectedSheet = expectedWorkbook.getSheetAt(i);

            Map<String, Object> sheetResult = new HashMap<>();
            sheetResult.put("Sheet Name", actualSheet.getSheetName());

            if (expectedSheet == null) {
                sheetResult.put("error", "FAIL - Expected sheet missing");
            } else {
                Map<String, String> comparisonResults = new HashMap<>();
                compareSheets(actualSheet, expectedSheet, comparisonResults);
                sheetResult.putAll(comparisonResults);
            }

            resultList.add(sheetResult);
        }

        return resultList;
    }

    public List<Map<String, Object>> compareFiles(File actualFile, File expectedFile) throws IOException {
        Workbook actualWorkbook = WorkbookFactory.create(actualFile);
        Workbook expectedWorkbook = WorkbookFactory.create(expectedFile);

        List<Map<String, Object>> resultList = new ArrayList<>();

        int numberOfSheets = actualWorkbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet actualSheet = actualWorkbook.getSheetAt(i);
            Sheet expectedSheet = expectedWorkbook.getSheetAt(i);

            Map<String, Object> sheetResult = new HashMap<>();
            sheetResult.put("Sheet Name", actualSheet.getSheetName());

            if (expectedSheet == null) {
                sheetResult.put("error", "FAIL - Expected sheet missing");
            } else {
                Map<String, String> comparisonResults = new HashMap<>();
                compareSheets(actualSheet, expectedSheet, comparisonResults);
                sheetResult.putAll(comparisonResults);
            }

            resultList.add(sheetResult);
        }

        return resultList;
    }

    private void compareSheets(Sheet actualSheet, Sheet expectedSheet, Map<String, String> result) {
        Iterator<Row> actualRowIterator = actualSheet.iterator();
        Iterator<Row> expectedRowIterator = expectedSheet.iterator();

        while (actualRowIterator.hasNext() && expectedRowIterator.hasNext()) {
            Row actualRow = actualRowIterator.next();
            Row expectedRow = expectedRowIterator.next();

            compareRows(actualRow, expectedRow, result);
        }
    }

    private void compareRows(Row actualRow, Row expectedRow, Map<String, String> result) {
        int lastCellNum = Math.max(actualRow.getLastCellNum(), expectedRow.getLastCellNum());
        for (int i = 0; i < lastCellNum; i++) {
            Cell actualCell = actualRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            Cell expectedCell = expectedRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

            String actualValue = getCellValue(actualCell);
            String expectedValue = getCellValue(expectedCell);

            String cellAddress = actualCell.getAddress().formatAsString();

            if (!actualValue.equals(expectedValue)) {
                result.put(cellAddress, "FAIL - Expected value: " + expectedValue + " vs Actual value: " + actualValue);
            } else {
                result.put(cellAddress, "PASS");
            }
        }
    }

    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return Double.toString(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

}
