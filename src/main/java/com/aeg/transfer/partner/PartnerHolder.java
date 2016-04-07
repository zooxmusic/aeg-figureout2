package com.aeg.transfer.partner;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Log4j2
@Component
public class PartnerHolder {

    @Value("${partners.file.path}")
    private String partnersFilePath;
    private Path partnersJson;

    private Partners partners = null;

    public Partner find(String name) throws IOException, URISyntaxException {
        for(Partner partner : partners.getPartners()) {
            if(partner.getName().toUpperCase().equalsIgnoreCase(name.toUpperCase())) return partner;
        }
        return null;
    }

    /*public static void main(String[] args) {
        PartnerHolder p = new PartnerHolder();
        try {
            p.read();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    private void read() throws URISyntaxException, IOException {
        //URL url = PartnerHolder.class.getResource("/META-INF/spring/integration/partners.json");
        //List<String> lines = Files.readAllLines(Paths.get(url.toURI()), Charset.forName("UTF-8"));

        List<String> lines = Files.readAllLines(partnersJson, Charset.forName("UTF-8"));
        StringBuilder builder = new StringBuilder();
        ExpressionParser parser = new SpelExpressionParser();

        for (String line : lines) {
            builder.append(line);
        }

        String json = sanitize(builder.toString());
        partners = new Gson().fromJson(json, Partners.class);
    }

    @PostConstruct
    public void init() throws Exception {
        URL url = PartnerHolder.class.getResource(partnersFilePath);
        partnersJson = Paths.get(url.toURI());
        read();
    }
    private String sanitize(String original) {
        GenericApplicationContext parent = new StaticApplicationContext();
        Environment env = parent.getEnvironment();
        String aegHome = env.getProperty("AEG_HOME");
        return original.replaceAll("#AEG_HOME", aegHome);
    }

    public Partners getPartners() {
        return partners;
    }

    public List<Partner> getPartnerList() {
        return getPartners().getPartners();
    }
}
