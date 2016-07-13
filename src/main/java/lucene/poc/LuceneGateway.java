package lucene.poc;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LuceneGateway {

    public static final String LUCENE_INDEX_PATH = "lucene_index";
    private static volatile LuceneGateway INSTANCE;

    private IndexWriter writer;
    private IndexSearcher searcher;
    private Analyzer analyzer = new StandardAnalyzer();


    public void insertObjects(List<MongodbObject> mongoObjects) throws IOException {
        for (MongodbObject mongoObject : mongoObjects) {
            Document doc = new Document();
            doc.add(new TextField("name", mongoObject.getName(), Field.Store.YES));
            doc.add(new TextField("id", mongoObject.getId(), Field.Store.YES));
            writer.addDocument(doc);
        }
        writer.close();
    }

    public List<MongodbObject> findObjects(String queryLine, int pageSize) throws ParseException, IOException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(LUCENE_INDEX_PATH)));
        searcher = new IndexSearcher(reader);

        queryLine = "*" + queryLine.toLowerCase() + "*";

        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        booleanQuery.add(new WildcardQuery(new Term("name", queryLine)), BooleanClause.Occur.SHOULD);
        booleanQuery.add(new WildcardQuery(new Term("id", queryLine)), BooleanClause.Occur.SHOULD);

        Query query = booleanQuery.build();
        System.out.println("Searching for: " + query.toString());
        TopDocs results = searcher.search(query, pageSize);
        ScoreDoc[] hits = results.scoreDocs;
        int numTotalHits = results.totalHits;

        System.out.println("Found : "+numTotalHits +" hits");

        List<MongodbObject> storedObjects = new ArrayList<>();
        for (int i = 0; i < hits.length; i++) {
            Document doc = searcher.doc(hits[i].doc);
            MongodbObject mongoObject = new MongodbObject(doc.get("id"), doc.get("name"));
            storedObjects.add(mongoObject);
            System.out.println(mongoObject);
        }
        return storedObjects;
    }
    public static LuceneGateway getInstance() throws IOException {
        LuceneGateway localInstance = INSTANCE;
        if (localInstance == null) {
            synchronized (LuceneGateway.class) {
                localInstance = INSTANCE;
                if (localInstance == null) {
                    INSTANCE = localInstance = new LuceneGateway();
                }
            }
        }
        return localInstance;
    }



    private LuceneGateway() throws IOException {
        Directory dir = FSDirectory.open(Paths.get(LUCENE_INDEX_PATH));

        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        writer = new IndexWriter(dir, iwc);



    }
}
