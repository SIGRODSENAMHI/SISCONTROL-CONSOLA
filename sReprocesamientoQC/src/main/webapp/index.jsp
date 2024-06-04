<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<!-- FUNCIONES -->
	<script type="text/javascript" src="js/jquery-1.8.3.js"></script>
	<script type="text/javascript">
	redirecciona = function(controller){
		$("#WORK").empty();
		var aleatorio = Math.floor(Math.random() * 152) + 57;
		$("#WORK").load(controller);
		return false;
	};
	</script>
</head>
<body>
	<a class="parent"><span style="color: rgb(0,171,225);font-family: Century Gothic" onclick="redirecciona('IrAControlQuartz')">Ir A Control Quartz</span></a>
	<div id="WORK"></div>
</body>
</html>
