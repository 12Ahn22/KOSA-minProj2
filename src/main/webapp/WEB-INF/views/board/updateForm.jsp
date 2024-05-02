<%--
  Created by IntelliJ IDEA.
  User: COM
  Date: 2024-05-01
  Time: 오후 6:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
	<title>RATTY | 게시글 수정</title>
	<%@include file="../include/bootStrap.jsp" %>
</head>
<body>
<%@include file="../include/header.jsp" %>
<main class="container">
	<input type="hidden" name="id" id="id" value="${board.id}">
	<input type="hidden" name="author" id="author" value="${board.author}">
	<label for="title">제목:</label><br>
	<input type="text" id="title" name="title" value="${board.title}"><br>
	<label>비밀번호:</label>
	<input type="password" id="password" name="password"><br>
	<label for="content">내용:</label><br>
	<textarea id="content" name="content" rows="4" cols="50">${board.content}</textarea><br><br>
	<input class="btn btn-primary" type="submit" value="수정">
	<a class="btn btn-secondary" href="javascript:history.back();">취소</a>
</main>
</body>
</html>