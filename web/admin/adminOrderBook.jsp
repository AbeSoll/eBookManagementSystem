<%@page import="com.dao.admin.AdminOrderDAO"%>
<%@page import="java.util.List"%>
<%@page import="com.database.DBConnect"%>
<%@page import="com.detail.OrderListDetail"%>
<%
    if(session.getAttribute("admin")==null){
        response.sendRedirect("./adminLogin.jsp");
    } else {
        AdminOrderDAO orderDAO = new AdminOrderDAO(DBConnect.getConnection());
        List<OrderListDetail> list = orderDAO.getOrderList();
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="adminHead.jsp" %>
        <title>Admin Panel | Book Store</title>
        <!-- DataTables CSS -->
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.11.3/css/jquery.dataTables.css">
        <!-- jQuery -->
        <script type="text/javascript" charset="utf8" src="https://code.jquery.com/jquery-3.5.1.js"></script>
        <!-- DataTables JS -->
        <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.11.3/js/jquery.dataTables.js"></script>
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <!-- Bootstrap JS -->
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </head>
    <body>
        <%@include file="adminNavbar.jsp" %>
        
        <div class="container-fluid mt-2 mb-5">
            <div class="row mb-1">
                <div class="col-12 text-center">
                    <h3>Order List</h3>
                </div>
            </div>
            <%
                if(!list.isEmpty()) {
            %>
                    <div class="row">
                        <div class="col-12 table-responsive">
                            <table id="orderTable" class="table table-striped">
                                <thead>
                                    <tr>
                                        <td>No.</td>
                                        <td>Order No</td>
                                        <td>Price (RM)</td>
                                        <td>Time</td>
                                        <td>Payment Method</td>
                                        <td>Status</td>
                                        <td>View</td>
                                        <td>Delivered</td>
                                    </tr>
                                </thead>
                                <tbody>
                                <%
                                    int srNo = 0;
                                    for(OrderListDetail cd : list) {
                                        srNo += 1;
                                %>
                                    <tr>
                                        <td><%= srNo %></td>
                                        <td><%= cd.getOrderID() %></td>
                                        <td><%= cd.getPrice() %>
                                        </td>
                                        <td><%= cd.getDate() %></td>
                                        <td><%= cd.getPaymentMethod() %></td>
                                        <td><%= cd.getStatus() %></td>
                                        <td>
                                            <a class="btn btn-primary btn-sm" target="_blank" href="./adminOrderView.jsp?orderId=<%= cd.getOrderID() %>" >
                                               <i class="fas fa-external-link-alt"></i> View
                                            </a>
                                        </td>
                                        <td>
                                            <button class="btn btn-success btn-sm" onclick="showConfirmationModal(<%= cd.getOrderID() %>)">
                                                Delivered
                                            </button>
                                        </td>
                                    </tr>
                                <%
                                    }
                                %>
                                </tbody>
                            </table>
                        </div>    
                    </div>
            <%
                } else {
            %>
            <div class="row">
                <div class="col-12 text-center text-warning">
                    <h1><i class="far fa-frown"></i></h1>
                    <h3>Empty Order List.</h3>
                </div>
            </div>
            <%
                }
            %>
        </div>

        <!-- Bootstrap Modal -->
        <div class="modal fade" id="confirmationModal" tabindex="-1" role="dialog" aria-labelledby="confirmationModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="confirmationModalLabel">Confirm Delivery</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        Are you sure you want to mark this order as delivered?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                        <button type="button" class="btn btn-success" id="confirmDeliveryButton">Yes, Deliver</button>
                    </div>
                </div>
            </div>
        </div>

        <script>
            let currentOrderId;

            function showConfirmationModal(orderId) {
                currentOrderId = orderId;
                $('#confirmationModal').modal('show');
            }

            $('#confirmDeliveryButton').click(function() {
                deliveredOrder(currentOrderId);
            });

            function deliveredOrder(orderId) {
                $.ajax({
                    url: "../DeliveredOrderServlet?orderId=" + orderId,
                    method: "get",
                    success: function(data){
                        if(data.trim() === "done"){
                            $('#confirmationModal').modal('hide');
                            alert("The parcel has been delivered!");
                            location.reload();
                        } else {
                            alert("Something went wrong!");
                        }
                    },
                    error: function() {
                        alert("Something went wrong!");
                    }
                });
            }

            $(document).ready(function() {
                $('#orderTable').DataTable();
            });
        </script>
    </body>
</html>
<%
    }
%>
