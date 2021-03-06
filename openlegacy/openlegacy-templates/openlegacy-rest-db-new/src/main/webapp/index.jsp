<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.springframework.context.*" %>
<%@ page import="org.springframework.web.context.support.*" %>
<%@ page import="org.openlegacy.db.services.*" %>
<%@ page import="org.openlegacy.db.definitions.*" %>
<%@ page import="java.util.*" %>
<%
	ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext.getServletContext());
	DbEntitiesRegistry registry = context.getBean(DbEntitiesRegistry.class);
	Collection<DbEntityDefinition> entityDefinitions = registry.getEntitiesDefinitions();

%>

<html>
<head>
<script>
    dojoConfig= {
        parseOnLoad: true,
        async: false
    };

    function loadData(data){
    	if (dojo.byId("requestType").value == "json"){
    		if (data != null){
    			dojo.byId('result').value = JSON.stringify(data);
    			if (data.model != null){
    				var actions = data.model.entity.actions;
    				if (actions != undefined) {
	    				for(var i=0;i<actions.length;i++){
	    					var option=document.createElement("option");
	    					option.text = data.model.entity.actions[i].alias;
	    					dojo.byId("actionType").add(option);
	    				}
    				}
    				data.model.entity.actions = null;
    				dojo.byId('postData').innerHTML = JSON.stringify(data.model.entity);
    			}
    		}
    		else{
    			dojo.byId('result').value = "OK"; 
    		}
    	}
    	else{
    		if (data != ""){
    			dojo.byId('result').value = data;
    			var start = data.indexOf("<entity");
    			dojo.byId('postData').innerHTML = data.substr(start,data.indexOf("</entity>")+9-start);
    			var actions = data.match(/<alias>\w+<\/alias>/g);
    			for(var i=0;i<actions.length;i++){
    				var option=document.createElement("option");
    				option.text = actions[i].substr(7,actions[i].indexOf("</")-7);
    				dojo.byId("actionType").add(option);
    			}
    		}
    		else{
    			dojo.byId('result').value = "OK"; 
    		}
    	}

    }
function get(){
	var requestType = "application/" + dojo.byId('requestType').value;
	var url = location.href + dojo.byId('getUrl').value;
	dojo.byId("getMessage").innerHTML = "GET:" + url + " ; Content-Type: " + requestType + " ; Accept: " + requestType;
	var xhrArgs = {
			handleAs : dojo.byId("requestType").value == "json" ? "json" : "text",
			headers: { "Content-Type": requestType, "Accept": requestType },
			url : url,
			load : function(data) {
				loadData(data);
				dojo.byId("postRequestType").value = dojo.byId("requestType").value;
				dojo.byId("postUrl").value = dojo.byId('getUrl').value;
				
			},
			error : function(e) {
				alert(e);
			}
		}
		var deferred = dojo.xhrGet(xhrArgs);
	}

function post(){
	var requestType = "application/" + dojo.byId('postRequestType').value;
	var data = dojo.byId("postData").value;
	var url= location.href + dojo.byId('postUrl').value;
	dojo.byId("postMessage").innerHTML = "POST:" + url + " ; Content-Type: " + requestType;
	
	var xhrArgs = {
			handleAs : "text",
			postData : encodeURIComponent(data),
			headers: { "Accept": requestType, "Content-Type": requestType },
			url : url,
			load : function(data) {
				data = JSON.parse(data);
				loadData(data);
			},
			error : function(e) {
				alert(e);
			}
		}
		var deferred = dojo.xhrPost(xhrArgs);
	}

function changeAction(){
	var url = dojo.byId("postUrl").value.split("\?")[0];
	dojo.byId("postUrl").value = url + "?action=" + dojo.byId("actionType").value;  
}
</script>
<script src="js/dojo.custom.build.js"
	data-dojo-config="parseOnLoad: true, async: 1">
</script>
<script>
require(["dojo/parser", "dijit/form/ComboBox","dijit/TitlePane"]);

</script>
<link
	href="webapp/css/tundra.css"
	rel="stylesheet" type="text/css" />
</head>
<body class="tundra">
	<img src="webapp/css/images/logo.png"
		width="150px" height="50px" />
	<div><a href="app">Goto the App</a></div>
	<div align="center" style="font-family: verdana;font-size: 1.5em;">REST API test page</div>
	<div data-dojo-type="dijit.TitlePane"
		data-dojo-props="title: 'HTTP Get',open:true">

		Available URL's: <select id="getUrl"
			data-dojo-type="dijit.form.ComboBox">
			<option>login?user=XX&password=YY</option>
			<option>logoff</option>
			<%
			for (DbEntityDefinition definition : entityDefinitions){
				String keyStr = "";
				if (definition.getKeys().size() > 0){
					keyStr = "/&lt;ID&gt;";
				}
				out.write("<option>" + definition.getEntityName() + "</option>");
				out.write("<option>" + definition.getEntityName() + keyStr + "</option>");
			}
			%>
		</select> 
		Method: <select id="requestType">
			<option value="json">JSON</option>
			<option value="xml">XML</option>
		</select> <br />
		<button onclick="get()">HTTP Get</button>
		<br />
		<div id="getMessage"></div>
		<br />
		<textarea id="result" rows="8" cols="100"></textarea>
	</div>
	<div data-dojo-type="dijit.TitlePane"
		data-dojo-props="title: 'HTTP POST',open:false">
		Content: <br />
		<textarea rows="6" cols="100" id="postData">
		</textarea>
		<br /> To URL: <input id="postUrl" value="" size="40" /> 
		Action: <select id="actionType" onchange="changeAction();">
			<option value="">Submit</option>
		</select>
		Method: <select id="postRequestType">
			<option value="json">JSON</option>
			<option value="xml">XML</option>
		</select> <br />
		<button onclick="post()">HTTP Post</button>
		<div id="postMessage"></div>
	</div>

</body>
</html>
