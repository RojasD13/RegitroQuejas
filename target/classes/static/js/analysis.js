// Habilitar botón tras el CAPTCHA
function captchaSuccess() {
    document.getElementById('btn-buscar').disabled = false;
}
// Deshabilitar botón si expira el CAPTCHA
function captchaExpired() {
    document.getElementById('btn-buscar').disabled = true;
    if (typeof grecaptcha !== 'undefined' && grecaptcha.reset) {
        grecaptcha.reset();
    }
}
document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('searchForm');
    const tabla = document.getElementById('tabla-container');

    if (form) {
        form.addEventListener('submit', function (e) {
            e.preventDefault(); 
            tabla.style.display = 'block';
            tabla.scrollIntoView({ behavior: 'smooth' });
        });
    }
});
