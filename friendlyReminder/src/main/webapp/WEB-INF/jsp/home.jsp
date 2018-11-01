<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<head>
<spring:url value="/resources/css/style.css" var="styleCSS" />
<spring:url value="/resources/images/friends_on_phone.jpg" var="backgroundImage"/>
<link href="${styleCSS}" rel="stylesheet" />
</head>
<style>
    body {
        background: url("${backgroundImage}") no-repeat;
        background-size: 100%;
    }
</style>
<html lang="en">
    <body title="Background Photo by rawpixel on Unplash">
        <div class="center">
            <h1>Friendly Reminder</h1>
            <p>Don't forget to call the people that matter!</p>
            <h2>An App by ${author}</h2>
        </div>
    </body>
</html>