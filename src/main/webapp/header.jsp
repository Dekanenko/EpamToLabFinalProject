<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="flagLib"%>

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="/assets/css/style.css">
    <script src="/assets/js/popUp.js"></script>
    <script src="/assets/js/carTableSort.js"></script>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-0evHe/X+R7YkIZDRvuzKMRqM+OrBnVFBL6DOitfPri4tjfHxaWutUpFmBp4vmVor" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-pprn3073KE6tl6bjs2QrFaJGz5/SUsLqktiwsUTF55Jfv3qYSDhgCecCxMW52nD2" crossorigin="anonymous"></script>

</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-2">
            <c:if test="${param.page != null}">
                <form method="post" action="changeLocale.jsp?page=/${param.page}">
                    <flagLib:defineFlag locale="${currentLocale}"/>
                    <select name="locale" onchange="this.form.submit()">
                        <option><fmt:message key="label.language"/></option>
                        <option value="en">en</option>
                        <option value="uk">uk</option>
                    </select>
                </form>
            </c:if>
        </div>
        <div class="col-md-7">
            <h1><fmt:message key="header_jsp.welcome"/> ${param.login} !</h1>
        </div>
        <div class="col-md-2">
            <a href="/userEdit.jsp"><button class="btn btn-default bnt-xs"><fmt:message key="header_jsp.btn.edit_account"/></button></a>
        </div>
        <div class="col-md-1">
            <a href="/controller?command=logOut"><button class="btn btn-default bnt-xs"><fmt:message key="header_jsp.btn.log_out"/></button></a>
        </div>
    </div>
</div>
</body>
</html>
