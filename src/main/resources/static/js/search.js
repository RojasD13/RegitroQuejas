// VARIABLES GLOBALES
let currentPage = 1, itemsPerPage = 10, totalItems = 0, allRows = [];
let _pendingAction = { id: null, type: null, data: null };

// MAPA DE ESTADOS PARA MANTENER CONSISTENCIA CON EL ENUM DE JAVA
const STATE_MAP = {
    'PROCESO': { display: 'Abierta', class: 'proceso' },
    'REVISION': { display: 'En Revisión', class: 'revision' },
    'CERRADO': { display: 'Cerrada', class: 'cerrado' }
};

// FUNCIONES DE MODALES Y UTILIDADES 
function openModal(modalId) {
    const modal = document.getElementById(modalId);
    modal.classList.add('show');
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    modal.classList.remove('show');
}

function setupModalCloseOnOutsideClick(modalId) {
    document.addEventListener('click', e => {
        const modal = document.getElementById(modalId);
        if (e.target === modal && modal.classList.contains('show')) {
            closeModal(modalId);
        }
    });
}

// MANEJO DE ACCIONES DE QUEJAS
function confirmDeleted(id) {
    _pendingAction = { id, type: 'delete' };
    PasswordAuth.showModal('Confirmar eliminación', '¿Eliminar esta queja? Requiere autorización.', executePendingAction);
}

function cambiarEstado(id) {
    _pendingAction = { id, type: 'changeState' };
    openStateModal();
}

function agregarComentario(id) {
    _pendingAction = { id, type: 'addComment' };
    openCommentModal();
}

function openStateModal() {
    openModal('stateModal');
}

function selectState(state) {
    _pendingAction.data = state;
    closeModal('stateModal');
    const stateDisplay = STATE_MAP[state] ? STATE_MAP[state].display : state;
    PasswordAuth.showModal('Confirmar cambio', `Autorizar cambio a: ${stateDisplay}`, executePendingAction);
}

function openCommentModal() {
    openModal('commentModal');
    document.getElementById('commentText').value = '';
    document.getElementById('commentError').style.display = 'none';
}

// FUNCIÓN EJECUTADORA DE ACCIONES PENDIENTES
function executePendingAction() {
    const { id, type, data } = _pendingAction;
    switch (type) {
        case 'delete': submitActionForm(id, `/quejas/${id}/ocultar`); break;
        case 'changeState': submitStateChange(); break;
        case 'addComment': submitComment(); break;
    }
}

// FUNCIÓN PARA ENVIAR FORMULARIOS (BORRAR)
function submitActionForm(actionId, actionUrl, extraData = {}) {
    const form = document.getElementById('deleteForm');
    form.action = actionUrl;
    form.querySelectorAll('input[data-dynamic]').forEach(el => el.remove());
    Object.entries(extraData).forEach(([key, value]) => {
        const input = document.createElement('input');
        input.type = 'hidden'; input.name = key; input.value = value;
        input.setAttribute('data-dynamic', 'true');
        form.appendChild(input);
    });
    form.submit();
}

// FUNCIÓN PARA CAMBIAR ESTADO (CORREGIDA)
function submitStateChange() {
    const { id, data: state } = _pendingAction;
    const confirmBtn = document.getElementById('btnModalConfirmar');

    const originalButtonText = confirmBtn.innerHTML;
    confirmBtn.disabled = true;
    confirmBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Confirmando...';
    const urlParams = new URLSearchParams(window.location.search);
    const entidadId = urlParams.get('entidadId');

    fetch(`/quejas/${id}/cambiar-estado`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json', 'X-Requested-With': 'XMLHttpRequest' },
        body: JSON.stringify({ state: state })
    })
        .then(res => {
            if (res.ok) {
                updateQuejaState(id, state);
                closeModal('passwordModal');
                // No intentamos procesar JSON si la respuesta está vacía
                return res.status === 204 ? null : res.json();
            } else {
                return res.json().then(err => {
                    throw new Error(err.message || 'Error al cambiar el estado');
                });
            }
        })
        .catch(error => {
            console.error('Error en fetch:', error);
            alert('Ocurrió un error al cambiar el estado: ' + error.message);
            closeModal('passwordModal');
        })
        .finally(() => {
            confirmBtn.disabled = false;
            confirmBtn.innerHTML = originalButtonText;
        });
}

