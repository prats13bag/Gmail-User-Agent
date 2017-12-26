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

class outboxsendbtHandler extends emailidreader implements Runnable
{
	PreparedStatement psmt;
	Connection con;
	DefaultTableModel sentitemmodel,outboxmodel;
	Object val;
	ResultSet res;
	JTable outboxtable;
	int []rowNum;
	public outboxsendbtHandler(Object value, JTable outboxtable, DefaultTableModel sentitemmodel, DefaultTableModel outboxmodel)
	{
		this.val=value;
		this.outboxtable=outboxtable;
		this.outboxmodel=outboxmodel;
		this.sentitemmodel=sentitemmodel;
		rowNum=outboxtable.getSelectedRows();
		try	{
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			String url="jdbc:mysql://localhost:3306/";
			String dbusername="root";
			String dbpassword="root";
			con=DriverManager.getConnection(url,dbusername,dbpassword);
		}
		catch(SQLException sqle) {
			System.out.println("Error! Could not connect to database at line number 39 in outboxsendbtHandler.java file");
			try
			{
				con.close();
			}
			catch(Exception conn){
				System.out.println("Failed to close database connection at line number 45 in outboxsendbtHandler.java file");
			}
		}

	}
	public void run()
	{
		System.out.println("Mail Sent");
   		try
		{
		Properties prop=new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "465");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		Session session=Session.getInstance(prop,new SimpleMailAuthenticator(emailid,emailpswrd));
		Message msg=new MimeMessage(session);
		try
		{
			psmt=con.prepareStatement("SELECT * FROM outboxdbtable WHERE DATE=?");
			psmt.setString(1,(String)val);
			res=psmt.executeQuery();
			while(res.next())
			{
				msg.setRecipient(Message.RecipientType.TO,new InternetAddress(res.getString("TO")));
				msg.setSubject(res.getString("SUBJECT"));
				msg.setText(res.getString("MESSAGE"));
			}
		}
		catch(Exception e)
		{
			System.out.println("Error! Could not connect to database at line number 76 in outboxsendbtHandler.java file");
		}
		Transport.send(msg);
		System.out.println("E-mail is sent successfully");
		int ins=0;
		int idel=0;
		try
		{
			psmt=con.prepareStatement("INSERT INTO sentitemdbtable SELECT *FROM outboxdbtable where date=?");
			psmt.setString(1,(String)val);
			ins=psmt.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("Error! Could not insert data into database");
		}
		try
		{
		 	psmt=con.prepareStatement("Update sentitemdbtable SET  date=? where date=?");
			psmt.setString(1,new java.util.Date().toString());
			psmt.setString(2,(String)val);
			psmt.executeUpdate();
		}
		catch(Exception v)
		{
			System.out.println("Error! Could not update database");
		}
		try
		{
			psmt=con.prepareStatement("delete FROM outboxdbtable  where date= ?");
			psmt.setString(1,(String)val);
			idel=psmt.executeUpdate();
		}
		catch(Exception m){}
		if(idel!=0&&ins!=0)
		{
			try
			{
				psmt =con.prepareStatement("SELECT *FROM sentitemdbtable where date=?");
				psmt.setString(1,new java.util.Date().toString());
				res=psmt.executeQuery();
				while(res.next())
				{
					sentitemmodel.addRow(new String[]{res.getString("To"),res.getString("subject"),res.getString("date")});
				}
			}
			catch(Exception n){}
			for(int i=rowNum.length;i>0;i--)
			{
				outboxmodel.removeRow(rowNum[i-1]);
			}
		}
		}
		catch(Exception e)
		{
			System.out.println("Error! Could not connect to server. Please check connection");
			int up=0;
	 		try
	 		{
	 			psmt=con.prepareStatement("Update outboxdbtable SET  date=? where date=?");
				psmt.setString(1,new java.util.Date().toString());
				psmt.setString(2,(String)val);
				up=psmt.executeUpdate();
			}
			catch(Exception v){}
			if(up!=0)
			{
			  	for(int i=rowNum.length;i>0;i--)
				{
					outboxmodel.removeRow(rowNum[i-1]);
				}
				try
				{
					psmt =con.prepareStatement("SELECT *FROM outboxdbtable where date=?");
					psmt.setString(1,new java.util.Date().toString());
					res=psmt.executeQuery();
					while(res.next())
					{
						outboxmodel.addRow(new String[]{res.getString("To"),res.getString("subject"),res.getString("date")});
					}
				}
				catch(Exception n){
					System.out.println("Error! Could not update database");
				}
			}
		}
	}
}