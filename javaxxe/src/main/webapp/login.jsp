<%--
  Created by IntelliJ IDEA.
  User: JoeZhou
  Date: 2021/10/19
  Time: 11:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Login Page</title>
    <link rel="stylesheet" href="static/css/bootstrap.min.css">
    <style>h2, h4 {
        font-family: 'Alegreya Sans', sans-serif;
    }</style>
</head>

<body class="bg-light">
<div class="container">
    <div class="py-5 text-center">
        <h2>Login</h2>
        <p class="lead"></p>
    </div>

    <div class="row">
        <div class="col-md-12 order-md-1">
            <form action="<%= request.getContextPath() %>/login" method="post">
                <% if (request.getAttribute("error") != null) {
                    out.println("<div class=\"alert alert-danger form-control\">" + request.getAttribute("error") + "</div>");
                }%>
                <h4 class="mb-3">Login</h4>
                <div class="mb-3">
                    <label for="username">Username</label>
                    <input type="passport" class="form-control" id="username" name="username" placeholder="Username"
                           required>
                </div>
                <div class="mb-3">
                    <label for="password">Password</label>
                    <input type="passport" class="form-control" id="password" name="password" placeholder="Password"
                           required>
                </div>
                <hr class="mb-4">
                <button class="btn btn-primary btn-lg btn-block" type="submit">Login</button>
                <hr class="mb-4">
                <div id="checkin-result"></div>
            </form>
        </div>
    </div>

    <footer class="my-5 pt-5 text-muted text-center text-small">
        <ul class="list-inline">
            <li class="list-inline-item"><a href="#">Privacy</a></li>
            <li class="list-inline-item"><a href="#">Terms</a></li>
            <li class="list-inline-item"><a href="#">Support</a></li>
        </ul>
    </footer>
</div>
</body>
</html>