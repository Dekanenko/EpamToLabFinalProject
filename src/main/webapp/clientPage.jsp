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
    <script src="/assets/js/pdfOrder.js"></script>
    <script src="/assets/js/carTableSort.js"></script>
    <script src="/assets/js/orderTableSort.js"></script>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-0evHe/X+R7YkIZDRvuzKMRqM+OrBnVFBL6DOitfPri4tjfHxaWutUpFmBp4vmVor" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-pprn3073KE6tl6bjs2QrFaJGz5/SUsLqktiwsUTF55Jfv3qYSDhgCecCxMW52nD2" crossorigin="anonymous"></script>

    <title>Client Page</title>
</head>
<body>

<c:if test="${currentUser.getRoleId()!=4}">
    <c:redirect url="errorPage.jsp"/>
</c:if>

<jsp:include page="/header.jsp">
    <jsp:param name="login" value="${currentUser.getLogin()}"/>
    <jsp:param name="page" value="/clientPage.jsp"/>
</jsp:include>

<br>
<c:if test="${sessionScope.currentUser.isAffordable()}">
    <div class="container">
        <br>
        <a href="controller?command=showCars"><button class="btn btn-primary btn-xs"><fmt:message key="btn.show_cars"/></button></a>
        <a href="controller?command=showUserOrders&userId=${currentUser.getId()}"><button class="btn btn-primary btn-xs" onclick="renderTable()"><fmt:message key="btn.show_orders"/></button></a>
        <br>
        <br>
        <c:if test="${requestScope.containsKey('noCarsError')}">
            <p class="text-center text-danger"><fmt:message key="label.no_car"/></p>
        </c:if>
        <div id="carTable" class="carTableCls">
            <c:if test="${requestScope.containsKey('carList')}">
                <form action="controller" method="post">
                    <input type="hidden" name="command" value="carSearch">
                    <input type="text" name="brand" placeholder="<fmt:message key="th.carBrand"/>">
                    <select name="qualityClass">
                        <option value="all"><fmt:message key="option.all"/></option>
                        <option value="A">A</option>
                        <option value="B">B</option>
                        <option value="C">C</option>
                        <option value="D">D</option>
                        <option value="E">E</option>
                        <option value="F">F</option>
                        <option value="M">M</option>
                        <option value="S">S</option>
                    </select>

                    <label><fmt:message key="th.orderOptions"/>:</label>
                    <select name="carDamageOption">
                        <option value="all"><fmt:message key="option.all"/></option>
                        <option value="false"><fmt:message key="label.carAdj_not_damaged"/></option>
                        <option value="true"><fmt:message key="label.carAdj_damaged"/></option>
                    </select>

                    <select name="carUsageOption">
                        <option value="all"><fmt:message key="option.all"/></option>
                        <option value="true"><fmt:message key="th.carIn_usage"/></option>
                        <option value="false"><fmt:message key="th.car_not_in_usage"/></option>
                    </select>

                    <input type="submit" class="btn btn-success btn-xs" value="<fmt:message key='btn.search'/>">
                </form>
                <table id="carTableId" hidden>
                    <tr>
                        <th>Brand</th>
                        <th>Class</th>
                        <th onClick="sortCarTable(2), renderCarTable()" class="nameColum">Name</th>
                        <th onClick="sortCarTable(3), renderCarTable()" class="costColum">Cost per day</th>
                        <th>In usage</th>
                        <th>Damaged</th>
                    </tr>
                    <c:forEach var="car" items="${carList}">
                        <tr>
                            <td>${car.getBrand()}</td>
                            <td>${car.getQualityClass()}</td>
                            <td>${car.getName()}</td>
                            <td><localeDoubleTag:localeDoubleTag locale="${currentLocale}" number="${car.getCost()}"/></td>
                            <td>${car.isUsed()}</td>
                            <td>${car.isDamaged()}</td>
                            <td>
                                <c:if test="${car.isUsed() != true && car.isDamaged() != true}">
                                    <a href="controller?command=goToCarOrderPage&carId=${car.getId()}&passportId=${currentUser.getPassportId()}">
                                        <button class="btn btn-primary btn-xs"><fmt:message key="btn.order"/></button>
                                    </a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </table>

                <table id="carTablePagination" class="table table-bordered table-striped">
                    <th><fmt:message key="th.carBrand"/></th>
                    <th><fmt:message key="th.carClass"/></th>
                    <th onclick="sortCarTable(2), renderCarTable()"><fmt:message key="th.carName"/></th>
                    <th onclick="sortCarTable(3), renderCarTable()"><fmt:message key="th.carCost_per_day"/></th>
                    <th><fmt:message key="th.carIn_usage"/></th>
                    <th><fmt:message key="th.carDamaged"/></th>
                    <th></th>
                </table>

                <c:if test="${carList.size() == 0}">
                    <p class="text-center text-danger"><fmt:message key="label.no_car"/></p>
                </c:if>

                <button id="prevButtonCar" class="btn btn-warning"><fmt:message key="btn.previous"/></button>
                <button id="nextButtonCar" class="btn btn-warning"><fmt:message key="btn.next"/></button>

                <script src="/assets/js/carPagination.js"></script>
                <button class="btn btn-primary btn-xs" onclick="closeCarTable()"><fmt:message key="btn.close"/></button>
            </c:if>
        </div>

        <c:if test="${requestScope.containsKey('noOrdersError')}">
            <p class="text-center text-danger"><fmt:message key="label.no_orders"/></p>
        </c:if>
        <c:if test="${requestScope.containsKey('orderList')}">
            <div id="orderTable" class="orderTableCls">
                <form action="controller" method="post">
                    <input type="hidden" name="command" value="orderSearch">
                    <input type="hidden" name="option" value="${currentUser.getId()}">

                    <label><fmt:message key="th.orderFirst_date"/>:</label>
                    <input type="date" name="firstDate" onchange="dateCompare()">
                    <label><fmt:message key="th.orderLast_date"/>:</label>
                    <input type="date" name="lastDate" onchange="dateCompare()">

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
                <div id="pdfTable" hidden>
                    <table id="orderTableId" class="table table-bordered table-striped">
                        <tr>
                            <th>Order id</th>
                            <th>Status</th>
                            <th>Car brand</th>
                            <th>Car class</th>
                            <th>Car name</th>
                            <th>First date</th>
                            <th>Last date</th>
                            <th>Driver option</th>
                            <th onClick="sortOrderTable(8), renderOrderTable()" class="costColum">Cost</th>
                        </tr>
                        <c:forEach var="order" items="${orderList}">
                            <tr>
                                <td>${order.getId()}</td>
                                <td>${statusMap.get(order.getStatusId())}</td>
                                <td>${carMap.get(order.getCarId()).getBrand()}</td>
                                <td>${carMap.get(order.getCarId()).getQualityClass()}</td>
                                <td>${carMap.get(order.getCarId()).getName()}</td>
                                <td>${order.getFirstDate()}</td>
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
                                    <c:if test="${order.getStatusId() == 3}">
                                        <button class="btn btn-primary btn-xs" onclick="openOrderConfirmForm(${order.getId()}, ${order.getPassportId()}, '${order.getMessage()}')">
                                            <fmt:message key="btn.show_cause"/>
                                        </button>
                                    </c:if>
                                    <c:if test="${order.getStatusId() == 2}">
                                        <button class="btn btn-primary btn-xs" onclick="openFinishForm(${order.getId()})">
                                            <fmt:message key="btn.finish"/>
                                        </button>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>

                <table id="orderTablePagination" class="table table-bordered table-striped">
                    <th><fmt:message key="tn.order_id"/></th>
                    <th><fmt:message key="th.orderStatus"/></th>
                    <th><fmt:message key="th.orderCar_brand"/></th>
                    <th><fmt:message key="th.orderCar_class"/></th>
                    <th><fmt:message key="th.orderCar_name"/></th>
                    <th><fmt:message key="th.orderFirst_date"/></th>
                    <th><fmt:message key="th.orderLast_date"/></th>
                    <th><fmt:message key="th.orderDriver_option"/></th>
                    <th><fmt:message key="th.orderCost"/></th>
                    <th></th>
                </table>

                <c:if test="${orderList.size() == 0}">
                    <p class="text-center text-danger"><fmt:message key="label.no_orders"/></p>
                </c:if>

                <button id="prevButtonOrder" class="btn btn-warning"><fmt:message key="btn.previous"/></button>
                <button id="nextButtonOrder" class="btn btn-warning"><fmt:message key="btn.next"/></button>

                <script src="/assets/js/orderPagination.js"></script>

                <button class="btn btn-primary btn-xs" onclick="closeOrderTable()"><fmt:message key="btn.close"/></button>

                <img onclick="createOrderPDF(), printOrder()" class="pdfImg" src="images/pdf.png">
            </div>
        </c:if>
        <br>
        <div id="confirmForm" class="confirmFormCls">
            <form class="form-horizontal" action="controller" method="post">
                <input type="hidden" name="command" value="confirmDeny">
                <input type="text" id="deniedOrderId" name="orderId" hidden value="">
                <input type="text" id="orderPassportId" name="orderPassport" hidden value="">
                <input type="text" id="orderUserId" name="userId" hidden value="${currentUser.getId()}">
                <div class="form-group">
                    <input class="form-control" type="text" id="messageId" value="" readonly>
                </div>
                <br>
                <input class="btn btn-success btn-xs" type="submit" value="<fmt:message key='btn.confirm'/>">
            </form>
        </div>
        <br>
        <div id="finishForm" class="finishFormCls">
            <form class="form-horizontal" action="controller" method="post">
                <input type="hidden" name="userId" value="${currentUser.getId()}">
                <input type="hidden" name="command" value="returnOrder">
                <input type="text" id="finishedOrderId" name="orderId" hidden>

                <div class="form-group">
                    <input class="returnDate" type="text" id="returnDateId" name="returnDate" readonly required>
                </div>

                <br>
                <input class="btn btn-success btn-xs" type="submit" value="<fmt:message key='btn.finish'/>">
            </form>
        </div>
    </div>
</c:if>

<c:if test="${!sessionScope.currentUser.isAffordable()}">
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
