<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<head>
    <%@include file="parts/header.jsp"%>
    <spring:url value="/users/addBook.do" var="addBookEndpoint"/>
    <spring:url value="/home" var="homeEndpoint"/>
</head>
<html lang="en">
<body>
<div class="center">
    Create a New Contact Book
</div>
<form name='f' action="${addBookEndpoint}" method='POST'>
    <table>
        <tr>
            <td>Name:</td>
            <td><input type='text' name='contactBookName' value=''></td>
        </tr>
        <tr>
            <td><input name="submit" type="submit" value="submit" /></td>
            <td><a href="${homeEndpoint}">Go Back</a></td>
        </tr>
    </table>
</form>
</body>
</html>