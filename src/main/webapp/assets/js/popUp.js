
function openStaffForm() {
    document.getElementById("staffForm").style.display = "block";
}

function closeStaffForm() {
    document.getElementById("staffForm").style.display = "none";
}

function openCarForm() {
    document.getElementById("carForm").style.display = "block";
}

function closeCarForm() {
    document.getElementById("carForm").style.display = "none";
}

function closeUserTable() {
    document.getElementById("userTable").style.display = "none";
}

function closeCarTable() {
    document.getElementById("carTable").style.display = "none";
}

function closePassportTable() {
    document.getElementById("passportTable").style.display = "none";
}

function openPassportForm() {
    document.getElementById("passportForm").style.display = "block";
}

function closePassportForm() {
    document.getElementById("passportForm").style.display = "none";
}

function closeOrderTable() {
    document.getElementById("orderTable").style.display = "none";
}

function openCarEditForm(carId, brand, name, cost, cls) {
    document.getElementById("editId").value = carId;
    document.getElementById("brandId").value = brand;
    document.getElementById("nameId").value = name;
    document.getElementById("costId").value = cost;
    document.getElementById("carCls").value = cls;
    document.getElementById("carCls").innerText = cls;
    if(document.getElementById("carEditForm").style.display == "block"){
        document.getElementById("carEditForm").style.display = "none";
    }else {
        document.getElementById("carEditForm").style.display = "block";
    }
}

function openOrderDenyForm(orderId, carId, orderCost, userId) {
    document.getElementById("deniedOrderId").value = orderId;
    document.getElementById("orderCarId").value = carId;
    document.getElementById("orderCostId").value = orderCost;
    document.getElementById("orderUserId").value = userId;
    if(document.getElementById("denyForm").style.display == "block"){
        document.getElementById("denyForm").style.display = "none";
    }else {
        document.getElementById("denyForm").style.display = "block";
    }
}

function openOrderAcceptForm(x) {
    document.getElementById("acceptedOrderId").value = x;
    if(document.getElementById("acceptForm").style.display == "block"){
        document.getElementById("acceptForm").style.display = "none";
    }else {
        document.getElementById("acceptForm").style.display = "block";
    }
}

function openOrderConfirmForm(orderId, orderPassportId, message) {
    document.getElementById("deniedOrderId").value = orderId;
    document.getElementById("orderPassportId").value = orderPassportId;
    document.getElementById("messageId").value = message;
    if(document.getElementById("confirmForm").style.display == "block"){
        document.getElementById("confirmForm").style.display = "none";
    }else {
        document.getElementById("confirmForm").style.display = "block";
    }
}

function openFinishForm(orderId) {
    document.getElementById("finishedOrderId").value = orderId;

    const today = new Date();
    const todayNoTime = new Date(today.getTime());
    todayNoTime.setUTCHours(0,0,0,0);

    //configure the date of car return
    var d = new Date(todayNoTime),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;

    document.getElementById("returnDateId").value = [year, month, day].join('-');

    if(document.getElementById("finishForm").style.display == "block"){
        document.getElementById("finishForm").style.display = "none";
    }else {
        document.getElementById("finishForm").style.display = "block";
    }
}

var fineCheck = 0;

function openConfirmFinishForm(orderId, carId, userId, passportId, lastDate, returnDate, driverOption) {
    document.getElementById("finishedOrderId").value = orderId;
    document.getElementById("finishedCarId").value = carId;
    document.getElementById("finishedUserId").value = userId;
    document.getElementById("finishedPassportId").value = passportId;
    document.getElementById("driverOptionId").value = driverOption;

    const lastD = new Date(lastDate);
    const returnD = new Date(returnDate);

    //compare the date of return with last order day
    if(returnD > lastD){
        fineCheck = 1;
        document.getElementById("fineId").removeAttribute('disabled');
        document.getElementById("carLaterReturnMessage").removeAttribute('hidden')
    }

    if(document.getElementById("confirmFinishForm").style.display == "block"){
        document.getElementById("confirmFinishForm").style.display = "none";
    }else {
        document.getElementById("confirmFinishForm").style.display = "block";
    }
}

function damagedCar(){
    document.getElementById("fineId").removeAttribute('disabled');
}

function notDamagedCar(){
    if(fineCheck != 1){
        document.getElementById("fineId").setAttribute('disabled', true);
        document.getElementById("fineId").value = 0;
    }
}

