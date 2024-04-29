<%--
  Created by IntelliJ IDEA.
  User: COM
  Date: 2024-04-29
  Time: 오후 1:25
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
		<h1>Pet Rat, 애완 쥐</h1>
		<img src="/images/rat.png" alt="애완 쥐 사진">
		<h2>애완 쥐란?</h2>
		<p>애완 쥐는 매력적인 작은 동물로, 많은 사람들에게 인기가 있습니다. 쥐들은 사랑스럽고 호기심이 많으며, 적응력이 좋아서 다양한 환경에서 쉽게 살아갈 수 있습니다.</p>
		<h2>애완 쥐의 특징</h2>
		<ul>
			<li>귀여운 외모</li>
			<li>호기심이 많음</li>
			<li>친근하고 사람들과 잘 어울림</li>
			<li>피부병이나 알레르기 유발 가능성이 낮음</li>
			<li>작은 공간에서도 키울 수 있음</li>
			<li>낮은 유지비용</li>
		</ul>
		<h2>애완 쥐를 키우는 방법</h2>
		<p>애완 쥐를 키우는 것은 책임감 있는 일입니다. 쥐들은 매일적인 관심과 적절한 환경을 필요로 합니다. 아래는 애완 쥐를 키우는 몇 가지 팁입니다.</p>
		<ol>
			<li>적절한 케이지 선택: 쥐가 편안하게 느낄 수 있는 크기와 형태의 케이지를 선택해주세요.</li>
			<li>올바른 사료: 애완 쥐에게는 특별한 사료가 필요합니다. 특히 과일, 채소, 곡물 등을 골고루 섞어 줍니다.</li>
			<li>청결 유지: 케이지를 매일 청소하고 물병을 신선한 물로 채워주세요.</li>
			<li>사회화: 쥐가 사람들과 소통하고 놀 수 있도록 시간을 할애해주세요.</li>
			<li>건강 관리: 정기적인 건강 점검과 예방 접종을 받도록 주의해주세요.</li>
		</ol>
		<p>애완 쥐를 키우는 것은 보람이 있고 즐거운 경험이 될 것입니다. 그들과의 시간을 즐기며 행복한 생활을 함께 할 수 있습니다!</p>
	<main/>
</body>
</html>