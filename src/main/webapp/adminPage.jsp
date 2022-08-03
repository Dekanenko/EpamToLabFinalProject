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
    <script src="/assets/js/pdfCar.js"></script>
    <script src="/assets/js/pdfUser.js"></script>
    <script src="/assets/js/popUp.js"></script>
    <script src="/assets/js/verification.js"></script>
    <script src="/assets/js/carTableSort.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-0evHe/X+R7YkIZDRvuzKMRqM+OrBnVFBL6DOitfPri4tjfHxaWutUpFmBp4vmVor" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-pprn3073KE6tl6bjs2QrFaJGz5/SUsLqktiwsUTF55Jfv3qYSDhgCecCxMW52nD2" crossorigin="anonymous"></script>
    <title>Admin Page</title>
</head>
<body>

<c:if test="${currentUser.getRoleId()!=1}">
    <c:redirect url="errorPage.jsp"/>
</c:if>

<jsp:include page="header.jsp">
    <jsp:param name="login" value="${currentUser.getLogin()}"/>
    <jsp:param name="page" value="adminPage.jsp"/>
</jsp:include>
<br>
<div class="container">
    <div class="row">
        <div class="col-md-2">
            <button class="btn btn-primary btn-xs" onclick="openCarForm()"><fmt:message key="btn.register_car"/></button>
        </div>
        <div class="col-md-2"></div>
        <div class="col-md-4">
            <a href="controller?command=showUsers"><button class="btn btn-primary btn-xs"><fmt:message key="btn.show_users"/></button></a>
            <a href="controller?command=showCars"><button class="btn btn-primary btn-xs"><fmt:message key="btn.show_cars"/></button></a>
        </div>
        <div class="col-md-1"></div>
        <div class="col-md-2">
            <button class="btn btn-primary btn-xs" onclick="openStaffForm()"><fmt:message key="btn.register_staff"/></button>
        </div>
    </div>
</div>
<br>

