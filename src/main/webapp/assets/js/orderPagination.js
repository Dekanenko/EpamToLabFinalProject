var variation = 0;

//create json from table
function tableToJson1(table) {
    var data = [];
    var headers = [];
    //get header
    for (var i=0; i<table.rows[0].cells.length; i++) {
        headers[i] = table.rows[0].cells[i].innerHTML.toLowerCase().replace(/ /gi,'');
    }

    //define the table
    if(table.rows[0].cells.length == 9){
        variation = 1;
    }else{
        variation = 2;
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
const pageSize = 4;
var curPage = 1;

renderOrderTable();

//navigation buttons
document.querySelector('#nextButtonOrder').addEventListener('click', nextPage, false);
document.querySelector('#prevButtonOrder').addEventListener('click', previousPage, false);

function previousPage() {
    if(curPage > 1) curPage--;
    renderOrderTable();
}

function nextPage() {
    if((curPage * pageSize) < data.length) curPage++;
    renderOrderTable();
}

//render table (cut table in parts)
function renderOrderTable() {

    let firstDate, firstNoTime, todayNoTime;
    const today = new Date();

    //get table for fill
    let table = document.getElementById("orderTablePagination");

    //get json info from needed table
    data = JSON.parse(tableToJson1(document.getElementById('orderTableId')));

    //main body
    let result = '';
    data.filter((row, index) => {
        let start = (curPage-1)*pageSize;
        let end =curPage*pageSize;
        if(index >= start && index < end) return true;
    }).forEach(c => {
        result += `<tr>
     <td>${c.orderid}</td>
     <td>${c.status}</td>`;
        if(variation == 2){
            result += `<td>${c.userlogin}</td>`;
        }
     result +=`<td>${c.carbrand}</td>
     <td>${c.carclass}</td>
     <td>${c.carname}</td>
     <td>${c.firstdate}</td>
     <td>${c.lastdate}</td>
     <td>${c.driveroption}</td>
     <td>${c.cost}</td>`;
        if(variation == 2){
            //check the date in order to block accept btn if the date goes off
            firstDate = new Date(c.firstdate);

            firstNoTime = new Date(firstDate.getTime());
            todayNoTime = new Date(today.getTime());

            firstNoTime.setUTCHours(0,0,0,0);
            todayNoTime.setUTCHours(0,0,0,0);

            if(todayNoTime > firstNoTime){
                result += `<td></td>`;
            }else {
                result += `<td>${c.options}</td>`;
            }
        }
    result += `<td>${c.undefined}</td>
     </tr>`;
    });

    //add header
    let header = `<tr>`;

    for (var i=0; i<table.rows[0].cells.length; i++) {
        if(variation == 1){
            if(i != 8){
                header += `<th>`+table.rows[0].cells[i].innerHTML+`</th>`;
            }
            if(i == 8){
                header += `<th onClick="sortOrderTable(8), renderOrderTable()" class="costColum">`+table.rows[0].cells[i].innerHTML+`</th>`;
            }
        }
        if(variation == 2){
            if(i != 9){
                header += `<th>`+table.rows[0].cells[i].innerHTML+`</th>`;
            }
            if(i == 9){
                header += `<th onClick="sortOrderTable(9), renderOrderTable()" class="costColum">`+table.rows[0].cells[i].innerHTML+`</th>`;
            }
        }
    }

    header += `</tr>`;

    //add info to table
    table.innerHTML = header;
    table.innerHTML += result;
}
