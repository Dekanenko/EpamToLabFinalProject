//compare date (first date, last date and today's date)
function dateCompare(){
    var calcBtn = document.getElementById('calcBtn');

    const date1 = new Date(document.getElementById("fDate").value);
    const date2 = new Date(document.getElementById("lDate").value);
    const today = new Date();

    const date1NoTime = new Date(date1.getTime());
    const date2NoTime = new Date(date2.getTime());
    const todayNoTime = new Date(today.getTime());

    date1NoTime.setUTCHours(0,0,0,0);
    date2NoTime.setUTCHours(0,0,0,0);
    todayNoTime.setUTCHours(0,0,0,0);

    if(date1NoTime >= todayNoTime && date1NoTime<date2NoTime && ((date2NoTime - date1NoTime) <= 2700000000)
        && (date1NoTime - todayNoTime) <= 172800000){
        calcBtn.removeAttribute('disabled');
    }else {
        calcBtn.setAttribute('disabled', true);
    }

    var sbtn = document.getElementById('submitBtn');
    sbtn.setAttribute('disabled', true);
    document.getElementById('totalCost').value = 0;
}

//calculate total order cost depends on car cost, driver option and duration
function costCalculation(){
    var sbtn = document.getElementById('submitBtn');
    var div = document.getElementById('balanceUp');
    let costPerDay = document.getElementById('totalCostPerDay').value;
    let userCash = document.getElementById('currentUserCash').value;
    const date1 = new Date(document.getElementById("fDate").value);
    const date2 = new Date(document.getElementById("lDate").value);

    const date1NoTime = new Date(date1.getTime());
    const date2NoTime = new Date(date2.getTime());

    date1NoTime.setUTCHours(0,0,0,0);
    date2NoTime.setUTCHours(0,0,0,0);

    let days = (date2NoTime - date1NoTime)/(1000*3600*24);
    let totalCost = days*costPerDay;

    document.getElementById('totalCost').value = totalCost;

    if(totalCost <= userCash){
        sbtn.removeAttribute('disabled');
        div.setAttribute('hidden', true);
    }else{
        sbtn.setAttribute('disabled', true);
        div.removeAttribute('hidden');
    }
}

//total order cost without driver option
function noDriverCost(){
    let costPerDay = document.getElementById('costPerDay').value;

    document.getElementById('totalCostPerDay').setAttribute('value', costPerDay);
    document.getElementById('totalCost').value = 0;
    document.getElementById('submitBtn').setAttribute('disabled', true);
}

//total order cost with driver option (+10% to total cost)
function withDriverCost(){
    let costPerDay = document.getElementById('costPerDay').value;
    let totalCostPerDay = Math.round(costPerDay)+Math.round(costPerDay/10);

    document.getElementById('totalCostPerDay').setAttribute('value', totalCostPerDay);

    document.getElementById('totalCost').value = 0;
    document.getElementById('submitBtn').setAttribute('disabled', true);
}

function anotherPassport(){
    document.getElementById('currentPassId').setAttribute('value', 0);
}
