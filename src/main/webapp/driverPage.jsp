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
    <link rel="stylesheet" href="/assets/css/style.css">
    <script src="/assets/js/popUp.js"></script>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-0evHe/X+R7YkIZDRvuzKMRqM+OrBnVFBL6DOitfPri4tjfHxaWutUpFmBp4vmVor" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-pprn3073KE6tl6bjs2QrFaJGz5/SUsLqktiwsUTF55Jfv3qYSDhgCecCxMW52nD2" crossorigin="anonymous"></script>

    <title>DriverPage</title>
</head>
<body>

<c:if test="${currentUser.getRoleId()!=3}">
    <c:redirect url="errorPage.jsp"/>
</c:if>

<jsp:include page="header.jsp">
    <jsp:param name="login" value="${currentUser.getLogin()}"/>
    <jsp:param name="page" value="driverPage.jsp"/>
</jsp:include>
<br>
<div class="container">
    <div class="row">
        <div class="col-md-2">
            <a href="controller?command=showDriverOrder&driverId=${currentUser.getId()}"><button class="btn btn-primary btn-xs"><fmt:message key="btn.show_orders"/></button></a>
        </div>
        <div class="col-md-8">
            <c:if test="${requestScope.containsKey('orderError')}">
                <p class="text-center text-danger"><fmt:message key="label.no_orders"/></p>
            </c:if>
            <br>
            <div id="orderTable" class="orderTableCls">
                <c:if test="${requestScope.containsKey('order')}">
                    <table class="table table-bordered table-striped">
                        <tr>
                            <th><fmt:message key="tn.order_id"/></th>
                            <th><fmt:message key="th.userLogin"/></th>
                            <th><fmt:message key="th.orderCar_brand"/></th>
                            <th><fmt:message key="th.orderCar_class"/></th>
                            <th><fmt:message key="th.orderCar_name"/></th>
                            <th><fmt:message key="th.orderFirst_date"/></th>
                            <th><fmt:message key="th.orderLast_date"/></th>
                        </tr>
                        <tr>
                            <td>${order.getId()}</td>
                            <td>${order.getUserLogin()}</td>
                            <td>${car.getBrand()}</td>
                            <td>${car.getQualityClass()}</td>
                            <td>${car.getName()}</td>
                            <td>${order.getFirstDate()}</td>
                            <td>${order.getLastDate()}</td>
                        </tr>
                    </table>
                    <button class="btn btn-primary btn-xs" onclick="closeOrderTable()"><fmt:message key="btn.close"/></button>
                </c:if>
            </div>
        </div>
        <div class="col-md-2"></div>
    </div>
</div>

</body>
</html>
