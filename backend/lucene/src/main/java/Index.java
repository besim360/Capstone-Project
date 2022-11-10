import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;

public class Index {

    // eventually this method should take only a JSON as input and use the JSON data to make the document
    public static void makeDocument(IndexWriter iWriter, String title, String text, String source, String topics, String code,
                                    String authors, String startYear, String endYear) throws IOException {

        // do we need ID in lucene anymore?

        Document doc = new Document();
        doc.add(new Field("title", title, TextField.TYPE_STORED));
        doc.add(new Field("text", text, TextField.TYPE_STORED));
        doc.add(new Field("source", source, TextField.TYPE_STORED));
        doc.add(new Field("topics", topics, TextField.TYPE_STORED));
        doc.add(new Field("code", code, TextField.TYPE_STORED));
        doc.add(new Field("authors", authors, TextField.TYPE_STORED));
        doc.add(new Field("startYear", startYear, TextField.TYPE_STORED));
        doc.add(new Field("endYear", endYear, TextField.TYPE_STORED));
        // need to add all the other attributes needed for citation as fields or just JSON...

        iWriter.addDocument(doc);
    }

    public static void main(String[] args) {

    }
}



