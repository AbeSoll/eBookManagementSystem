package com.dao.admin;

import com.detail.BookDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO implements BookDAOinterFace {

    private final Connection conn;

    public BookDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public synchronized String addBook(BookDetail bd, StringBuilder photoExt) {
        try {
            // Ensure unique photo name
            String photoName = generateUniquePhotoName(bd.getBookCategory(), photoExt);
            while (isPhotoNameExists(photoName)) {
                photoName = generateUniquePhotoName(bd.getBookCategory(), photoExt);
            }

            // Insert book details
            String query = "INSERT INTO book (bookName, authorName, price, bookCategory, available, photo) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pt = conn.prepareStatement(query)) {
                pt.setString(1, bd.getBookName());
                pt.setString(2, bd.getAuthorName());
                pt.setInt(3, bd.getPrice());
                pt.setString(4, bd.getBookCategory());
                pt.setInt(5, bd.getAvailable());
                pt.setString(6, photoName);
                int i = pt.executeUpdate();
                if (i == 1) {
                    return photoName;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "no";
    }

    private String generateUniquePhotoName(String pre, StringBuilder post) {
        int randomNum = (int) (Math.random() * (999999999 - 111111111 + 1) + 111111111);
        return pre + randomNum + "." + post;
    }

    private boolean isPhotoNameExists(String photoName) throws SQLException {
        String query = "SELECT * FROM book WHERE photo = ?";
        try (PreparedStatement pt = conn.prepareStatement(query)) {
            pt.setString(1, photoName);
            try (ResultSet rs = pt.executeQuery()) {
                return rs.next();
            }
        }
    }

@Override
public List<BookDetail> showBook(int pageNo) {
    List<BookDetail> list = new ArrayList<>();
    try {
        String query = "SELECT * FROM book OFFSET ? ROWS FETCH NEXT 10 ROWS ONLY";
        try (PreparedStatement pt = conn.prepareStatement(query)) {
            pt.setInt(1, (pageNo - 1) * 10);
            try (ResultSet rs = pt.executeQuery()) {
                while (rs.next()) {
                    BookDetail bd = extractBookDetailFromResultSet(rs);
                    list.add(bd);
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}


    private BookDetail extractBookDetailFromResultSet(ResultSet rs) throws SQLException {
        BookDetail bd = new BookDetail();
        bd.setId(rs.getInt("bookId"));
        bd.setBookName(rs.getString("bookName"));
        bd.setAuthorName(rs.getString("authorName"));
        bd.setPrice(rs.getInt("price"));
        bd.setBookCategory(rs.getString("bookCategory"));
        bd.setAvailable(rs.getInt("available"));
        bd.setPhoto(rs.getString("photo"));
        return bd;
    }

    @Override
    public int bookCount() {
        int totalCount = 0;
        try {
            String query = "SELECT COUNT(*) AS total FROM book";
            try (PreparedStatement pt = conn.prepareStatement(query)) {
                try (ResultSet rs = pt.executeQuery()) {
                    if (rs.next()) {
                        totalCount = rs.getInt("total");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalCount;
    }

    @Override
    public void deleteBook(int id) {
        try {
            String query = "DELETE FROM book WHERE bookId = ?";
            try (PreparedStatement pt = conn.prepareStatement(query)) {
                pt.setInt(1, id);
                pt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BookDetail editBookDetail(int bookId) {
        BookDetail bd = null;
        try {
            String query = "SELECT * FROM book WHERE bookId = ?";
            try (PreparedStatement pt = conn.prepareStatement(query)) {
                pt.setInt(1, bookId);
                try (ResultSet rs = pt.executeQuery()) {
                    if (rs.next()) {
                        bd = extractBookDetailFromResultSet(rs);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bd;
    }

    @Override
    public String editBook(BookDetail bd) {
        try {
            String query = "UPDATE book SET bookName = ?, authorName = ?, price = ?, bookCategory = ?, available = ? WHERE bookId = ?";
            try (PreparedStatement pt = conn.prepareStatement(query)) {
                pt.setString(1, bd.getBookName());
                pt.setString(2, bd.getAuthorName());
                pt.setInt(3, bd.getPrice());
                pt.setString(4, bd.getBookCategory());
                pt.setInt(5, bd.getAvailable());
                pt.setInt(6, bd.getId());
                int i = pt.executeUpdate();
                if (i == 1) {
                    return "done";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "no";
    }
}
