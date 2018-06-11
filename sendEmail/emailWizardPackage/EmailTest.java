package emailWizardPackage;



import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import utils.ConnectionMSQL;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

class Article {
    private String article_id;
    private String title;
    private String category;

    public Article() {
        // TODO Auto-generated constructor stub
    }

    public Article(String article_id, String title, String category) {
        super();
        this.article_id = article_id;
        this.title = title;
        this.category = category;
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}

class ArticleDao {
    private Connection con;

    private static final String GET_ARTICLES = "SELECT * FROM articles";

//    private void connect() throws InstantiationException,
//            IllegalAccessException, ClassNotFoundException, SQLException {
//        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
//                .newInstance();
//        con = DriverManager
//                .getConnection("jdbc:sqlserver://100.00.000.000\\SQLEXPRESS:3316;databaseName=Employee");
//    }

    public List<Article> getArticles() throws Exception {
    	ConnectionMSQL connToDB = ConnectionMSQL.getInstance();
    	connToDB.setMSQLData("www.db4free.net", "3306", "bd", "user", "pass");
    	con=connToDB.getConnection();
        PreparedStatement ps = con.prepareStatement(GET_ARTICLES);
        ResultSet rs = ps.executeQuery();
        List<Article> result = new ArrayList<Article>();
        while (rs.next()) {
            result.add(new Article(rs.getString("article_id"), rs
                    .getString("title"), rs.getString("category")));
        }
        disconnect();
        return result;
    }

    private void disconnect() throws SQLException {
        if (con != null) {
            con.close();
        }
    }
}

class EmailSender {
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
                return new PasswordAuthentication("b1@gmail.com", "123456");
            }
        });
    }

    public void sendEmail(Article e) throws MessagingException {
         init();
         Message message = new MimeMessage(session);
         message.setFrom(new InternetAddress("b1@gmail.com"));
//         message.setRecipients(Message.RecipientType.TO,
//             InternetAddress.parse(e.getArticle_id()));
         message.setRecipient(Message.RecipientType.TO, new InternetAddress("b2@gmail.com"));
         message.setRecipient(Message.RecipientType.TO, new InternetAddress("b1@gmail.com"));
         message.setSubject("Report");
         message.setText(e.getTitle() + " " + e.getCategory());
         Transport.send(message);
    }
    public void sendEmail(List<Article> articles) throws MessagingException{
        for (Article article : articles) {
            sendEmail(article);
        }
    }
}



public class EmailTest {
//    public static void main(String[] args) throws Exception {
//    	ArticleDao dao=new ArticleDao();
//        List<Article> list=dao.getArticles();
//        EmailSender sender=new EmailSender();
//        sender.sendEmail(list);
//    }
}