<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<head>
    <%@include file="parts/header.jsp"%>
    <spring:url value="/signUp/register" var="registerEndpoint"/>
</head>
<html lang="en">
<body>
    <div class="center">
        Sign Up Here
    </div>
    <form name='f' action="${registerEndpoint}" method='POST'>
        <table>
            <tr>
                <td>User:</td>
                <td><input type='text' name='username' value='${defaultUsername}'></td>
            </tr>
            <tr>
                <td>First Name:</td>
                <td><input type='text' name='firstName' value='${defaultFirstName}'/></td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><input type='password' name='password' /></td>
            </tr>
            <tr>
                <td>Confirm Password:</td>
                <td><input type='password' name='confirmPassword' /></td>
            </tr>
            <tr>
                <td><input name="submit" type="submit" value="submit" /></td>
            </tr>
        </table>
    </form>
</body>
</html>