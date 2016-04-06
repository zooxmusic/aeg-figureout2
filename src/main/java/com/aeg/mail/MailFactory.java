package com.aeg.mail;

/**
 * Created by bszucs on 4/6/2016.
 */
public class MailFactory {

    public static MailManMessageHandlerSpec outboundAdapter(String host) {
        return new MailManMessageHandlerSpec(host);
    }
}
