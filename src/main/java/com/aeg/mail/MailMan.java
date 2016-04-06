package com.aeg.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class MailMan extends JavaMailSenderImpl {

    private final static String DEFAULT_TEMPLATE = "default.ftl";

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {
        super.send(mimeMessage);
    }

    public void setProperties(Properties mailProperties) {
        setJavaMailProperties(mailProperties);
    }

/*    private SuccessTemplate successTemplate;
    public void setSuccessTemplate(SuccessTemplate successTemplate) {
        this.successTemplate = successTemplate;
    }
    private ErrorTemplate errorTemplate;
    public void setErrorTemplate(ErrorTemplate errorTemplate) {
        this.errorTemplate = errorTemplate;
    }
*/
    public void deliverWithTemplate(final String from, final String subject, final List<String> recipients, final List<String> attachmentNames) throws IOException, URISyntaxException {

        Map<String, File> attachments = MailUtil.createAttachments(attachmentNames);
        MimeMessagePreparator templateMessage = MailUtil.createTemplatedMimeMessage(new FreemarkerConfig(), DEFAULT_TEMPLATE, recipients, from, subject, attachments);

        deliver(templateMessage);
    }

    /*public void deliverSuccess() {
        deliver(successTemplate);
    }

    public void deliverError() {
        deliver(errorTemplate);
    }*/

    public void deliver(MimeMessagePreparator message) {
        try {
            send(message);
        } catch (MailException e) {
            log.error("Failed to send email for templated message", e);
        }
    }

    public void deliver(SimpleMailMessage message) {
        try{
            send(message);
        } catch (MailException ex) {
            log.error("Failed to send email for templated message:" +
                    "\nFrom:" + message.getFrom() +
                    "\nTo:" + message.getTo() +
                    "\nMessage:" + message.getText(), ex);
        }
    }
}
