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
    <%@include file="../include/meta.jsp" %>
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
                <div id="content"></div>
                <label>ViewCount :</label><span id="view_count"></span><br/>
                <label>작성자 : </label><span id="author"></span><br/>
                <label>작성일 : </label><span id="created_at"></span><br/>
            </div>
            <div class="modal-footer justify-content-between">
                <div>
                    <button type="button" class="btn btn-primary" data-bs-mode="update" data-bs-toggle="modal"
                            data-bs-target="#passwordModal">수정
                    </button>
                    <button type="button" class="btn btn-danger" data-bs-mode="delete" data-bs-toggle="modal"
                            data-bs-target="#passwordModal">삭제
                    </button>
                </div>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="passwordModal" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="modal-password-title">게시글</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <p>게시글 비밀번호를 입력하세요.</p>
                <input type="password" id="password-modal-password"/>
            </div>
            <div class="modal-footer justify-content-between">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-primary" id="password-modal-btn">제출</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="/js/common.js"></script>
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
            const searchKey = document.getElementById("searchKey").value;
            location = `?pageNo=\${num}&size=\${size}&searchKey=\${searchKey}`;
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
    if (urlParams.get("searchKey")) {
        searchKey.value = urlParams.get("searchKey");
    }

    const boardViewModel = document.getElementById("boardViewModel");
    const span_bno = document.getElementById("bno");
    const span_title = document.getElementById("title");
    const content = document.getElementById("content");
    const span_viewCount = document.getElementById("view_count");
    const span_author = document.getElementById("author");
    const span_createdAt = document.getElementById("created_at");
    let selectedId;

    boardViewModel.addEventListener('hidden.bs.modal', (e) => {
        span_bno.innerText = "";
        span_title.innerText = "";
        content.innerHTML = "";
        span_viewCount.innerText = "";
        span_author.innerText = "";
        span_createdAt.innerText = "";
    });

    boardViewModel.addEventListener('show.bs.modal', (e) => {
        selectedId = null;
        const a = e.relatedTarget;
        const id = a.getAttribute('data-bs-id');

        // 요청
        fetch(`view?id=\${id}`, {
            method: "GET",
            headers: {"Content-type": "application/json; charset=utf-8"}
        }).then((res) => res.json())
            .then((data) => {
                if (data.status === 204) {
                    //성공
                    const board = data.board;
                    span_bno.innerText = board.id;
                    span_title.innerText = board.title;
                    content.innerHTML = board.content;
                    span_viewCount.innerText = board.view_count;
                    span_author.innerText = board.author;
                    span_createdAt.innerText = board.created_at;
                    selectedId = board.id;
                } else {
                    alert("게시글을 가져오는 데 실패했습니다.");
                }
            });
    })

    const passwordModal = document.getElementById("passwordModal");
    const passwordModalTitle = document.getElementById("modal-password-title");
    let selectedMode;
    passwordModal.addEventListener('show.bs.modal', (e) => {
        const relatedTarget = e.relatedTarget;
        const mode = relatedTarget.getAttribute('data-bs-mode');
        if (mode === "update") {
            passwordModalTitle.textContent = "게시글 수정";
            selectedMode = "update";
        } else if (mode === "delete") {
            passwordModalTitle.textContent = "게시글 삭제";
            selectedMode = "delete";
        }
    })

    passwordModal.addEventListener('hidden.bs.modal', (e) => {
        passwordModalTitle.textContent = "게시글";
        password.value = "";
    })

    const passwordSubmitBtn = document.getElementById("password-modal-btn");
    const password = document.getElementById("password-modal-password");
    passwordSubmitBtn.addEventListener("click", (e) => {
        if (selectedId === null) {
            alert("게시글이 선택되지않았습니다.");
            return;
        }

        // 비밀번호 확인 요청
        myFetch("checkPassword", {
                id: selectedId,
                password: password.value,
            },
            (data) => {
                if (data.status === 204) {
                    // 이후, 요청 보내기
                    if (selectedMode === "update") {
                        // updateForm으로 이동
                        location = `update?id=\${selectedId}&password=\${password.value}`;
                    }
                    if (selectedMode === "delete") {
                        if (confirm("정말 삭제하시겠습니까?")) {
                            myFetch("delete", {
                                    id: selectedId,
                                    password: password.value
                                },
                                (data) => {
                                    console.log("data", data);
                                    if (data.status === 204) {
                                        alert("게시글 삭제에 성공했습니다.")
                                        const searchParams = new URLSearchParams(location.search);
                                        location = `list?\${searchParams.toString()}`;
                                    } else {
                                        alert("게시글 삭제에 실패했습니다.")
                                    }
                                });
                        }
                    }
                } else {
                    alert("비밀번호가 잘못되었습니다.");
                }
            });
    })
</script>
</body>
</html>