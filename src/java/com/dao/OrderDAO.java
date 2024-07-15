/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;
import com.detail.CartDetail;
import com.detail.OrderCartList;
import com.detail.OrderListDetail;
import com.detail.ShippingDetail;
import com.javaclass.Email;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author chetan
 */
public class OrderDAO {
    
    private final Connection conn;

    public OrderDAO(Connection conn) {
        this.conn = conn;
    }
    
    public boolean checkShippingAddress(int userId) {
        try {
            String query = "select * from shipping where userId = ?";
            PreparedStatement pt = conn.prepareStatement(query);
            pt.setInt(1, userId);
            ResultSet rs = pt.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    
   public boolean checkBookAvailable(int bookId) {
       try{
            String query = "select * from book where bookId = ?";
            PreparedStatement pt = conn.prepareStatement(query);
            pt.setInt(1, bookId);
            ResultSet rs = pt.executeQuery();
            if(rs.next()){
                if(rs.getInt("available")>0){
                    return true;
                }
            } 
        } catch(SQLException e){
            e.printStackTrace();
        }
       return false;
   }
   
public synchronized String confirmOrder(String email, int userId, String pMethod) {
    try {
        // fetch all information
        CartDAO cartDAO = new CartDAO(conn);
        List<CartDetail> cartList = cartDAO.getCart(userId);
        StringBuilder mailContent = new StringBuilder("\nPayment Method - " + pMethod);
        int totalPrice = 0;
        int orderId = 0;

        for (CartDetail cd : cartList) {
            if (checkBookAvailable(cd.getBookId())) {
                totalPrice += cd.getPrice();
                mailContent.append("\n\nBook Name - ").append(cd.getBookName());
                mailContent.append("\nQuantity - ").append(cd.getQuantity());
                mailContent.append("\nPrice - ").append(cd.getPrice());
            }
        }

        mailContent.append("\n\nBooks Price - ").append(totalPrice);
        int totalOrderPrice = (totalPrice > 699) ? totalPrice : (totalPrice + 70);
        mailContent.append("\nDelivery Charge - ").append((totalPrice > 699) ? "0" : "70");
        mailContent.append("\nTotal Order Price - ").append(totalOrderPrice);

        if (totalPrice == 0) {
            return "no";
        }

        // insert data in order table
        ShippingDAO shipDAO = new ShippingDAO(conn);
        ShippingDetail sd = shipDAO.getAddress(userId);

        String insertOrderQuery = "INSERT INTO orderlist (userId, price, paymentMethod, name, phone, address1, address2, landmark, city, pincode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pt1 = conn.prepareStatement(insertOrderQuery, PreparedStatement.RETURN_GENERATED_KEYS);
        pt1.setInt(1, userId);
        pt1.setInt(2, totalOrderPrice);
        pt1.setString(3, pMethod);
        pt1.setString(4, sd.getName());
        pt1.setString(5, sd.getPhone());
        pt1.setString(6, sd.getAddress1());
        pt1.setString(7, sd.getAddress2());
        pt1.setString(8, sd.getLandmark());
        pt1.setString(9, sd.getCity());
        pt1.setString(10, sd.getPinCode());
        int orderInserted = pt1.executeUpdate();

        if (orderInserted == 1) {
            ResultSet generatedKeys = pt1.getGeneratedKeys();
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            }

            String insertOrderCartQuery = "INSERT INTO ordercart (orderId, bookName, authorName, quantity, price) VALUES (?, ?, ?, ?, ?)";
            for (CartDetail cd : cartList) {
                if (checkBookAvailable(cd.getBookId())) {
                    PreparedStatement pt2 = conn.prepareStatement(insertOrderCartQuery);
                    pt2.setInt(1, orderId);
                    pt2.setString(2, cd.getBookName());
                    pt2.setString(3, cd.getAuthorName());
                    pt2.setInt(4, cd.getQuantity());
                    pt2.setInt(5, cd.getPrice());
                    int orderCartInserted = pt2.executeUpdate();

                    if (orderCartInserted == 1) {
                        cartDAO.deleteCart(cd.getBookId(), userId);

                        String updateBookQuery = "UPDATE book SET available = available - 1 WHERE bookId = ?";
                        PreparedStatement pt3 = conn.prepareStatement(updateBookQuery);
                        pt3.setInt(1, cd.getBookId());
                        pt3.executeUpdate();
                    }
                }
            }
        }

        StringBuilder emailContent = new StringBuilder("Hi,");
        emailContent.append("\nOrder number - ").append(orderId);
        emailContent.append(mailContent);
        emailContent.append("\n\nThanks for ordering\nIf you are having any issue with your account, please don't hesitate to contact us.\n\nThanks!\nQuiz Maker");
        String subject = "Order Confirm";

        Thread emailThread = new Thread(new Email(email, subject, emailContent.toString()));
        emailThread.start();

        return "done";
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return "no";
}

    public List<OrderListDetail> getOrderList(int userId) {
       List<OrderListDetail> list = new ArrayList<OrderListDetail>();
       OrderListDetail cd;
        try {
            String query1 = "select * from orderlist where userId = ? ORDER BY orderID DESC";
            PreparedStatement pt1 = conn.prepareStatement(query1);
            pt1.setInt(1, userId);
            ResultSet rs1 = pt1.executeQuery();
            while(rs1.next()){
                cd = new OrderListDetail();
                cd.setOrderID(rs1.getInt("orderId"));
                cd.setPaymentMethod(rs1.getString("paymentMethod"));
                cd.setPrice(rs1.getInt("price"));
                cd.setStatus(rs1.getString("status"));
                cd.setDate(rs1.getTimestamp("time").toString());
                list.add(cd);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
       
       return list;
    }
    
    public OrderListDetail getOrderView(int userId, int orderId) {
        OrderListDetail cd = null;
        try {
            String query1 = "select * from orderlist where orderId = ? and userId = ?";
            PreparedStatement pt1 = conn.prepareStatement(query1);
            pt1.setInt(1, orderId);
            pt1.setInt(2, userId);
            ResultSet rs1 = pt1.executeQuery();
            while(rs1.next()){
                cd = new OrderListDetail();
                cd.setOrderID(rs1.getInt("orderId"));
                cd.setPaymentMethod(rs1.getString("paymentMethod"));
                cd.setPrice(rs1.getInt("price"));
                cd.setStatus(rs1.getString("status"));
                cd.setDate(rs1.getTimestamp("time").toString());
                cd.setName(rs1.getString("name"));
                cd.setPhone(rs1.getString("phone"));
                cd.setAddress1(rs1.getString("address1"));
                cd.setAddress2(rs1.getString("address2"));
                cd.setLandmark(rs1.getString("landmark"));
                cd.setCity(rs1.getString("city"));
                cd.setPinCode(rs1.getString("pincode"));
                String query2 = "select * from ordercart where orderId = ?";
                PreparedStatement pt2 = conn.prepareStatement(query2);
                pt2.setInt(1, orderId);
                ResultSet rs2 = pt2.executeQuery();
                List<OrderCartList> list = new ArrayList<OrderCartList>();
                OrderCartList ocl;
                while(rs2.next()) {
                    ocl = new OrderCartList();
                    ocl.setBookName(rs2.getString("bookName"));
                    ocl.setAuthorName(rs2.getString("authorName"));
                    ocl.setPrice(rs2.getInt("price"));
                    ocl.setQuantity(rs2.getInt("quantity"));
                    list.add(ocl);
                }
                cd.setOcl(list);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
       return cd;
   }
    
    
}
