package com.angelotricarico.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.angelotricarico.SensitiveData;

public class SendMail {

	public static void sendMail(final String subject, final String body) {

		new Thread(new Runnable() {
			@Override
			public void run() {

				Properties properties = System.getProperties();
				properties.setProperty("mail.smtp.host", SensitiveData.host);
				Session session = Session.getDefaultInstance(properties);

				try {
					MimeMessage message = new MimeMessage(session);
					message.setFrom(new InternetAddress(SensitiveData.from));
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(SettingsPreference.loadEmailAddressAlert()));
					message.setSubject(subject);
					message.setText(body, "utf-8", "html");

					Transport.send(message);
					AmazonUtility.log("Mail Sent to: " + SettingsPreference.loadEmailAddressAlert());
				} catch (MessagingException e) {
					AmazonUtility.log(e.toString());
				}

			}
		}).start();

	}

}