/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aeg;

import com.aeg.config.DirBean;
import com.aeg.transfer.partner.FileMapping;
import com.aeg.transfer.partner.Partner;
import com.aeg.transfer.partner.PartnerReader;
import com.aeg.transfer.partner.Partners;
import com.jcraft.jsch.ChannelSftp;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.messaging.support.GenericMessage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@SpringBootApplication
public class AegApplication extends SpringBootServletInitializer{
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(AegApplication.class);
	}
	public static void main(String[] args) {
		SpringApplication.run(AegApplication.class, args);
	/*}

	public static void main(final String... args) {*/

		final AbstractApplicationContext context =
				new ClassPathXmlApplicationContext("classpath:META-INF/spring/integration/*-context.xml");


		context.registerShutdownHook();



		try {
			Partners partners = PartnerReader.read();
			DefaultSftpSessionFactory sftpSessionFactory = context.getBean(DefaultSftpSessionFactory.class);
			//final PollableChannel replyChannel = (PollableChannel) context.getBean("output");
			DirBean dirBean = context.getBean(DirBean.class);
			for(Partner partner : partners.getPartners()) {
				sftpSessionFactory.setHost(partner.getHost());
				sftpSessionFactory.setPort(partner.getPort());
				sftpSessionFactory.setUser(partner.getUsername());
				sftpSessionFactory.setPassword(partner.getPassword());

				for(FileMapping fileMapping : partner.getInboundFileMappings()) {
					dirBean.setLocalDir(fileMapping.getLocal());
					dirBean.setRemoteDir(fileMapping.getRemote());

					SftpSession session = sftpSessionFactory.getSession();
					final DirectChannel requestChannel = (DirectChannel) context.getBean("inboundChannel");
					//String dir = "/toIMS_UAT/SRP/";
					requestChannel.send(new GenericMessage<Object>(dirBean.getRemoteDir() + "*"));
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

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}


		/*final DirectChannel requestChannel = (DirectChannel) context.getBean("inboundMGetRecursive");
		final PollableChannel replyChannel = (PollableChannel) context.getBean("output");
		

		String dir = "/HVAC - Files For Testing/";
		requestChannel.send(new GenericMessage<Object>(dir + "*"));
		Message<?> result = replyChannel.receive(1000);

		List<File> localFiles = (List<File>) result.getPayload();

		for (File file : localFiles) {
			System.out.println(file.getName());
		}*/

		System.exit(0);

	}

	private static void rename(SftpSession session, String dir) throws IOException {
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
