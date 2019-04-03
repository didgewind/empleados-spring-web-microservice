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
<form:form modelAttribute="empleado">
<p><form:errors path="cif" />
<c:if test="${opcion!='muestra' and opcion!='elimina'}">
<form:errors path="nombre" />
<form:errors path="apellidos" />
<form:errors path="edad" />
</c:if>
</p>
<p><spring:message code="cif"/>: <form:input type="text" path="cif" id="input_cif"/></p>
<p><spring:message code="nombre"/>: <form:input type="text" path="nombre" id="input_nombre"/></p>
<p><spring:message code="apellidos"/>: <form:input type="text" path="apellidos" id="input_apps"/></p>
<p><spring:message code="edad"/>: <form:input type="text" path="edad" id="input_edad"/></p>
<p>
	<input type="submit" name="muestraUno" value="Muestra Uno" />
	<input type="submit" name="muestraTodos" value="Muestra Todos" />
	<input type="submit" name="inserta" value="Inserta" />
	<input type="submit" name="modifica" value="Modifica" />
	<input type="submit" name="elimina" value="Elimina" />
</p>
</form:form>
<p><a href="/">Inicio</a></p>
<p>
<c:choose>
	<c:when test="${opcion=='muestra' }">
		${empleado}
	</c:when>
	<c:when test="${opcion=='muestraTodos' }">
		<table>
			<tr><th>Cif</th><th>Nombre</th><th>Apellidos</th><th>Edad</th></tr>
			<c:forEach items="${listaEmpleados}" var="empleado">
				<tr>
					<td>${empleado.cif}</td>
					<td>${empleado.nombre}</td>
					<td>${empleado.apellidos}</td>
					<td>${empleado.edad}</td>
				</tr>
			</c:forEach>
		</table>
	</c:when>
</c:choose>
</p>
		${mensaje}
</body>
</html>















