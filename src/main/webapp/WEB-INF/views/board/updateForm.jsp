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
    <%@include file="../include/meta.jsp" %>
    <%-- ckeditor 관련 자바 스크립트  --%>
    <script src="https://cdn.ckeditor.com/ckeditor5/12.4.0/classic/ckeditor.js"></script>
    <script src="https://ckeditor.com/apps/ckfinder/3.5.0/ckfinder.js"></script>
</head>
<body>
<%@include file="../include/header.jsp" %>
<main class="container">
    <form id="uForm" method="post" enctype="multipart/form-data">
        <input type="hidden" id="token" name="token" value="${token}">
        <input type="hidden" name="id" id="id" value="${board.id}">
        <input type="hidden" name="author" id="author" value="${board.author}">
        <p><span>작성자: </span><span>${board.author}</span></p>
        <label for="title">제목:</label><br>
        <input type="text" id="title" name="title" value="${board.title}" required><br>
        <label>비밀번호:</label>
        <input type="password" id="password" name="password" required><br>
        <textarea id="editor" name="content"></textarea>
        <div id="div_file">
            <input  type='file' name='file' />
        </div>
        <c:if test="${not empty board.boardFileVO}">
            <label>첨부파일 : </label><a id="board_file" href="fileDownload/${board.id}">${board.boardFileVO.original_filename}</a><br/>
        </c:if>
        <input class="btn btn-primary" type="submit" value="수정">
        <a class="btn btn-secondary" href="javascript:history.back();">취소</a>
    </form>
</main>
<script type="text/javascript" src="/js/common.js"></script>
<script>
    let editor;
    const csrfParameter = document.querySelector("meta[name='_csrf_parameter']").content;
    const csrfToken = document.querySelector("meta[name='_csrf']").content;
    const IMAGE_URL = "/board/boardImageUpload?token=${token}&" + csrfParameter + "=" + csrfToken;
    ClassicEditor
        .create(document.querySelector('#editor'),{
            ckfinder:{
                uploadUrl: IMAGE_URL
            }
        })
        .then((newEditor) => {
            // 에디터 초기화 시, 초기값 설정
            editor = newEditor;
            editor.setData(`${board.content}`);
        })
        .catch(error => {
            console.error(error);
        });
    const uForm = document.getElementById("uForm");
    uForm.addEventListener('submit', (e) => {
        e.preventDefault();
        myFileFetch("update", "uForm", json => {
            console.log("결과 = ", json)
            switch (json.status) {
                case 204:
                    //성공
                    alert("게시물을 수정 하였습니다");
                    // 새로 고침
                    location.reload();
                    break;
                default:
                    alert("게시물 수정에 실패했습니다");
            }
        });
    })
</script>
</body>
</html>