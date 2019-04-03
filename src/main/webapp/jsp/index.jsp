<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>web de empleados que utiliza microservicios</title>
</head>
<body>
<p>
<a id="link_gestor" href="gestEmpleados">App de empleados</a>
</p>
<p>Los usuarios son profe - profe y admin - admin
<p>
<form:form action="logout">
<input type="submit" id="btn_logout" value="Logout" />
</form:form>
</p>
</body>
</html>