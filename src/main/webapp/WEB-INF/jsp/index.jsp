<!doctype html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <title>Phone Orders Application</title>
    <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/redmond/jquery-ui.css" type="text/css" rel="stylesheet" />
    <script src="http://code.jquery.com/jquery-1.10.2.js"></script>
    <script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
    <style>
        tr {
            cursor: default;
        }
        .highlight {
            background: #D8D8D8;
        }
        h2 {
            color: green;
            font-size: 24px;
        }
        .main {
            margin-top: 20px;
        }
        label {
            font: normal Arial, sans-serif;
            float: left;
        }
        .corrLabel {
            color: #F60;
            font-size: 20px;
        }
        #corid {
            float: left;
            height: 20px;
            padding-right: 25px;
            width: 300px;
            margin-left: 20px;
            font-size: 15px;
        }
        #submit {
            -webkit-appearance: none !important;
            background-color: #DDEFEF;
            -webkit-border-radius !important: 0;
            border: medium none;
            cursor: pointer;
            height: 20px;
            width: 50px;
            margin-left: 20px;
            font-size: 10px;
        }
        .centered {
            margin: auto;
            text-align: center;
        }
        .trace-table {
            border: solid 1px #DDEEEE;
            border-collapse: collapse;
            font: normal 13px Arial, sans-serif;
        }
        .trace-table thead th {
            background-color: #DDEFEF;
            border: solid 1px #DDEEEE;
            color: #336B6B;
            padding: 10px;
            text-align: left;
            text-shadow: 1px 1px 1px #fff;
        }
        .trace-table tbody td {
            border: solid 1px #DDEEEE;
            color: #333;
            padding: 10px;
            text-shadow: 1px 1px 1px #fff;
        }
        .trace-table-highlight-all {
            overflow: hidden;
            z-index: 1;
        }
        .trace-table-highlight-all tbody td,
        .trace-table-highlight-all thead th {
            position: relative;
        }
        #error {
            color: red;
            font-size: 17px;
        }
    </style>
    <!-- Javascript -->
    <script>
        $(function() {
            getAllOrders();
            $("#newOrderDialog").dialog({
                autoOpen: false,
                modal: true,
                resizable: false,
                height: 150,
                width: 950
            });
            $("#activateOrderDialog").dialog({
                autoOpen: false,
                modal: true,
                resizable: false,
                height: 150,
                width: 950
            });
            $("#approveOrderDialog").dialog({
                autoOpen: false,
                modal: true,
                resizable: false,
                height: 150,
                width: 950
            });
            $("#new").click(function() {
                $("#error").hide();
                $("#newOrderDialog").dialog("open");
                $("#newOrderFirstName").focus();
            });
            $("#activate").click(function() {
                $("#error").hide();
                loadOrderForActivation();
            });
            $("#approve").click(function() {
                $("#error").hide();
                loadOrderForApproval();
            });
            $("#newOrder").submit(function() {
                submitNewOrder();
            });
            $("#error").hide();
        });

        function enableOrdersTableRowSelect(){
            var rows = $("#ordersTable tr").not(":first");
            rows.on("click", function(e) {
                var row = $(this);
                rows.removeClass("highlight");
                row.addClass("highlight");
                $("#selectedID").val(row.attr("value"));
                $("#selectedStatus").val(row.children().eq(6).html());
                $("#error").hide();
            });
        }

        function getAllOrders() {
            var jqxhr = $.ajax("/PhoneApp/order").done(function(data) {
                $("#ordersTable").show();
                $("#ordersTable > tbody").empty();
                $.each(data, function(index, value) {
                    var row = $("<tr value='" + (value.id ? value.id : "") + "'>" + "<td>" + (value.firstName ? value.firstName : "") + "</td><td>" + (value.lastName ? value.lastName : "") + "</td><td>" + (value.street ? value.street : "") + "</td><td>" + (value.city ? value.city : "") + "</td><td>" + (value.zip ? value.zip : "") + "</td><td>" + (value.phone ? value.phone : "") + "</td><td>" + (value.status ? value.status : "") + "</td></tr>");
                    $("#ordersTable > tbody").append(row);
                });
                enableOrdersTableRowSelect();
            }).fail(function(jqXHR, textStatus, errorThrown) {
                $("#ordersTable").hide();
            })
        }

        function approveOrder() {
            var id = $("#selectedID").val();
            var phone = $("#approveOrderPhone").val();
            var jqxhr = $.ajax("/PhoneApp/order/id/" + id).done(function(data) {
                var JSONobj = {
                    "firstName": data[0].firstName,
                    "lastName": data[0].lastName,
                    "street": data[0].street,
                    "city": data[0].city,
                    "zip": data[0].zip,
                    "status": "pending.activation",
                    "id": id,
                    "phone": phone,
                    "timestamp": data[0].timestamp
                };
                var data = JSON.stringify(JSONobj);
                $.ajax({
                    url: "/PhoneApp/order",
                    type: "PUT",
                    data: data,
                    contentType: "application/json",
                    success: function(result) {
                        closeAllDialogs();
                    }
                });
            })
        }

        function activateOrder() {
            var id = $("#selectedID").val();
            var jqxhr = $.ajax("/PhoneApp/order/id/" + id).done(function(data) {
                var JSONobj = {
                    "firstName": data[0].firstName,
                    "lastName": data[0].lastName,
                    "street": data[0].street,
                    "city": data[0].city,
                    "zip": data[0].zip,
                    "status": "closed",
                    "id": id,
                    "phone": data[0].phone,
                    "timestamp": data[0].timestamp
                };
                var data = JSON.stringify(JSONobj);
                $.ajax({
                    url: "/PhoneApp/order",
                    type: "PUT",
                    data: data,
                    contentType: "application/json",
                    success: function(result) {
                        closeAllDialogs();
                    }
                });
            })
        }

        function submitNewOrder() {
            var firstName = $("#newOrderFirstName").val();
            var lastName = $("#newOrderLastName").val();
            var street = $("#newOrderStreet").val();
            var city = $("#newOrderCity").val();
            var zip = $("#newOrderZip").val();
            var status = "pending.approval";
            var JSONobj = {
                "firstName": firstName,
                "lastName": lastName,
                "street": street,
                "city": city,
                "zip": zip,
                "status": status
            };
            var data = JSON.stringify(JSONobj);
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "/PhoneApp/order",
                data: data,
                dataType: "json",
                success: function(data) {
                    closeAllDialogs();
                },
                failure: function(errMsg) {
                    alert(errMsg);
                }
            });
        }

        function closeAllDialogs() {
            $("#approveOrder").trigger("reset");
            $("#approveOrderDialog").dialog("close");
            $("#approveOrdersTable > tbody").empty();
            $("#activateOrder").trigger("reset");
            $("#activateOrderDialog").dialog("close");
            $("#activateOrdersTable > tbody").empty();
            $("#newOrder").trigger("reset");
            $("#newOrderDialog").dialog("close");
            $("#selectedID").val("");
            $("#selectedStatus").val("");
            getAllOrders();
        }

        function loadOrderForApproval() {
            var id = $("#selectedID").val();
            if ($("#selectedStatus").val()== "pending.approval") {
                var jqxhr = $.ajax("/PhoneApp/order/id/" + id).done(function(data) {
                    var value = data[0];
                    var row = $("<tr><td>" + (value.firstName ? value.firstName : "") + "</td><td>" + (value.lastName ? value.lastName : "") + "</td><td>" + (value.street ? value.street : "") + "</td><td>" + (value.city ? value.city : "") + "</td><td>" + (value.zip ? value.zip : "") + "</td><td>" + "<input type='text' id='approveOrderPhone'>" + "</td><td>" + (value.status ? value.status : "") + "</td><td>" + "<input type='submit' value='Approve' id='approveOrderSubmit'>" + "</td></tr>");
                    $("#approveOrdersTable > tbody").append(row);
                    $("#approveOrderPhone").focus();
                    $("#approveOrder").submit(function() {
                        approveOrder();
                    });
                });
                $("#approveOrderDialog").dialog("open");
            } else if (id == "") {
                $("#error").html("No order selected.");
                $("#error").show();
            } else {
                $("#error").html("Only orders that are Pending Approval can be approved.");
                $("#error").show();
            }
        }

        function loadOrderForActivation() {
            var id = $("#selectedID").val();
            if ($("#selectedStatus").val() == "pending.activation") {
                var jqxhr = $.ajax("/PhoneApp/order/id/" + id).done(function(data) {
                    var value = data[0];
                    var row = $("<tr><td>" + (value.firstName ? value.firstName : "") + "</td><td>" + (value.lastName ? value.lastName : "") + "</td><td>" + (value.street ? value.street : "") + "</td><td>" + (value.city ? value.city : "") + "</td><td>" + (value.zip ? value.zip : "") + "</td><td>" + (value.phone ? value.phone : "") + "</td><td>" + (value.status ? value.status : "") + "</td><td>" + "<input type='submit' value='Activate' id='activateOrderSubmit'>" + "</td></tr>");
                    $("#activateOrdersTable > tbody").append(row);
                    $("#activateOrderSubmit").focus();
                    $("#activateOrder").submit(function() {
                        activateOrder();
                    });
                });
                $("#activateOrderDialog").dialog("open");
            } else if (id == "") {
                $("#error").html("No order selected.");
                $("#error").show();
            } else {
                $("#error").html("Only orders that are Pending Activation can be activated.");
                $("#error").show();
            };
        }

    </script>
