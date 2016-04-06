package com.aeg.mail;

import com.aeg.config.GMailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

public class ErrorTemplate extends SimpleMailMessage {

/*    @Autowired
    private GMailProperties gmail;

    public ErrorTemplate() {
        super.setFrom(gmail.getErrorFrom());
        super.setTo(gmail.getErrorTo());
        super.setSubject(gmail.getErrorSubject());
    }*/

}
