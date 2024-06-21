
function jeObj(id){
    return $("#"+id);
}
function jeVal(id){
    return $("#"+id).val().length>0;
}
function jeStr(id){
    return $("#"+id).val();
}
function jeInt(id){
    return parseInt(($("#"+id).val().length>0?$("#"+id).val():"0"),10);
}
function jeDec(id){
    return parseFloat($("#"+id).val().length>0?$("#"+id).val():"0.00");
}

function jeI(obj){
    if(obj.value!=""){
        obj.style.backgroundColor="#FFFBCF";
    }else{
        obj.style.backgroundColor="#FFD7D7";
    }
}
function jeSmt(id){
    $("#"+id).val("Espere...");
    $("#"+id).attr("disabled","true");
}
function jeHover(obj){
    $(obj).fadeTo(100, 0.7);
    setTimeout($(obj).fadeTo(200, 1), 100);
}
function jeUpper(obj){
    obj.value=obj.value.toUpperCase() ;
}
function jeSH(n){
    if($("#"+n).css("display")=="none"){
        $("#"+n).show("fast");
    }else{
        $("#"+n).hide("fast");
    }
}
function jeEffectShow(id) {
    $("#"+id).fadeTo(0, 0);
    $("#"+id).fadeTo(500, 1);
}
function jeEffectHide(id) {
    $("#"+id).hide();
}
function jeButton(prefijo, n, s){
    var i=1;
    for(i=1;i<=n;i++){
        if(i!=s){$("#"+prefijo+""+i).button();$("#"+prefijo+""+i).addClass("l");}
        else{$("#"+prefijo+""+i).addClass("ui-state-active ui-corner-all nr");}
    }
}
function jeLoad(t){
    if(t==0){
        $("#_xloader").show();
    }else{
        $("#_xloader").hide();
    }
}
function delAZ(o){
    var t=o.value;
    o.value=t.replace(/[a-z]/gi,'');
}
function delCom(o){
    var t=o.value;
    o.value=t.replace(/,/gi,'.');
}
function delLine(o){
    var t=o.value;
    o.value=t.replace(/\n/gi,'');
}
function jeNumeric(input){
    var RE = /^-{0,1}\d*\.{0,1}\d+$/;
    return (RE.test(input));
}
function jeIsNumeric(value){
    var rpta=parseFloat(value);
    return !isNaN(rpta);
}
function jeRow(n){
    $("#"+n).fadeTo("normal",0.3);
    setTimeout('$("#'+n+'").fadeTo("fast",1);', 300);
}
function jeAlert(msg){
    $dialog = $('<div></div>')
    .dialog({
        autoOpen: false,
        title: 'Atención',
        modal: true,
        buttons: {
            Ok: function() {
                $( this ).dialog( "close" );
            }
                }
        });
    $dialog.text(msg);
    $dialog.dialog('open');
}
function jeConfirm(msg){
    $dialog = $('<div></div>')
    .dialog({
        autoOpen: false,
        title: 'Atención',
        modal: true,
        buttons: {
            "Si": function() {
                $( this ).dialog( "close" );
                return true;
            },"No": function() {
                $( this ).dialog( "close" );
                return false;
            }
                }
        });
    $dialog.text(msg);
    $dialog.dialog('open');
}

//utils----------
function jeRound(num, dec){
    return (Math.round(num*Math.pow(10,dec))/Math.pow(10,dec));
}
function porcentComplete(n100,nX){
    return jeRound((100*nX)/n100,1);
}
function jeReloj(id){
    t = new Date();
    hora = t.getHours();minuto = t.getMinutes();segundo = t.getSeconds();
    $("#"+id).html((hora<10?"0"+hora:hora) + " : " + (minuto<10?"0"+minuto:minuto) + " : " + (segundo<10?"0"+segundo:segundo));
    setTimeout("jeReloj('"+id+"')",1000);
}
function getLastDay(anio, mes){
    if(mes==1 || mes==3 || mes==5 || mes==7 || mes==8 || mes==10 || mes==12){return 31;}
    else if(mes==4 || mes==6 || mes==9 || mes==11){return 30;}
    else{
        if((anio % 4 == 0) && ((anio % 100 != 0) || (anio % 400 == 0))){
            return 29;
        }else{
            return 28;
        }
    }
}
function jeProgressBar(color, width, percent) {
    var exeso=percent>100;
    var pixels = width * ((exeso?100:percent) / 100);
    var html='';
    html+='<div class="smallish-progress-wrapper" style="width: ' + width + 'px;">';
        html+='<div class="smallish-progress-bar" style="width: ' + pixels + 'px; background-color: ' + (exeso?'#FED4C8':color) + ';"></div>';
        html+='<div class="smallish-progress-text" style="width: ' + width + 'px;"> ' + percent + ' % </div>';
    html+='</div>';
    return html;
}

//prototypes ---

Date.prototype.addDays = function (dias){
    var fecha1 = new Date(this.getFullYear(), this.getMonth(),1);
    var fecha2 = new Date(this.getFullYear(), this.getMonth(),2);
    var diferencia = fecha2.getTime() - fecha1.getTime();
    var luego = new Date();
    luego.setTime( this.getTime() + (dias * diferencia )  );
    return luego;
};
Date.prototype.addDaysWeekEnds = function (dias){
    var fecha1 = new Date(this.getFullYear(), this.getMonth(),1);
    var fecha2 = new Date(this.getFullYear(), this.getMonth(),2);
    var dif = fecha2.getTime() - fecha1.getTime();
    var i=0;
    var fecha=new Date(this.getTime());
    for(i=0;i<dias;i++){
        fecha.setTime(this.getTime()+dif*i);
        if(fecha.getDay()==0 || fecha.getDay()==6)dias++;
    }
    return fecha;
};

Date.prototype.addDateTime = function (dias,horas,minutos){
    var inicio=new Date(this.getTime());
    var i=0; //var totalDias=dias;
    //minutos
    var min=new Date(inicio.getTime());
        min.setTime(min.getTime()+1000*60*minutos);
    //horas
    var hora=new Date(min.getTime());
    for(i=0;i<horas;i++){
        if(hora.getHours()==13){hora.setTime(hora.getTime()+1000*60*60);}//a la 1pm inicia break hasta 2pm (+1h)
        hora.setTime(hora.getTime()+1000*60*60);
        if(hora.getHours()==18){//si es 7pm pasar al dia siguiente sumando las 15 horas
            hora.setTime(hora.getTime()+1000*60*60*15);
            dias++;
        }
    }
    //dias
    var fecha=new Date(hora.getTime());
    for(i=0;i<dias;i++){
        fecha.setTime(fecha.getTime()+1000*60*60*24);
        if(fecha.getDay()==0 || fecha.getDay()==6)dias++;
    }
    return fecha;

};
