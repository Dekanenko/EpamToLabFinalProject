
//check the entered passwords and able or disable submit btn, also lock/unlock info message
function check() {
    var btn = document.getElementById("sbtn");
    if (document.getElementById('password').value ==
        document.getElementById('confirm_password').value) {
        document.getElementById('message').style.color = 'green';
        document.getElementById('message').innerHTML = document.getElementById('matchId').innerHTML;
        btn.removeAttribute('disabled');
    } else {
        document.getElementById('message').style.color = 'red';
        document.getElementById('message').innerHTML = document.getElementById('notMatchId').innerHTML;
        btn.setAttribute('disabled', true);
    }
}