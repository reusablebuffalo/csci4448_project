<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<head>
    <%@include file="parts/header.jsp"%>
    <spring:url value="/login/validate" var="loginValidationEndpoint"/>
</head>
<html lang="en">
<body>
<div class="center">
    User Login
</div>
<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger" role="alert">
        <strong>Warning!</strong> ${errorMessage}
    </div>
</c:if>
<form name='f' action="${loginValidationEndpoint}" method='POST'>
    <table>
        <tr>
            <td>User:</td>
            <td><input type='text' name='username' value='${defaultUsername}'></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input type='password' name='password' /></td>
        </tr>
        <tr>
            <td><input name="submit" type="submit" value="submit" /></td>
        </tr>
    </table>
</form>
</body>
</html>