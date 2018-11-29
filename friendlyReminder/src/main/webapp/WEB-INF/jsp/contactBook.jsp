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
    <spring:url value="/users/openBook/changeSort" var="changeStrategyEndpoint"/>
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
            <th>Sort By
                <form action="${changeStrategyEndpoint}" method="GET">
                    <input type=hidden name="contactBookId" value="${contactBook.id}"/>
                    <select name="sortingStrategy" onchange="this.form.submit()">
                        <c:forEach items="${sortStrategies}" var="strategy">
                            <c:choose >
                                <c:when test="${contactBook.sortingStrategy == strategy}">
                                    <option value="${strategy}" selected><c:out value="${strategy.alternateStrategyName}"/></option>
                                </c:when>
                                <c:when test="${contactBook.sortingStrategy != strategy}">
                                    <option value="${strategy}"><c:out value="${strategy.alternateStrategyName}"/></option>
                                </c:when>
                            </c:choose>
                        </c:forEach>
                    </select>
                </form>
            </th>
        </tr>
        <c:forEach var="contact" items="${contactBook.contactList}">
            <tr>
                <td> <c:out value="${contact.firstName}"/> <c:out value="${contact.lastName}"/> </td>
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