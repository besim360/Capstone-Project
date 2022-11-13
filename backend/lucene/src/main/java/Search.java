import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.*;
import org.apache.lucene.index.DirectoryReader;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public class Search {

    DirectoryReader ireader;
    IndexSearcher isearcher;
    Directory directory;
    Analyzer analyzer;
    String[] fields = {"title", "text", "authors", "code", "topics", "source"};
    MultiFieldQueryParser parser;

    public Search(Directory directory, Analyzer analyzer) throws IOException {
        this.directory = directory;
        this.analyzer = analyzer;
        this.ireader = DirectoryReader.open(directory);
        this.isearcher = new IndexSearcher(ireader);
        this.parser = new MultiFieldQueryParser(this.fields, this.analyzer);
    }

    // should eventually return a JSON to be passed back along to the middle-end
    public void makeSearch(String keyword) throws ParseException, IOException {

        String[] terms = keyword.split(" ");
        StringBuilder fuzzy = new StringBuilder();

        for (String term : terms){
            term += "~ ";
            fuzzy.append(term);
        }

        Query query = parser.parse(fuzzy.toString());

        var hits = isearcher.search(query, 10).scoreDocs;
        System.out.println("=======================================\nResults for '" + keyword + "'");
        for (var hit: hits) {
            Document hitDoc = isearcher.doc(hit.doc);
            System.out.println(hitDoc.get("title"));
        }
        System.out.println("=======================================\n\n");
    }

    public ScoreDoc[] fuzzySearch(String keyword){
        // break search into multiple pieces, using single field searches and then recombining them
        return null;
    }
}
