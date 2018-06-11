package emailWizardPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import utils.ConnectionMSQL;
import utils.ResultSetToHtmlTable;

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
	private static String filename = strDate+"_Data_quality_report.csv";
    private static final String GET_ARTICLES = "SELECT * FROM articles";
    
    public static void convertToCsv(ResultSet rs) throws SQLException, FileNotFoundException {
        PrintWriter csvWriter = new PrintWriter(new File(filename)) ;
        ResultSetMetaData meta = rs.getMetaData(); 
        int numberOfColumns = meta.getColumnCount() ; 
        String dataHeaders = "\"" + meta.getColumnName(1) + "\"" ; 
        for (int i = 2 ; i < numberOfColumns + 1 ; i ++ ) { 
                dataHeaders += ",\"" + meta.getColumnName(i).replaceAll("\"","\\\"") + "\"" ;
        }
        csvWriter.println(dataHeaders) ;
        while (rs.next()) {
            String row = "\"" + rs.getString(1).replaceAll("\"","\\\"") + "\""  ; 
            for (int i = 2 ; i < numberOfColumns + 1 ; i ++ ) {
                row += ",\"" + rs.getString(i).replaceAll("\"","\\\"") + "\"" ;
            }
        csvWriter.println(row) ;
        }
        csvWriter.close();
    }
    
    public void getArticles() throws Exception {
    	ConnectionMSQL connToDB = ConnectionMSQL.getInstance();
    	connToDB.setMSQLData("www.db4free.net", "3306", "DB", "user", "key");
    	con=connToDB.getConnection();
        PreparedStatement ps = con.prepareStatement(GET_ARTICLES);
        ResultSet rs = ps.executeQuery();
        convertToCsv(rs);
//        ResultSetToHtmlTable table = new ResultSetToHtmlTable();
        
//        PrintWriter out = new PrintWriter ("file.txt");
//		        String result = table.writeTable(rs, writer);
//        int result = dumpData(rs, out);
        
        disconnect();
    }
    
    private Session session;

    private void init() {
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "100.00.000.000");
//        props.put("mail.smtp.port", "20");
    	Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

        session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("sender@gmail.com", "password");
            }
        });
    }
	
	public void sendEmail() throws UnsupportedEncodingException, MessagingException {
	    init();
	    try {
			this.getArticles();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	    
	    
	    Message msg = new MimeMessage(session);

	    Address a = new InternetAddress("sender@gmail.com", "Maria's testing");
	    Address b = new InternetAddress("receiver@gmail.com");
	    BodyPart texto = new MimeBodyPart();
        texto.setText("Adjunto reporte de calidad de data del dia " + new Date());

        // Se compone el adjunto con la imagen
        BodyPart adjunto = new MimeBodyPart();
        adjunto.setDataHandler(
            new DataHandler(new FileDataSource(filename)));
        adjunto.setFileName(filename);

        // Una MultiParte para agrupar texto e imagen.
        MimeMultipart multiParte = new MimeMultipart();
        multiParte.addBodyPart(texto);
        multiParte.addBodyPart(adjunto);

	    try {
			msg.setFrom(a);
			msg.addRecipient(Message.RecipientType.TO, b);
			msg.setSubject("Articles");
			msg.setContent(multiParte);

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
		} catch (AddressException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void disconnect() throws SQLException {
        if (con != null) {
            con.close();
        }
    }

}
