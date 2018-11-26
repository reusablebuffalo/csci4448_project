<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<head>
    <%@include file="parts/header.jsp"%>
    <spring:url value="/users/addBook" var = "addBookEndpoint"/>
    <spring:url value="/users/deleteBook" var = "deleteBookBaselink"/>
    <spring:url value="/users/openBook" var = "openBookBaselink"/>
</head>
<html lang="en">
<body>
<div class="center">
    Welcome, ${firstName}!
    <h4>Contact Books</h4>
        <table border="1">
            <tr>
                <th>Contact Book Name</th>
                <th>Options</th>
            </tr>
            <c:forEach var="contactBook" items="${contactBooks}">
                <tr>
                    <c:url var="deleteBookLink" value="${deleteBookBaselink}">
                        <c:param name="contactBookId" value="${contactBook.id}"/>
                    </c:url>
                    <c:url var="openBookLink" value="${openBookBaselink}">
                        <c:param name="contactBookId" value="${contactBook.id}"/>
                    </c:url>
                    <td> <c:out value ="${contactBook.name}"/> </td>
                    <td> <a href="${deleteBookLink}"> Delete </a> <a href="${openBookLink}"> Open </a> </td>
                </tr>
            </c:forEach>
        </table>
        <a href="${addBookEndpoint}">Create New Contact Book</a>
</div>
</body>
</html>