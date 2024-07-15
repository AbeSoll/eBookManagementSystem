/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servlet.admin;

import com.dao.admin.BookDAO;
import com.database.DBConnect;
import com.detail.BookDetail;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 *
 * @author chetan
 */
@MultipartConfig
public class AdminBookAddServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            if (session.getAttribute("admin") == null) {
                return;
            }
            String bookName = request.getParameter("bookName").trim();
            String authorName = request.getParameter("authorName").trim();
            int price = Integer.parseInt(request.getParameter("price").trim());
            int totalBook = Integer.parseInt(request.getParameter("totalBook").trim());
            String category = request.getParameter("category").trim();
            Part part = request.getPart("photo");
            String fileName = part.getSubmittedFileName();
            BookDetail bd = new BookDetail();
            bd.setBookName(bookName);
            bd.setAuthorName(authorName);
            bd.setPrice(price);
            bd.setAvailable(totalBook);
            bd.setBookCategory(category);
            bd.setPhoto(fileName);

            StringBuilder sb = new StringBuilder();
            for (int i = fileName.length() - 1; i > 0; i--) {
                if (fileName.charAt(i) == '.') {
                    break;
                }
                sb.insert(0, fileName.charAt(i));
            }
            BookDAO dao = new BookDAO(DBConnect.getConnection());
            String f = dao.addBook(bd, sb);
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                if (!f.equals("no")) {
                    InputStream is = part.getInputStream();
                    byte[] data = new byte[is.available()];
                    is.read(data);

                    // Fixed directory for file storage
                    String fixedPath = "C:\\Users\\soleh\\OneDrive - Universiti Teknologi MARA\\Desktop\\eBookManagementSystem\\web\\img\\books-img";
                    File fileDir = new File(fixedPath);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    String fullPath = fixedPath + File.separator + f;
                    try (FileOutputStream fos = new FileOutputStream(fullPath)) {
                        fos.write(data);
                    }
                    out.println("done");
                } else {
                    out.println("no");
                }
            }
        } catch (IOException | NullPointerException | NumberFormatException | ServletException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}

