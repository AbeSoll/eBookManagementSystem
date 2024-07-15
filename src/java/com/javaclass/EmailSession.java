/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javaclass;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

/**
 *
 * @author chetan
 */
public class EmailSession {
 
    private static Session session;
    private final static String fromEmail = "dnpit800511@gmail.com"; // your Gmail ID
    private final static String password = "ndllnlchkzionzue"; // your Gmail App Password
 
    public static Session getSession(){
        if(session == null){
            System.out.println("TLSEmail Start");
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
            props.put("mail.smtp.auth", "true"); // Enabling SMTP Authentication
            props.put("mail.smtp.starttls.enable", "true"); // Enabling STARTTLS
            props.put("mail.smtp.port", "587"); // SMTP Port

            // Optional: Debugging information for JavaMail
            props.put("mail.debug", "true");

            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            };
            session = Session.getInstance(props, auth); // Use getInstance instead of getDefaultInstance
            System.out.println("Session created");
        }
        return session;
    }
}

