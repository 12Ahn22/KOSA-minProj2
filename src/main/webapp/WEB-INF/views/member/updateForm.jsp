<%--
  Created by IntelliJ IDEA.
  User: COM
  Date: 2024-04-29
  Time: 오후 5:32
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
    <title>RATTY | 회원 수정</title>
    <%@include file="../include/bootStrap.jsp" %>
</head>
<body>
<jsp:include page="../include/header.jsp"/>
<main class="container">
    <h1>회원 정보 수정</h1>
    <h2>${principal.id}</h2>
    <form id="uForm">
        <div>
            <label for="name">이름:</label>
            <input type="text" id="name" name="name" value="${principal.name}" required>
        </div>
        <div>
            <label for="password">비밀번호:</label>
            <input type="password" id="password" name="password" required autocomplete="off">
            <br/>
            <label for="password2">비밀번호확인:</label>
            <!-- 서버로 보내지 않을 내용은 name을 써주지않는다. -->
            <input type="password" id="password2" required autocomplete="off">
        </div>
        <div>
            <label for="phone">전화번호:</label>
            <input type="tel" id="phone" name="phone" value="${principal.phone}" required>
        </div>
        <div>
            <label for="address">주소:</label>
            <input type="text" id="address" name="address" value="${principal.address}" required>
        </div>
        <div>
            <label for="birthdate">생년월일:</label>
            <input type="date" id="birthdate" name="birthdate" value="${principal.birthdate}" required>
        </div>
        <div>
            <label>성별:</label>
            <input type="radio" id="female" name="gender" value="F" ${principal.gender.equals('F') ? 'checked'
                    :''} disabled>
            <label for="female">여성</label>
            <input type="radio" id="male" name="gender" value="M" ${principal.gender.equals('F') ? '' :'checked'}
                   disabled>
            <label for="male">남성</label>
        </div>
        <div>
            <%--                <label>취미:</label>--%>
            <%--                <c:forEach var="hobby" items="${hobbyList}">--%>
            <%--                    <input type="checkbox" id="${hobby.id}" name="mapHobbies" value="${hobby.hobby}">--%>
            <%--                    <label for="${hobby.id}">${hobby.hobby}</label>--%>
            <%--                </c:forEach>--%>
        </div>
        <input class="btn btn-primary" type="submit" value="생성"/>
        <a class="btn btn-secondary" href="/member/profile">취소</a>
    </form>
</main>
<script type="text/javascript" src="/js/common.js"></script>
</body>
</html>