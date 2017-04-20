package com.auto.common.utils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailUtils {

	class MailAuthenticator extends Authenticator
	{
		private String username;
		private String password;

		public MailAuthenticator(String username, String password) 
		{
			this.username = username;
			this.password = password;
		}

		String getPassword() {
			return password;
		}

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password);
		}

		String getUsername() {
			return username;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public void setUsername(String username) {
			this.username = username;
		}

	}

	public static void sendEmail(List<String> toList, String subject, String contentDetail, String attachment){  
		String from = ""; //change accordingly  
		//		String host = "smtp.gmail.com"; //or IP address  
		String host = ""; //or IP address  

		MailAuthenticator authenticator = new EmailUtils().new MailAuthenticator("", "");
		//Get the session object  
		Properties properties = System.getProperties();  
		properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");   
		properties.setProperty("mail.smtp.socketFactory.fallback", "false");   
		properties.setProperty("mail.smtp.port", "465");   
		properties.setProperty("mail.smtp.socketFactory.port", "465");  
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(properties, authenticator);  

		//compose the message  
		try{  
			MimeMessage message = new MimeMessage(session);  
			message.setFrom(new InternetAddress(from));
			for(String to : toList)
			{
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));  
			}

			message.setSubject(subject);  

			MimeMultipart msgMultipart = new MimeMultipart("mixed");
			message.setContent(msgMultipart);


			// Add Message Content
			MimeBodyPart contentPart = new MimeBodyPart();
			msgMultipart.addBodyPart(contentPart);

			MimeMultipart bodyMultipart  = new MimeMultipart("related");
			contentPart.setContent(bodyMultipart);
			MimeBodyPart htmlPart = new MimeBodyPart();
			bodyMultipart.addBodyPart(htmlPart);

			htmlPart.setContent(contentDetail, "text/html;charset=utf-8");

			// Add Attachment
			MimeBodyPart attch1 = new MimeBodyPart();
			msgMultipart.addBodyPart(attch1);
			File file = new File(attachment);
			DataSource ds1 = new FileDataSource(file);
			DataHandler dh1 = new DataHandler(ds1);
			attch1.setDataHandler(dh1);
			attch1.setFileName(file.getName());

			message.saveChanges();

			// Send message  
			Transport.send(message);
			System.out.println("message sent successfully....");  

		}catch (MessagingException mex) {mex.printStackTrace();}  
	}  

}
