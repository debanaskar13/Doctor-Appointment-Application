package site.debashisnaskar.rxflow.utils;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public class EmailUtil {

    private static final String emailUsername;
    private static final String emailPassword;

    private static final Logger logger = Logger.getLogger(EmailUtil.class.getName());
    private static final Dotenv dotenv = Dotenv.load();
    private static final Properties properties = new Properties();

    static {
        emailUsername = dotenv.get("ADMIN_EMAIL_USERNAME");
        emailPassword = dotenv.get("ADMIN_EMAIL_PASSWORD");
        properties.put("mail.smtp.host", dotenv.get("MAIL_SMTP_HOST"));
        properties.put("mail.smtp.port", dotenv.get("MAIL_SMTP_PORT"));
        properties.put("mail.smtp.auth", dotenv.get("MAIL_SMTP_AUTH"));
        properties.put("mail.smtp.socketFactory.port", dotenv.get("MAIL_SMTP_SOCKETFACTORY_PORT"));
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

    }

    private static Session getSession(){
        return Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailUsername,emailPassword);
            }
        });
    }

    public static void sendEmail(String to, String subject, String body) {

        try{
            Session session = getSession();
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailUsername));
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(body,"text/html;charset=utf-8");

            Transport.send(message);
            System.out.println("Email sent successfully");

        }catch (Exception e){
            e.printStackTrace();
            logger.severe(e.getMessage());
        }

    }

//    public static void main(String[] args) {
//
//        String to = "debashisnaskar1301@gmail.com";
//        String subject = "Rxflow Application Demo Testing Email Service";
//        Map<String , String> map = new HashMap<>();
//        map.put("name", "Ayan Das");
//        map.put("email", to);
//        map.put("role","User");
//        String htmlContent = EmailTemplateProvider.getAdminRegisterNotificationTemplate(map);
//
//        sendEmail(to, subject, htmlContent);
//    }

}
