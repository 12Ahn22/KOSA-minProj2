<%--
  Created by IntelliJ IDEA.
  User: COM
  Date: 2024-04-29
  Time: 오후 1:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>RATTY | 회원 가입</title>
    <%@include file="../include/bootStrap.jsp" %>
</head>
<body>
<jsp:include page="../include/header.jsp"/>
<main class="container">
    <h1>회원 가입</h1>
    <form id="uForm">
        <div>
            <label for="id">아이디:</label>
            <input type="text" id="id" name="id" required>
            <input class="btn btn-outline-success btn-sm" type="button" id="duplicateId" value="중복확인">
            <span id="duplicateMsg"></span>
        </div>
        <div>
            <label for="name">이름:</label>
            <input type="text" id="name" name="name" required>
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
            <input type="tel" id="phone" name="phone" required>
        </div>
        <div>
            <label for="address">주소:</label>
            <input type="text" id="address" name="address" required>
        </div>
        <div>
            <label for="birthdate">생년월일:</label>
            <input type="date" id="birthdate" name="birthdate" required>
        </div>
        <div>
            <label>성별:</label>
            <input type="radio" id="female" name="gender" value="F" checked>
            <label for="female">여성</label>
            <input type="radio" id="male" name="gender" value="M">
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
        <a class="btn btn-secondary" href="member?action=list">취소</a>
    </form>
</main>
<script type="text/javascript" src="/js/common.js"></script>
<script>

    // 계정 중복 체크
    let isDuplicated = false;
    document.getElementById("duplicateId").addEventListener("click",()=>{
        const id = document.getElementById("id").value;
        const duplicateMsg = document.getElementById("duplicateMsg");
        fetch("duplicate", {
            method: "POST",
            body: JSON.stringify({id}),
            headers: {"Content-type": "application/json; charset=utf-8"}
        }).then((res) => res.json())
            .then((data) => {
                if (data.status === 204) {
                    alert("사용 가능한 계정입니다.");
                    isDuplicated = true;
                    duplicateMsg.textContent = "사용 가능한 계정입니다.";
                    duplicateMsg.className = "text-primary";
                } else {
                    alert("사용 불가능한 계정입니다.");
                    isDuplicated = false;
                    duplicateMsg.textContent = "사용 불가능한 계정입니다.";
                    duplicateMsg.className = "text-danger";
                }
            });
    })

    // 회원 가입 요청
    const uForm = document.getElementById("uForm");
    uForm.addEventListener("submit", (e) => {
        e.preventDefault();

        // 유효성 검사
        validateSamePassword(password, password2);
        validatePhoneNumber(phone);
        if(!isDuplicated){
            alert("계정 중복 체크를 해주세요.");
            return;
        }

            fetch("insert", {
                method: "POST",
                body: formToSerialize("uForm"),
                headers: {"Content-type": "application/json; charset=utf-8"}
            }).then((res) => res.json())
                .then((data) => {
                    console.log("data" , data);
                    if (data.status === 204) {
                        alert("회원 가입에 성공했습니다.");
                        // 페이지 리다이렉트
                        location = "/member/login";
                    } else {
                        alert("회원 가입에 실패했습니다.");
                    }
                });
    })


    // id input 값을 수정하면, 계정 중복 검사 초기화
    document.getElementById("id").addEventListener("blur",()=>{
        isDuplicated = false;
        duplicateMsg.textContent = "계정 중복 검사를 진행해주세요.";
        duplicateMsg.className = "text-danger";
    })
</script>
</body>
</html>