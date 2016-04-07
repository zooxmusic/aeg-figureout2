package com.aeg.config;

import com.aeg.transfer.partner.Partner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class DirBean {
    private String host;
    private int port;
    private String username;
    private String password;

    private String localDir;
    private String remoteDir;
    private String pattern;

    public void setLocation(Partner partner) {
        this.host = partner.getHost();
        this.port = partner.getPort();
        this.username = partner.getUsername();
        this.password = partner.getPassword();
    }
    public String getLocalDir() {
        if(null == localDir || "".equalsIgnoreCase(localDir)) {
            GenericApplicationContext parent = new StaticApplicationContext();
            Environment env = parent.getEnvironment();
            return env.getProperty("java.io.tmpdir");
        }
        return localDir;
    }

    public String getRemoteDir() {
        return remoteDir;
    }

    public void setLocalDir(String localDir) {
        this.localDir = localDir;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setRemoteDir(String remoteDir) {
        this.remoteDir = remoteDir;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
