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

import com.aeg.config.ApplicationConfig;
import com.aeg.config.EnvironmentValidator;
import com.aeg.config.IntegrationConfig;
import com.aeg.config.MailConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties
@Import({IntegrationConfig.class, ApplicationConfig.class, MailConfig.class})
public class AegApplication extends SpringBootServletInitializer{

	@Bean
	public Validator environmentValidator() {
		return new EnvironmentValidator();
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(AegApplication.class);
	}

	@Override
	protected WebApplicationContext run(SpringApplication application) {
		return super.run(application);
	}

	public static void main(String[] args) {

		ConfigurableApplicationContext context = new SpringApplicationBuilder()
				.sources(AegApplication.class)
				.profiles()
				.run(args);
		Environment env = context.getEnvironment();
		boolean hasConfig = env.containsProperty("spring.config.location");
		System.out.println(context.getBean(MailConfig.FooService.class).foo("foo"));

		String configLoc =env.getProperty("spring.config.location");

		String name = env.getProperty("spring.application.name");
		System.out.println("Does my environment contain the ''spring.application.name'' property? " + name + "  hasConfig " + hasConfig + " value: " + configLoc);

	}

	/*public static void main(final String... args) {

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
			}*//*

					rename(session, dirBean.getRemoteDir());
				}
			}
			*//*dirBean.setLocalDir("C:/AEG/IMSTransferFiles/Inbound/ICF/XML_Applications");
			dirBean.setRemoteDir("/toIMS_UAT/EEP/");
			requestChannel.send(new GenericMessage<Object>(dirBean.getRemoteDir() + "*"));
			rename(session, dirBean.getRemoteDir());*//*

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
		}*//*

		System.exit(0);

	}*/

	/*private static void rename(SftpSession session, String dir) throws IOException {
		for (ChannelSftp.LsEntry entry : session.list(dir)) {
			if(entry.getFilename().contains(".done")) continue;
			if(!entry.getFilename().contains(".xml")) continue;
			System.out.println(entry.getFilename());
			String from = String.format("%s/%s", dir, entry.getFilename());
			String to = String.format("%s.done", from);
			session.rename(from, to);

		}
	}*/
}
