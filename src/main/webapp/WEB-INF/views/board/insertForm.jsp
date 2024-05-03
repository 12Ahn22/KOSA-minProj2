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
	<title>RATTY | 게시글 생성</title>
	<%@include file="../include/bootStrap.jsp" %>
	<%@include file="../include/meta.jsp" %>
	<%-- ckeditor 관련 자바 스크립트  --%>
	<script src="https://cdn.ckeditor.com/ckeditor5/12.4.0/classic/ckeditor.js"></script>
	<script src="https://ckeditor.com/apps/ckfinder/3.5.0/ckfinder.js"></script>
</head>
<body>
<%@include file="../include/header.jsp" %>
<main class="container">
	<form id="iForm" method="post" enctype="multipart/form-data">
		<input type="hidden" id="token" name="token" value="${token}"><br/>
		<label for="title">제목:</label><br>
		<input type="text" id="title" name="title" required><br>
		<label>비밀번호:</label>
		<input type="password" id="password" name="password" required><br>
		<textarea id="editor" name="content"></textarea>
		<div id="div_file">
			<input  type='file' name='file' />
		</div>
		<input class="btn btn-primary" type="submit" value="생성">
		<a class="btn btn-secondary" href="javascript:history.back();">취소</a>
	</form>
</main>
<script type="text/javascript" src="/js/common.js"></script>
<script>
	let editor;
	ClassicEditor
			.create(document.querySelector('#editor'))
			.then((newEditor) => {
				editor = newEditor;
			})
			.catch(error => {
				console.error(error);
			});
	const iForm = document.getElementById("iForm");
	iForm.addEventListener('submit', (e) => {
		e.preventDefault();
		myFileFetch("insert", "iForm", json => {
			switch (json.status) {
				case 204:
					//성공
					alert("게시물을 생성 하였습니다");
					// 새로 고침
					location = "list";
					break;
				default:
					alert("게시물 생성에 실패했습니다");
			}
		});
	})
</script>
</body>
</html>