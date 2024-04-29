<%--
  Created by IntelliJ IDEA.
  User: COM
  Date: 2024-04-29
  Time: 오전 10:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>RATTIES | 내 작은 친구</title>
    <%@include file="./include/bootStrap.jsp" %>
</head>
<body>
    <%@include file="./include/header.jsp" %>
    <main class="container">
        <section id="about">
            <h2>쥐에 대해</h2>
            <p>애완 쥐는 사람들 사이에서 매우 인기 있는 애완동물 중 하나입니다. 이들은 친화적이고 호기심이 많으며 관찰하기 쉬운 특성을 가지고 있습니다. 쥐는 작은 사이즈로 인해 집안 곳곳에서 키울 수 있고, 적은 양의 식량과 공간만 필요합니다.</p>
        </section>
        <section id="articles">
            <h2>최신 게시물</h2>
            <div class="post">
                <h3>쥐 키우는 팁</h3>
                <p>쥐를 키우는 데 필요한 기본 팁과 주의사항에 대해 알아봅니다.</p>
            </div>
            <div class="post">
                <h3>쥐 품종 소개</h3>
                <p>인기 있는 쥐 품종들과 그들의 특징을 살펴봅니다.</p>
            </div>
            <div class="post">
                <h3>쥐와 함께 하는 놀이</h3>
                <p>쥐와의 상호 작용을 즐길 수 있는 재미있는 놀이 아이디어를 소개합니다.</p>
            </div>
        </section>
        <section id="contact">
            <h2>연락처</h2>
            <p>문의사항이 있으시면 이메일을 통해 연락해주세요: info@petmicecommunity.com</p>
        </section>
    </main>
</body>
</html>