<div class="container">
    <div class="row">
        <div class="col-md-2">
            <div class="carReg" id="carForm">
                <form class="form-horizontal" action="controller" method="post">
                    <input type="hidden" name="command" value="carRegistration">
                    <div class="form-group">
                        <label class="control-label"><fmt:message key="label.enter_brand"/>:</label>
                        <input class="form-control" type="text" name="brand" required maxlength="45">
                    </div>

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="th.carClass"/>:</label>
                        <select name="qualityClass">
                            <option value="A">A</option>
                            <option value="B">B</option>
                            <option value="C">C</option>
                            <option value="D">D</option>
                            <option value="E">E</option>
                            <option value="F">F</option>
                            <option value="M">M</option>
                            <option value="S">S</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="label.enter_name"/>:</label>
                        <input class="form-control" type="text" name="name" maxlength="45" required>
                    </div>

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="label.enter_cost_per_day"/>:</label>
                        <input class="form-control" type="number" name="cost" step="0.01" min="0" required>
                    </div>
                    <br>
                    <input class="btn btn-success btn-xs" type="submit" value="<fmt:message key='btn.submit'/>">
                </form>
                <br>
                <button class="btn btn-primary btn-xs" onclick="closeCarForm()"><fmt:message key="btn.close"/></button>
            </div>

            <div class="carEdit" id="carEditForm">
                <form class="form-horizontal" action="controller" method="post">
                    <input type="hidden" name="command" value="carEdit">
                    <input type="hidden" name="id" id="editId">

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="th.carBrand"/>:</label>
                        <input class="form-control" type="text" name="brand" required maxlength="45" id="brandId">
                    </div>

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="th.carClass"/>:</label>
                        <select name="qualityClass">
                            <option id="carCls" value="" selected></option>
                            <option value="A">A</option>
                            <option value="B">B</option>
                            <option value="C">C</option>
                            <option value="D">D</option>
                            <option value="E">E</option>
                            <option value="F">F</option>
                            <option value="M">M</option>
                            <option value="S">S</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="th.carName"/>:</label>
                        <input class="form-control" type="text" name="name" required maxlength="45" id="nameId">
                    </div>

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="th.carCost_per_day"/>:</label>
                        <input class="form-control" type="number" name="cost" step="0.01" required id="costId">
                    </div>

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="th.carIn_usage"/>:</label>
                        <br>
                        <input type="radio" name="inUsage" value="true" required>
                        <label><fmt:message key="label.yes"/></label>
                        <input type="radio" name="inUsage" value="false">
                        <label><fmt:message key="label.no"/></label>
                    </div>

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="th.carDamaged"/>:</label>
                        <br>
                        <input type="radio" name="damaged" value="true" required>
                        <label><fmt:message key="label.yes"/></label>
                        <input type="radio" name="damaged" value="false">
                        <label><fmt:message key="label.no"/></label>
                    </div>
                    <br>
                    <input class="btn btn-success btn-xs" type="submit" value="<fmt:message key='btn.submit'/>">
                </form>
            </div>

        </div>
        <div class="col-md-7">
            <c:if test="${requestScope.containsKey('noUsersError')}">
                <p class="text-center text-danger"><fmt:message key="label.no_users"/></p>
            </c:if>
            <div id="userTable">
                <c:if test="${requestScope.containsKey('userList')}">
                    <form action="controller" method="post">
                        <input type="hidden" name="command" value="userSearch">
                        <input type="text" name="login" placeholder="<fmt:message key="label.nounLogin"/>">

                        <select name="role">
                            <option value="0"><fmt:message key="option.all"/></option>
                            <option value="1">Admin</option>
                            <option value="2">Manager</option>
                            <option value="3">Driver</option>
                            <option value="4">Client</option>
                        </select>

                        <input class="btn btn-primary btn-xs" type="submit" value="<fmt:message key='btn.search'/>">
                    </form>

                    <table id="userTableId" class="table table-bordered table-striped" hidden>
                        <tr>
                            <th>Login</th>
                            <th>Role</th>
                            <th>Status</th>
                            <th>Options</th>
                        </tr>
                        <c:forEach var="user" items="${userList}">
                            <tr>
                                <td>${user.getLogin()}</td>
                                <td>${roleMap.get(user.getRoleId())}</td>
                                <td>
                                    <c:if test="${user.isAffordable() == true}">
                                        <span class='available'><fmt:message key="label.available"/></span>
                                    </c:if>
                                    <c:if test="${user.isAffordable() == false}">
                                        <c:choose>
                                            <c:when test="${user.getRoleId() == 4}">
                                                <span class='banned'><fmt:message key="label.banned"/></span>
                                            </c:when>
                                            <c:when test="${user.getRoleId() == 3}">
                                                <span class='banned'><fmt:message key="label.not_available"/></span>
                                            </c:when>
                                        </c:choose>
                                    </c:if>
                                </td>
                                <td>
                                    <c:if test="${user.getRoleId() == 4}">
                                            <a href="controller?command=userStatusChange&userId=${user.getId()}&aff=${user.isAffordable()}">
                                                <button class="btn btn-primary btn-xs"><fmt:message key="btn.change_status"/></button>
                                            </a>
                                    </c:if>
                                </td>
                                <td>
                                    <c:if test="${user.getId() != sessionScope.currentUser.getId()}">
                                        <a href="controller?command=sendToUserDelete&delUserId=${user.getId()}&delUserPassportId=${user.getPassportId()}">
                                        <button class="btn btn-danger btn-xs"><fmt:message key="label.delete"/></button></a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>

                    <table id="userTablePagination" class="table table-bordered table-striped">
                        <th><fmt:message key="label.nounLogin"/></th>
                        <th><fmt:message key="label.role"/></th>
                        <th><fmt:message key="th.orderStatus"/></th>
                        <th><fmt:message key="th.orderOptions"/></th>
                        <th></th>
                    </table>

                    <c:if test="${userList.size() == 0}">
                        <p class="text-center text-danger"><fmt:message key="label.no_users"/></p>
                    </c:if>

                    <button id="prevButtonUser" class="btn btn-warning"><fmt:message key="btn.previous"/></button>
                    <button id="nextButtonUser" class="btn btn-warning"><fmt:message key="btn.next"/></button>

                    <script src="/assets/js/userPagination.js"></script>

                    <button class="btn btn-primary btn-xs" onclick="closeUserTable()"><fmt:message key="btn.close"/></button>
                    <img onclick="createUserPDF(), printUser()" class="pdfImg" src="images/pdf.png">
                </c:if>
            </div>

            <c:if test="${requestScope.containsKey('noCarsError')}">
                <p class="text-center text-danger"><fmt:message key="label.no_car"/></p>
            </c:if>
            <div id="carTable">
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

                        <input class="btn btn-primary btn-xs" type="submit" value="<fmt:message key='btn.search'/>">
                    </form>

                    <table id="carTableId" class="table table-bordered table-striped" hidden>
                        <tr>
                            <th>Brand</th>
                            <th>Class</th>
                            <th onclick="sortCarTable(2)">Name</th>
                            <th onclick="sortCarTable(3)">Cost per day</th>
                            <th>In usage</th>
                            <th>Damaged</th>
                            <th>Options</th>
                        </tr>
                        <c:forEach var="car" items="${carList}">
                            <tr>
                                <td>${car.getBrand()}</td>
                                <td>${car.getQualityClass()}</td>
                                <td>${car.getName()}</td>
                                <td><localeDoubleTag:localeDoubleTag locale="${currentLocale}" number="${car.getCost()}"/></td>
                                <td>${car.isUsed()}</td>
                                <td>${car.isDamaged()}</td>

                                <td><button class="btn btn-primary btn-xs" onclick="openCarEditForm(${car.getId()}, '${car.getBrand()}', '${car.getName()}', ${car.getCost()}, '${car.getQualityClass()}')"><fmt:message key="label.edit"/></button></td>
                                <td> <a href="controller?command=sendToCarDelete&delCarId=${car.getId()}"><button class="btn btn-danger btn-xs"><fmt:message key="label.delete"/></button></a> </td>

                            </tr>
                        </c:forEach>
                    </table>

                    <table id="carTablePagination" class="table table-bordered table-striped">
                        <th><fmt:message key="th.carBrand"/></th>
                        <th><fmt:message key="th.carClass"/></th>
                        <th onclick="sortCarTable(2), renderTable()"><fmt:message key="th.carName"/></th>
                        <th onclick="sortCarTable(3), renderTable()"><fmt:message key="th.carCost_per_day"/></th>
                        <th><fmt:message key="th.carIn_usage"/></th>
                        <th><fmt:message key="th.carDamaged"/></th>
                        <th><fmt:message key="th.orderOptions"/></th>
                        <th></th>
                    </table>

                    <c:if test="${carList.size() == 0}">
                        <p class="text-center text-danger"><fmt:message key="label.no_car"/></p>
                    </c:if>

                    <button id="prevButtonCar" class="btn btn-warning"><fmt:message key="btn.previous"/></button>
                    <button id="nextButtonCar" class="btn btn-warning"><fmt:message key="btn.next"/></button>

                    <script src="/assets/js/carPagination.js"></script>

                    <button class="btn btn-primary btn-xs" onclick="closeCarTable()"><fmt:message key="btn.close"/></button>
                    <img onclick="createCarPDF(), printCar()" class="pdfImg" src="images/pdf.png">
                </c:if>
            </div>
        </div>
        <div class="col-md-3">
            <div class="staffReg" id="staffForm">
                <form class="form-horizontal" action="controller" method="post">
                    <input type="hidden" name="command" value="userRegistration">
                    <div class="form-group">
                        <label class="control-label"><fmt:message key="login_jsp.label.enter_login"/>:</label>
                        <input class="form-control" type="text" name="login" required minlength="3" maxlength="45">
                    </div>

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="label.enter_email"/>:</label>
                        <input class="form-control" type="email" name="email" required minlength="7" maxlength="45">
                    </div>

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="login_jsp.label.enter_psd"/>:</label>
                        <input class="form-control" type="password" name="password" id="password" onkeyup="check()" required minlength="5" maxlength="50">
                    </div>

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="label.confirm_password"/>:</label>
                        <input class="form-control" type="password" name="confirm_password" id="confirm_password" onkeyup="check()" required>
                        <span id='message'></span>
                        <p id="notMatchId" hidden><fmt:message key="label.no_matching"/></p>
                        <p id="matchId" hidden><fmt:message key="label.matching"/></p>
                    </div>

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="label.role"/>:</label>
                        <select name="roleId">
                            <option value="1">Admin</option>
                            <option value="2">Manager</option>
                            <option value="3">Driver</option>
                        </select>
                    </div>
                    <br>
                    <input class="btn btn-success btn-xs" type="submit" disabled value="<fmt:message key='btn.verbRegister'/>" id="sbtn">
                </form>
                <br>
                <button class="btn btn-primary btn-xs" onclick="closeStaffForm()"><fmt:message key="btn.close"/></button>
            </div>
        </div>
    </div>
</div>

</body>
</html>
