// Variables globales para el modal y paginación
let quejaIdABorrar = null;
let currentPage = 1;
let itemsPerPage = 10;
let totalItems = 0;
let allRows = [];

document.addEventListener('DOMContentLoaded', function () {
    console.log('🔍 Página de búsqueda inicializada');
    initializeBuscarPage();
});
function initializeBuscarPage() {
    if (document.getElementById('tableContainer')) {
        initializePagination();
        scrollToTable();
    }
    setupConfirmationModal();
    setupActionButtons();
    setupFormValidation();
}
// Función de paginación
function initializePagination() {
    allRows = Array.from(document.querySelectorAll('.table-row'));
    totalItems = allRows.length;
    const paginationContainer = document.getElementById('paginationContainer');
    if (paginationContainer) {
        if (totalItems <= itemsPerPage) {
            paginationContainer.style.display = 'none';
            allRows.forEach(r => r.classList.remove('hidden'));
            return;
        } else {
            paginationContainer.style.display = 'flex';
        }
    }
    createPageButtons();
    updatePagination();
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    if (prevBtn) {
        prevBtn.addEventListener('click', () => {
            if (currentPage > 1) {
                currentPage--;
                updatePagination();
            }
        });
    }
    if (nextBtn) {
        nextBtn.addEventListener('click', () => {
            if (currentPage < Math.ceil(totalItems / itemsPerPage)) {
                currentPage++;
                updatePagination();
            }
        });
    }
}

function updatePagination() {
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const totalPages = Math.ceil(totalItems / itemsPerPage);
    allRows.forEach((row, index) => {
        if (index >= startIndex && index < endIndex) {
            row.classList.remove('hidden');
        } else {
            row.classList.add('hidden');
        }
    });
    const paginationInfo = document.getElementById('paginationInfo');
    if (paginationInfo) {
        const start = startIndex + 1;
        const end = Math.min(endIndex, totalItems);
        paginationInfo.innerHTML = `Mostrando ${start}-${end} de ${totalItems} resultados`;
    }
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    if (prevBtn) prevBtn.disabled = currentPage === 1;
    if (nextBtn) nextBtn.disabled = currentPage === totalPages;
    updatePageButtons();
}

function createPageButtons() {
    const totalPages = Math.ceil(totalItems / itemsPerPage);
    const pagesContainer = document.getElementById('paginationPages');
    if (!pagesContainer) return;
    pagesContainer.innerHTML = '';
    const maxVisiblePages = 5;
    let startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
    let endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);
    if (endPage - startPage + 1 < maxVisiblePages) {
        startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }
    for (let i = startPage; i <= endPage; i++) {
        const pageBtn = document.createElement('button');
        pageBtn.className = 'page-btn';
        pageBtn.textContent = i;
        pageBtn.setAttribute('aria-label', `Página ${i}`);
        if (i === currentPage) pageBtn.classList.add('active');
        pageBtn.addEventListener('click', () => {
            if (currentPage === i) return;
            currentPage = i;
            updatePagination();
        });
        pagesContainer.appendChild(pageBtn);
    }
}

function updatePageButtons() {
    const pageButtons = document.querySelectorAll('.page-btn');
    pageButtons.forEach(btn => {
        btn.classList.toggle('active', parseInt(btn.textContent, 10) === currentPage);
    });
}

// Función para scroll automático a la tabla
function scrollToTable() {
    const tableContainer = document.getElementById('tableContainer');
    if (tableContainer) {
        setTimeout(() => {
            tableContainer.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }, 100);
    }
}

function setupConfirmationModal() {
    const btnConfirmar = document.getElementById('btnConfirmar');
    const btnCancelar = document.getElementById('btnCancelar');
    const confirmModal = document.getElementById('confirmModal');
    if (btnConfirmar) {
        btnConfirmar.addEventListener('click', function () {
            if (quejaIdABorrar) {
                ocultarQueja(quejaIdABorrar);
            }
            confirmModal.style.display = 'none';
        });
    }
    if (btnCancelar) {
        btnCancelar.addEventListener('click', function () {
            confirmModal.style.display = 'none';
            quejaIdABorrar = null;
        });
    }
    if (confirmModal) {
        confirmModal.addEventListener('click', function (e) {
            if (e.target === this) {
                this.style.display = 'none';
                quejaIdABorrar = null;
            }
        });
    }
}
// Función para mostrar modal de confirmación
function confirmarBorrado(quejaId) {
    quejaIdABorrar = quejaId;
    document.getElementById('confirmModal').style.display = 'block';
}
// Función para ocultar queja
function ocultarQueja(quejaId) {
    const form = document.getElementById('deleteForm');
    form.action = `/quejas/${quejaId}/ocultar`;
    form.submit();
}
// Funciones para cambiar estado y agregar comentario (placeholders)
function cambiarEstado(quejaId) {
    console.log('Cambiar estado de queja:', quejaId);
    alert(`Funcionalidad para cambiar estado de queja ${quejaId} - Por implementar`);
}
function agregarComentario(quejaId) {
    console.log('Agregar comentario a queja:', quejaId);
    alert(`Funcionalidad para agregar comentario a queja ${quejaId} - Por implementar`);
}
// Hacer funciones disponibles globalmente
window.confirmarBorrado = confirmarBorrado;
window.cambiarEstado = cambiarEstado;
window.agregarComentario = agregarComentario;
// Funcionalidad para los botones de acción (código existente)
function setupActionButtons() {
    const actionButtons = document.querySelectorAll('.btn-action');
    actionButtons.forEach(button => {
        button.addEventListener('click', function () {
            const action = this.querySelector('i').classList.contains('fa-eye') ? 'ver' : 'descargar';
            const row = this.closest('tr');
            const resumen = row ? row.querySelector('.resumen-cell')?.textContent : '';
            if (action === 'ver') {
                alert(`Ver detalles de: ${resumen}`);
            } else {
                alert(`Descargando: ${resumen}`);
            }
        });
    });
}

function setupFormValidation() {
    const searchForm = document.getElementById('searchForm');
    if (searchForm) {
        searchForm.addEventListener('submit', function (e) {
            const entidad = document.getElementById('entidad');
            if (entidad && !entidad.value) {
                alert('Por favor seleccione una entidad');
                e.preventDefault();
            }
        });
    }
}