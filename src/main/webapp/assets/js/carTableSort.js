function sortCarTable(n) {
    var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0, xValue, yValue;
    table = document.getElementById("carTableId");
    switching = true;
    dir = "asc";
    while (switching) {
        switching = false;
        rows = table.rows;
        console.log(rows);
        for (i = 1; i < (rows.length - 1); i++) {
            shouldSwitch = false;
            x = rows[i].getElementsByTagName("TD")[n];
            y = rows[i + 1].getElementsByTagName("TD")[n];
            if (dir == "asc") {
                if(n == 2){
                    if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                        shouldSwitch = true;
                        break;
                    }
                }else {
                    xValue = x.innerHTML.replace(',','.');
                    yValue = y.innerHTML.replace(',','.');
                    if(Number(xValue) > Number(yValue)){
                        shouldSwitch = true;
                        break;
                    }
                }

            } else if (dir == "desc") {
                if(n == 2) {
                    if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                        shouldSwitch = true;
                        break;
                    }
                }else {
                    xValue = x.innerHTML.replace(',','.');
                    yValue = y.innerHTML.replace(',','.');
                    if(Number(xValue) < Number(yValue)){
                        shouldSwitch = true;
                        break;
                    }
                }
            }
        }
        if (shouldSwitch) {
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
            switchcount ++;
        } else {
            if (switchcount == 0 && dir == "asc") {
                dir = "desc";
                switching = true;
            }
        }
    }
}