// FUNCIÓN ACTUALIZAR ESTADO DE LA QUEJA (CORREGIDA)
function updateQuejaState(id, newState) {
    const row = document.querySelector(`tr[data-queja-id="${id}"]`);
    if (row) {
        const estadoCell = row.querySelector('.estado-cell');
        if (estadoCell) {
            const statusBadge = estadoCell.querySelector('.status-badge');
            if (statusBadge) {
                // Usamos el mapa de estados para obtener el display name correcto
                const stateInfo = STATE_MAP[newState] || { display: newState, class: newState.toLowerCase() };
                statusBadge.textContent = stateInfo.display;
                statusBadge.className = `status-badge status-${stateInfo.class}`;
            }
        }
    }
}

// FUNCIÓN PARA AGREGAR COMENTARIOS (CORREGIDA)
function submitComment() {
    const commentText = document.getElementById('commentText').value.trim();
    const errorElement = document.getElementById('commentError');
    const confirmBtn = document.getElementById('btnCommentConfirmar');

    if (!commentText) {
        errorElement.style.display = 'block';
        return;
    }
    errorElement.style.display = 'none';

    const originalButtonText = confirmBtn.innerHTML;
    confirmBtn.disabled = true;
    confirmBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Agregando...';

    fetch(`/api/quejas/${_pendingAction.id}/comentarios`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'X-Requested-With': 'XMLHttpRequest' },
        body: JSON.stringify({ text: commentText })
    })
        .then(res => {
            if (res.ok) {
                closeModal('commentModal');
                updateCommentCount(_pendingAction.id);
                // No intentamos procesar JSON si la respuesta está vacía
                return res.status === 204 ? null : res.json();
            } else {
                return res.json().then(err => {
                    throw new Error(err.message || 'Error al agregar comentario');
                });
            }
        })
        .catch(error => {
            console.error(error);
            alert('Ocurrió un error al agregar el comentario: ' + error.message);
        })
        .finally(() => {
            confirmBtn.disabled = false;
            confirmBtn.innerHTML = originalButtonText;
        });
}

// FUNCIÓN COMENTARIOS DE UNA QUEJA
function updateCommentCount(id) {
    const row = document.querySelector(`tr[data-queja-id="${id}"]`);
    if (row) {
        const comentariosCell = row.querySelector('.comentarios-cell');
        if (comentariosCell) {
            const countSpan = comentariosCell.querySelector('.comentarios-count');
            if (countSpan) {
                const currentCount = parseInt(countSpan.textContent.match(/\d+/)[0]);
                countSpan.textContent = `${currentCount + 1} comentario(s)`;
            }
        }
    }
}

// FUNCIÓN PARA VER COMENTARIOS
function verComentarios(id) {
    const modal = document.getElementById('commentsModal');
    const commentsList = document.getElementById('commentsList');

    openModal('commentsModal');
    commentsList.innerHTML = '<p>Cargando comentarios...</p>';

    fetch(`/api/quejas/${id}/comentarios`)
        .then(response => response.ok ? response.json() : Promise.reject('No se pudieron cargar los comentarios.'))
        .then(comments => {
            if (!comments.length) {
                commentsList.innerHTML = '<div class="empty-state"><h3>No hay comentarios para esta queja.</h3></div>';
                return;
            }

            const sortedComments = comments.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

            commentsList.innerHTML = `
                <div class="results-table-container">
                    <table class="results-table">
                        <thead>
                            <tr>
                                <th>Comentario</th>
                                <th style="width: 30%;">Fecha y hora</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${sortedComments.map(comment => `
                                <tr>
                                    <td style="word-wrap: break-word; max-width: 0;">${comment.text}</td>
                                    <td style="white-space: nowrap;">${new Date(comment.timestamp).toLocaleString()}</td>
                                </tr>
                            `).join('')}
                        </tbody>
                    </table>
                </div>
            `;
        })
        .catch(error => {
            console.error('Error al cargar comentarios:', error);
            commentsList.innerHTML = '<div class="empty-state"><h3>Ocurrió un error al cargar los comentarios.</h3></div>';
        });
}

