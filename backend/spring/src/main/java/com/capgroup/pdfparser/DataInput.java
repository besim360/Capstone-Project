package com.capgroup.pdfparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.capgroup.spring.model.Article;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class DataInput {



    public static ArrayList<Article> enterData(String location) throws IOException {

        HashMap<String, String> sourceMap = createMapFromSheet("backend/excelfiles/SourcesYes_18Jan2023.xlsx", 63, 1, 0);
        HashMap<String, String> subjectMap = createMapFromSheet("backend/excelfiles/SubjectsYes_18Jan2023.xlsx", 582, 2, 0);

        FileInputStream file = new FileInputStream(new File(location));
        Workbook workbook = new XSSFWorkbook(file);

        Sheet sheet = workbook.getSheetAt(0);
        ArrayList<Article> articles = new ArrayList<>(5706);
        for (Row row : sheet){

            if (row.getRowNum() == 0){
                continue;
            }

            Article article = new Article();

            if (row.getCell(0) != null) {
                article.setAuthors(row.getCell(0).getStringCellValue());
            }

            if (row.getCell(1) != null) {
                article.setTitle(row.getCell(1).getStringCellValue());
            }

            if (row.getCell(2) != null) {
                article.setSourceAbbrev(row.getCell(2).getStringCellValue());
                String longSource = abbrevToLong(row.getCell(2).getStringCellValue(), sourceMap);
                if (longSource != null){
                    article.setSourceLong(longSource);
                } else {
                    System.out.println(article.getSourceAbbrev());
                }
            }

            if (row.getCell(3) != null) {
                article.setVolNum(row.getCell(3).getStringCellValue());
            }

            if (row.getCell(4) != null) {
                article.setDate(row.getCell(4).getStringCellValue());
            }

            if (row.getCell(5).getStringCellValue().contains("-")){
                String[] years = row.getCell(5).getStringCellValue().split("-");
                article.setStartYear(Integer.parseInt(years[0]));
                article.setEndYear(Integer.parseInt(years[1]));
            } else {
                article.setStartYear(Integer.parseInt(row.getCell(5).getStringCellValue()));
                article.setEndYear(Integer.parseInt(row.getCell(5).getStringCellValue()));
            }

            if (row.getCell(6) != null) {
                article.setPages(row.getCell(6).getStringCellValue());
            }

            // need to add topics
            if (row.getCell(7) != null) {
                String codeString = row.getCell(7).getStringCellValue().replace("/", ", ");
                String[] codeArray = codeString.split(", ");

                article.setSubjectCodes(codeString);
                article.setTopics(codeToTopic(codeArray, subjectMap));
            }

            if (row.getCell(8) != null) {
                article.setDoi(row.getCell(8).getStringCellValue());
            }
            articles.add(article);
        }
        return articles;
    }

    private static String abbrevToLong(String abbrev, HashMap<String, String> map){
        return map.get(abbrev);
    }

    private static String codeToTopic(String[] codes, HashMap<String, String> map){

        String topics = "";
        for (String code : codes){
            String value = map.get(code);
            if (value != null){
                topics = topics + ", " + value;
            }
        }
        if (topics.contains(", ")) {
            return topics.substring(2);
        }
        return topics;

    }

    private static HashMap<String, String> createMapFromSheet(String sheetLocation, int initialCapacity, int keyCol, int valueCol) throws IOException {

        FileInputStream file = new FileInputStream(new File(sheetLocation));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        HashMap<String, String> map = new HashMap<>(initialCapacity);
        for (Row row : sheet){
            if (row.getRowNum() == 0){
                continue;
            }
            map.put(row.getCell(keyCol).getStringCellValue(), row.getCell(valueCol).getStringCellValue());
        }
        return map;
    }

    /*
    public static void main(String[] args) throws IOException {
        //String currentPath = new java.io.File(".").getCanonicalPath();
        //System.out.println("Current dir:" + currentPath);
        DataInput.enterData("backend/excelfiles/MainFormYes_18Jan2023.xlsx");
    }
     */
}
