<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localeDoubleLib" prefix="localeDoubleTag"%>
<%@ taglib prefix="ftm" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="/assets/css/style.css">
    <script src="/assets/js/pdfOrder.js"></script>
    <script src="/assets/js/popUp.js"></script>
    <script src="/assets/js/orderTableSort.js"></script>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-0evHe/X+R7YkIZDRvuzKMRqM+OrBnVFBL6DOitfPri4tjfHxaWutUpFmBp4vmVor" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-pprn3073KE6tl6bjs2QrFaJGz5/SUsLqktiwsUTF55Jfv3qYSDhgCecCxMW52nD2" crossorigin="anonymous"></script>

    <title>ManagerPage</title>
</head>
<body>

<c:if test="${currentUser.getRoleId()!=2}">
    <c:redirect url="errorPage.jsp"/>
</c:if>

<jsp:include page="header.jsp">
    <jsp:param name="login" value="${currentUser.getLogin()}"/>
    <jsp:param name="page" value="managerPage.jsp"/>
</jsp:include>
<br>

<div class="container">
    <a href="controller?command=showAllOrders"><button class="btn btn-primary btn-xs"><fmt:message key="btn.show_orders"/></button></a>
    <br>
    <br>
    <div class="row">

        <div class="col-md-11">
            <c:if test="${requestScope.containsKey('noOrdersError')}">
                <p class="text-center text-danger"><fmt:message key="label.no_orders"/></p>
            </c:if>
            <div id="orderTable" class="orderTableCls">
                <c:if test="${requestScope.containsKey('orderList')}">
                    <form action="controller" method="post">
                        <input type="hidden" name="command" value="orderSearch">
                        <input type="hidden" name="option" value="-1">

                        <label><fmt:message key="th.orderFirst_date"/>:</label>
                        <input type="date" name="firstDate">
                        <label><fmt:message key="th.orderLast_date"/>:</label>
                        <input type="date" name="lastDate">

                        <select name="status">
                            <option value=""><fmt:message key="option.all"/></option>
                            <option value="1">In process</option>
                            <option value="2">Accepted</option>
                            <option value="3">Denied</option>
                            <option value="4">Finished</option>
                        </select>

                        <select name="driver">
                            <option value="all"><fmt:message key="option.all"/></option>
                            <option value="false"><fmt:message key="label.without_driver"/></option>
                            <option value="true"><fmt:message key="label.with_driver"/></option>
                        </select>

                        <input class="btn btn-primary btn-xs" type="submit" value="<fmt:message key='btn.search'/>">
                    </form>
                    <table id="orderTableId" class="table table-bordered table-striped" hidden>
                        <tr>
                            <th>Order id</th>
                            <th>Status</th>
                            <th>User login</th>
                            <th>Car brand</th>
                            <th>Car class</th>
                            <th>Car name</th>
                            <th>First date</th>
                            <th>Last date</th>
                            <th>Driver option</th>
                            <th onClick="sortCarTable(9), renderCarTable()" class="costColum">Cost</th>
                            <th>Options</th>
                        </tr>
                        <c:forEach var="order" items="${orderList}">
                            <tr>
                                <td>${order.getId()}</td>
                                <td>${statusMap.get(order.getStatusId())}</td>
                                <td>${order.getUserLogin()}</td>
                                <td>${carMap.get(order.getCarId()).getBrand()}</td>
                                <td>${carMap.get(order.getCarId()).getQualityClass()}</td>
                                <td>${carMap.get(order.getCarId()).getName()}</td>
                                <td id="orderFirstDate">${order.getFirstDate()}</td>
                                <td>${order.getLastDate()}</td>
                                <td>
                                    <c:if test="${!order.getDriverOption()}">
                                        <fmt:message key="label.no"/>
                                    </c:if>
                                    <c:if test="${order.getDriverOption()}">
                                        <fmt:message key="label.yes"/>
                                    </c:if>
                                </td>
                                <td><localeDoubleTag:localeDoubleTag locale="${currentLocale}" number="${order.getCost()}"/></td>
                                <td>
                                    <c:if test="${order.getStatusId() == 1}">
                                        <c:if test="${!order.getDriverOption()}">
                                            <a href="/controller?command=acceptOrderWithoutDriver&orderId=${order.getId()}">
                                                <button class="btn btn-primary btn-xs"><fmt:message key="btn.accept"/></button>
                                            </a>
                                        </c:if>
                                        <c:if test="${order.getDriverOption()}">
                                            <button class="btn btn-primary btn-xs" onclick="openOrderAcceptForm(${order.getId()})"><fmt:message key="btn.accept"/></button>
                                        </c:if>
                                    </c:if>
                                </td>
                                <td>
                                    <c:if test="${order.getStatusId() == 1}">
                                        <button class="btn btn-danger btn-xs" onclick="openOrderDenyForm(${order.getId()}, ${order.getCarId()}, ${order.getCost()}, ${order.getUserId()})">
                                            <fmt:message key="btn.deny"/>
                                        </button>
                                    </c:if>
                                    <c:if test="${order.getStatusId() == 4}">
                                        <button class="btn btn-primary btn-xs" onclick="openConfirmFinishForm(${order.getId()}, ${order.getCarId()}, ${order.getUserId()}, ${order.getPassportId()}, '${order.getLastDate()}', '${order.getReturnDate()}', ${order.getDriverOption()})">
                                            <fmt:message key="btn.confirm_finish"/>
                                        </button>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>

                    <table id="orderTablePagination" class="table table-bordered table-striped">
                        <th><fmt:message key="tn.order_id"/></th>
                        <th><fmt:message key="th.orderStatus"/></th>
                        <th><fmt:message key="th.userLogin"/></th>
                        <th><fmt:message key="th.orderCar_brand"/></th>
                        <th><fmt:message key="th.orderCar_class"/></th>
                        <th><fmt:message key="th.orderCar_name"/></th>
                        <th><fmt:message key="th.orderFirst_date"/></th>
                        <th><fmt:message key="th.orderLast_date"/></th>
                        <th><fmt:message key="th.orderDriver_option"/></th>
                        <th><fmt:message key="th.orderCost"/></th>
                        <th><fmt:message key="th.orderOptions"/></th>
                        <th></th>
                    </table>

                    <c:if test="${orderList.size() == 0}">
                        <p class="text-center text-danger"><fmt:message key="label.no_orders"/></p>
                    </c:if>

                    <button id="prevButtonOrder" class="btn btn-warning"><fmt:message key="btn.previous"/></button>
                    <button id="nextButtonOrder" class="btn btn-warning"><fmt:message key="btn.next"/></button>

                    <script src="/assets/js/orderPagination.js"></script>
                    <script src="/assets/js/orderVerification.js">

                    </script>

                    <button class="btn btn-primary btn-xs" onclick="closeOrderTable()"><fmt:message key="btn.close"/></button>
                    <img onclick="createOrderPDF(), printOrder()" class="pdfImg" src="images/pdf.png">
                </c:if>
            </div>
        </div>
        <div class="col-md-1"></div>
    </div>
