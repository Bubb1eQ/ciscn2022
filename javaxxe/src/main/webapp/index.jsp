<%@ page import="ctf.model.Service" %><%--
  Created by IntelliJ IDEA.
  User: JoeZhou
  Date: 2021/10/19
  Time: 11:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Home Page</title>
    <link rel="stylesheet" href="static/css/bootstrap.min.css">
    <style>
        h2, h4 {
            font-family: 'Alegreya Sans', sans-serif;
        }

        a.disabled {
            pointer-events: none;
            cursor: default;
            color: #ccc;
        }
    </style>
</head>

<body class="bg-light">
<div class="container">
    <div class="py-5 text-center">
        <h2>Running Services</h2>
        <p class="lead">Your running services are as follow</p>
    </div>
    <div>
        <div id="result" class="alert alert-success form-control" hidden="true"></div>
        <% if (request.getAttribute("error") != null) {
            out.println("<div class=\"alert alert-danger form-control\">" + request.getAttribute("error") + "</div>");
        }%>
        <%
            String name = null;
            String operation = null;
            String params = null;

            Service service = null;
            if (request.getAttribute("service") != null) {
                service = (Service) request.getAttribute("service");
                out.println("        <div class=\"form-group\">\n" +
                        "            <label class=\"col-form-label text-md-right\" for=\"servicename\">serviceName</label>\n" +
                        "            <input class='form-control input-md' name=\"service\" id=\"servicename\" rows=\"1\" placeholder=\"Service Name\" value=\"" + service.getName() + "\"></input>\n" +
                        "        </div>");
                out.println("        <div class=\"form-group\">\n" +
                        "            <label class=\"col-form-label text-md-right\" for=\"operation\">operateName</label>\n" +
                        "            <input class='form-control input-md' id=\"operation\" rows=\"1\" placeholder=\"Operation Name\" value=\"" + service.getOperation() + "\"></input>\n" +
                        "        </div>");
                out.println("        <div class=\"form-group\">\n" +
                        "            <label class=\"col-form-label text-md-right\" for=\"params\">params</label>\n" +
                        "            <input class='form-control input-md' id=\"params\" type=\"params\" placeholder=\"Params\" required=\"\" value=\"" + service.getParams() + "\">\n" +
                        "        </div>");
            }
        %>
    </div>
    <hr class="mb-4">
    <button id="addService" class="btn btn-primary btn-lg btn-block" type="submit">Add</button>
    <hr class="mb-4">
    <footer class="my-5 pt-5 text-muted text-center text-small">
        <ul class="list-inline">
            <li class="list-inline-item"><a href="#">Privacy</a></li>
            <li class="list-inline-item"><a href="#">Terms</a></li>
            <li class="list-inline-item"><a href="#">Support</a></li>
        </ul>
    </footer>
</div>
<script>
    (async () => {
        await new Promise((resolve) => {
            window.addEventListener('load', resolve);
        });
    })();

    document.getElementById('addService').addEventListener('click', async (e) => {
        e.preventDefault();
        const servicename = document.getElementById('servicename').value;
        const operation = document.getElementById('operation').value;
        const params = document.getElementById('params').value;

        let data = `<root>
    <input>
        <serviceCall>
            <serviceName>` + servicename + `</serviceName>
            <operateName>` + operation + `</operateName>
            <params>` + params + `</params>
            <return></return>
        </serviceCall>
    </input>
</root>`
        const serviceCall = data.replace(/\s+/g, "")

        const res = await (await fetch(window.location.href, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: 'serviceCall=' + serviceCall
        })).text();

        if (res.error) {
            return;
        }
        document.getElementById("result").hidden = false;
        document.getElementById("result").innerText = res;
    });

</script>

</body>
</html>