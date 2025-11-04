// VARIABLES GLOBALES
let currentPage = 1, itemsPerPage = 10, totalItems = 0, allRows = [];
let _pendingAction = { id: null, type: null, data: null };
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
    PasswordAuth.showModal('Confirmar cambio', `Autorizar cambio a: ${state}`, executePendingAction);
}
function openCommentModal() {
    openModal('commentModal');
    document.getElementById('commentText').value = '';
    document.getElementById('commentError').style.display = 'none';
}
function executePendingAction() {
    const { id, type, data } = _pendingAction;
    switch (type) {
        case 'delete': submitActionForm(id, `/quejas/${id}/ocultar`); break;
        case 'changeState': submitActionForm(id, `/quejas/${id}/cambiar-estado`, { state: data }); break;
        case 'addComment': submitComment(); break;
    }
}
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
// FUNCIÓN PARA AGREGAR COMENTARIOS
function submitComment() {
    const commentText = document.getElementById('commentText').value.trim();
    document.getElementById('commentError').style.display = commentText ? 'none' : 'block';
    if (!commentText) return;

    fetch(`/api/quejas/${_pendingAction.id}/comentarios`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'X-Requested-With': 'XMLHttpRequest' },
        body: JSON.stringify({ text: commentText })
    })
        .then(res => {
            if (res.ok) {
                window.location.reload();
            } else {
                return res.json().then(err => {
                    throw new Error(err.message || 'Error al agregar comentario');
                });
            }
        })
        .catch(error => {
            console.error(error);
            alert('Ocurrió un error al agregar el comentario: ' + error.message);
        });
    closeModal('commentModal');
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
                            ${comments.map(comment => `
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
// INICIALIZACIÓN
document.addEventListener('DOMContentLoaded', () => {
    if (document.getElementById('tableContainer')) {
        initializePagination();
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

    ['stateModal', 'commentModal', 'commentsModal'].forEach(setupModalCloseOnOutsideClick);
});