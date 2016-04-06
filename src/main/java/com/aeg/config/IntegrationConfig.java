package com.aeg.config;

import com.aeg.transfer.filter.XmlFileListFilter;
import com.jcraft.jsch.ChannelSftp;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.expression.DynamicExpression;
import org.springframework.integration.expression.ExpressionSource;
import org.springframework.integration.file.filters.FileListFilter;
import org.springframework.integration.file.filters.RegexPatternFileListFilter;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.sftp.gateway.SftpOutboundGateway;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
import org.springframework.messaging.PollableChannel;

import java.util.List;

@EnableIntegration
@Configuration
//@ConfigurationProperties(locations = {"/META-INF/spring/integration/integration.properties"})
public class IntegrationConfig {

    @Value("${default.port}")
    private Integer defaultPort;

    @Bean
    public DirBean dirBean() {
        return new DirBean();
    }

    @Bean
    public SessionFactory<ChannelSftp.LsEntry> sftpSessionFactory() {
        return new CachingSessionFactory(defaultSftpSessionFactory());
    }

    @Bean
    public SessionFactory<ChannelSftp.LsEntry> defaultSftpSessionFactory() {
        DefaultSftpSessionFactory sftpSessionFactory = new DefaultSftpSessionFactory();
        sftpSessionFactory.setHost("${default.host}");
        sftpSessionFactory.setPort(defaultPort);
        sftpSessionFactory.setUser("${default.username}");
        sftpSessionFactory.setPassword("${default.password}");
        sftpSessionFactory.setAllowUnknownKeys(true);
        //sftpSessionFactory.setPrivateKey(resource);
        return sftpSessionFactory;
    }

    @Bean
    public DirectChannel inboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public PollableChannel outboundChannel() {
        return new QueueChannel();
    }

    /*@Bean
    public SftpOutboundGateway outboundGateway() {

        SftpRemoteFileTemplate remoteFileTemplate = new SftpRemoteFileTemplate(sftpSessionFactory());
        remoteFileTemplate.setRemoteDirectoryExpression(new SpelExpressionParser().parseExpression("@dirBean.remote"));
        SftpOutboundGateway sftpOutboundGateway = new SftpOutboundGateway(remoteFileTemplate, "mget", "payload");
        sftpOutboundGateway.setAutoCreateLocalDirectory(true);
        sftpOutboundGateway.setFilter(new XmlFileListFilter());
        sftpOutboundGateway.setLocalDirectoryExpression(new SpelExpressionParser().parseExpression("@dirBean.local"));
        sftpOutboundGateway.setOutputChannel(outboundChannel());
        sftpOutboundGateway.setFileExistsMode(FileExistsMode.IGNORE);
        return sftpOutboundGateway;
    }

    @Bean
    public IntegrationFlow outboundGatewayFlow() {
        return IntegrationFlows.from(inboundChannel())
                .handle(outboundGateway())
                .channel(outboundChannel())
                .get();


    }
    */
}
