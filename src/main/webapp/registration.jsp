<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="flagLib"%>

<!doctype html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <link rel="stylesheet" href="/assets/css/style.css">
  <script src="/assets/js/verification.js"></script>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-0evHe/X+R7YkIZDRvuzKMRqM+OrBnVFBL6DOitfPri4tjfHxaWutUpFmBp4vmVor" crossorigin="anonymous">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-pprn3073KE6tl6bjs2QrFaJGz5/SUsLqktiwsUTF55Jfv3qYSDhgCecCxMW52nD2" crossorigin="anonymous"></script>
  <title>Registration</title>
</head>
<body>
  <div class="container">
    <br>
    <div class="row">
      <div class="col-md-3">
        <form method="post" action="changeLocale.jsp?page=/registration.jsp">
          <flagLib:defineFlag locale="${currentLocale}"/>
          <select name="locale" onchange="this.form.submit()">
            <option><fmt:message key="label.language"/></option>
            <option value="en">en</option>
            <option value="uk">uk</option>
          </select>
        </form>
      </div>
      <div class="col-md-6">
        <form action="controller" method="post" class="form-horizontal">
          <input type="hidden" name="command" value="userRegistration">
          <input type="hidden" name="roleId" value="4">

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
          <br>
          <input type="submit" disabled value="<fmt:message key='btn.sign_up'/>" id="sbtn" class="btn btn-success">
        </form>
        <br>
        <a href="login.jsp"><input type="button" value="<fmt:message key='label.login'/>" class="btn btn-primary btn-xs"></a>
        <br>
        <br>
      </div>
      <div class="col-md-3"></div>
    </div>
  </div>
</body>
</html>

