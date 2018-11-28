<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<head>
    <%@include file="parts/header.jsp"%>
    <spring:url value="/home" var = "homeEndpoint"/>
    <spring:url value="/users/addContact" var = "addContactEndpoint">
        <spring:param name="contactBookId" value="${contactBook.id}"/>
    </spring:url>
    <spring:url value="/users/deleteContact" var = "deleteContactBase"/>
    <spring:url value="/users/openContact" var = "openContactBase"/>
    <spring:url value="/users/updateContact" var = "updateContactBase"/>
</head>
<html lang="en">
<body>
<div class="center">
    Hello, ${firstName}!
    <h4>Contacts Inside Book:${contactBook.name}</h4>
    <table border="1">
        <tr>
            <th>Contact</th>
            <th>Last Contact Date</th>
            <th>Options</th>
            <th>Sort By</th>
        </tr>
        <c:forEach var="contact" items="${contactBook.contactList}">
            <tr>
                <td> <c:out value="${contact.firstName}"/> </td>
                <td> <c:out value="${contact.lastContactDate}"/> </td>
                <c:url var="deleteContactLink" value="${deleteContactBase}">
                    <c:param name="contactId" value="${contact.id}"/>
                    <c:param name="contactBookId" value="${contactBook.id}"/>
                </c:url>
                <c:url var="openContactLink" value="${openContactBase}">
                    <c:param name="contactId" value="${contact.id}"/>
                    <c:param name="contactBookId" value="${contactBook.id}"/>
                </c:url>
                <c:url var="updateContactLink" value="${updateContactBase}">
                    <c:param name="contactId" value="${contact.id}"/>
                    <c:param name="contactBookId" value="${contactBook.id}"/>
                </c:url>
                <td> <a href="${openContactLink}"> Open </a> <a href="${updateContactLink}"> Update </a> <a href="${deleteContactLink}"> Delete </a> </td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="2"> <a href="${addContactEndpoint}">Add New Contact</a></td>
            <td colspan="2"> <a href="${homeEndpoint}"> Go Back </a> </td>
        </tr>
    </table>

</div>
</body>
</html>