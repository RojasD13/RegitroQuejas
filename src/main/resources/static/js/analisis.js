document.addEventListener('DOMContentLoaded', function () {
    console.log('Página de análisis inicializada');

    initializeAnalisisPage();
});
// Función para inicializar funcionalidad específica de la página de análisis
function initializeAnalisisPage() {
    const form = document.getElementById('searchForm');
    const tabla = document.getElementById('tabla-container');

    if (form && tabla) {
        form.addEventListener('submit', function (e) {
            e.preventDefault();
            showTable();
        });
    }
}
// Función para mostrar la tabla con efectos visuales
function showTable() {
    const tabla = document.getElementById('tabla-container');
    if (tabla) {
        tabla.style.display = 'block';
        tabla.style.opacity = '0';
        tabla.style.transform = 'translateY(20px)';
        tabla.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
        setTimeout(() => {
            tabla.style.opacity = '1';
            tabla.style.transform = 'translateY(0)';
        }, 50);
        setTimeout(() => {
            tabla.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }, 100);
        animateTableRows();
    }
}
// Función para animar las filas de la tabla
function animateTableRows() {
    const rows = document.querySelectorAll('#resultsTable tbody tr');
    rows.forEach((row, index) => {
        row.style.opacity = '0';
        row.style.transform = 'translateX(-20px)';
        row.style.transition = `opacity 0.3s ease ${index * 0.1}s, transform 0.3s ease ${index * 0.1}s`;
        setTimeout(() => {
            row.style.opacity = '1';
            row.style.transform = 'translateX(0)';
        }, 200 + (index * 100));
    });
}
// Función para agregar efectos hover a las filas de la tabla
function addTableHoverEffects() {
    const rows = document.querySelectorAll('#resultsTable tbody tr');
    rows.forEach(row => {
        row.addEventListener('mouseenter', function () {
            this.style.transform = 'scale(1.02)';
            this.style.transition = 'transform 0.2s ease';
            this.style.boxShadow = '0 2px 8px rgba(0,0,0,0.1)';
        });
        row.addEventListener('mouseleave', function () {
            this.style.transform = 'scale(1)';
            this.style.boxShadow = 'none';
        });
    });
}
// Llamar a la función de efectos hover cuando se muestre la tabla
document.addEventListener('DOMContentLoaded', function () {
    const tabla = document.getElementById('tabla-container');
    if (tabla && tabla.style.display !== 'none') {
        addTableHoverEffects();
    }
});