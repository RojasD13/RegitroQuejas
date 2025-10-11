// SISTEMA MODULAR DE AUTENTICACIÓN CON CONTRASEÑA
const PasswordAuth = {
    ADMIN_PASSWORD: 'ADMIN',
    currentCallback: null,
    showModal: function(title, message, onSuccess) {
        document.getElementById('modalTitle').textContent = title;
        document.getElementById('modalMessage').textContent = message;
        this.currentCallback = onSuccess;
        document.getElementById('passwordModal').style.display = 'block';

        document.getElementById('adminPassword').value = '';
        document.getElementById('passwordError').style.display = 'none';

        setTimeout(() => {
            document.getElementById('adminPassword').focus();
        }, 100);
    },
    validatePassword: function() {
        const password = document.getElementById('adminPassword').value;
        const errorDiv = document.getElementById('passwordError');

        if (password === this.ADMIN_PASSWORD) {
            errorDiv.style.display = 'none';
            return true;
        } else {
            errorDiv.style.display = 'block';
            errorDiv.textContent = 'Contraseña incorrecta. Ingrese "ADMIN"';
            return false;
        }
    },
    confirm: function() {
        if (this.validatePassword() && this.currentCallback) {
            this.currentCallback();
            this.closeModal();
        }
    },
    closeModal: function() {
        document.getElementById('passwordModal').style.display = 'none';
        document.getElementById('adminPassword').value = '';
        document.getElementById('passwordError').style.display = 'none';
        this.currentCallback = null;
    }
};
// EVENTOS DEL MODAL
document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('btnModalConfirmar').addEventListener('click', () => {
        PasswordAuth.confirm();
    });

    document.getElementById('btnModalCancelar').addEventListener('click', () => {
        PasswordAuth.closeModal();
    });

    document.getElementById('adminPassword').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            PasswordAuth.confirm();
        }
    });

    document.getElementById('adminPassword').addEventListener('input', function() {
        document.getElementById('passwordError').style.display = 'none';
    });

    document.getElementById('passwordModal').addEventListener('click', function(e) {
        if (e.target === this) {
            PasswordAuth.closeModal();
        }
    });
});