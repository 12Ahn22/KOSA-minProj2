<%--
  Created by IntelliJ IDEA.
  User: COM
  Date: 2024-04-29
  Time: 오후 5:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="isAuthenticated()">
    <sec:authentication property="principal" var="principal"/>
</sec:authorize>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>RATTY | PROFILE</title>
    <%@include file="../include/bootStrap.jsp" %>
</head>
<body>
<%@include file="../include/header.jsp" %>
<main class="container">
    <input type="hidden" id="memberId" value="${principal.id}"/>
    <h1>${principal.id}</h1>
    <div><span>이름:</span><span>${principal.name}</span></div>
    <div><span>성별:</span><span>${principal.gender}</span></div>
    <div><span>전화번호:</span><span>${principal.phone}</span></div>
    <div><span>주소:</span><span>${principal.address}</span></div>
    <div><span>취미:</span>
<%--        <c:forEach var="hobby" items="${member.hobbies}">--%>
<%--            <span>${hobby.hobby}</span>--%>
<%--        </c:forEach>--%>
    </div>
    <div>
        <a href="update?id=${member.id}">수정</a>
        <button id="deleteBtn">삭제</button>
    </div>
</main>
</body>
</html>