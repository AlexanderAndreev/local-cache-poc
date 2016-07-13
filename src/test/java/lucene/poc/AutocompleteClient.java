package lucene.poc;

import org.apache.lucene.queryparser.classic.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class AutocompleteClient {
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("Autocomplete client started");

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        while (true) {
            System.out.println("Enter query: ");
            String line = in.readLine();
            System.out.println("Output: "+ line);
            LuceneGateway luceneGateway = LuceneGateway.getInstance();
            luceneGateway.findObjects(line, 10);
        }

    }
}
