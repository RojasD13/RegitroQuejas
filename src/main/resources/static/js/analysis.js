
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