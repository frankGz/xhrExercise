/**
 *  oh-li-gei!
 *  The difference is scoping. var is scoped to the nearest function block and let is scoped to the nearest enclosing block, which can be smaller than a function block. Both are global if outside any block.
 *
 *	Also, variables declared with let are not accessible before they are declared in their enclosing block. As seen in the demo, this will throw a ReferenceError exception.
 */

var sisDiv;
var primeDiv;
var dashDiv;
var sisHead;
var primeHead;
var dashHead;

window.onload = function(){
	sisDiv = document.getElementById("sisDiv");
	primeDiv = document.getElementById("primeDiv");
	dashDiv = document.getElementById("dashDiv");
	sisHead = document.getElementById("sisHead");
	primeHead = document.getElementById("primeHead");
	dashHead = document.getElementById("dashHead");
}

function showDash(){
	dashDiv.style.display = "inline";
	primeDiv.style.display = "none";
	sisDiv.style.display = "none";

	dashHead.style.display = "";
	primeHead.style.display = "none";
	sisHead.style.display = "none";
}

function showPrime(){
	dashDiv.style.display = "none";
	primeDiv.style.display = "inline";
	sisDiv.style.display = "none";

	dashHead.style.display = "none";
	primeHead.style.display = "";
	sisHead.style.display = "none";
}

function showSis(){
	dashDiv.style.display = "none";
	primeDiv.style.display = "none";
	sisDiv.style.display = "inline";

	dashHead.style.display = "none";
	primeHead.style.display = "none";
	sisHead.style.display = "";
}

/*
function init()
{
	show("dashDiv");
}

function show(v)
{
	hideAll();
	let n = document.getElementById(v);
	n.style.display = "block";
}

function hideAll()
{
	let list = document.getElementsByClassName("view");
	for (let e of list)
	{
		e.style.display = "none";
	}
}
*/

function prime(f)
{
	let min = f.elements["min"].value;
	let max = f.elements["max"].value;
	let qs = "min=" + min + "&max=" + max + "&calc"; 
	doSimpleAjax("Prime.do", qs, primeResult);
}

function rePrime(){
	let min = document.getElementById("last").value;
	let max = document.getElementById("max").value;
	document.getElementById("min").value = min;
	let qs = "min=" + min + "&max=" + max; 
	doSimpleAjax("Prime.do", qs, primeResult);
}

function primeResult(request)
{
	if (request.readyState==4 && request.status==200)
	{
		console.log(request.responseText);
		let resp = JSON.parse(request.responseText);
		let html = "";
		if (resp.status == 1)
		{ 
			html += "<h4 class=\"text-center\" id=\"result\">" + resp.result;
			html += "<input value=\"" + resp.result + "\" id=\"last\" type=\"hidden\"/>"
			html += " <button type='button' id=\"recalc\" onclick='rePrime()'>next</button></h4> "
		}
		else
		{
			html += "<h4 style='color:red' class='text-center'><i>" + resp.error + "</i></h4>";
		}
		document.getElementById("primeResult").innerHTML = html;
	}
}

function sis(f){
	let prefix = f.elements["prefix"].value;
	let minGpa = f.elements["minGpa"].value;
	let sortBy = f.elements["sortBy"].value;
	
	let qs = "prefix=" + prefix + "&minGpa=" + minGpa + "&sortBy=" + sortBy;
	console.log(qs);
	doSimpleAjax("Sis.do", qs, sisResult);
}

function sisResult(request)
{
	if (request.readyState==4 && request.status==200)
	{
		console.log(request.responseText);
		let resp = JSON.parse(request.responseText);
		let table = "";
		let message = "";
		if (resp.status == 1)
		{ 
			message += "<i>" + resp.message + "</i>";
			for(var i in resp.list){
				table += "<tr>";
				table += "<td style='padding: 6px'>" + resp.list[i].name + "</td>";
				table += "<td style='padding: 6px'>" + resp.list[i].major + "</td>";
				table += "<td style='padding: 6px'>" + resp.list[i].courses + "</td>";
				table += "<td style='padding: 6px'>" + resp.list[i].gpa + "</td>";
				table += "</tr>";
			}
		}
		else
		{
			message += "<i style='color:red'>" + resp.messae + "</i>";
		}
		document.getElementById("sisTable").innerHTML = table;
		document.getElementById("sisMessage").innerHTML = message;
	}
}
function doSimpleAjax(address, data, handler)
{
    var request = new XMLHttpRequest();
    request.onreadystatechange = function() {handler(request);};
    request.open("GET", (address + "?" + data), true);
    request.send(null);
}