</div>
<br>
<div class="container">
    <div class="row">
        <div class="col-md-4">
            <div id="denyForm" class="denyFormCls">
                <form class="form-horizontal" action="controller" method="post">
                    <input type="hidden" name="command" value="orderDeny">
                    <input type="text" id="deniedOrderId" name="orderId" hidden value="">
                    <input type="text" id="orderCarId" name="carId" hidden value="">
                    <input type="text" id="orderCostId" name="orderCost" hidden value="">
                    <input type="text" id="orderUserId" name="userId" hidden value="">

                    <div class="form-group">
                        <textarea name="message" cols="15" rows="6" required minlength="5" maxlength="200"></textarea>
                    </div>
                    <br>
                    <input class="btn btn-danger btn-xs" type="submit" value="<fmt:message key='btn.deny'/>">
                </form>
            </div>
        </div>
        <div class="col-md-4">
            <c:if test="${driverList.size() == 0}">
                <p class="text-center text-danger"><fmt:message key="label.no_drivers"/></p>
            </c:if>
            <div id="acceptForm" class="acceptFormCls">
                <c:if test="${driverList.size() > 0}">
                    <form class="form-horizontal" action="controller" method="post">
                        <input type="hidden" name="command" value="acceptOrderWithDriver">
                        <input type="text" id="acceptedOrderId" name="orderId" hidden value="">

                        <div class="form-group">
                            <label class="control-label"><fmt:message key="label.choose_driver"/>:</label>
                            <select name="driverId" required>
                                <c:forEach var="driver" items="${driverList}">
                                    <c:if test="${driver.isAffordable()}">
                                        <option value="${driver.getId()}">${driver.getLogin()}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>
                        <br>
                        <input class="btn btn-success btn-xs" type="submit" value="<fmt:message key='btn.accept'/>">
                    </form>
                </c:if>
            </div>
        </div>
        <div class="col-md-4">
            <div id="confirmFinishForm" class="confirmFinishFormCls">
                <form class="form-horizontal" action="controller" method="post">
                    <input type="hidden" name="command" value="confirmFinish">
                    <input type="text" id="finishedOrderId" name="orderId" hidden value="">
                    <input type="text" id="finishedCarId" name="carId" hidden value="">
                    <input type="text" id="finishedUserId" name="userId" hidden value="">
                    <input type="text" id="finishedPassportId" name="passportId" hidden value="">
                    <input type="text" id="driverOptionId" name="driverOption" hidden value="">

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="label.car"/>:</label>
                        <br>
                        <input type="radio" name="damaged" value="false" onclick="notDamagedCar()" required>
                        <label><fmt:message key="label.not_damaged"/></label>
                        <br>
                        <input type="radio" name="damaged" value="true" onclick="damagedCar()">
                        <label><fmt:message key="th.carDamaged"/></label>
                    </div>

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="label.fine"/>:</label>
                        <input class="form-control" type="number" name="fine" id="fineId" min="0" value="0" disabled required>
                    </div>

                    <span id="carLaterReturnMessage" hidden><fmt:message key="label.car_returned_later"/></span>
                    <br>
                    <input class="btn btn-success btn-xs" type="submit" value="<fmt:message key='btn.confirm'/>">
                </form>
            </div>
        </div>
    </div>
</div>

</body>
</html>
