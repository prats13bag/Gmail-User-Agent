package src.MUA;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class MailSender extends emailidreader
{
	MailSender()
	{
		try
		{
			Properties prop=new Properties();
			prop.put("mail.smtp.host", "smtp.gmail.com");
			prop.put("mail.smtp.port", "465");
			prop.put("mail.smtp.auth", "true");
			prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			Session session=Session.getInstance(prop,new SimpleMailAuthenticator(emailid,emailpswrd));
			Message msg=new MimeMessage(session);
			msg.setRecipient(Message.RecipientType.TO,new InternetAddress(emailid));
			msg.setSubject("Test Message for Send Functionality");
			msg.setText("Hello! How are you?");
			Transport.send(msg);
			System.out.println("Test email sent successfully");
		}
		catch(Exception e)
		{
			System.out.println("Error encountered at line 27 in MailSender.java file");
			e.printStackTrace();
		}
	}
	public static void main(String args[])
	{
		new MailSender();
	}
}