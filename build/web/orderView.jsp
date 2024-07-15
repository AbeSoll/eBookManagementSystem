<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.detail.OrderCartList"%>
<%@page import="com.detail.OrderListDetail"%>
<%@page import="com.dao.OrderDAO"%>

<%
    if (session.getAttribute("userL") == null) {
        response.sendRedirect("./index.jsp");
    } else {
        UserDetail ud3 = (UserDetail) session.getAttribute("userL");
        int orderId = 0;
        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
        } catch (Exception e) {
            orderId = 0;
        }
        OrderDAO orderDAO = new OrderDAO(DBConnect.getConnection());
        OrderListDetail old = orderDAO.getOrderView(ud3.getId(), orderId);
        if (old == null) {
            response.sendRedirect("./index.jsp");
        } else {
%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="./component/header.jsp" %>
        <title>My Order | Book Store</title>
        <style>
            .order-details {
                background-color: #f8f9fa;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            }
            .order-summary {
                margin-bottom: 20px;
            }
            .order-summary h5 {
                margin-bottom: 15px;
                font-weight: bold;
            }
            .order-summary .detail {
                margin-bottom: 10px;
                line-height: 1.5;
            }
            .order-summary .detail span {
                font-weight: bold;
            }
            .book-list table {
                width: 100%;
                background-color: #ffffff;
                border: 1px solid #dee2e6;
                border-radius: 5px;
                overflow: hidden;
            }
            .book-list th, .book-list td {
                padding: 10px;
                text-align: left;
            }
            .book-list th {
                background-color: #343a40;
                color: #ffffff;
            }
            .book-list td {
                border-bottom: 1px solid #dee2e6;
            }
            .book-list tr:last-child td {
                border-bottom: none;
            }
        </style>
    </head>
    <body>
        <%@include file="./component/navbar.jsp" %>
        
        <div class="container mt-5">
            <div class="row justify-content-center">
                <div class="col-md-10">
                    <div class="order-details">
                        <div class="order-summary">
                            <center><h5>Order Summary</h5></center>
                            <div class="detail"><span>Order No:</span> <%= old.getOrderID() %></div>
                            <div class="detail"><span>Total Order Price:</span> RM <%= old.getPrice() %></div>
                            <div class="detail"><span>Order Date:</span> <%= old.getDate() %></div>
                            <div class="detail"><span>Payment Mode:</span> <%= old.getPaymentMethod() %></div>
                            <div class="detail"><span>Status:</span> <%= old.getStatus() %></div>
                            <div class="detail"><span>Name:</span> <%= old.getName() %></div>
                            <div class="detail"><span>Phone:</span> <%= old.getPhone() %></div>
                            <div class="detail"><span>Address Line 1:</span> <%= old.getAddress1() %></div>
                            <div class="detail"><span>Address Line 2:</span> <%= old.getAddress2() %></div>
                            <div class="detail"><span>Landmark:</span> <%= old.getLandmark() %></div>
                            <div class="detail"><span>City:</span> <%= old.getCity() %></div>
                            <div class="detail"><span>Pin Code:</span> <%= old.getPinCode() %></div>
                        </div>

                        <div class="book-list">
                            <h5 class="text-center">Book List</h5>
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>Book Name</th>
                                        <th>Author Name</th>
                                        <th>Quantity</th>
                                        <th>Price (RM)</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        for (OrderCartList ocl : old.getOcl()) {
                                    %>
                                    <tr>  
                                        <td><%= ocl.getBookName() %></td>
                                        <td><%= ocl.getAuthorName() %></td>
                                        <td><%= ocl.getQuantity() %></td>
                                        <td><%= ocl.getPrice() %></td>
                                    </tr>
                                    <%
                                        }
                                    %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <script>
            $("#searchBook").attr("action","./newBook.jsp");
            $("#searchBook2").attr("action","./newBook.jsp");
        </script>
    </body>
</html>
<%
        }
    }
%>
