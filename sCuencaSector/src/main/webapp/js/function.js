
function print_today() {
	  var now = new Date();
	  var months = new Array('01','02','03','04','05','06','07','08','09','10','11','12');
	  var date = ((now.getDate()<10) ? "0" : "")+ now.getDate();
	  function fourdigits(number) {
	    return (number < 1000) ? number + 1900 : number;
	  }
	  var today =  date + "/"+ months[now.getMonth()] + "/" + (fourdigits(now.getYear()));
	  return today;
}

function print_toMontYear() {
	  var now = new Date();
	  var months = new Array('Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Setiembre','Octubre','Noviembre','Diciembre');
	  var date = ((now.getDate()<10) ? "0" : "")+ now.getDate();
	  function fourdigits(number) {
	    return (number < 1000) ? number + 1900 : number;
	  }
	  var today = months[now.getMonth()] + " " + (fourdigits(now.getYear()));
	  return today;
}

function print_toMontYearNumero() {
	  var now = new Date();
	  var months = new Array('01','02','03','04','05','06','07','08','09','10','11','12');
	  var date = ((now.getDate()<10) ? "0" : "")+ now.getDate();
	  function fourdigits(number) {
	    return (number < 1000) ? number + 1900 : number;
	  }
	  var today = months[now.getMonth()] + "/" + (fourdigits(now.getYear()));
	  return today;
}


function refrescarPagina() {
	location.reload();
}

mensajeError = function(mensaje){
	$("#divMsj").removeClass;
	$("#divMsj").addClass("error");
	$("#idmensaje").html(mensaje);
	$("#divMsj").fadeIn();
	$("#divMsj").fadeOut(10000);
};


mensajeAlerta = function(mensaje){
	$("#divMsj").removeClass;
	$("#divMsj").addClass("alerta");
	$("#idmensaje").html(mensaje);
	$("#divMsj").fadeIn();
	$("#divMsj").fadeOut(9000);
};

mensajeExito = function(mensaje){
	$("#divMsj").removeClass;
	$("#divMsj").addClass("exito");
	$("#idmensaje").html(mensaje);
	$("#divMsj").fadeIn();
	$("#divMsj").fadeOut(7000);
};

//LIMPIA FORMULARIO
limpiaForm = function(miForm) {
	// recorremos todos los campos que tiene el formulario
	$(':input', miForm).each(function() {
		var type = this.type;
		var tag = this.tagName.toLowerCase();
		// limpiamos los valores de los campos…
		if (type == 'text' || type == 'password' || tag == 'textarea' || type=='hidden')
			this.value = "";
		// excepto de los checkboxes y radios, le quitamos el checked
		// pero su valor no debe ser cambiado
		else if (type == 'checkbox' || type == 'radio')
			this.checked = false;
		// los selects le ponesmos el indice a -
		else if (tag == 'select')
			this.selectedIndex = 0;
	});
};

//LIMPIA DIV
limpiaDiv = function(div) {
	// recorremos todos los campos que tiene el formulario
	$(':input', div).each(function() {
		var type = this.type;
		var tag = this.tagName.toLowerCase();
		// limpiamos los valores de los campos…
		if (type == 'text' || type == 'password' || tag == 'textarea' || type=='hidden')
			this.value = "";
		// excepto de los checkboxes y radios, le quitamos el checked
		// pero su valor no debe ser cambiado
		else if (type == 'checkbox' || type == 'radio')
			this.checked = false;
		// los selects le ponesmos el indice a -
		else if (tag == 'select')
			this.selectedIndex = 0;
	});	
};

//ESCRIBE EN VARIABLE LOS CHECK BOX SELECIONADOS PASRA DIV
function checkboxSelecionados(div,variable) {
	// recorremos todos los campos que tiene el formulario
	$(':input', div).each(function() {
		var type = this.type;
		// pero su valor no debe ser cambiado
		if (type == 'checkbox' || type == 'radio'){
			//alert(this.name);
			var checkeado=$("#" + this.name).attr("checked");
			if(checkeado){
				//alert(this.name + " :: esta marcado>> " + variable);
				var value = $("#"+ variable).val() + ";" + this.name;
				$("#"+variable).val(value);
			}
		}				
	});	
};

