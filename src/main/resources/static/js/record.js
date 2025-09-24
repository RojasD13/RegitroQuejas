(function () {
    'use strict';
    document.addEventListener('DOMContentLoaded', function () {
        console.log('📋 Registro: record.js cargado');
        initializeCharacterCounter();
        initializeFormValidation();
    });
    // Contador de caracteres para el textarea #descripcion
    function initializeCharacterCounter() {
        const textarea = document.getElementById('descripcion');
        const charCount = document.getElementById('charCount');
        if (!textarea || !charCount) return;
        textarea.addEventListener('input', function () {
            const currentLength = this.value.length;
            const maxLength = parseInt(this.getAttribute('maxlength'), 10) || 1000;
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
    // Validación y comportamiento del formulario de queja
    function initializeFormValidation() {
        const form = document.querySelector('.queja-form');
        if (!form) return;
        const selectEntidad = document.getElementById('entidad');
        const textareaDescripcion = document.getElementById('descripcion');
        const submitButton = form.querySelector('.btn-primary');
        if (!selectEntidad || !textareaDescripcion || !submitButton) return;
        function validateForm() {
            const entidadValida = selectEntidad.value !== '' && selectEntidad.value !== 'Seleccione una entidad';
            const descripcionValida = textareaDescripcion.value.trim().length >= 10;
            updateFieldValidation(selectEntidad, entidadValida);
            updateFieldValidation(textareaDescripcion, descripcionValida);
            submitButton.disabled = !(entidadValida && descripcionValida);
            return entidadValida && descripcionValida;
        }
        selectEntidad.addEventListener('change', validateForm);
        textareaDescripcion.addEventListener('input', validateForm);
        validateForm();
        form.addEventListener('submit', function (e) {
            if (!validateForm()) {
                e.preventDefault();
                showValidationMessage('Por favor, complete todos los campos requeridos correctamente.');
            } else {
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
    }
    // Actualiza apariencia de un campo según sea válido o no
    function updateFieldValidation(field, isValid) {
        if (!field) return;
        const valueLength = field.value ? field.value.length : 0;
        if (valueLength > 0) {
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
    // Muestra un mensaje de validación (alerta) sobre el formulario
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
        const formContainer = document.querySelector('.form-container');
        if (formContainer) {
            formContainer.insertBefore(messageElement, formContainer.firstChild);
            setTimeout(() => {
                if (messageElement.parentNode) {
                    messageElement.remove();
                }
            }, 5000);
        }
    }
})();
