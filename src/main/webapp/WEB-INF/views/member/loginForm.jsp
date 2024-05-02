<%--
  Created by IntelliJ IDEA.
  User: COM
  Date: 2024-04-29
  Time: 오후 1:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>RATTY | LOGIN</title>
    <%@include file="../include/bootStrap.jsp" %>
    <%@include file="../include/meta.jsp" %>
</head>
<body>
<%@include file="../include/header.jsp" %>
<main class="container">
    <h1>LOGIN</h1>
    <c:if test="${error}"><span class="text-danger">${exception}</span></c:if>

    <form id="loginForm" method="post" action="/member/login">
        <div>
            <label for="id">아이디:</label>
            <input type="text" id="id" name="id" required>
        </div>
        <div>
            <label for="password">비밀번호:</label>
            <input type="password" id="password" name="password" required>
        </div>
        <input class="btn btn-primary" type="submit" value="로그인">
        <div>
            <label for="autoLogin">자동로그인체크</label>
            <input type="checkbox" name="autoLogin" id="autoLogin" value="Y">
        </div>
    </form>
</main>
</body>
</html>