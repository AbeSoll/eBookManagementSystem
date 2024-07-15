<%-- 
    Document   : index
    Created on : 31-May-2021, 9:09:14 PM
    Author     : chetan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.detail.BookDetail"%>
<%@page import="java.util.List"%>
<%@page import="com.database.DBConnect"%>
<%@page import="com.dao.admin.BookDAO"%>

<%
// Check if the admin session attribute is null, then redirect to adminLogin.jsp
if(session.getAttribute("admin")==null){
    response.sendRedirect("./adminLogin.jsp");
} else {
    // Page number for pagination, default is 1
    int pageNo = 1;
    try {
        // Try to parse the pageNo parameter from the request
        pageNo = Integer.parseInt(request.getParameter("pageNo"));
    } catch(Exception e) {
        e.printStackTrace(); // Print stack trace for debugging
    }

    // Create a BookDAO instance with a database connection
    BookDAO dao = new BookDAO(DBConnect.getConnection());
    // Get the list of books for the current page
    List<BookDetail> list = dao.showBook(pageNo);
    // Get the total number of pages for pagination
    int totalPage = dao.bookCount();
%>

<!DOCTYPE html>
<html>
<head>
    <%@include file="adminHead.jsp" %> <!-- Include adminHead.jsp for common head content -->
    <title>Admin Panel | Book Store</title>
    <!-- DataTables CSS -->
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.21/css/jquery.dataTables.css">
    <!-- jQuery -->
    <script type="text/javascript" charset="utf8" src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <!-- DataTables JS -->
    <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.21/js/jquery.dataTables.js"></script>
    <script>
        $(document).ready( function () {
            $('#bookTable').DataTable({
                "lengthMenu": [ [10, 25, 50, -1], [10, 25, 50, "All"] ]
            });
        });

        function deleteBook(bookId) {
            if (confirm("Are you sure you want to delete this book?")) {
                window.location.href = "../AdminDeleteBookServlet?id=" + bookId;
            }
        }
    </script>
</head>
<body>
    <%@include file="adminNavbar.jsp" %> <!-- Include adminNavbar.jsp for navigation -->

    <div class="container-fluid mt-2 mb-5">
        <div class="row mb-1">
            <div class="col-12 text-center">
                <h3>All Books</h3>
            </div>
        </div>
        <div class="row">
            <div class="col-12 table-responsive">
                <table id="bookTable" class="display">
                    <thead class="thead-dark-custom">
                        <tr>
                            <td>Id</td>
                            <td>Book Name</td>
                            <td>Author Name</td>
                            <td>Price (RM)</td>
                            <td>Book Categories</td>
                            <td>Availability</td>
                            <td>Action</td>
                        </tr>
                    </thead>
                    <tbody>
                        <% // Loop through the list of books and display them in table rows
                        for(BookDetail bd: list) {
                        %>
                            <tr>
                                <td><h6><%= bd.getId() %></h6></td>
                                <td>
                                    <div style="display: flex;">
                                        <img width="50px" class="img-fluid mr-1" src="../img/books-img/<%= bd.getPhoto() %>" />
                                        <span><%= bd.getBookName() %></span>
                                    </div>
                                </td>
                                <td><%= bd.getAuthorName() %></td>
                                <td><%= bd.getPrice() %></td>
                                <td><%= bd.getBookCategory() %></td>
                                <td><%= bd.getAvailable() %></td>
                                <td>
                                    <div style="display:flex">
                                        <a class="btn btn-primary btn-sm mr-2" href="./adminEditBook.jsp?bookId=<%= bd.getId() %>" >Edit</a>
                                        <a class="btn btn-danger btn-sm" href="javascript:void(0);" onclick="deleteBook(<%= bd.getId() %>)">Delete</a>
                                    </div>
                                </td>
                            </tr>
                        <% } // End of for loop %>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="row mt-3 mb-2"> 
            <div class="col-12 text-center">
                <label>Page</label>
            </div>
            <div class="col-12 text-center">
                <nav>
                    <ul class="pagination pagination-sm justify-content-center">
                        <% // Pagination logic to display page numbers
                        int pageN = 0;
                        while(totalPage > 0) {
                            pageN+=1;
                            if(pageN == pageNo){
                        %>
                                <li class="page-item active">
                                    <span class="page-link" ><%= pageN %></span>
                                </li>
                        <% } else { %>
                                <li class="page-item">
                                    <a class="page-link" href="./adminAllBook.jsp?pageNo=<%= pageN %>" >
                                        <%= pageN %>
                                    </a>
                                </li>
                        <% }
                            totalPage -= 10; // Adjust totalPage for next page
                        } // End of while loop %>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
</body>
</html>

<% } // End of else block %>