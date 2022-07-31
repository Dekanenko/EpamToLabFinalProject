<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-0evHe/X+R7YkIZDRvuzKMRqM+OrBnVFBL6DOitfPri4tjfHxaWutUpFmBp4vmVor" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-pprn3073KE6tl6bjs2QrFaJGz5/SUsLqktiwsUTF55Jfv3qYSDhgCecCxMW52nD2" crossorigin="anonymous"></script>
    <title>Car delete</title>
</head>
<body>
<c:if test="${!requestScope.containsKey('mark')}">
    <c:redirect url="errorPage.jsp"/>
</c:if>

<div class="container">
    <div class="row">
        <div class="col-md-4"></div>
        <div class="col-md-4">
            <c:if test="${requestScope.containsKey('delId')}">
                <h1><fmt:message key="label.are_you_sure"/>?</h1>
                <br>
                <form action="controller" method="post">
                    <input type="hidden" name="command" value="deleteCar">
                    <input type="hidden" name="id" value="${delId}">
                    <input class="btn btn-success btn-xs" type="submit" value="<fmt:message key='btn.delete_car'/>">
                </form>
                <br>
                <a href="/managePage"><button class="btn btn-primary btn-xs"><fmt:message key="btn.cancel"/></button></a>
            </c:if>
        </div>
        <div class="col-md-4"></div>
    </div>
</div>

</body>
</html>
