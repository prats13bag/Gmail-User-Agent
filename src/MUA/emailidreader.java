package src.MUA;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class emailidreader {
    static String emailid;
    static String emailpswrd;
    emailidreader() {
        Properties property = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("LoginCredentials.properties");
            property.load(input);

            if (property.containsKey("emailid")) {
                emailid = property.getProperty("emailid");
            }
            if (property.containsKey("emailpswrd")) {
                emailpswrd = property.getProperty("emailpswrd");
            }
        } catch (Exception e) {
            System.out.println("LoginCredentials.prop file is not found or some problem in reading the prop file");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}