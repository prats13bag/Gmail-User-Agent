package src.MUA;
import javax.mail.*;

public class SimpleMailAuthenticator extends Authenticator
{
	private String username=null;
	private String password=null;
	public SimpleMailAuthenticator(String username,String password)
	{
		this.username=username;
		this.password=password;

	}
	public PasswordAuthentication getPasswordAuthentication ()
	{
		return new PasswordAuthentication(username,password);
	}
}