<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<head>
    <%@include file="parts/header.jsp"%>
    <spring:url value="/users/addBook" var = "addBookEnpoint"/>
</head>
<html lang="en">
<body>
<div class="center">
    Welcome, ${firstName}!
    <h4>Contact Books</h4>
        <table>
            <tr>
                <th>Contact Book Name</th>
                <th>Option1</th>
                <th>Option2</th>
            </tr>
            <c:forEach var="contactBook" items="${contactBooks}">
                <tr>
                    <td> <c:out value ="${contactBook.name}"/> </td>
                    <td> <a href="">Delete</a> </td>
                    <td> <a href="">Update</a> </td>
                    <td> <a href="">Open</a> </td>
                </tr>
            </c:forEach>
        </table>
        <a href="${addBookEnpoint}">Create New Contact Book</a>
</div>
</body>
</html>