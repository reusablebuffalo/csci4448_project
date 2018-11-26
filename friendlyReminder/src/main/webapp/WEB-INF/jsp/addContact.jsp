<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<head>
    <%@include file="parts/header.jsp"%>
    <spring:url value="/users/openBook" var="goBackEndpoint">
        <spring:param name="contactBookId" value="${contactBookId}"/>
    </spring:url>
</head>
<html lang="en">
<body>
<div class="center">
    <c:choose>
        <c:when test="${newContact eq true}">
            <spring:url value="/users/addContact.do" var="addContactEndpoint">
                <spring:param name="contactBookId" value="${contactBookId}"/>
            </spring:url>
            Add a New Contact!
        </c:when>
        <c:when test="${newContact eq false}">
            <spring:url value="/users/updateContact.do" var="addContactEndpoint">
                <spring:param name="contactBookId" value="${contactBookId}"/>
                <spring:param name="contactId" value="${contactId}"/>
            </spring:url>
            Update Contact!
        </c:when>
    </c:choose>

</div>
<form name='f' id='contactForm' action="${addContactEndpoint}" method='POST'>
    <table>
        <tr>
            <td>First Name:</td>
            <td><input type='text' name='contactFirstName' value='${contact.firstName}'></td>
        </tr>
        <tr>
            <td>Last Name:</td>
            <td><input type='text' name='contactLastName' value='${contact.lastName}'></td>
        </tr>
        <tr>
            <td>Notes:</td>
            <td><input type='text' name='contactNotes' value='${contact.notes}'></td>
        </tr>
        <tr>
            <td>Email Address:</td>
            <td><input type='text' name='contactEmailAddress' value='${contact.emailAddress}'></td>
        </tr>
        <tr>
            <td>Phone Number:</td>
            <td><input type='text' name='contactPhoneNumber' value='${contact.phoneNumber}'></td>
        </tr>
        <tr>
            <td><input name="submit" type="submit" value="submit" /></td>
            <td><a href="${goBackEndpoint}">Go Back</a></td>
        </tr>
    </table>
</form>
Relative Importance:
<select name='importance' form='contactForm'>
    <c:if test="${contact.relativeImportance != null}">
        <option value="${contact.relativeImportance}" selected>${contact.relativeImportance}</option>
    </c:if>
    <c:forEach items="${roles}" var="role">
        <c:if test="${role != contact.relativeImportance}">
            <option value="${role}">${role}</option>
        </c:if>
    </c:forEach>
</select>
<%--<select name="importance" form='contactForm'>--%>
    <%--<option value="HIGH">High</option>--%>
    <%--<option value="MEDIUM">Medium</option>--%>
    <%--<option value="LOW">Low</option>--%>
<%--</select>--%>
</body>
</html>