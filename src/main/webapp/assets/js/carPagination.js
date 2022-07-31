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
    if(table.rows[0].cells.length == 6){
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
const pageSize = 6;
var curPage = 1;

renderCarTable();

//navigation buttons
document.querySelector('#nextButtonCar').addEventListener('click', nextPage, false);
document.querySelector('#prevButtonCar').addEventListener('click', previousPage, false);

function previousPage() {
    if(curPage > 1) curPage--;
    renderCarTable();
}

function nextPage() {
    if((curPage * pageSize) < data.length) curPage++;
    renderCarTable();
}

//render table (cut table in parts)
function renderCarTable() {
    //get table for fill
    let table = document.getElementById("carTablePagination");

    //get json info from needed table
    data = JSON.parse(tableToJson1(document.getElementById('carTableId')));

    //main body
    let result = '';
    data.filter((row, index) => {
        let start = (curPage-1)*pageSize;
        let end =curPage*pageSize;
        if(index >= start && index < end) return true;
    }).forEach(c => {
        result += `<tr>
     <td>${c.brand}</td>
     <td>${c.class}</td>
     <td>${c.name}</td>
     <td>${c.costperday}</td>
     <td>${c.inusage}</td>
     <td>${c.damaged}</td>`;

        if(variation == 2){
            result += `<td>${c.options}</td>`;
        }
        result += `<td>${c.undefined}</td>
     </tr>`;

    });

    //add header
     let header = `<tr>`;

    for (var i=0; i<table.rows[0].cells.length; i++) {
        if(i == 2){
            header += `<th onClick="sortCarTable(2), renderCarTable()" class="nameColum">`+table.rows[0].cells[i].innerHTML+`</th>`;
        }
        if(i == 3){
            header += `<th onClick="sortCarTable(3), renderCarTable()" class="costColum">`+table.rows[0].cells[i].innerHTML+`</th>`;
        }
        if(i != 2 && i !=3 ){
            header += `<th>`+table.rows[0].cells[i].innerHTML+`</th>`;
        }
    }
    header += `</tr>`;

    //add info to table
    table.innerHTML = header;
    table.innerHTML += result;
}

