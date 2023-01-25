package com.capgroup.spring;

import com.capgroup.pdfparser.PDFMain;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void parsePDF(){ //not a good usage of testing but ensures the library is giving an expected output for later usage
		try{
			String text = PDFMain.parseFile(new File("/home/danschool/Downloads/Aagbola_Liberating_JTWC_2012.pdf"));
			System.out.println(text);
		} catch (IOException e){
			e.printStackTrace();
		}
	}

}
