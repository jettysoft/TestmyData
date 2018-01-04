package com.testmydata.util;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMailUsingAuthentication {

	private String SMTP_HOST_NAME = "smtp.gmail.com"; // for google
	private static final String encodingOptions = "text/html; charset=UTF-8";
	private static final String SMTP_PORT = "465";
	String emailMsgTxt;
	String[] emailList;
	String localFileName;
	boolean isRec = false;
	private String attachName;
	public static String SMTP_AUTH_USER = "";// Username
	private static String SMTP_AUTH_PWD = ""; // Password
	Properties props1 = null;

	public SendMailUsingAuthentication() {
		FileIOOperations fileIo = new FileIOOperations();
		props1 = fileIo.readPropertyFile("emailSettings.properties");
		if (props1.size() > 0) {
			if (props1 != null && props1.getProperty("email") != null && props1.getProperty("email") != "") {
				SMTP_AUTH_USER = EncryptAndDecrypt.decryptData(props1.getProperty("email"));
			}
			if (props1 != null && props1.getProperty("password") != null && props1.getProperty("password") != "") {
				SMTP_AUTH_PWD = EncryptAndDecrypt.decryptData(props1.getProperty("password"));
			}
		}
	}

	/**
	 * To be used when mailing with an attachement
	 * 
	 * @param mssg
	 *            Mail message
	 * @param dir
	 *            String array with mailing addresses
	 * @param fn
	 *            Filename to attach
	 */
	public SendMailUsingAuthentication(String mssg, String[] dir, String fn, String attachName) {
		try {
			emailMsgTxt = mssg;
			emailList = dir;
			localFileName = fn;
			isRec = true;
			this.attachName = attachName;
			FileIOOperations fileIo = new FileIOOperations();
			props1 = fileIo.readPropertyFile("emailSettings.properties");
			if (props1.size() > 0) {
				if (props1 != null && props1.getProperty("email") != null && props1.getProperty("email") != "") {
					SMTP_AUTH_USER = EncryptAndDecrypt.decryptData(props1.getProperty("email"));
				}
				if (props1 != null && props1.getProperty("password") != null && props1.getProperty("password") != "") {
					SMTP_AUTH_PWD = EncryptAndDecrypt.decryptData(props1.getProperty("password"));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Add List of Email address to who email needs to be sent to
	public String postMail(String recipients[], String subject, String message, String from, String invoiceNumber)
			throws MessagingException {

		String returnValue = "failure";

		try {
			boolean debug = false;
			// Set the host smtp address
			Properties props = new Properties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", SMTP_HOST_NAME);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", SMTP_PORT);
			props.put("mail.smtp.socketFactory.port", SMTP_PORT);
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

			SMTPAuthenticator auth = new SMTPAuthenticator();
			Session session = Session.getDefaultInstance(props, auth);

			session.setDebug(debug);

			// create a message
			Message msg = new MimeMessage(session);

			// set the from and to address
			InternetAddress addressFrom = new InternetAddress("donotreply");
			msg.setFrom(addressFrom);

			InternetAddress[] addressTo = new InternetAddress[recipients.length];
			for (int i = 0; i < recipients.length; i++) {
				addressTo[i] = new InternetAddress(recipients[i]);
			}
			msg.setRecipients(Message.RecipientType.TO, addressTo);
			// Setting the Subject and Content Type
			// msg.setSubject(subject);
			if (subject == "TheBusinessStore - Employee T4") {
				msg.setSubject(subject);
			} else {
				if (props1 != null && props1.getProperty("subject") != null && props1.getProperty("subject") != "") {
					msg.setSubject(props1.getProperty("subject") + " " + invoiceNumber);
				}
			}
			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();
			// Fill the message
			// messageBodyPart.setText(emailMsgTxt);
			if (message.contains("Please contact your Employer for any Enquiry")) {
				messageBodyPart.setContent(message, "text/html");
			} else {
				if (props1 != null && props1.getProperty("body") != null && props1.getProperty("body") != "") {
					messageBodyPart.setText(props1.getProperty("body"));
				} else {
					messageBodyPart.setContent(message, "text/html");
				}
			}
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			if (isRec) {
				messageBodyPart = new MimeBodyPart();
				// System.out.println(localFileName);
				DataSource source = new FileDataSource(localFileName);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(attachName);
				multipart.addBodyPart(messageBodyPart);
				if (subject.equals("TheBusinessStore - Employee T4")) {

				} else {
					// addAttachment(multipart);
				}
				msg.setContent(multipart);
				XTrustProvider.install();
				Transport.send(msg);
			}
			returnValue = "success";

		} catch (Exception e) {
			e.printStackTrace();
			returnValue = "error";
		}
		return returnValue;
	}

	@SuppressWarnings("unused")
	private static void addAttachment(Multipart multipart) {
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		DataSource source = new FileDataSource(
				new File(".", "/conf").getAbsolutePath() + File.separator + "TERMS-AND-CONDITIONS.docx");
		try {
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName("TERMS-AND-CONDITIONS.docx");
			multipart.addBodyPart(messageBodyPart);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	public String postMailWithOutAttachment(String recipients[], String subject, String message, String from)
			throws MessagingException {

		String returnValue = "failure";

		try {

			boolean debug = false;
			// Set the host smtp address
			Properties props = new Properties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", SMTP_HOST_NAME);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", SMTP_PORT);
			props.put("mail.smtp.socketFactory.port", SMTP_PORT);
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			// props.put("mail.smtp.socketFactory.fallback", "true");

			SMTPAuthenticator auth = new SMTPAuthenticator();
			Session session = Session.getDefaultInstance(props, auth);

			session.setDebug(debug);

			// create a message
			Message msg = new MimeMessage(session);

			// set the from and to address
			InternetAddress addressFrom = new InternetAddress("donotreply");
			msg.setFrom(addressFrom);

			InternetAddress[] addressTo = new InternetAddress[recipients.length];
			for (int i = 0; i < recipients.length; i++) {
				addressTo[i] = new InternetAddress(recipients[i]);
			}
			msg.setRecipients(Message.RecipientType.TO, addressTo);
			// Setting the Subject and Content Type
			msg.setSubject(subject);
			/*
			 * if (props1 !=null && props1.getProperty("subject") != null &&
			 * props1.getProperty("subject") != ""){
			 * msg.setSubject(props1.getProperty("subject")); } else {
			 * msg.setSubject(subject); }
			 */

			msg.setContent(message, "text/html");
			/*
			 * if (props1 !=null && props1.getProperty("body") != null &&
			 * props1.getProperty("body") != ""){
			 * msg.setContent(props1.getProperty("body"),"text/html"); } else {
			 * msg.setContent(message, "text/html"); }
			 */
			msg.setHeader("Content-Type", encodingOptions);
			XTrustProvider.install();
			Transport.send(msg);
			returnValue = "success";

		} catch (Exception e) {
			e.printStackTrace();
			returnValue = "error";
		}
		return returnValue;
	}

	public String postFlyerwithImage(String recipient, String subject, String message, DataSource image, String from)
			throws MessagingException {

		String returnValue = "failure";

		try {

			boolean debug = false;
			// Set the host smtp address
			Properties props = new Properties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", SMTP_HOST_NAME);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", SMTP_PORT);
			props.put("mail.smtp.socketFactory.port", SMTP_PORT);
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			// props.put("mail.smtp.socketFactory.fallback", "true");

			SMTPAuthenticator auth = new SMTPAuthenticator();
			Session session = Session.getDefaultInstance(props, auth);

			session.setDebug(debug);

			// create a message
			Message msg = new MimeMessage(session);

			// set the from and to address
			InternetAddress addressFrom = new InternetAddress("donotreply");
			msg.setFrom(addressFrom);

			InternetAddress addressTo = new InternetAddress(recipient);

			msg.addRecipient(Message.RecipientType.TO, addressTo);
			// Setting the Subject and Content Type
			msg.setSubject(subject);

			// This mail has 2 part, the BODY and the embedded image
			MimeMultipart multipart = new MimeMultipart("related");

			// First part
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(message, "text/html");
			// add it
			multipart.addBodyPart(messageBodyPart);
			if (image != null) {
				// second part (the image)
				messageBodyPart = new MimeBodyPart();

				messageBodyPart.setDataHandler(new DataHandler(image));
				messageBodyPart.setHeader("Content-ID", "<image>");

				// add image to the multipart
				multipart.addBodyPart(messageBodyPart);
			}
			// put everything together
			msg.setContent(multipart);

			XTrustProvider.install();
			Transport.send(msg);
			returnValue = "success";

		} catch (Exception e) {
			e.printStackTrace();
			returnValue = "error";
		}
		return returnValue;
	}

	public String sendCashEntryUpdates(String recipients[], String subject, String message, String from)
			throws MessagingException {
		String returnValue = "failure";

		try {
			boolean debug = false;
			// Set the host smtp address
			Properties props = new Properties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", SMTP_HOST_NAME);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", SMTP_PORT);
			props.put("mail.smtp.socketFactory.port", SMTP_PORT);
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

			SMTPAuthenticator auth = new SMTPAuthenticator();
			Session session = Session.getDefaultInstance(props, auth);

			session.setDebug(debug);

			// create a message
			Message msg = new MimeMessage(session);

			// set the from and to address
			InternetAddress addressFrom = new InternetAddress("donotreply");
			msg.setFrom(addressFrom);

			InternetAddress[] addressTo = new InternetAddress[recipients.length];
			for (int i = 0; i < recipients.length; i++) {
				addressTo[i] = new InternetAddress(recipients[i]);
			}
			msg.setRecipients(Message.RecipientType.TO, addressTo);

			// Setting the Subject and Content Type
			msg.setSubject(subject);
			msg.setContent(message, "text/html");
			msg.setHeader("Content-Type", encodingOptions);
			XTrustProvider.install();
			Transport.send(msg);
			returnValue = "success";

		} catch (Exception e) {
			returnValue = "error";
		}

		return returnValue;
	}

	public String postIndividualMail(String recipient, String subject, String message, String from)
			throws MessagingException {

		String returnValue = "failure";

		try {

			boolean debug = false;
			// Set the host smtp address
			Properties props = new Properties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", SMTP_HOST_NAME);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", SMTP_PORT);
			props.put("mail.smtp.socketFactory.port", SMTP_PORT);
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			// props.put("mail.smtp.socketFactory.fallback", "true");

			SMTPAuthenticator auth = new SMTPAuthenticator();
			Session session = Session.getDefaultInstance(props, auth);

			session.setDebug(debug);

			// create a message
			Message msg = new MimeMessage(session);

			// set the from and to address
			InternetAddress addressFrom = new InternetAddress("donotreply");
			msg.setFrom(addressFrom);

			InternetAddress addressTo = new InternetAddress(recipient);

			msg.addRecipient(Message.RecipientType.TO, addressTo);
			// Setting the Subject and Content Type
			msg.setSubject(subject);
			/*
			 * if (props1 !=null && props1.getProperty("subject") != null &&
			 * props1.getProperty("subject") != ""){
			 * msg.setSubject(props1.getProperty("subject")); } else {
			 * msg.setSubject(subject); }
			 */

			msg.setContent(message, "text/html");
			/*
			 * if (props1 !=null && props1.getProperty("body") != null &&
			 * props1.getProperty("body") != ""){
			 * msg.setContent(props1.getProperty("body"),"text/html"); } else {
			 * msg.setContent(message, "text/html"); }
			 */
			msg.setHeader("Content-Type", encodingOptions);
			XTrustProvider.install();
			Transport.send(msg);
			returnValue = "success";

		} catch (Exception e) {
			e.printStackTrace();
			returnValue = "error";
		}
		return returnValue;
	}

	private class SMTPAuthenticator extends javax.mail.Authenticator {
		@Override
		public javax.mail.PasswordAuthentication getPasswordAuthentication() {
			String username = SMTP_AUTH_USER;
			String password = SMTP_AUTH_PWD;
			return new javax.mail.PasswordAuthentication(username, password);
		}
	}

}
