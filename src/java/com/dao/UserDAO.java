/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.database.DBConnect;
import com.detail.UserDetail;
import com.javaclass.Email;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author chetan
 */
public class UserDAO {
 
    public String userRegistration(String name, String email, String password) {
        try {
            String ePassword = /*PasswordEncrypt.hashPassword*/(password);
            Connection conn = DBConnect.getConnection();
            String query1 = "SELECT * FROM useraccount WHERE email = ? AND active = TRUE";
            PreparedStatement pt1 = conn.prepareStatement(query1);
            pt1.setString(1, email);
            ResultSet rs1 = pt1.executeQuery();
            if (rs1.next()) {
                return "alreadyUser";
            } else {
                String query2 = "SELECT * FROM useraccount WHERE email = ?";
                PreparedStatement pt2 = conn.prepareStatement(query2);
                pt2.setString(1, email);
                ResultSet rs2 = pt2.executeQuery();
                if (rs2.next()) {
                    String query3 = "UPDATE useraccount SET name = ?, password = ?, active = FALSE WHERE email = ?";
                    PreparedStatement pt3 = conn.prepareStatement(query3);
                    pt3.setString(1, name);
                    pt3.setString(2, ePassword);
                    pt3.setString(3, email);
                    int i = pt3.executeUpdate();
                    if (i == 1) {
                        String query4 = "DELETE FROM userverifications WHERE email = ?";
                        PreparedStatement pt4 = conn.prepareStatement(query4);
                        pt4.setString(1, email);
                        pt4.executeUpdate();
                        int otp = getOTP();
                        String subject = "Account Verification";
                        String body = "Hello " + name + ",\n\n"
                                + "Your verification link is : \n"
                                + "http://localhost:8080/eBookManagementSystem/GetVerifiedUserServlet?email=" + email + "&token=" + otp + "\n\n"
                                + "If you are having any issue with your account, please don't hesitate to contact us.\n\n"
                                + "Thanks!\n"
                                + "Quiz Maker";
                        Thread td = new Thread(new Email(email, subject, body));
                        td.start();
                        String query5 = "INSERT INTO userverifications (email, token) VALUES (?, ?)";
                        PreparedStatement pt5 = conn.prepareStatement(query5);
                        pt5.setString(1, email);
                        pt5.setInt(2, otp);
                        pt5.executeUpdate();
                        return "done";
                    }
                } else {
                    String query3 = "INSERT INTO useraccount (name, email, password, active) VALUES (?, ?, ?, FALSE)";
                    PreparedStatement pt3 = conn.prepareStatement(query3);
                    pt3.setString(1, name);
                    pt3.setString(2, email);
                    pt3.setString(3, ePassword);
                    int i = pt3.executeUpdate();
                    if (i == 1) {
                        String query4 = "DELETE FROM userverifications WHERE email = ?";
                        PreparedStatement pt4 = conn.prepareStatement(query4);
                        pt4.setString(1, email);
                        pt4.executeUpdate();
                        int otp = getOTP();
                        String subject = "Account Verification";
                        String body = "Hello " + name + ",\n\n"
                                + "Your verification link is : \n"
                                + "http://localhost:8080/eBookManagementSystem/GetVerifiedUserServlet?email=" + email + "&token=" + otp + "\n\n"
                                + "If you are having any issue with your account, please don't hesitate to contact us.\n\n"
                                + "Thanks!\n"
                                + "Quiz Maker";
                        Thread td = new Thread(new Email(email, subject, body));
                        td.start();
                        String query5 = "INSERT INTO userverifications (email, token) VALUES (?, ?)";
                        PreparedStatement pt5 = conn.prepareStatement(query5);
                        pt5.setString(1, email);
                        pt5.setInt(2, otp);
                        pt5.executeUpdate();
                        return "done";
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "no";
    }
    
    private int getOTP() {
       return (int)(Math.random()*(999999999-111111111+1)+111111111);
    }
    
    public String userLogin(String email, String password) {
        try {
            Connection conn = DBConnect.getConnection();
            String ePassword = /*PasswordEncrypt.hashPassword*/(password);
            String query = "SELECT * FROM useraccount WHERE email = ? AND password = ?";
            PreparedStatement pt = conn.prepareStatement(query);
            pt.setString(1, email);
            pt.setString(2, ePassword);
            ResultSet rs = pt.executeQuery();
            if (rs.next()) {
                return "done";
            } else {
                return "invalid";
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "no";
    }
    
    public void verifyUser(String email, int token) {
        try {
            Connection conn = DBConnect.getConnection();
            String query = "SELECT * FROM userverifications WHERE email = ? AND token = ?";
            PreparedStatement pt = conn.prepareStatement(query);
            pt.setString(1, email);
            pt.setInt(2, token);
            ResultSet rs = pt.executeQuery();
            if (rs.next()) {
                String query4 = "DELETE FROM userverifications WHERE email = ?";
                PreparedStatement pt4 = conn.prepareStatement(query4);
                pt4.setString(1, email);
                pt4.executeUpdate();
                String query5 = "UPDATE useraccount SET active = TRUE WHERE email = ?";
                PreparedStatement pt5 = conn.prepareStatement(query5);
                pt5.setString(1, email);
                pt5.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public UserDetail userDetail(String email) {
        UserDetail ud = null;
        try {
            Connection conn = DBConnect.getConnection();
            String query = "SELECT * FROM useraccount WHERE email = ?";
            PreparedStatement pt = conn.prepareStatement(query);
            pt.setString(1, email);
            ResultSet rs = pt.executeQuery();
            if (rs.next()) {
                ud = new UserDetail();
                ud.setId(rs.getInt("id"));
                ud.setName(rs.getString("name"));
                ud.setEmail(rs.getString("email"));
                ud.setActive(rs.getBoolean("active"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ud;
    }
    
    public String deleteAccount(int id) {
        try {
            Connection conn = DBConnect.getConnection();
            String query = "DELETE FROM useraccount WHERE id = ?";
            PreparedStatement pt = conn.prepareStatement(query);
            pt.setInt(1, id);
            pt.executeUpdate();
            return "done";
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "no";
    }

    public String changePassword(int id, String oPassword, String nPassword) {
        try {
            String eoPassword = /*PasswordEncrypt.hashPassword*/(oPassword);
            String enPassword = /*PasswordEncrypt.hashPassword*/(nPassword);            
            Connection conn = DBConnect.getConnection();
            String query = "SELECT * FROM useraccount WHERE id = ? AND password = ?";
            PreparedStatement pt = conn.prepareStatement(query);
            pt.setInt(1, id);
            pt.setString(2, eoPassword);
            ResultSet rs = pt.executeQuery();
            if (rs.next()) {
                String query2 = "UPDATE useraccount SET password = ? WHERE id = ?";
                PreparedStatement pt2 = conn.prepareStatement(query2);
                pt2.setString(1, enPassword);
                pt2.setInt(2, id);
                int i = pt2.executeUpdate();
                if (i == 1) {
                    return "done";
                }
            } else {
                return "notMatched";
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "no";
    }

    public UserDetail login(String email, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

