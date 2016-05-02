package edu.arizona.biosemantics.micropie.web.server;

import java.security.Security;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This sends mail to Intended receipients using Gmail SMTP Server and Account
 *
 * @author Ashwin
 *
 */
public class MailSender {

    private static final transient Log logger = LogFactory
    .getLog(MailSender.class);


	String emailUserName = Configuration.emailAddress;
	String emailUserPassword = Configuration.emailPassword;

	
    private static final String SMTP_HOST_NAME = Configuration.emailSMTPServer;
    private static final String SMTP_PORT = Configuration.emailSMTPPort;
    private static final String emailMsgTxt = "Test Message Contents";
    private static final String emailSubjectTxt = "A test from gmail";
    private static final String emailFromAddress = "filelistener.fujitsu@test";
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    // private static final String[] sendTo = { "ashwin.rayaprolu@idhasoft.com",
    //        "ashwin.rayaprolu@gmail.com" };

    private static final String[] sendTo = { "maojin0@gmail.com" };
    
    public static void main(String args[]) throws Exception {

        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        String[] filenames = { "maojin0_at_gmail_dot_com_2014_12_03_11_22_27_184-output/matrix.csv" };

        new MailSender().sendSSLMessage(Arrays.asList(sendTo), emailSubjectTxt, emailMsgTxt,
                emailFromAddress, filenames);
        System.out.println("Sucessfully Sent mail to All Users");
    }

    /**
     * @param recipients
     * @param subject
     * @param messageContent
     * @param from
     * @param attachments
     * @throws Exception
     */
    public static void sendMail(List recipients, String subject,
            String messageContent, String from, String[] attachments)
            throws Exception {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        String[] filenames = { "maojin0_at_gmail_dot_com_2014_12_03_11_22_27_184-output/matrix.csv" };

        new MailSender().sendSSLMessage(recipients, subject, messageContent,
                from, filenames);
        System.out.println("Sucessfully Sent mail to All Users");
    }

    /**
     * @param attachments
     * @param multipart
     * @throws MessagingException
     * @throws AddressException
     */
    protected void addAtachments(String[] attachments, Multipart multipart)
            throws MessagingException, AddressException {
        for (int i = 0; i <= attachments.length - 1; i++) {
            String filename = attachments[i];
            
            String[] filenameArray = attachments[i].split("[/\\\\]");
            String filenameOnAttachemnt = filenameArray[filenameArray.length-1];            
            
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();

            // use a JAF FileDataSource as it does MIME type detection
            DataSource source = new FileDataSource(filename);
            attachmentBodyPart.setDataHandler(new DataHandler(source));

            // assume that the filename you want to send is the same as the
            // actual file name - could alter this to remove the file path
            attachmentBodyPart.setFileName(filenameOnAttachemnt);

            // add the attachment
            multipart.addBodyPart(attachmentBodyPart);
        }
    }

    /**
     * @param recipients
     * @param subject
     * @param messageContent
     * @param from
     * @throws MessagingException
     */
    public void sendSSLMessage(List recipients, String subject,
            String messageContent, String from, String[] attachments)
            throws MessagingException {
        boolean debug = false;

        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                        		emailUserName, emailUserPassword);
                    }
                });

        session.setDebug(debug);

        Message message = new MimeMessage(session);
        InternetAddress addressFrom = new InternetAddress(from);
        message.setFrom(addressFrom);

        for (Iterator it = recipients.iterator(); it.hasNext();) {
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress((String) it.next()));
        }

        // Setting the Subject and Content Type
        message.setSubject(subject);

        // Create a message part to represent the body text
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(messageContent, "text/html");

        // use a MimeMultipart as we need to handle the file attachments
        Multipart multipart = new MimeMultipart();

        // add the message body to the mime message
        multipart.addBodyPart(messageBodyPart);

        // add any file attachments to the message
        addAtachments(attachments, multipart);

        // Put all message parts in the message
        message.setContent(multipart);

        Transport.send(message);

        logger.info("Sent Mail ");
    }
}
