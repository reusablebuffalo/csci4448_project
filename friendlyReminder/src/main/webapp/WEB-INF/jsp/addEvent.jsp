<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<head>
    <%@include file="parts/header.jsp"%>
    <spring:url value="/users/openContact" var="goBackEndpoint">
        <spring:param name="contactId" value="${contactId}"/>
        <spring:param name="contactBookId" value="${contactBookId}"/>
    </spring:url>
    <spring:url value="/users/addEvent.do" var="addEventEndpoint">
        <spring:param name="contactId" value="${contactId}"/>
        <spring:param name="contactBookId" value="${contactBookId}"/>
    </spring:url>
</head>
<html lang="en">
<body>
<div class="center">

</div>
<form name='f' id='eventForm' action="${addEventEndpoint}" method='POST'>
    <table>
        <tr>
            <td>Date:</td>
            <td><input type="date" name="eventDate"
                       value=""
                       min="2018-01-01"/></td>
        </tr>
        <tr>
            <td>Notes:</td>
            <td><input type='text' name='eventNotes' value=''/></td>
        </tr>
        <tr>
            <td><input name="submit" type="submit" value="Add"/></td>
            <td><a href="${goBackEndpoint}">Go Back</a></td>
        </tr>
    </table>
    Communication Type:
    <select name='eventType' form='eventForm'>
        <c:forEach items="${communicationTypes}" var="communicationType">
                <option value="${communicationType}">${communicationType}</option>
        </c:forEach>
    </select>
</form>
</body>
</html>