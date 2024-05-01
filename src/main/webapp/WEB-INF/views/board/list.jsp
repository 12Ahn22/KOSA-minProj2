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
	<title>RATTY | 게시글</title>
	<%@include file="../include/bootStrap.jsp" %>
</head>
<body>
<%@include file="../include/header.jsp" %>
<main>
	<h1>게시물 리스트</h1>
	<a class="btn btn-primary mb-2" href="insert">새 글 작성하기</a>
	<form id="searchForm" method="get" action="list">
		<select id="size" name="size" >
			<c:forEach var="size" items="${sizes}">
				<option value="${size.value}" ${pageRequestVO.size == size.codeid ? 'selected' : ''} >${size.name}</option>
			</c:forEach>
		</select>
		<input
				type="text"
				name="searchKey"
				id="searchKey"
				placeholder="Search..."
		/>
		<input type="submit" value="검색" />
	</form>
	<table class="table">
		<thead>
		<tr>
			<th scope="col">no</th>
			<th scope="col">제목</th>
			<th scope="col">작성자</th>
			<th scope="col">작성일</th>
			<th scope="col">조회수</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach var="board" items="${pageResponseVO.list}">
			<tr>
				<td>${board.bno}</td>
				<td><a data-bs-toggle="modal" data-bs-toggle="modal" data-bs-target="#boardViewModel" data-bs-bno="${board.bno}">${board.title}</a></td>
				<td>${board.author}</td>
				<td>${board.createdAt}</td>
				<td>${board.viewCount}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<!--  페이지 네비게이션 바 출력  -->
	<div class="float-end">
		<ul class="pagination flex-wrap">
			<c:if test="${pageResponseVO.prev}">
				<li class="page-item">
					<a class="page-link" data-num="${pageResponseVO.start -1}">이전</a>
				</li>
			</c:if>

			<c:forEach
					begin="${pageResponseVO.start}"
					end="${pageResponseVO.end}"
					var="num"
			>
				<li class="page-item ${pageResponseVO.pageNo == num ? 'active':''}">
					<a class="page-link" data-num="${num}">${num}</a>
				</li>
			</c:forEach>

			<c:if test="${pageResponseVO.next}">
				<li class="page-item">
					<a class="page-link" data-num="${pageResponseVO.end + 1}">다음</a>
				</li>
			</c:if>
		</ul>
	</div>
</main>
</body>
</html>