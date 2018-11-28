<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<head>
    <%@include file="parts/header.jsp"%>
    <spring:url value="/users/openBook" var="goBackEndpoint">
        <spring:param name="contactBookId" value="${contactBookId}"/>
    </spring:url>
    <spring:url value="/users/addEvent" var = "addEventEndpoint">
        <spring:param name="contactBookId" value="${contactBookId}"/>
        <spring:param name="contactId" value="${contact.id}"/>
    </spring:url>
    <spring:url value="/users/deleteEvent" var = "deleteEventBase"/>
</head>
<html lang="en">
<body>
<div class="center">
    <h5>Contact Info for ${contact.firstName} ${contact.lastName}</h5>
    <table align="center" border="1">
        <tr>
            <td>First Name:</td>
            <td>${contact.firstName}</td>
        </tr>
        <tr>
            <td>Last Name:</td>
            <td>${contact.lastName}</td>
        </tr>
        <tr>
            <td>Notes:</td>
            <td>${contact.notes}</td>
        </tr>
        <tr>
            <td>Email Address:</td>
            <td>${contact.emailAddress}</td>
        </tr>
        <tr>
            <td>Phone Number:</td>
            <td>${contact.phoneNumber}</td>
        </tr>
        <tr>
            <td>Relative Importance:</td>
            <td>${contact.relativeImportance}</td>
        </tr>
    </table>
</div>
<div class="center">
        <h5>Communication Events</h5>
    <table align="center" border="1">
        <tr>
            <th>Date</th>
            <th>Type</th>
            <th>Notes</th>
            <th>Options</th>
        </tr>
        <c:forEach var="event" items="${contact.communicationEvents}">
            <tr>
                <td> ${event.dateAsString} </td>
                <td> ${event.communicationType} </td>
                <td>${event.note}</td>
                <c:url var="deleteEventLink" value="${deleteEventBase}">
                    <c:param name="eventId" value="${event.id}"/>
                    <c:param name="contactId" value="${contact.id}"/>
                    <c:param name ="contactBookId" value="${contactBookId}"/>
                </c:url>
                <td> <a href="${deleteEventLink}"> Delete </a> </td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="2"> <a href="${addEventEndpoint}">Add New Communication Event</a></td>
            <td colspan="2"> <a href="${goBackEndpoint}"> Go Back </a> </td>
        </tr>
    </table>
</div>
</body>
</html>