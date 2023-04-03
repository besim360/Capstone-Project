package com.capgroup.pdfparser;

import com.capgroup.spring.model.Article;
import com.capgroup.spring.model.Subject;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;



@Component
public class DataInput {


    /**
     * Reads Excel file located at pathToMain and creates an Article for each row based on contents of that file.
     * @param pathToMain path to the main Excel file
     * @param pathToSources path to the Excel file containing sources
     * @param subjectMap map containing subjects to insert into article
     * @param pathToTexts path to a folder containing PDF files for parsing
     * @return an ArrayList of articles to build the index/database from
     * @throws IOException problems with file parsing may occur
     */
    @Transactional
    public static ArrayList<Article> enterData(String pathToMain, String pathToSources, Map<String, Subject> subjectMap, String pathToTexts) throws IOException {

        HashMap<String, String> sourceMap = createMapFromSheet(pathToSources, 63, 1, 0);
        //HashMap<String, String> subjectMap = createMapFromSheet(pathToSubjects, 582, 2, 0);

        FileInputStream file = new FileInputStream(new File(pathToMain));
        Workbook workbook = new XSSFWorkbook(file);

        Sheet sheet = workbook.getSheetAt(0);
        ArrayList<Article> articles = new ArrayList<>(6110);
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


            if (row.getCell(2) != null){
                if (row.getCell(2).getStringCellValue().charAt(0) != '_') {
                    File f = new File(pathToTexts + row.getCell(2).getStringCellValue());
                    if (f.exists() && !f.isDirectory()) {
                        article.setFullText(PDFMain.parseFile(f));
                    }
                }
            }



            if (row.getCell(3) != null) {
                article.setSourceAbbrev(row.getCell(3).getStringCellValue());
                String longSource = abbrevToLong(row.getCell(3).getStringCellValue(), sourceMap);
                if (longSource != null){
                    article.setSourceLong(longSource);
                } else {
                    //System.out.println(article.getSourceAbbrev());
                }
            }

            if (row.getCell(4).getStringCellValue().contains("-")){
                String[] years = row.getCell(4).getStringCellValue().split("-");
                article.setStartYear(Integer.parseInt(years[0]));
                article.setEndYear(Integer.parseInt(years[1]));
            } else {
                article.setStartYear(Integer.parseInt(row.getCell(4).getStringCellValue()));
                article.setEndYear(Integer.parseInt(row.getCell(4).getStringCellValue()));
            }

            if (row.getCell(5) != null) {
                article.setVolNum(row.getCell(5).getStringCellValue());
            }

            if (row.getCell(6) != null) {
                article.setDate(row.getCell(6).getStringCellValue());
            }

            if (row.getCell(7) != null) {
                article.setPages(row.getCell(7).getStringCellValue());
            }

            if (row.getCell(8) != null) {
                article.setDoi(row.getCell(8).getStringCellValue());
            }

            // need to add topics
            if (row.getCell(9) != null) {
                String codeString = row.getCell(9).getStringCellValue().replace("/", " "); //replace is here due to mistake in excel files
                //System.out.println("Code from cell: " + codeString);
                String[] codeArray = codeString.split(" ");
                //Set<Subject> subjectSet = new HashSet<>();
                for (var code : codeArray){
                    if (!code.isEmpty()) {
                        //System.out.println("Code: " + code);
                        try {
                           //var codeLong = Long.parseLong(code);
                           var subject = subjectMap.get(code);
                           if (subject != null) {
                               //subjectSet.add(subject); //grabs all relevant subject objects and inserts them into the set
                               article.addSubject(subject);
                           }
                        } catch (NumberFormatException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
                //article.setSubjectCodes(subjectSet);

                //article.setTopics(codeToTopic(codeArray, subjectMap).replaceAll("\\p{Punct}", ""));
            }


            articles.add(article);
        }
        return articles;
    }
    public static Map<String, Subject> getSubjects(String pathToSubjects) throws IOException{

        FileInputStream file = new FileInputStream(new File(pathToSubjects));
        Workbook workbook = new XSSFWorkbook(file);
        Map<String, Subject> subjectMap = new HashMap<>(64);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet){
            if (row.getRowNum() == 0){
                continue;
            }
            Subject subject = new Subject();
            //assuming subjectTopic,subjectGenTopic, subjectCode all mandatory
            var subjectTopic = row.getCell(0);
            var subjectGenTopic = row.getCell(1);
            var subjectCode = row.getCell(2);
            if (subjectCode  == null || subjectGenTopic == null || subjectTopic == null){
                System.out.println("WARNING: One of the subject code columns are incomplete.");
                continue;
            }
            subject.setSubjectCode(subjectCode.getStringCellValue());
            var genTopicString = subjectGenTopic.getStringCellValue();
            var topicString = subjectTopic.getStringCellValue();
            genTopicString = genTopicString.replaceAll("\\p{Punct}", "");
            topicString = topicString.replaceAll("\\p{Punct}", "");
            subject.setTopics(topicString);
            subject.setGeneralTopic(genTopicString);
            subjectMap.put(subject.getSubjectCode(), subject);
        }
        return subjectMap;
    }

    private static String abbrevToLong(String abbrev, HashMap<String, String> map){
        return map.get(abbrev);
    }

    /*private static String codeToTopic(String[] codes, HashMap<String, String> map){

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

    }*/

    /**
     * Creates a HashMap from an Excel file where the keys of the map are the values from a specified column and the
     * values of the map are the values from another specified column
     * @param sheetLocation the path to the sheet
     * @param initialCapacity the initial capacity of the HashMap
     * @param keyCol the key's column number
     * @param valueCol the value's column number
     * @return a HashMap with storing representing all rows in the provided Excel file with a key-value pair based on
     *          the provided keyCol and valueCol
     * @throws IOException
     */
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
}
