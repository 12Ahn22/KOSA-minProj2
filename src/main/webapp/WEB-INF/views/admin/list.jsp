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
    <title>RATTY | 회원 리스트</title>
    <%@include file="../include/bootStrap.jsp" %>
</head>
<body>
<%@include file="../include/header.jsp" %>
<main class="container">
    <h1>회원 리스트</h1>
    <%--검색 폼--%>
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
        <label for="lockCheck">계정 잠금 여부 체크:</label><input type="checkbox" id="lockCheck" name="lockCheck"
    ${param.lockCheck.equals("on") ? 'checked':''}/>
        <input type="submit" class="btn btn-primary" value="검색"/>
    </form>

    <%--검색 결과 테이블--%>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">아이디</th>
            <th scope="col">이름</th>
            <th scope="col">성별</th>
            <th scope="col">휴대번호</th>
            <th scope="col">권한</th>
            <th scope="col">계정 잠금</th>
            <th scope="col"></th>
        </tr>
        </thead>
        <tbody id="table">
        <c:forEach var="member" items="${pageResponseVO.list}">
            <tr>
                <td><a href="view?id=${member.id}">${member.id}</a></td>
                <td>${member.name}</td>
                <td>${member.gender}</td>
                <td>${member.phone}</td>
                <td>${member.getAuthName()}</td>
                <td><input type="checkbox" name="account_locked"
                           value="${member.id}" ${member.account_locked == 'Y' ? 'checked' : ''}/></td>
                <td>
                    <button name="delete-btn" class="btn btn-danger" data-id="${member.id}">계정 삭제</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <!--  페이지 네비게이션 바 출력  -->
    <div class="float-end">
        <ul class="pagination flex-wrap">
            <c:if test="${pageResponseVO.prev}">
                <li class="page-item">
                    <a class="page-link" data-num="${pageResponseVO.start -1}"
                    >이전</a
                    >
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
            const lockCheck = document.getElementById("lockCheck");
            location = `?pageNo=\${num}&size=\${size}&searchKey=\${searchKey}&lockCheck=\${lockCheck.checked? lockCheck.value : ''}`;
        });

    // 옵션 변경에 의한 검색 요청
    document.querySelector('#size').addEventListener('change', (e) => {
        searchForm.submit();
    });

    document.getElementById("lockCheck").addEventListener('change', (e) => {
        searchForm.submit();
    })

    // 계정 잠금 요청
    document.getElementById("table").addEventListener("change", (e) => {
        if (e.target.name === "account_locked") {
            // 계정 잠금 요청 보내기
            const id = e.target.value;
            const checked = e.target.checked;
            fetch("updateAccountLock", {
                method: "POST",
                body: JSON.stringify({
                    id,
                    account_locked: checked ? 'Y' : 'N'
                }),
                headers: {"Content-type": "application/json; charset=utf-8"}
            }).then((res) => res.json())
                .then((data) => {
                    if (data.status === 204) {
                        alert("회원 잠금 변경에 성공했습니다.");
                    } else {
                        alert("회원 잠금 변경에 실패했습니다.");
                    }
                });

        }
    })

    // 계정 삭제 요청
    document.getElementById("table").addEventListener("click", (e) => {
        const searchParams = new URLSearchParams(location.search);
        if (e.target.name === "delete-btn") {
            fetch("delete", {
                method: "POST",
                body: JSON.stringify({id: e.target.dataset["id"]}),
                headers: {"Content-type": "application/json; charset=utf-8"}
            }).then((res) => res.json())
                .then((data) => {
                    if (data.status === 204) {
                        alert("회원 삭제에 성공했습니다.");
                        location=`list?\${searchParams.toString()}`;
                    } else {
                        alert("회원 삭제에 실패했습니다.");
                    }
                });
        }
    })

    const url = new URL(window.location.href);
    const urlParams = url.searchParams;
    if (urlParams.get("searchKey")) {
        searchKey.value = urlParams.get("searchKey");
    }
</script>
</body>
</html>