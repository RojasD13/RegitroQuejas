// JavaScript para Centro de Quejas
document.addEventListener('DOMContentLoaded', function () {
    console.log('🏛️ Centro de Quejas cargado correctamente');

    // Inicializar componentes
    initializeCharacterCounter();
    initializeFormValidation();
    initializeSidebarInteractions();
    initializeFormAnimations();

    // Auto-cerrar alertas después de 5 segundos
    autoCloseAlerts();
});

// Función para el contador de caracteres
function initializeCharacterCounter() {
    const textarea = document.getElementById('descripcion');
    const charCount = document.getElementById('charCount');

    if (textarea && charCount) {
        // Actualizar contador al escribir
        textarea.addEventListener('input', function () {
            const currentLength = this.value.length;
            const maxLength = this.getAttribute('maxlength');

            charCount.textContent = currentLength;

            // Cambiar color según proximidad al límite
            if (currentLength > maxLength * 0.9) {
                charCount.style.color = '#e74c3c'; // Rojo
            } else if (currentLength > maxLength * 0.7) {
                charCount.style.color = '#f39c12'; // Amarillo
            } else {
                charCount.style.color = '#7f8c8d'; // Gris normal
            }
        });

        // Inicializar contador
        charCount.textContent = textarea.value.length;
    }
}

// Función para validación del formulario
function initializeFormValidation() {
    const form = document.querySelector('.queja-form');
    const selectEntidad = document.getElementById('entidad');
    const textareaDescripcion = document.getElementById('descripcion');
    const submitButton = form.querySelector('.btn-primary');

    if (form) {
        // Validación en tiempo real
        function validateForm() {
            const entidadValida = selectEntidad.value !== 'Seleccione una entidad';
            const descripcionValida = textareaDescripcion.value.trim().length >= 10;

            // Actualizar estilos de validación
            updateFieldValidation(selectEntidad, entidadValida);
            updateFieldValidation(textareaDescripcion, descripcionValida);

            // Habilitar/deshabilitar botón de envío
            submitButton.disabled = !(entidadValida && descripcionValida);

            return entidadValida && descripcionValida;
        }

        // Eventos de validación
        selectEntidad.addEventListener('change', validateForm);
        textareaDescripcion.addEventListener('input', validateForm);

        // Validación inicial
        validateForm();

        // Validación al enviar
        form.addEventListener('submit', function (e) {
            if (!validateForm()) {
                e.preventDefault();
                showValidationMessage('Por favor, complete todos los campos requeridos correctamente.');
            } else {
                // Mostrar loading en el botón
                submitButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Enviando...';
                submitButton.disabled = true;
            }
        });

        // Limpiar formulario
        const resetButton = form.querySelector('.btn-secondary');
        if (resetButton) {
            resetButton.addEventListener('click', function () {
                setTimeout(() => {
                    validateForm();
                    document.getElementById('charCount').textContent = '0';
                }, 50);
            });
        }
    }
}

// Función para actualizar estilos de validación
function updateFieldValidation(field, isValid) {
    if (field.value.length > 0) {
        if (isValid) {
            field.style.borderColor = '#27ae60';
            field.classList.remove('invalid');
            field.classList.add('valid');
        } else {
            field.style.borderColor = '#e74c3c';
            field.classList.remove('valid');
            field.classList.add('invalid');
        }
    } else {
        field.style.borderColor = '#bdc3c7';
        field.classList.remove('valid', 'invalid');
    }
}

// Función para mostrar mensajes de validación
function showValidationMessage(message) {
    // Remover mensajes existentes
    const existingMessage = document.querySelector('.validation-message');
    if (existingMessage) {
        existingMessage.remove();
    }

    // Crear nuevo mensaje
    const messageElement = document.createElement('div');
    messageElement.className = 'alert alert-error validation-message';
    messageElement.innerHTML = `
        <i class="fas fa-exclamation-triangle"></i>
        <span>${message}</span>
        <button class="alert-close" onclick="this.parentElement.remove()">
            <i class="fas fa-times"></i>
        </button>
    `;

    // Insertar antes del formulario
    const formContainer = document.querySelector('.form-container');
    formContainer.insertBefore(messageElement, formContainer.firstChild);

    // Auto-remover después de 5 segundos
    setTimeout(() => {
        if (messageElement.parentNode) {
            messageElement.remove();
        }
    }, 5000);
}

// Función para interacciones del sidebar
function initializeSidebarInteractions() {
    const sidebarLinks = document.querySelectorAll('.sidebar-menu a');

    sidebarLinks.forEach(link => {
        link.addEventListener('click', function (e) {
            // Efecto visual
            this.style.transform = 'scale(0.98)';
            setTimeout(() => {
                this.style.transform = 'scale(1)';
            }, 150);
        });
    });

    // Responsive sidebar toggle (para móviles)
    createMobileToggle();
}

// Función para crear toggle móvil
function createMobileToggle() {
    if (window.innerWidth <= 768) {
        // Crear botón de toggle si no existe
        let toggleButton = document.querySelector('.sidebar-toggle');
        if (!toggleButton) {
            toggleButton = document.createElement('button');
            toggleButton.className = 'sidebar-toggle';
            toggleButton.innerHTML = '<i class="fas fa-bars"></i>';
            toggleButton.style.cssText = `
                position: fixed;
                top: 1rem;
                left: 1rem;
                z-index: 1001;
                background: #1e4d72;
                color: white;
                border: none;
                padding: 0.5rem;
                border-radius: 4px;
                font-size: 1.2rem;
                cursor: pointer;
                display: block;
            `;

            document.body.appendChild(toggleButton);

            // Funcionalidad del toggle
            toggleButton.addEventListener('click', function () {
                const sidebar = document.querySelector('.sidebar');
                sidebar.classList.toggle('active');
            });
        }
    }
}

// Función para animaciones del formulario
function initializeFormAnimations() {
    // Animación al enfocar campos
    const formFields = document.querySelectorAll('.form-select, .form-textarea');

    formFields.forEach(field => {
        field.addEventListener('focus', function () {
            this.parentNode.style.transform = 'scale(1.02)';
            this.parentNode.style.transition = 'transform 0.2s ease';
        });

        field.addEventListener('blur', function () {
            this.parentNode.style.transform = 'scale(1)';
        });
    });

    // Animación para los botones
    const buttons = document.querySelectorAll('.btn-primary, .btn-secondary');

    buttons.forEach(button => {
        button.addEventListener('mousedown', function () {
            this.style.transform = 'scale(0.95)';
        });

        button.addEventListener('mouseup', function () {
            this.style.transform = 'scale(1)';
        });

        button.addEventListener('mouseleave', function () {
            this.style.transform = 'scale(1)';
        });
    });
}

// Función para auto-cerrar alertas
function autoCloseAlerts() {
    const alerts = document.querySelectorAll('.alert');

    alerts.forEach(alert => {
        setTimeout(() => {
            if (alert.parentNode) {
                alert.style.opacity = '0';
                alert.style.transform = 'translateY(-20px)';
                setTimeout(() => {
                    alert.remove();
                }, 300);
            }
        }, 5000);
    });
}

// Función para m