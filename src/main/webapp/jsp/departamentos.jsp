<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Gestión de empleados</title>
</head>
<body>
<c:forEach items="${departamentos}" var="dpto">
<h1>${dpto.id} - ${dpto.desc}</h1>
	<table>
		<tr><th>Cif</th><th>Nombre</th><th>Apellidos</th><th>Edad</th></tr>
		<c:forEach items="${dpto.empleados}" var="empleado">
			<tr>
				<td>${empleado.cif}</td>
				<td>${empleado.nombre}</td>
				<td>${empleado.apellidos}</td>
				<td>${empleado.edad}</td>
			</tr>
		</c:forEach>
	</table>
</c:forEach>
</body>
</html>















