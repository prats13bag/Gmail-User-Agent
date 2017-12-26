package src.MUA;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.tree.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.sql.*;
import java.util.*;
import javax.swing.table.*;

public class recieveallbtHandler extends emailidreader implements Runnable {
	PreparedStatement psmt;
	Connection con;
	DefaultTableModel model;
	ResultSet res;
	public 	recieveallbtHandler(PreparedStatement psmt,Connection con,DefaultTableModel model)
	{
		this.psmt=psmt;
		this.con=con;
		this.model=model;
	}

	public void run() {
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
			System.out.println("--------------------------------Message "+(i+1)+"---------------------------------");
			Address[]from=arr[i].getFrom();
			System.out.println("From:		"+from[0]);
			System.out.println("Subject:	"+arr[i].getSubject());
			System.out.println("Date:		"+arr[i].getSentDate());
			System.out.println("Message:	"+arr[i].getContent());

			try
			{

				psmt=con.prepareStatement("Select Date FROM MESSAGEDETAILS where date=?");
				psmt.setString(1,arr[i].getSentDate().toString());
				res=psmt.executeQuery();
				String date="";
				while(res.next())
				{
					date=res.getString("Date");

				}
				if(!date.equals(arr[i].getSentDate().toString()))
				{
						psmt=con.prepareStatement("INSERT INTO MESSAGEDETAILS values(?,?,?,?)");
						psmt.setString(1,(String)from[0].toString());
						psmt.setString(2,(String)arr[i].getSubject());
						psmt.setString(3,arr[i].getSentDate().toString());
						psmt.setString(4,(String)arr[i].getContent());
						int b=psmt.executeUpdate();
						if(b!=0)
						model.addRow(new String[]{(String)from[0].toString(),(String)arr[i].getSubject(),arr[i].getSentDate().toString()});
				}
			}
			catch(Exception e)
			{

				System.out.println("Error! Could not insert data into database");
			}
		}
		inboxfolder.close(false);
		store.close();
		System.out.println("Successfully received all e-mails");
		}
		catch(Exception e)
		{
			System.out.println("Error! Could not connect to the server. Please check connection");
		}
	}
}