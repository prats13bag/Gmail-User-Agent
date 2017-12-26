package src.MUA;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.sql.*;
import java.util.*;

public class recievemail extends emailidreader
{
	public static void main(String args[])
	{
		try
		{
		Properties prop=new Properties();
		prop.put("mail.imap.host", "imap.gmail.com");
		prop.put("mail.imap.port", "993");
		prop.put("mail.imap.auth", "true");
		prop.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		Session session=Session.getInstance(prop,null);
		Store store=session.getStore("imap");
		store.connect(emailid,emailpswrd);
		Folder inboxfolder=store.getFolder("inbox");
		inboxfolder.open(Folder.READ_ONLY);
		Message arr[]=inboxfolder.getMessages();
		for(int i=0;i<arr.length;i++)
		{
			System.out.println("--------------------------------Message"+(i+1)+"---------------------------------");
			Address[]from=arr[i].getFrom();
			System.out.println("From:  "+from[0]);
			System.out.println("Subject  "+arr[i].getSubject());
			System.out.println("Date   "+arr[i].getSentDate());
			System.out.println("Message   "+arr[i].getContent());


		}
		inboxfolder.close(false);
		store.close();
		System.out.println("Successfully received");
		}
		catch(Exception e)
		{
			System.out.println("Error! Mails were not received");
			e.printStackTrace();
		}
	}
}