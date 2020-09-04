package com.dropwizard.demo.send;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

public class SendMail {
	
	public static boolean sendForgotPasswordMail(String email, HttpSession httpSession, String verify) {
		
		String hostname = "smtp.gmail.com";
		String username = "parmar.db111@gmail.com";
		String password = "pooja.parmar";
		
		Properties properties = new Properties();
		properties.put("mail.smtp.host", hostname);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); //TLS
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.ssl.trust", hostname);
        
        Session session = Session.getDefaultInstance(properties);
        
        try {
			
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipient( Message.RecipientType.TO, new InternetAddress(email));
            
            int otpToVerify = 0;
			do {
				otpToVerify = new Random().nextInt(999999);
			}while(Integer.toString(otpToVerify).length()<6);
			
            String htmlContent = "";
            
            if(verify.equalsIgnoreCase("forgotpassword")) {
            	message.setSubject("OTP to reset password");
            	htmlContent = "Your OneTimePassword is " + otpToVerify + " to reset your password. <br/>Valid for 5 minutes.";	
            	httpSession.setAttribute("otpToVerifyToForgotPassword", otpToVerify);
        		httpSession.setAttribute("otpSentAtMilliSecondsToForgotPassword", System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));
            } else if(verify.equalsIgnoreCase("verifyaccount")) {
            	message.setSubject("OTP to Verify Account");
            	htmlContent = "Your OneTimePassword is " + otpToVerify + " to verify your account. <br/>Valid for 5 minutes.";
            	httpSession.setAttribute("otpToVerifyForAccountActivation", otpToVerify);
        		httpSession.setAttribute("otpSentAtMilliSecondsForAccountActivation", System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));
            }
				
            message.setContent(htmlContent, "text/html;charset=UTF-8");
            
            Transport transport = session.getTransport("smtp");	//"smtp"
            transport.connect(hostname, username, password); 
    		transport.sendMessage(message, message.getAllRecipients());
    		transport.close();
    		
    		return true;
    		
		}catch (Exception e) {
			if(verify.equalsIgnoreCase("forgotpassword")) {
            	httpSession.removeAttribute("otpToVerifyToForgotPassword");
        		httpSession.removeAttribute("otpSentAtMilliSecondsToForgotPassword");
            } else if(verify.equalsIgnoreCase("verifyaccount")) {	
            	httpSession.removeAttribute("otpToVerifyForAccountActivation");
        		httpSession.removeAttribute("otpSentAtMilliSecondsForAccountActivation");
            }
			return false;
		}

	}
}
