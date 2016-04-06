package com.aeg.mail;

import com.aeg.config.GMailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;

public class SuccessTemplate extends SimpleMailMessage {
/*    @Autowired
    private GMailProperties gmail;

    public SuccessTemplate() {
        super.setFrom(gmail.getSuccessFrom());
        super.setTo(gmail.getSuccessTo());
        super.setSubject(gmail.getSuccessSubject());
    }*/

}
