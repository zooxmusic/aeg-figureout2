package com.aeg.service;

import com.aeg.config.DirBean;
import com.aeg.transfer.partner.FileMapping;
import com.aeg.transfer.partner.Partner;
import com.aeg.transfer.partner.PartnerHolder;
import com.jcraft.jsch.ChannelSftp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.file.remote.RemoteFileTemplate;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class TransferService {

    @Autowired
    private PartnerHolder partnerHolder;

    @Autowired
    private SessionFactory sftpSessionFactory;

    @Autowired
    private DefaultSftpSessionFactory defaultSftpSessionFactory;

    @Autowired
    private DirBean dirBean;

    @Autowired
    private DirectChannel inboundChannel;

    public void transferOutbound() throws Exception {
        transferOutbound(null);
    }

    public void transferInbound() throws Exception {
        transferInbound(null);
    }


    public void transferOutbound(String partnerName) throws Exception {
        try {
//        SessionFactory<ChannelSftp.LsEntry> sessionFactory = ac.getBean(CachingSessionFactory.class);
            RemoteFileTemplate<ChannelSftp.LsEntry> template = new RemoteFileTemplate<ChannelSftp.LsEntry>(sftpSessionFactory);
            //SftpTestUtils.createTestFiles(template); // Just the directory

            for (Partner partner : getPartner(partnerName)) {

                defaultSftpSessionFactory.setHost(partner.getHost());
                defaultSftpSessionFactory.setPort(partner.getPort());
                defaultSftpSessionFactory.setUser(partner.getUsername());
                defaultSftpSessionFactory.setPassword(partner.getPassword());

                for (FileMapping fileMapping : partner.getOutboundFileMappings()) {
                    System.out.println("   LOCAL DIR: " + fileMapping.getLocal());
                    dirBean.setLocalDir(fileMapping.getLocal());
                    dirBean.setRemoteDir(fileMapping.getRemote());
                    dirBean.setPattern(fileMapping.getPattern());
                    //final File file = new File(sourceFileName);
                    //Assert.isTrue(file.exists(), String.format("File '%s' does not exist.", sourceFileName));

                    File dir = new File(fileMapping.getLocal());
                    File[] files = dir.listFiles();
                    if(null == files) {
                        dir.mkdirs();
                        continue;
                    }
                    for (File file : files) {

                        System.out.println(" looping through :" + file.getAbsolutePath());
                        System.out.println(" suppose to copy to :" + dirBean.getRemoteDir());

                        final Message<File> message = MessageBuilder.withPayload(file).build();
                        //final MessageChannel inputChannel = ac.getBean("inputChannel", MessageChannel.class);
                        //inboundChannel.send(new GenericMessage<Object>(dirBean.getRemoteDir() + "*"));
                        //Thread.sleep(1000);

                        inboundChannel.send(message);
                        Thread.sleep(2000);

                        rename(Paths.get(file.toURI()));

                    /*Assert.isTrue(SftpTestUtils.fileExists(template, destinationFileName));


				    System.out.println(String.format("Successfully transferred '%s' file to a " +
						"remote location under the name '%s'", sourceFileName, destinationFileName));*/
                    }
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void transferOutbound2(String partnerName) throws Exception {

        for(Partner partner : getPartner(partnerName)) {

            defaultSftpSessionFactory.setHost(partner.getHost());
            defaultSftpSessionFactory.setPort(partner.getPort());
            defaultSftpSessionFactory.setUser(partner.getUsername());
            defaultSftpSessionFactory.setPassword(partner.getPassword());

            for (FileMapping fileMapping : partner.getOutboundFileMappings()) {
                System.out.println("   LOCAL DIR: " + fileMapping.getLocal());
                dirBean.setLocalDir(fileMapping.getLocal());
                dirBean.setRemoteDir(fileMapping.getRemote());
                dirBean.setPattern(fileMapping.getPattern());

                File dir = new File(fileMapping.getLocal());
                for(File f : dir.listFiles()) {
                    try {
                        System.out.println(" looping through :" + f.getAbsolutePath());
                        System.out.println(" suppose to copy to :" + dirBean.getRemoteDir());

                        RemoteFileTemplate<File> template = new RemoteFileTemplate<>(sftpSessionFactory);
                        final Message<File> message = MessageBuilder.withPayload(f).build();
                        //template.send(message, FileExistsMode.REPLACE);
                        inboundChannel.send(new GenericMessage<Object>(dirBean.getRemoteDir() + "*"));
                        Thread.sleep(1000);
                        inboundChannel.send(message);
                        rename(Paths.get(f.toURI()));
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }


                /*Path path = FileSystems.getDefault().getPath(fileMapping.getLocal());
                try (DirectoryStream<Path> ds = Files.newDirectoryStream(path, dirBean.getPattern())) {
                    for (Path p : ds) {
                        // Iterate over the paths in the directory and print filenames
                        System.out.println("FILE NAME: " + p.getFileName());


                        final Message<File> message = MessageBuilder.withPayload(p.toFile()).build();

                        localChannel.send(message);

                        rename(p);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                    //RemoteFileTemplate<ChannelSftp.LsEntry> template = new RemoteFileTemplate<ChannelSftp.LsEntry>(defaultSftpSessionFactory);
                    //SftpTestUtils.createTestFiles(template); // Just the directory

            }
        }
    }



    private void rename(Path file) throws IOException {

        String newName = String.format("%s.done", file.getFileName().toString());
        Path dir = file.getParent();
        Path fn = file.getFileSystem().getPath(newName);
        Path target = (dir == null) ? fn : dir.resolve(fn);
        Files.move(file, target);
    }

    public void transferInbound(String partnerName) throws Exception {
        //DefaultSftpSessionFactory sftpSessionFactory = context.getBean(DefaultSftpSessionFactory.class);
        //final PollableChannel replyChannel = (PollableChannel) context.getBean("output");
        //DirBean dirBean = context.getBean(DirBean.class);

        for(Partner partner : getPartner(partnerName)) {
            defaultSftpSessionFactory.setHost(partner.getHost());
            defaultSftpSessionFactory.setPort(partner.getPort());
            defaultSftpSessionFactory.setUser(partner.getUsername());
            defaultSftpSessionFactory.setPassword(partner.getPassword());

            for(FileMapping fileMapping : partner.getInboundFileMappings()) {
                dirBean.setLocalDir(fileMapping.getLocal());
                dirBean.setRemoteDir(fileMapping.getRemote());
                dirBean.setPattern(fileMapping.getPattern());

                SftpSession session = defaultSftpSessionFactory.getSession();
                //final DirectChannel requestChannel = (DirectChannel) context.getBean("inboundChannel");
                //String dir = "/toIMS_UAT/SRP/";
                inboundChannel.send(new GenericMessage<Object>(dirBean.getRemoteDir() + "*"));
        /*if (!session.exists(sftpConfiguration.getOtherRemoteDirectory())) {
            throw new FileNotFoundException("Remote directory does not exists... Continuing");
        }*/

                rename(session, dirBean.getRemoteDir());
            }
        }
        /*dirBean.setLocalDir("C:/AEG/IMSTransferFiles/Inbound/ICF/XML_Applications");
        dirBean.setRemoteDir("/toIMS_UAT/EEP/");
        requestChannel.send(new GenericMessage<Object>(dirBean.getRemoteDir() + "*"));
        rename(session, dirBean.getRemoteDir());*/


    }

    private List<Partner> getPartner(String name) throws IOException, URISyntaxException {
        if(null == name || "".equalsIgnoreCase(name.trim())) return partnerHolder.getPartnerList();
        List<Partner> partners = new ArrayList<Partner>();

        Partner partner = partnerHolder.find(name);
        if(null != partner) {
            partners.add(partner);
        }
        return partners;
    }

    private void rename(SftpSession session, String dir) throws IOException {
        for (ChannelSftp.LsEntry entry : session.list(dir)) {
            if(entry.getFilename().contains(".done")) continue;
            if(!entry.getFilename().contains(".xml")) continue;
            System.out.println(entry.getFilename());
            String from = String.format("%s/%s", dir, entry.getFilename());
            String to = String.format("%s.done", from);
            session.rename(from, to);

        }
    }
}
