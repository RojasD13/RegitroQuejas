document.addEventListener('DOMContentLoaded', function () {
    console.log('Centro de Quejas cargado correctamente');
    initializeCharacterCounter();
    initializeFormValidation();
    initializeSidebarInteractions();
    initializeFormAnimations();
    initializeCaptchaHandlers();
    autoCloseAlerts();
});
// Función al completar CAPTCHA exitosamente
function captchaSuccess() {
    const btnBuscar = document.getElementById('btn-buscar');
    if (btnBuscar) {
        btnBuscar.disabled = false;
    }
    if (window.location.pathname.includes('buscar') || document.getElementById('tableContainer')) {
        enviarNotificacionCaptcha();
    }
}
// Función cuando el CAPTCHA expira
function captchaExpired() {
    const btnBuscar = document.getElementById('btn-buscar');
    if (btnBuscar) {
        btnBuscar.disabled = true;
    }

    if (typeof grecaptcha !== 'undefined' && grecaptcha.reset) {
        grecaptcha.reset();
    }
}
// Función para inicializar manejadores de CAPTCHA
function initializeCaptchaHandlers() {
    if (typeof grecaptcha !== 'undefined' && grecaptcha.reset) {
        grecaptcha.reset();
    }
    window.captchaSuccess = captchaSuccess;
    window.captchaExpired = captchaExpired;
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
// Función para el contador de caracteres
function initializeCharacterCounter() {
    const textarea = document.getElementById('descripcion');
    const charCount = document.getElementById('charCount');
    if (textarea && charCount) {
        textarea.addEventListener('input', function () {
            const currentLength = this.value.length;
            const maxLength = this.getAttribute('maxlength');
            charCount.textContent = currentLength;
            if (currentLength > maxLength * 0.9) {
                charCount.style.color = '#e74c3c';
            } else if (currentLength > maxLength * 0.7) {
                charCount.style.color = '#f39c12';
            } else {
                charCount.style.color = '#7f8c8d';
            }
        });
        charCount.textContent = textarea.value.length;
    }
}
// Función para validación del formulario
function initializeFormValidation() {
    const form = document.querySelector('.queja-form');
    const selectEntidad = document.getElementById('entidad');
    const textareaDescripcion = document.getElementById('descripcion');
    if (form) {
        const submitButton = form.querySelector('.btn-primary');
        function validateForm() {
            const entidadValida = selectEntidad && selectEntidad.value !== '' && selectEntidad.value !== 'Seleccione una entidad';
            const descripcionValida = textareaDescripcion ? textareaDescripcion.value.trim().length >= 10 : true;
            if (selectEntidad) updateFieldValidation(selectEntidad, entidadValida);
            if (textareaDescripcion) updateFieldValidation(textareaDescripcion, descripcionValida);
            if (submitButton) {
                submitButton.disabled = !(entidadValida && descripcionValida);
            }
            return entidadValida && descripcionValida;
        }
        if (selectEntidad) selectEntidad.addEventListener('change', validateForm);
        if (textareaDescripcion) textareaDescripcion.addEventListener('input', validateForm);
        validateForm();
        form.addEventListener('submit', function (e) {
            if (!validateForm()) {
                e.preventDefault();
                showValidationMessage('Por favor, complete todos los campos requeridos correctamente.');
            } else if (submitButton) {
                submitButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Enviando...';
                submitButton.disabled = true;
            }
        });
        const resetButton = form.querySelector('.btn-secondary');
        if (resetButton) {
            resetButton.addEventListener('click', function () {
                setTimeout(() => {
                    validateForm();
                    const charCount = document.getElementById('charCount');
                    if (charCount) charCount.textContent = '0';
                }, 50);
            });
        }
        if (selectEntidad) {
            selectEntidad.addEventListener('change', function (e) {
                if (this.value === '') {
                    this.selectedIndex = 0;
                }
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
    const existingMessage = document.querySelector('.validation-message');
    if (existingMessage) {
        existingMessage.remove();
    }
    const messageElement = document.createElement('div');
    messageElement.className = 'alert alert-error validation-message';
    messageElement.innerHTML = `
        <i class="fas fa-exclamation-triangle"></i>
        <span>${message}</span>
        <button class="alert-close" onclick="this.parentElement.remove()">
            <i class="fas fa-times"></i>
        </button>
    `;
    const formContainer = document.querySelector('.form-container') || document.querySelector('.search-container');
    if (formContainer) {
        formContainer.insertBefore(messageElement, formContainer.firstChild);
    }
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
            this.style.transform = 'scale(0.98)';
            setTimeout(() => {
                this.style.transform = 'scale(1)';
            }, 150);
        });
    });
    createMobileToggle();
}
// Función para crear toggle móvil
function createMobileToggle() {
    if (window.innerWidth <= 768) {
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
            toggleButton.addEventListener('click', function () {
                const sidebar = document.querySelector('.sidebar');
                if (sidebar) {
                    sidebar.classList.toggle('active');
                }
            });
        }
    }
}
// Función para animaciones del formulario
function initializeFormAnimations() {
    const formFields = document.querySelectorAll('.form-select, .form-textarea');
    formFields.forEach(field => {
        field.addEventListener('focus', function () {
            if (this.parentNode) {
                this.parentNode.style.transform = 'scale(1.02)';
                this.parentNode.style.transition = 'transform 0.2s ease';
            }
        });
        field.addEventListener('blur', function () {
            if (this.parentNode) {
                this.parentNode.style.transform = 'scale(1)';
            }
        });
    });
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