package com.aeg.mail;

import org.springframework.integration.dsl.core.MessageHandlerSpec;
import org.springframework.integration.mail.MailSendingMessageHandler;

import java.util.Properties;


public class MailManMessageHandlerSpec extends MessageHandlerSpec<MailManMessageHandlerSpec, MailSendingMessageHandler> {

    private MailMan mailMan = new MailMan();

    public MailManMessageHandlerSpec (String host) {
        this.mailMan.setPassword("Z00xMu$1c");
        this.mailMan.setUsername("brian@zooxmusic.com");
        this.mailMan.setPort(587);
        this.mailMan.setHost(host);
        this.mailMan.setJavaMailProperties(getProperties());
        this.target = new MailSendingMessageHandler(this.mailMan);
    }


    private Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        return properties;
    }

    @Override
    protected MailSendingMessageHandler doGet() {
        return get();
    }

}