</head>
<body>
    <!-- HTML -->
    <div id="newOrderDialog" title="Create New Order">
        <form id="newOrder">
            <table class="trace-table trace-table-highlight-all" border="1">
                <thead>
                    <tr>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Street Address</th>
                        <th>City</th>
                        <th>Zip</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td> <input type="text" id="newOrderFirstName"> </td>
                        <td> <input type="text" id="newOrderLastName"> </td>
                        <td> <input type="text" id="newOrderStreet"> </td>
                        <td> <input type="text" id="newOrderCity"> </td>
                        <td> <input type="text" id="newOrderZip"> </td>
                        <td> <input type="submit" value="Submit" id="newOrderSubmit"> </td>
                    </tr>
                </tbody>
            </table>
        </form>
    </div>
    <div id="activateOrderDialog" title="Activate Order">
        <form id="activateOrder">
            <table id="activateOrdersTable" class="trace-table trace-table-highlight-all" border="1">
                <thead>
                    <tr>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Street Address</th>
                        <th>City</th>
                        <th>Zip</th>
                        <th>Phone</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody> </tbody>
            </table>
        </form>
    </div>
    <div id="approveOrderDialog" title="Approve Order">
        <form id="approveOrder">
            <table id="approveOrdersTable" class="trace-table trace-table-highlight-all" border="1">
                <thead>
                    <tr>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Street Address</th>
                        <th>City</th>
                        <th>Zip</th>
                        <th>Phone</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody> </tbody>
            </table>
        </form>
    </div>
    <div id="main">
        <table id="ordersTable" class="trace-table trace-table-highlight-all" border="1">
            <thead>
                <tr>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Street Address</th>
                    <th>City</th>
                    <th>Zip</th>
                    <th>Phone</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>
        </br>
        <input id="approve" type="button" value="Approve" />
        <input id="activate" type="button" value="Activate" />
        <input id="new" type="button" value="New" />
        <input type="hidden" id="selectedID" value="" />
        <input type="hidden" id="selectedStatus" value="" /> </div>
    <br>
    <div id="error"></div>
</body>
</html>
