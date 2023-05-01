package com.capgroup.pdfparser;

//sourced from Apache's website and Stack Overflow
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


public class PDFMain {
    /**
     * Parses PDF and returns all text in string format, make sure to wrap any calls of this function with a try and except
     * @param pdfFile File that will be parsed
     * @return entire file in a single string
     * @throws IOException when document fails to be parsed
     */
    public static String parseFile(File pdfFile) throws IOException {
        PDDocument doc = PDDocument.load(pdfFile);
        var stripper = new PDFTextStripper();
        stripper.setLineSeparator(" ");
        String text = stripper.getText(doc).replaceAll("\n","");
        doc.close();
        return text;
    }
    /**
     * Parses PDF and returns all text in string format, make sure to wrap any calls of this function with a try and except
     * @param pdfFile InputStream that will be parsed
     * @return entire file in a single string
     * @throws IOException when document fails to be parsed
     */
    public static String parseFile(InputStream pdfFile) throws IOException {
        PDDocument doc = PDDocument.load(pdfFile);
        var stripper = new PDFTextStripper();
        stripper.setLineSeparator(" ");
        String text = stripper.getText(doc).replaceAll("\n","");
        doc.close();
        return text;
    }
}
