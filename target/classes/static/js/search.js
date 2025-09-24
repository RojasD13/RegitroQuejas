// Función al completar CAPTCHA exitosamente
function captchaSuccess() {
    document.getElementById('btn-buscar').disabled = false;
    // Enviar notificación por email cuando se habilite el botón
    enviarNotificacionCaptcha();
}
// Función cuando el CAPTCHA expira
function captchaExpired() {
    document.getElementById('btn-buscar').disabled = true;
    if (typeof grecaptcha !== 'undefined' && grecaptcha.reset) {
        grecaptcha.reset();
    }
}
// Función para enviar notificación por email
async function enviarNotificacionCaptcha() {
    try {
        const response = await fetch('/api/notificar-captcha-completado', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            }
        });
        if (response.ok) {
            console.log('Notificación enviada correctamente');
        } else {
            console.warn('Error al enviar notificación:', response.statusText);
        }
    } catch (error) {
        console.error('Error de red al enviar notificación:', error);
    }
}
// Variables globales para paginación y funcionalidad
let complaintIdToDelete = null;
let currentPage = 1;
let itemsPerPage = 10;
let totalItems = 0;
let allRows = [];
// ========================================
// FUNCIONES ESPECÍFICAS PARA QUEJAS
// ========================================
// (USANDO SISTEMA MODULAR)
function confirmDeleted(complaintId) {
    complaintIdToDelete = complaintId;
    PasswordAuth.showModal(
        'Confirmar eliminación de queja',
        '¿Está seguro de que desea eliminar esta queja? Esta acción requiere autorización administrativa.',
        function () {
            hideComplaint(complaintIdToDelete);
        }
    );
}
function hideComplaint(quejaId) {
    const form = document.getElementById('deleteForm');
    form.action = `/quejas/${quejaId}/ocultar`;
    form.submit();
}
// ========================================
// FUNCIONES DE PAGINACIÓN
// ========================================
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
document.addEventListener('DOMContentLoaded', function () {
    if (document.getElementById('tableContainer')) {
        initializePagination();
        scrollToTable();
    }
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
    const searchForm = document.getElementById('searchForm');
    if (searchForm) {
        searchForm.addEventListener('submit', function (e) {
            const entidad = document.getElementById('entidad').value;
            if (!entidad) {
                alert('Por favor seleccione una entidad');
                e.preventDefault();
            }
        });
    }
    const entidadSelect = document.getElementById('entidad');
    if (entidadSelect) {
        entidadSelect.addEventListener('change', function (e) {
            if (this.value === '') {
                this.selectedIndex = 0;
            }
        });
    }
});
document.addEventListener('DOMContentLoaded', () => {
    if (typeof grecaptcha !== 'undefined' && grecaptcha.reset) {
        grecaptcha.reset();
    }
});
//----------------------
// --- Cambiar estado con confirmación por contraseña ---
function cambiarEstado(complaintId) {
    // guarda temporalmente el id
    const id = complaintId;
    PasswordAuth.showModal(
        'Confirmar cambio de estado',
        '¿Está seguro de que desea cambiar el estado de esta queja? Esta acción requiere autorización administrativa.',
        function () {
            submitChangeStateForm(id);
        }
    );
}

function submitChangeStateForm(quejaId) {
    const form = document.getElementById('deleteForm'); // reutilizamos el form oculto
    // Ajusta esta URL si tu controlador usa otro endpoint
    form.action = `/quejas/${quejaId}/cambiar-estado`;
    form.submit();
}

/* ---------- Cambio de estado: selección + confirmación por contraseña ---------- */

let _pendingStateChange = { id: null, newState: null };

function cambiarEstado(quejaId) {
    // Guardamos el id y abrimos el modal de selección
    _pendingStateChange.id = quejaId;
    _pendingStateChange.newState = null;
    openStateModal();
}

function openStateModal() {
    const modal = document.getElementById('stateModal');
    if (!modal) {
        alert('No se encontró el modal de selección de estado.');
        return;
    }
    modal.style.display = 'block';
}

function closeStateModal() {
    const modal = document.getElementById('stateModal');
    if (modal) modal.style.display = 'none';
}

// Llamada cuando el usuario selecciona una opción (PROCESO/REVISION/CERRADO)
function selectState(state) {
    _pendingStateChange.newState = state;
    closeStateModal();
    const title = 'Confirmar cambio de estado';
    const message = 'Ingresar contraseña para autorizar el cambio a: ' + state;
    PasswordAuth.showModal(title, message, function () {
        submitChangeStateForm(_pendingStateChange.id, _pendingStateChange.newState);
    });
}

function submitChangeStateForm(quejaId, estado) {
    const form = document.getElementById('deleteForm');
    if (!form) {
        alert('No se encontró el formulario oculto.');
        return;
    }
    form.action = '/quejas/' + quejaId + '/cambiar-estado';
    let input = form.querySelector('input[name="state"]');
    if (!input) {
        input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'state';
        form.appendChild(input);
    }
    input.value = estado;
    form.submit();
}
document.addEventListener('click', function (e) {
    const stateModal = document.getElementById('stateModal');
    if (!stateModal) return;
    if (stateModal.style.display === 'block' && e.target === stateModal) {
        closeStateModal();
    }
});


