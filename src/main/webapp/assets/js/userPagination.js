//create json from table
function tableToJson1(table) {
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

var data;
const pageSize = 6;
var curPage = 1;

renderUserTable();

//navigation buttons
document.querySelector('#nextButtonUser').addEventListener('click', nextPage, false);
document.querySelector('#prevButtonUser').addEventListener('click', previousPage, false);

function previousPage() {
    if(curPage > 1) curPage--;
    renderUserTable();
}

function nextPage() {
    if((curPage * pageSize) < data.length) curPage++;
    renderUserTable();
}

//render table (cut table in parts)
function renderUserTable() {
    //get table for fill
    let table = document.getElementById("userTablePagination");

    //get json info from needed table
    data = JSON.parse(tableToJson1(document.getElementById('userTableId')));

    //main body
    let result = '';
    data.filter((row, index) => {
        let start = (curPage-1)*pageSize;
        let end =curPage*pageSize;
        if(index >= start && index < end) return true;
    }).forEach(c => {
        result += `<tr>
     <td>${c.login}</td>
     <td>${c.role}</td>
     <td>${c.status}</td>
     <td>${c.options}</td>
     <td>${c.undefined}</td>
     </tr>`;
    });

    //add header
    let header = `<tr>`;

    for (var i=0; i<table.rows[0].cells.length; i++) {
        header += `<th>`+table.rows[0].cells[i].innerHTML+`</th>`;
    }
    header += `</tr>`;

    //add info to table
    table.innerHTML = header;
    table.innerHTML += result;
}
