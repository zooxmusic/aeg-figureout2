package com.aeg.transfer.partner;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Component
public class PartnerHolder {

    @Value("${partners.file.path}")
    private String partnersFilePath;
    private Path partnersJson;

    private Partners partners = null;

    public Partner find(String name) throws IOException, URISyntaxException {
        for(Partner partner : partners.getPartners()) {
            if(partner.getName().equalsIgnoreCase(name)) return partner;
        }
        return null;
    }

    private void read() throws URISyntaxException, IOException {
        List<String> lines = Files.readAllLines(partnersJson, Charset.forName("UTF-8"));
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(line);
        }
        String json = builder.toString();
        //System.out.println(json);

        partners = new Gson().fromJson(json, Partners.class);
    }

    @PostConstruct
    public void init() throws Exception {
        URL url = PartnerHolder.class.getResource(partnersFilePath);
        partnersJson = Paths.get(url.toURI());
        read();
    }

    public Partners getPartners() {
        return partners;
    }

    public List<Partner> getPartnerList() {
        return getPartners().getPartners();
    }
}
