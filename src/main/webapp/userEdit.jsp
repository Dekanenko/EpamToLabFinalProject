<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="flagLib"%>
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
    <script src="/assets/js/verification.js"></script>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-0evHe/X+R7YkIZDRvuzKMRqM+OrBnVFBL6DOitfPri4tjfHxaWutUpFmBp4vmVor" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-pprn3073KE6tl6bjs2QrFaJGz5/SUsLqktiwsUTF55Jfv3qYSDhgCecCxMW52nD2" crossorigin="anonymous"></script>

    <title>Edit Page</title>
</head>
<body>

<c:if test="${!sessionScope.containsKey('currentUser')}">
    <c:redirect url="errorPage.jsp"/>
</c:if>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-2">
            <form method="post" action="changeLocale.jsp?page=/userEdit.jsp">
                <flagLib:defineFlag locale="${currentLocale}"/>
                <select name="locale" onchange="this.form.submit()">
                    <option><fmt:message key="label.language"/></option>
                    <option value="en">en</option>
                    <option value="uk">uk</option>
                </select>
            </form>
        </div>
        <div class="col-md-10">
            <a href="/managePage"><button class="btn btn-primary btn-xs"><fmt:message key="btn.main_page"/></button></a>
            <a href="/controller?command=logOut"><button class="btn btn-primary btn-xs"><fmt:message key="header_jsp.btn.log_out"/></button></a>

            <c:if test="${currentUser.getRoleId() == 4}">
                <a href="/controller?command=sendToUserDelete&delUserId=${currentUser.getId()}&delUserPassportId=${currentUser.getPassportId()}">
                    <button class="btn btn-primary btn-xs"><fmt:message key="label.delete"/></button></a>
                <c:if test="${currentUser.getPassportId() <= 0}">
                    <c:set var="comm" value="addPassport"/>
                    <button onclick="openPassportForm()" class="btn btn-primary btn-xs"><fmt:message key="btn.add_passport"/></button>
                </c:if>
                <c:if test="${currentUser.getPassportId() > 0}">
                    <c:set var="comm" value="editPassport"/>
                    <button onclick="openPassportForm()" class="btn btn-primary btn-xs"><fmt:message key="btn.edit_passport"/></button>
                    <a href="controller?command=showPassport&passportId=${currentUser.getPassportId()}">
                        <button class="btn btn-primary btn-xs"><fmt:message key="btn.show_passport"/></button></a>
                    <a href="/controller?command=deletePassport&userId=${currentUser.getId()}&passportId=${currentUser.getPassportId()}">
                        <button class="btn btn-primary btn-xs"><fmt:message key="btn.remove_passport"/></button>
                    </a>
                </c:if>
            </c:if>
        </div>
    </div>
</div>

<div class="container">
    <div class="row">
        <div class="col-md-2">
            <p><fmt:message key="label.currentBalance"/>: <localeDoubleTag:localeDoubleTag locale="${currentLocale}" number="${currentUser.getCash()}"/></p>
        </div>
        <div class="col-md-6">
            <br>
            <form action="controller" method="post" class="form-horizontal">
                <input type="hidden" name="command" value="userEdit">
                <input type="hidden" name="id" value="${currentUser.getId()}">
                <input type="hidden" name="roleId" value="${currentUser.getRoleId()}">
                <input type="hidden" name="passportId" value="${currentUser.getPassportId()}">
                <input type="hidden" name="aff" value="${currentUser.isAffordable()}">
                <input type="hidden" name="firstCash" value="${currentUser.getCash()}">
                <br>
                <div class="form-group">
                    <label class="control-label"><fmt:message key="label.nounLogin"/>:</label>
                    <input class="form-control" type="text" name="login" required minlength="3" maxlength="45" value="${currentUser.getLogin()}">
                </div>

                <div class="form-group">
                    <label class="control-label"><fmt:message key="label.email"/>:</label>
                    <input class="form-control" type="email" name="email" required minlength="7" maxlength="45"  value="${currentUser.getEmail()}">
                </div>

                <div class="form-group">
                    <label class="control-label"><fmt:message key="label.top_up_your_account"/>:</label>
                    <input class="form-control" type="number" name="cash" value="0" step="0.01" min="0">
                </div>

                <div class="form-group">
                    <label class="control-label"><fmt:message key="label.password"/>:</label>
                    <input class="form-control" type="password" name="password" id="password" onkeyup="check()" required minlength="5" maxlength="50">
                </div>

                <div class="form-group">
                    <label class="control-label"><fmt:message key="label.confirm_password"/>:</label>
                    <input class="form-control" type="password" name="confirm_password" id="confirm_password" onkeyup="check()" required>
                    <span id='message'></span>
                    <p id="notMatchId" hidden><fmt:message key="label.no_matching"/></p>
                    <p id="matchId" hidden><fmt:message key="label.matching"/></p>
                </div>
                <br>
                <input class="btn btn-success btn-xs" type="submit" disabled value="<fmt:message key="label.edit"/>" id="sbtn">
                <c:if test="${requestScope.containsKey('userEditError')}">
                    <p class="text-center text-danger"><fmt:message key="label.user_edit_error"/></p>
                </c:if>
            </form>

            <div id="passportTable">
                <c:if test="${requestScope.containsKey('passport')}">
                    <br>
                    <table class="table table-bordered table-striped">
                        <tr>
                            <th><fmt:message key="label.userName"/></th>
                            <th><fmt:message key="label.userSurname"/></th>
                            <th><fmt:message key="label.userUniqueNum"/></th>
                        </tr>
                        <tr>
                            <td>${passport.getName()}</td>
                            <td>${passport.getSurname()}</td>
                            <td>${passport.getUniqueNum()}</td>
                        </tr>
                    </table>
                    <button onclick="closePassportTable()" class="btn btn-primary btn-xs"><fmt:message key="btn.close"/></button>
                </c:if>
                <c:if test="${requestScope.containsKey('getPassportError')}">
                    <p class="text-center text-danger"><fmt:message key="label.error_passport_get"/></p>
                </c:if>
            </div>
        </div>
        <div class="col-md-4">
            <br>
            <div class="passportForm" id="passportForm">
                <form action="controller" method="post" class="form-horizontal">
                    <input type="hidden" name="command" value="${comm}">
                    <input type="hidden" name="userId" value="${currentUser.getId()}">
                    <input type="hidden" name="passportId" value="${currentUser.getPassportId()}">

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="label.userName"/>:</label>
                        <input class="form-control" type="text" name="name" required minlength="3" maxlength="45">
                    </div>

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="label.userSurname"/>:</label>
                        <input class="form-control" type="text" name="surname" required minlength="3" maxlength="45">
                    </div>

                    <div class="form-group">
                        <label class="control-label"><fmt:message key="label.userUniqueNum"/>:</label>
                        <input class="form-control" type="text" name="uniqueNum" required minlength="5" maxlength="45">
                    </div>
                    <br>
                    <input class="btn btn-success btn-xs" type="submit" value="<fmt:message key='btn.submit'/>">
                    <br>
                    <br>
                </form>
                <br>
                <button onclick="closePassportForm()" class="btn btn-primary btn-xs"><fmt:message key="btn.close"/></button>
            </div>
            <c:if test="${requestScope.containsKey('passportError')}">
                <p class="text-center text-danger"><fmt:message key="label.unique_num_error"/></p>
            </c:if>
        </div>
    </div>
</div>
</body>
</html>

