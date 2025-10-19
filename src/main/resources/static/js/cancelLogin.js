document.addEventListener('DOMContentLoaded', function () {
    const loginButton = document.getElementById('btn-cancel');

    loginButton.addEventListener('click', function (event) {
        event.preventDefault();
        window.location.href = '/registro';
    });
});