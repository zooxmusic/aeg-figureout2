package com.aeg.transfer.partner;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by bszucs on 4/6/2016.
 */
public class PartnerReader {

    public static Partners read() throws URISyntaxException, IOException {
        //JsonSlurper slurper = new JsonSlurper();
        URL uri = PartnerReader.class.getResource("/META-INF/spring/integration/partners.json");
        //BufferedReader br = new BufferedReader(new InputStreamReader(is));

        List<String> lines = Files.readAllLines(Paths.get(uri.toURI()), Charset.forName("UTF-8"));
        StringBuilder builder = new StringBuilder();
        for(String line : lines) {
            builder.append(line);
        }
        String json = builder.toString();
        System.out.println(json);
        //return new JsonSlurper().parse(is);
        //JsonReader reader = new JsonReader(new InputStreamReader(is));

        /*GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Partner.class, new PartnerSerializer());
        gsonBuilder.registerTypeAdapter(Partner.class, new PartnerDeserializer());
        gsonBuilder.registerTypeAdapter(FileMapping.class, new FileMappingSerializer());
        gsonBuilder.registerTypeAdapter(FileMapping.class, new FileMappingDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();*/

        Partners partners = new Gson().fromJson(json, Partners.class);
        //Partners partners = gson.fromJson(json);
        //String json = gson.toJson(partners);

        //Partners partners =  new Gson().fromJson(reader,  Partners.getClass())
        return partners;
    }

}
