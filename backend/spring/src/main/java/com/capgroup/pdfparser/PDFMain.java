package com.capgroup.pdfparser;

//sourced from Apache's website and Stack Overflow
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


public class PDFMain {
    /*it is up in the air whether or not parsing of a directory will be handled here or in conjunction with parsing the .xlsl dumps, currently this
    class supports the possibility of parsing all the existing files, then parsing a PDF on a new article being added to the database not through filemaker pro
     */
    public static String parseFile(File pdfFile) throws IOException {
        PDDocument doc = PDDocument.load(pdfFile);
        String text = new PDFTextStripper().getText(doc);
        doc.close();
        //System.out.println(text);
        return text;
    }
}
