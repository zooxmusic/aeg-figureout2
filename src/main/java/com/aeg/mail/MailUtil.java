package com.aeg.mail;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.internet.MimeMessage;

import lombok.extern.log4j.Log4j2;
import org.joda.time.LocalDate;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.cache.FileTemplateLoader;

@Log4j2
public class MailUtil {

    public static Map<String, File> createAttachments(List<String> attachmentNames) {
        if(null == attachmentNames) return null;
        if(attachmentNames.size() < 1) return null;
        Map<String, File> attachments = new HashMap<String, File>();
        for(String fileName : attachmentNames) {
            try {
                attachments.put(fileName, new File(fileName));
            }catch(Exception e) {
                //log.error(e.getMessage(), e);
            }
        }

        return attachments;
    }

    public static MimeMessagePreparator createTemplatedMimeMessage(
            final FreemarkerConfig freemarkerConfig,
            final String template,
            final List<String> recipients,
            final String from,
            final String subject,
            final Map<String, File> attachments) throws IOException, URISyntaxException {

        MimeMessagePreparator preparator = null;
        FileTemplateLoader templateLoader = null;

        Map<String, Object> freeMarkderTemplateMap = new HashMap<String, Object>();
        freeMarkderTemplateMap.put("startDate", LocalDate.now());
        freeMarkderTemplateMap.put("endDate", LocalDate.now());

        URL url = MailUtil.class.getResource("/mail/templates");
        templateLoader = new FileTemplateLoader(new File(url.toURI()));
        freemarkerConfig.setTemplateLoader(templateLoader);


        try {
            preparator = new MimeMessagePreparator() {

                String messageText =
                        FreeMarkerTemplateUtils.processTemplateIntoString(
                                freemarkerConfig.getTemplate(template),freeMarkderTemplateMap);

                @Override
                public void prepare(MimeMessage message) throws Exception {

                    MimeMessageHelper helper = new MimeMessageHelper(message, true);
                    helper.setFrom(from);
                    helper.setSubject(subject);
                    helper.setText(messageText,true);

                    for(String address : recipients){
                        helper.addTo(address);
                    }
                    if(null != attachments){
                        for(Entry<String, File> attachment : attachments.entrySet()){
                            helper.addAttachment(attachment.getKey(), attachment.getValue());
                        }
                    }
                }
            };



        } catch (Exception ex) {

            /*log.error("Failed to create email for templated message:" +
                    "\nFrom:" + from +
                    "\nTo:" + recipients +
                    "\nMessageMap:" + freeMarkderTemplateMap, ex);*/

        }
        return preparator;
    }

    public static MimeMessagePreparator createMimeMessage(
            final String mimeMessage,
            final String from,
            final List<String> recipients,
            final String subject,
            final Map<String, File> attachments){

        MimeMessagePreparator preparator = new MimeMessagePreparator() {

            @Override
            public void prepare(MimeMessage message) throws Exception {

                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                for(String address : recipients){
                    helper.addTo(address);
                }

                helper.setFrom(from);
                helper.setSubject(subject);

                if(attachments!=null){

                    for(Entry<String, File> attachment : attachments.entrySet()){

                        helper.addAttachment(attachment.getKey(), attachment.getValue());
                    }

                }

                helper.setText(mimeMessage);
            }
        };

       return preparator;
    }

}
