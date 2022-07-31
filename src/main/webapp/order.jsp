<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localeDoubleLib" prefix="localeDoubleTag"%>

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="/assets/css/style.css">
    <script src="/assets/js/popUp.js"></script>
    <script src="/assets/js/orderVerification.js"></script>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-0evHe/X+R7YkIZDRvuzKMRqM+OrBnVFBL6DOitfPri4tjfHxaWutUpFmBp4vmVor" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-pprn3073KE6tl6bjs2QrFaJGz5/SUsLqktiwsUTF55Jfv3qYSDhgCecCxMW52nD2" crossorigin="anonymous"></script>

    <title>Order Page</title>
</head>
<body>

<c:if test="${currentUser.getRoleId()!=4 || car == null}">
    <c:redirect url="errorPage.jsp"/>
</c:if>

<jsp:include page="header.jsp">
    <jsp:param name="login" value="${currentUser.getLogin()}"/>
</jsp:include>

<br>
<c:if test="${currentUser.isAffordable()}">
    <div class="order">
        <table class="table table-bordered table-striped">
            <tr>
                <th><fmt:message key="th.carBrand"/></th>
                <th><fmt:message key="th.carClass"/></th>
                <th><fmt:message key="th.carName"/></th>
                <th><fmt:message key="th.carCost_per_day"/></th>
                <th><fmt:message key="th.carIn_usage"/></th>
                <th><fmt:message key="th.carDamaged"/></th>
            </tr>
            <tr>
                <td>${car.getBrand()}</td>
                <td>${car.getQualityClass()}</td>
                <td>${car.getName()}</td>
                <td><localeDoubleTag:localeDoubleTag locale="${currentLocale}" number="${car.getCost()}"/></td>
                <td>${car.isUsed()}</td>
                <td>${car.isDamaged()}</td>
            </tr>
        </table>
    </div>

    <div class="container">
        <div class="row">
            <div class="col-md-3">
                <p> <fmt:message key="label.currentBalance"/>: <localeDoubleTag:localeDoubleTag locale="${currentLocale}" number="${currentUser.getCash()}"/></p>
            </div>
            <div class="col-md-6">
                <br>
                <form class="form-horizontal" action="controller" method="post">
                    <input type="hidden" name="command" value="makeOrder">
                    <input type="text" hidden name="userId" value="${currentUser.getId()}">
                    <input type="text" hidden name="passportId" id="currentPassId" value="${currentUser.getPassportId()}">
                    <input type="text" hidden name="carId" value="${car.getId()}">
                    <input type="text" hidden name="userLog" value="${currentUser.getLogin()}">
                    <input type="number" hidden id="currentUserCash" name="userCash" value="${currentUser.getCash()}">
                    <input type="number" id="costPerDay" hidden value="${car.getCost()}" >
                    <input type="number" id="totalCostPerDay" hidden value="${car.getCost()}">

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="th.orderFirst_date"/>:</label>
                        <input class="form-control" type="date" id="fDate" name="firstDate" onchange="dateCompare()" required>
                    </div>

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="th.orderLast_date"/>:</label>
                        <input class="form-control" type="date" id="lDate" name="lastDate" onchange="dateCompare()" required>
                    </div>

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="th.orderDriver_option"/>:</label>
                        <input type="radio" name="driver" id="noDriver" value="false" checked required onclick="noDriverCost()">
                        <label><fmt:message key="label.without_driver"/></label>
                        <input type="radio" name="driver" id="driverCost" value="true" onclick="withDriverCost()">
                        <label><fmt:message key="label.with_driver"/></label>
                    </div>
                    <br>
                    <div class="form-group">
                        <input class="btn btn-primary btn-xs" type="button" id="calcBtn" value="<fmt:message key='btn.calculate'/>" onclick="costCalculation()" disabled>

                        <input type="number" id="totalCost" name="cost" value="" readonly>
                        <div id="balanceUp" hidden>
                            <a href="userEdit.jsp"><fmt:message key="label.top_up_your_account"/></a>
                        </div>
                    </div>
                    <br>
                    <div class="form-group">
                        <label class="control-label"><fmt:message key="label.userName"/>:</label>
                        <input class="form-control" type="text" name="name" required minlength="3" maxlength="45" value="${passport.getName()}" onchange="anotherPassport()">
                    </div>

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="label.userSurname"/>:</label>
                        <input class="form-control" type="text" name="surname" required minlength="3" maxlength="45" value="${passport.getSurname()}" onchange="anotherPassport()">
                    </div>

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="label.userUniqueNum"/>:</label>
                        <input class="form-control" type="text" name="uniqueNum" required minlength="5" maxlength="45" value="${passport.getUniqueNum()}" onchange="anotherPassport()">
                    </div>
                    <br>
                    <input class="btn btn-success btn-xs" type="submit" id="submitBtn" value="<fmt:message key='btn.submit'/>" disabled>
                </form>
            </div>
            <div class="col-md-3">
                <a href="/clientPage.jsp"><button class="btn btn-primary btn-xs"><fmt:message key="btn.go_back"/></button></a>
            </div>
        </div>
    </div>
</c:if>

<c:if test="${!currentUser.isAffordable()}">
    <div class="container">
        <div class="row">
            <div class="col-md-5"></div>
            <div class="col-md-2">
                <span class="text-center text-danger"><fmt:message key="span.banned"/></span>
            </div>
            <div class="col-md-5"></div>
        </div>
    </div>
</c:if>
</body>
</html>
