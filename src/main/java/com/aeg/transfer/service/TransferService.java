package com.aeg.transfer.service;

import com.aeg.config.DirBean;
import com.aeg.transfer.partner.FileMapping;
import com.aeg.transfer.partner.Partner;
import com.aeg.transfer.partner.PartnerHolder;
import com.jcraft.jsch.ChannelSftp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransferService {

    @Autowired
    private PartnerHolder partnerHolder;

    @Autowired
    private DefaultSftpSessionFactory defaultSftpSessionFactory;

    @Autowired
    private DirBean dirBean;

    @Autowired
    private DirectChannel inboundChannel;

    public void transfer() throws Exception {
        transfer(null);
    }

    public void transfer(String partnerName) throws Exception {


        List<Partner> partners = getPartner(partnerName);

        //DefaultSftpSessionFactory sftpSessionFactory = context.getBean(DefaultSftpSessionFactory.class);
        //final PollableChannel replyChannel = (PollableChannel) context.getBean("output");
        //DirBean dirBean = context.getBean(DirBean.class);
        for(Partner partner : partners) {
            defaultSftpSessionFactory.setHost(partner.getHost());
            defaultSftpSessionFactory.setPort(partner.getPort());
            defaultSftpSessionFactory.setUser(partner.getUsername());
            defaultSftpSessionFactory.setPassword(partner.getPassword());

            for(FileMapping fileMapping : partner.getInboundFileMappings()) {
                dirBean.setLocalDir(fileMapping.getLocal());
                dirBean.setRemoteDir(fileMapping.getRemote());

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
