$(document).ready(function(){
    $("#registerForm").on("submit", function(event){
        event.preventDefault();
        var f = $(this).serialize();
        $.ajax({
            url: "UserRegistrationServlet",
            data: f,
            method: "post",
            success: function(data){
                if (data.trim() === "done") {
                    location.reload();
                } else if (data.trim() === "alreadyUser") {
                    $("#rStatus").show().html("User already exists!");
                } else {
                    $("#rStatus").show().html("Something went wrong!");
                }
            },
            error: function(){
                $("#rStatus").show().html("Something went wrong!");
            }
        });
    });

    $("#loginForm").on("submit", function(event){
        event.preventDefault();
        var f = $(this).serialize();
        $.ajax({
            url: "UserLoginServlet",
            data: f,
            method: "post",
            success: function(data){
                if (data.trim() === "Login Successful") {
                    window.location.href = "index.jsp";
                } else if (data.trim() === "Invalid Credentials") {
                    $("#lStatus").show().html("Please provide valid credentials!");
                } else {
                    $("#lStatus").show().html("Login Failed");
                }
            },
            error: function(){
                $("#lStatus").show().html("Something went wrong!");
            }
        });
    });
});
