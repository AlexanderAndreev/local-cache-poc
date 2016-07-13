package lucene.poc;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Test;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.util.List;

public class LuceneTest {

    private MongoGenerator generator;
    @BeforeClass
    private void setUp() throws IOException {
        generator = new MongoGenerator();
    }


    @Test
    public void test() throws IOException, ParseException {
        int numerOfObjects = 2000;
        MongoGenerator g = new MongoGenerator();
        LuceneGateway luceneGateway = LuceneGateway.getInstance();
        luceneGateway.insertObjects(g.generateMongoObjects(numerOfObjects));
        List<MongodbObject> result = luceneGateway.findObjects("", Integer.MAX_VALUE);
        Assert.assertEquals(result.size(), numerOfObjects);
    }
}
