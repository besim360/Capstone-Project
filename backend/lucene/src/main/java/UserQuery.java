import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

import static junit.framework.TestCase.assertEquals;

public class UserQuery {
    public static void main(String[] args) throws IOException, ParseException {

        // This block of code is for adding documents to the index,
        // for right now the index is temporary and the docs are just examples for testing
        Analyzer analyzer = new StandardAnalyzer();
        Path indexPath = Files.createTempDirectory("tempIndex");
        Directory directory = FSDirectory.open(indexPath);

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter iwriter = new IndexWriter(directory, config);

        Index.makeDocument(iwriter, "Lucene",
                "This is a test for the searching method in lucene. I really hope that this test works",
                "CS420", "Computer Science, Algorithms", "a/001", "Gabe Reed",
                "2022", "2023");
        Index.makeDocument(iwriter, "Whoosh",
                "Testing lucen is really fun, lucene!",
                "CS450", "Computer Science, Software", "b/001", "Gabe Reed",
                "2022", "2023");
        Index.makeDocument(iwriter, "Pylucene",
                "Article about digital forensics",
                "CS425", "Computer Science, Forensics", "a/001", "Gabe Reed",
                "2022", "2023");
        Index.makeDocument(iwriter, "MySQL",
                "Whoosh whoosh whoosh",
                "CS447", "Computer Science, Video Game Design", "a/001", "Gabe Reed",
                "2022", "2023");
        Index.makeDocument(iwriter, "Keycloak",
                "Whoish whoish whoish",
                "CS442", "Computer Science, Computer Graphics", "a/001", "Gabe Reed",
                "2020", "2023");

        iwriter.close();

        // beginning a query input loop for entering keywords in the terminal
        Search search = new Search(directory, analyzer);
        String line;
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        while (true){
            System.out.println("Enter a query to search for (enter exit to quit program)");
            line = reader.readLine();
            if(line.equals("exit")){
                break;
            }
            search.makeSearch(line);
        }

        search.ireader.close();
    }
}
