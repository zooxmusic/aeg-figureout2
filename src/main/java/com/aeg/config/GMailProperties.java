package com.aeg.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component("gmail")
@ConfigurationProperties(prefix="mail.smtp")
public class GMailProperties {

    private String host;
    private String protocol;
    private Integer port;
    private String username;
    private String password;
    private Boolean auth;
    private Boolean starttlsEnable;

    private String successFrom;
    private String successTo;
    private String successSubject;

    private String errorFrom;
    private String errorTo;
    private String errorSubject;

}