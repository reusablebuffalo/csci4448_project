<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<head>
    <spring:url value="/resources/css/style.css" var="styleCSS" />
    <spring:url value="/logout" var="logoutEndpoint" />
    <link href="${styleCSS}" rel="stylesheet" />
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger" role="alert">
            <strong>Warning!</strong> ${errorMessage}
        </div>
    </c:if>
    <c:if test="${not empty infoMessage}">
        <div class="alert alert-info" role="alert">
            <strong>Info!</strong> ${infoMessage}
        </div>
    </c:if>
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success" role="alert">
            <strong>Success!</strong> ${successMessage}
        </div>
    </c:if>
    <a href="${logoutEndpoint}"> Logout </a>
</head>