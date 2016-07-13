package lucene.poc;

import lucene.poc.MongodbObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class MongoGenerator {

    private List<String> cityNames;

    public MongoGenerator() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("dump").getFile());
        cityNames = FileUtils.readLines(file, "utf-8");
    }

    private String getRandomCityName(){
        return cityNames.get(ThreadLocalRandom.current().nextInt(cityNames.size()));
    }



    public List<MongodbObject> generateMongoObjects(int amount){
        List<MongodbObject> resultList = new ArrayList<>();
        while(amount > 0 ){
            MongodbObject e = new MongodbObject(UUID.randomUUID().toString(), getRandomCityName());
            resultList.add(e);
            amount--;
            System.out.println(e);
        }
        return  resultList;
    }

}
