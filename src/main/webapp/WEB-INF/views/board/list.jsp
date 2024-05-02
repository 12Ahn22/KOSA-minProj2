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
<main class="container">
    <h1>게시물 리스트</h1>
    <a class="btn btn-primary mb-2" href="insert">새 글 작성하기</a>
    <form id="searchForm" method="get" action="list">
        <select id="size" name="size">
            <c:forEach var="size" items="${sizes}">
                <option value="${size.value}" ${pageRequestVO.size == size.value ? 'selected' : ''} >${size.name}</option>
            </c:forEach>
        </select>
        <input
                type="text"
                name="searchKey"
                id="searchKey"
                placeholder="Search..."
        />
        <input type="submit" value="검색" class="btn btn-primary"/>
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
                <td>${board.id}</td>
                <td><a data-bs-toggle="modal" data-bs-toggle="modal" data-bs-target="#boardViewModel"
                       data-bs-id="${board.id}">${board.title}</a></td>
                <td>${board.author}</td>
                <td>${board.created_at}</td>
                <td>${board.view_count}</td>
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
                    var="num">
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
<!-- 상세보기 Modal -->
<div class="modal fade" id="boardViewModel" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="staticBackdropLabel">게시물 상세보기</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <label>게시물 번호:</label><span id="bno"></span><br/>
                <label>제목 : </label><span id="title"></span><br/>
                <label>내용 : </label><span id="content"></span><br/>
                <label>ViewCount :</label><span id="view_count"></span><br/>
                <label>작성자 : </label><span id="author"></span><br/>
                <label>작성일 : </label><span id="created_at"></span><br/>
            </div>
            <div class="modal-footer justify-content-between">
                <div>
                    <button type="button" class="btn btn-primary">수정</button>
                    <button type="button" class="btn btn-danger">삭제</button>
                </div>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>
<script>
    document
        .querySelector('.pagination')
        .addEventListener('click', function (e) {
            e.preventDefault();

            const target = e.target;

            if (target.tagName !== 'A') {
                return;
            }
            const num = target.dataset['num'];
            const size = document.getElementById("size").value;
            location = `?pageNo=\${num}&size=\${size}`;
        });

    const searchForm = document.getElementById("searchForm");
    document.querySelector('#size').addEventListener('change', (e) => {
        searchForm.submit();
    });

    searchForm.addEventListener('submit', (e) => {
        e.preventDefault();
        searchForm.submit();
    });

    const url = new URL(window.location.href);
    const urlParams = url.searchParams;
    const searchKey = document.getElementById("searchKey");
    if(urlParams.get("searchKey")){
        searchKey.value = urlParams.get("searchKey");
    }

    const boardViewModel = document.getElementById("boardViewModel");
    const span_bno = document.getElementById("bno");
    const span_title = document.getElementById("title");
    const span_content = document.getElementById("content");
    const span_viewCount = document.getElementById("view_count");
    const span_author= document.getElementById("author");
    const span_createdAt = document.getElementById("created_at");

    boardViewModel.addEventListener('hidden.bs.modal',(e)=>{
        span_bno.innerText = "";
        span_title.innerText = "";
        span_content.innerText = "";
        span_viewCount.innerText = "";
        span_author.innerText = "";
        span_createdAt.innerText = "";
    });

    boardViewModel.addEventListener('shown.bs.modal',  (e) => {
        const a = e.relatedTarget;
        const id = a.getAttribute('data-bs-id');

        // 요청
        fetch(`view?id=\${id}`, {
            method: "GET",
            headers: { "Content-type": "application/json; charset=utf-8" }
        }).then((res) => res.json())
            .then((data) => {
                if (data.status === 204) {
                    //성공
                    const board = data.board;
                    span_bno.innerText = board.id;
                    span_title.innerText = board.title;
                    span_content.innerText = board.content;
                    span_viewCount.innerText = board.view_count;
                    span_author.innerText = board.author;
                    span_createdAt.innerText = board.created_at;
                } else {
                    alert("게시글을 가져오는 데 실패했습니다.");
                }
            });

    })
</script>
</body>
</html>