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
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>RATTY | PROFILE</title>
    <%@include file="../include/bootStrap.jsp" %>
    <%@include file="../include/meta.jsp" %>
</head>
<body>
<%@include file="../include/header.jsp" %>
<main class="container">
    <input type="hidden" id="memberId" value="${member.id}"/>
    <h1>${member.id}</h1>
    <div><span>이름:</span><span>${member.name}</span></div>
    <div><span>성별:</span><span>${member.gender}</span></div>
    <div><span>전화번호:</span><span>${member.phone}</span></div>
    <div><span>주소:</span><span>${member.address}</span></div>
    <div><span>취미:</span>
        <%--        <c:forEach var="hobby" items="${member.hobbies}">--%>
        <%--            <span>${hobby.hobby}</span>--%>
        <%--        </c:forEach>--%>
    </div>
    <div>
        <a href="update" class="btn btn-primary">수정</a>
        <button id="deleteBtn" class="btn btn-secondary">삭제</button>
    </div>
</main>
<script type="text/javascript" src="/js/common.js"></script>
<script>
    const deleteBtn = document.getElementById("deleteBtn");
    const memberId = document.getElementById("memberId").value;
    console.log("memberId", memberId)
    deleteBtn.addEventListener("click", () => {
        if (confirm("탈퇴하시겠습니까?")) {
            myFetch("delete", {
                id: memberId
            }, (data) => {
                if (data.status === 204) {
                    alert("회원 탈퇴에 성공했습니다.");
                    // 페이지 리다이렉트
                    location = "/";
                } else {
                    alert(data.statusMessage);
                }
            })
        }
    })
</script>
</body>
</html>