// FUNCIÓN PARA VER HISTORIAL DE ESTADOS
function verHistorialEstados(id) {
    const modal = document.getElementById('historyModal');
    const historyList = document.getElementById('historyList');

    openModal('historyModal');
    historyList.innerHTML = '<p>Cargando historial...</p>';

    fetch(`/api/quejas/${id}/historial-estados`)
        .then(response => response.ok ? response.json() : Promise.reject('No se pudo cargar el historial de estados.'))
        .then(history => {
            if (!history.length) {
                historyList.innerHTML = '<div class="empty-state"><h3>No hay historial de estados para esta queja.</h3></div>';
                return;
            }
            const sortedHistory = history.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

            historyList.innerHTML = `
                <div class="results-table-container">
                    <table class="results-table">
                        <thead>
                            <tr>
                                <th>Estado</th>
                                <th>Fecha del cambio</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${sortedHistory.map(item => {
                // Usamos el mapa de estados para obtener el display name correcto
                const stateInfo = STATE_MAP[item.state] || { display: item.state };
                return `
                                <tr>
                                    <td>${stateInfo.display}</td>
                                    <td>${new Date(item.timestamp).toLocaleString()}</td>
                                </tr>
                            `}).join('')}
                        </tbody>
                    </table>
                </div>
            `;
        })
        .catch(error => {
            console.error('Error al cargar historial:', error);
            historyList.innerHTML = '<div class="empty-state"><h3>Ocurrió un error al cargar el historial de estados.</h3></div>';
        });
}

// FUNCIONES DE PAGINACIÓN
function initializePagination() {
    allRows = Array.from(document.querySelectorAll('.table-row'));
    totalItems = allRows.length;
    const container = document.getElementById('paginationContainer');
    container.style.display = totalItems <= itemsPerPage ? 'none' : 'flex';

    if (totalItems > itemsPerPage) {
        createPageButtons();
        updatePagination();
        document.getElementById('prevBtn').onclick = () => { if (currentPage > 1) { currentPage--; updatePagination(); } };
        document.getElementById('nextBtn').onclick = () => { if (currentPage < Math.ceil(totalItems / itemsPerPage)) { currentPage++; updatePagination(); } };
    } else {
        allRows.forEach(r => r.classList.remove('hidden'));
    }
}

function updatePagination() {
    const start = (currentPage - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    const totalPages = Math.ceil(totalItems / itemsPerPage);

    allRows.forEach((row, i) => row.classList.toggle('hidden', !(i >= start && i < end)));

    document.getElementById('paginationInfo').innerHTML = `Mostrando ${start + 1}-${Math.min(end, totalItems)} de ${totalItems} resultados`;
    document.getElementById('prevBtn').disabled = currentPage === 1;
    document.getElementById('nextBtn').disabled = currentPage === totalPages;

    document.querySelectorAll('.page-btn').forEach(btn => {
        btn.classList.toggle('active', parseInt(btn.textContent) === currentPage);
    });
}

function createPageButtons() {
    const totalPages = Math.ceil(totalItems / itemsPerPage);
    const pagesContainer = document.getElementById('paginationPages');
    pagesContainer.innerHTML = '';

    let startPage = Math.max(1, currentPage - 2);
    let endPage = Math.min(totalPages, startPage + 4);
    if (endPage - startPage < 4) startPage = Math.max(1, endPage - 4);

    for (let i = startPage; i <= endPage; i++) {
        const btn = document.createElement('button');
        btn.className = 'page-btn';
        btn.textContent = i;
        btn.classList.toggle('active', i === currentPage);
        btn.onclick = () => { currentPage = i; updatePagination(); };
        pagesContainer.appendChild(btn);
    }
}

// FUNCIÓN ORDENAR QUEJAS POR ID (CORREGIDA)
function sortQuejasById() {
    const tableBody = document.getElementById('tableBody');
    if (!tableBody) return;

    const rows = Array.from(tableBody.querySelectorAll('tr'));

    rows.sort((a, b) => {
        const idA = parseInt(a.getAttribute('data-queja-id'));
        const idB = parseInt(b.getAttribute('data-queja-id'));
        return idA - idB;
    });

    tableBody.innerHTML = '';
    rows.forEach(row => tableBody.appendChild(row));

    initializePagination();
}

// INICIALIZACIÓN
document.addEventListener('DOMContentLoaded', () => {
    if (document.getElementById('tableContainer')) {
        sortQuejasById();
        document.getElementById('tableContainer').scrollIntoView({ behavior: 'smooth', block: 'start' });
    }

    document.getElementById('searchForm')?.addEventListener('submit', e => {
        if (!document.getElementById('entidad').value) {
            alert('Por favor seleccione una entidad');
            e.preventDefault();
        }
    });

    document.getElementById('btnCommentConfirmar').onclick = submitComment;
    document.getElementById('btnCommentCancelar').onclick = () => closeModal('commentModal');

    ['stateModal', 'commentModal', 'commentsModal', 'historyModal'].forEach(setupModalCloseOnOutsideClick);
});