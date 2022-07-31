
function tableToJson(table) {
    var data = [];
    var headers = [];
    //get header
    for (var i=0; i<table.rows[0].cells.length; i++) {
        headers[i] = table.rows[0].cells[i].innerHTML.toLowerCase().replace(/ /gi,'');
    }

    //put all data
    for (var i=1; i<table.rows.length; i++) {
        var tableRow = table.rows[i]; var rowData = {};
        for (var j=0; j<tableRow.cells.length; j++) {
            rowData[ headers[j] ] = tableRow.cells[j].innerHTML;
        } data.push(rowData);
    }

    return JSON.stringify(data);
}

function printUser() {
    //get json info from needed table
    let data = JSON.parse(tableToJson(document.getElementById('userTableId')));

    //get internationalized header
    let tableTop = document.getElementById("userTablePagination");

    let result = '<tr>';

    for (var i=0; i<tableTop.rows[0].cells.length - 2; i++) {
        result += `<th>`+tableTop.rows[0].cells[i].innerHTML+`</th>`;
    }

    //construct the table
    result +=`</tr>`;

    data.forEach(c => {
        result += `<tr>
     <td>${c.login}</td>
     <td>${c.role}</td>
     <td>${c.status}</td>
     </tr>`;
    });

    //add info to table
    var table = document.createElement("table");
    table.innerHTML = result;

    //add table to div
    var div = document.createElement("div");
    div.append(table);

    return div;
}

function createUserPDF() {
    var style = "<style>";
    style = style + "table {width: 100%;font: 17px Calibri;}";
    style = style + "table, th, td {border: solid 1px #DDD; border-collapse: collapse;";
    style = style + "padding: 2px 3px;text-align: center;}";
    style = style + "</style>";

    //create window object
    var win = window.open('', '', 'height=700,width=700');

    win.document.write('<html><head>');
    win.document.write('<title>Users</title>');
    //add style inside the head tag
    win.document.write(style);
    //add table
    win.document.write(printUser().innerHTML);
    win.document.write('</head>');
    win.document.write('<body>');
    win.document.write('</body></html>');
    win.document.close(); 	// close the current window
    win.print();    //print the contents
}