package lucene.poc;

import java.util.UUID;

public class MongodbObject {
    private String name;
    private String id;

    public MongodbObject(String id, String name) {
        this.id = id;
        this.name = name;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "lucene.poc.MongodbObject{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
