package com.aeg.config;

import com.aeg.mail.MailFactory;
import com.aeg.mail.MailMan;
import com.aeg.mail.MailManMessageHandlerSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.BridgeTo;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.mail.Mail;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableConfigurationProperties(GMailProperties.class)
@EnableAutoConfiguration
@IntegrationComponentScan
public class MailConfig {


    @Autowired
    private GMailProperties gmail;


    @MessagingGateway(defaultRequestChannel="foo.input")
    public static interface FooService {

        String foo(String request);

    }

    @Bean
    IntegrationFlow foo() {
        return f -> f
                .transform("payload + payload")
                .handle(String.class, (p, h) -> p.toUpperCase())
                .routeToRecipients(r ->
                        r.recipient("bridgeToNowhere", "true")
                                .recipient("smtpChannel", "true"));
    }

    @BridgeTo
    @Bean
    public MessageChannel bridgeToNowhere() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel smtpChannel() {
        return new DirectChannel();
    }


    @Bean
    IntegrationFlow smtp() {
        return IntegrationFlows.from(smtpChannel())
                .enrichHeaders(Mail.headers()
                        .subject("Transfer Email")
                        .to("bszucs@ameresco.com")
                        .from("transfer@aeg.com"))
                .handle(MailFactory.outboundAdapter(gmail.getHost()))
                .get();
                /*.handle(Mail.outboundAdapter(gmail.getHost())
                        .credentials(gmail.getUsername(), gmail.getPassword())
                        .port(gmail.getPort())
                        .protocol(gmail.getProtocol())
                        .defaultEncoding("UTF-8")
                        .javaMailProperties(p -> p
                                .put("gmail.debug", "false")
                                .put("gmail.smtp.auth", true)
                                .put("gmail.smtp.starttls.enable", true)))
                .get();*/


    }

}
