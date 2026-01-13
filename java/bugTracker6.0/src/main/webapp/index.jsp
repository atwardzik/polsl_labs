<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Log In</title>
    <link rel="icon" type="image/x-icon" href="images/favicon.svg">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="css/main.css"/>
    <link rel="stylesheet" href="css/login.css"/>
    <script src="js/login.js"></script>
</head>
<body>
    <h1><%= "Bug Tracker" %></h1>
    <br/>
    Enter <a href="tracker.html">Issues List</a> without logging in.
    <br/><br/>

    <form class="loginForm">
        <div>
        <label><strong>Username:</strong></label>
        <input type="text" placeholder="Enter Username" class="loginUsername">
        </div>

        <div>
        <label><strong>Password:</strong></label>
        <input type="password" placeholder="Enter Password" class="loginPassword">
        </div>

        <div class="message"></div>

        <div><button type="submit" class="modifier" onclick="login(event);">Log In</button></div>
    </form>
</body>
</html>