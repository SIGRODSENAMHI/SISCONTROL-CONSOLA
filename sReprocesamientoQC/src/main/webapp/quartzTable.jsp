<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="js/jquery-1.8.3.js"></script>
<script type="text/javascript">
	$('.update-quartz').click(function () {
		var start = 1;
		var stop = 0;
		var row = $(this);
		var status = $(this).closest("tr").find('td:eq(10)').text();
	    var codigo = $(this).closest("tr").find('td:eq(0)').text();
	    var enumName = $(this).closest("tr").find('td:eq(2)').text();
	    var frecuencia = $(this).closest("tr").find("td:eq(4) input[type='text']").val();
	    var datos = "enumName="+enumName+"&codigo="+codigo+"&frecuencia="+frecuencia;
	    if(status == start){
	    	$.post("QuartzStop", datos,
    				function(resultado) {
	    			console.log(resultado);
		    		var rpta = new Array();
					var rpta = resultado.split(";");
    					if (rpta[0] == 1) {
    						row.closest("tr").find("td:eq(8) img").attr('src','iconos/0.png');
    						row.closest("tr").find('td:eq(10)').text("0");
    						row.closest("tr").find('td:eq(11)').text("En Pausa");
//     						$("#divMsj").addClass("exito");
    						$("#idmensaje").html(rpta[1]);
//     						$("#divMsj").fadeIn();
//     						$("#divMsj").fadeOut(7000);
    					} else {
//     						$("#divMsj").addClass("error");
    						$("#idmensaje").html("¡ " + rpta[1] + " !");
//     						$("#divMsj").fadeIn();
//     						$("#divMsj").fadeOut(7000);
    					}
    				});
	    }else{
	    	$.post("QuartzStart", datos,
    				function(resultado) {
		    		var rpta = new Array();
					var rpta = resultado.split(";");
    					if (rpta[0] == 1) {
    						row.closest("tr").find("td:eq(8) img").attr('src','iconos/1.png');
    						row.closest("tr").find('td:eq(10)').text("1");
    						row.closest("tr").find('td:eq(11)').text("En Ejecución");
//     						$("#divMsj").addClass("exito");
    						$("#idmensaje").html(rpta[1]);
//     						$("#divMsj").fadeIn();
//     						$("#divMsj").fadeOut(7000);
    					} else {
//     						$("#divMsj").addClass("error");
    						$("#idmensaje").html("¡ " + rpta[1] + " !");
//     						$("#divMsj").fadeIn();
//     						$("#divMsj").fadeOut(7000);
    					}
    				});
	    }
	});
</script>
</head>
<body>
	<form id="frm">
	<!-- DIV QUE CONTIENE LOS MENSAJES -->
<!-- 		<div id="divMsj" style="display: none;"><span id="idmensaje"></span></div> -->
		<div id="divMsj" ><span id="idmensaje"></span></div>
	<!-- -->
	<table >
	<c:forEach items="${quartzList}" var="bean">
		<tr>
			<td>${bean.codigo}</td>
			<td style="display: none;">${bean.className}</td>
			<td style="display: none;">${bean.enumName}</td>
			<td>${bean.descripcion}</td>
			<td><input type="text" value='${bean.frecuencia}'/></td>
			<td style="display: none;">${bean.jobId}</td>
			<td style="display: none;">${bean.triggerId}</td>
			<td style="display: none;">${bean.logFilePath}</td>
			<td><img alt="" src="iconos/${bean.jobControl}.png" class="update-quartz"></td>
			<td><img alt="" src="iconos/download.png" title="Descargar Log"></td>
			<td style="display: none;">${bean.jobControl}</td>
			<c:if test="${bean.jobControl eq 1}">
				<td>En ejecución</td>
			</c:if>
			<c:if test="${bean.jobControl eq 0}">
				<td>En pausa</td>
			</c:if>
		</tr>
	</c:forEach>
	</table>
	</form>
</body>
</html>