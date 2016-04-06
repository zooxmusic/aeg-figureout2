package com.aeg.mail;


import org.springframework.boot.context.properties.ConfigurationProperties;

@org.springframework.context.annotation.Configuration
@ConfigurationProperties(prefix="mail.smtp")
public class FreemarkerConfig extends freemarker.template.Configuration {
}
