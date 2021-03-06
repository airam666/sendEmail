package emailWizardPackage;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import utils.ConnectionMSQL;
import utils.ConnectMSSQLServer;
import utils.CSVWriter;

import com.microsoft.sqlserver.jdbc.*; 

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author fpalacios
 *
 */
public class EmailWizard {
	private static Connection con;
	private static Date myDate = new Date();
	private static SimpleDateFormat sm = new SimpleDateFormat("MM-dd-yyyy");
	private static String strDate = sm.format(myDate);
	private static final String BIG_ASS_QUERY = "SELECT * FROM articles";
	private static String filename = strDate+"_Data_quality_report.csv";
//    private static final String GET_ARTICLES = "SELECT * FROM articles";
    
       
    
    public void getArticles() throws Exception {
    	//ConnectionMSQL connToDB = ConnectionMSQL.getInstance();
    	try {
	    	ConnectMSSQLServer connToDB = ConnectMSSQLServer.getInstance();
	    	connToDB.setMSSQLServerData("host", "port", "db", "user", "pass");
	    	Thread.sleep(500);
	    	con=connToDB.getConnection();
	//        PreparedStatement ps = con.prepareStatement(GET_ARTICLES);
	    	PreparedStatement ps = con.prepareStatement(BIG_ASS_QUERY);
	        Thread.sleep(500);
	        ResultSet rs = ps.executeQuery();
	        //creates a csv with the data query
	        CSVWriter.convertToCsv(rs, filename);
	        
        disconnect();
    	}catch (Exception e) {
    		System.out.println("error " + e.getMessage());
    	}
    }
    
    private Session session;
    
    /*
     * Authenticates the email user.
     */
    private void init() {
    	Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

        session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("b1@gmail.com", "pass");
            }
        });
    }
    
    /*
     * Sends an email with the data attached	
     */
	public void sendEmail() throws UnsupportedEncodingException, MessagingException {
	    init();
	    try {
			this.getArticles();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	    
	    
	    Message msg = new MimeMessage(session);

	    Address a = new InternetAddress("b1@gmail.com", "Maria's testing");
	    Address b = new InternetAddress("b2@gmail.com");
	    BodyPart texto = new MimeBodyPart();
        texto.setText("Adjunto reporte de calidad de data del dia " + new Date());

        // the attach with the file is composed
        BodyPart adjunto = new MimeBodyPart();
        adjunto.setDataHandler(
            new DataHandler(new FileDataSource(filename)));
        adjunto.setFileName(filename);

        // a multipart compose text and file.
        MimeMultipart multiPart = new MimeMultipart();
        multiPart.addBodyPart(texto);
        multiPart.addBodyPart(adjunto);

	    try {
			msg.setFrom(a);
			msg.addRecipient(Message.RecipientType.TO, b);
			msg.setSubject("Articles");
			msg.setContent(multiPart);

	    Transport.send(msg);
	    } catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public EmailWizard() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EmailWizard wiz = new EmailWizard();
		try {
			wiz.sendEmail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		System.out.println("Done!");
	}
	
	private void disconnect() throws SQLException {
        if (con != null) {
            con.close();
        }
    }

}
