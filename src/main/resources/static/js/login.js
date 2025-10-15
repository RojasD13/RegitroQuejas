document.addEventListener('DOMContentLoaded', function () {
    const loginButton = document.getElementById('btn-login');

    loginButton.addEventListener('click', function (event) {
        event.preventDefault();
        window.location.href = '/registro';
    });
});