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
     */
    public static String parseFile(File pdfFile) throws IOException {
        PDDocument doc = PDDocument.load(pdfFile);
        var stripper = new PDFTextStripper();
        stripper.setLineSeparator(" ");
        String text = stripper.getText(doc).replaceAll("\n","");
        doc.close();
        return text;
    }
    public static String parseStream(InputStream pdfFile) throws IOException {
        PDDocument doc = PDDocument.load(pdfFile);
        var stripper = new PDFTextStripper();
        stripper.setLineSeparator(" ");
        String text = stripper.getText(doc).replaceAll("\n","");
        doc.close();
        return text;
    }
}
