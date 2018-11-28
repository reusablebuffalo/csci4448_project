<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<head>
    <%@include file="parts/header.jsp"%>
    <spring:url value="/resources/images/friends_on_phone.jpg" var="backgroundImage"/>
    <spring:url value="/signUp" var="signUpEndpoint"/>
    <spring:url value="/login" var="loginEndpoint"/>
</head>
<style>
    body {
        background: url("${backgroundImage}") no-repeat;
        background-size: 100%;
    }
</style>
<html lang="en">
    <body title="Background Photo by rawpixel on Unplash">
        <div id="homepage" class="center">
            <h1>Friendly Reminder</h1>
            <p>Don't forget to call the people that matter!</p>
            <h2>An App by Ian Smith</h2>
            <a href="${signUpEndpoint}">Sign Up</a>
            <a href="${loginEndpoint}">Login</a>
        </div>
    </body>
</html>