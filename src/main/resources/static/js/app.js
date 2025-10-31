(function () {
    'use strict';
    const Messages = {
        AUTH_SERVICE_UNAVAILABLE: 'El servicio de autenticación no está disponible. Algunas funciones pueden estar limitadas.',
        CONNECTION_ERROR: 'Error de conexión. Verifica tu conexión a internet.',
        LOGIN_CHECK_ERROR: 'Error al verificar el estado de inicio de sesión',
        INVALID_PASSWORD: 'Contraseña incorrecta. Ingrese "ADMIN"',
        FORM_VALIDATION_ERROR: 'Por favor, complete todos los campos requeridos correctamente.',
        COMMENT_ERROR: 'Ocurrió un error al agregar el comentario: ',
        CAPTCHA_ERROR: 'Por favor seleccione una entidad',
        NO_COMMENTS: 'No hay comentarios para esta queja.',
        COMMENTS_LOAD_ERROR: 'Ocurrió un error al cargar los comentarios.'
    };
    document.addEventListener('DOMContentLoaded', function () {
        initializeApp();
    });

    function initializeApp() {
        initializeSidebarInteractions();
        initializeFormAnimations();
        autoCloseAlerts();
        initializeCharacterCounters();
        initializeResponsiveElements();
        initializeLoginState();
    }
    function initializeLoginState() {
        checkLoginStatus();
        window.addEventListener('storage', checkLoginStatus);
    }

    function checkLoginStatus() {
        fetch('/api/user/status')
            .then(response => {
                if (!response.ok) {
                    throw new Error(Messages.LOGIN_CHECK_ERROR);
                }
                return response.json();
            })
            .then(data => {
                updateLoginButton(data.loggedIn, data.username || '');
            })
            .catch(error => {
                console.error(Messages.LOGIN_CHECK_ERROR, error);
                updateLoginButton(false);
                showNotification(
                    error.message && error.message.includes(Messages.LOGIN_CHECK_ERROR) ?
                        Messages.AUTH_SERVICE_UNAVAILABLE :
                        Messages.CONNECTION_ERROR,
                    'warning'
                );
            });
    }
    function updateLoginButton(isLoggedIn, username = '') {
        const loginButton = document.getElementById('loginButton');
        const userInfo = document.getElementById('userInfo');
        const userName = document.getElementById('userName');

        if (isLoggedIn) {
            loginButton.innerHTML = '<i class="fas fa-sign-out-alt"></i><span>Cerrar Sesión</span>';
            loginButton.onclick = e => { e.preventDefault(); logout(); };
            userName.textContent = username;
            userInfo.style.display = 'block';
            localStorage.setItem('userLoggedIn', 'true');
            localStorage.setItem('username', username);
        } else {
            loginButton.innerHTML = '<i class="fas fa-sign-in-alt"></i><span>Iniciar Sesión</span>';
            loginButton.href = '/login';
            loginButton.onclick = null;
            userInfo.style.display = 'none';
            localStorage.removeItem('userLoggedIn');
            localStorage.removeItem('username');
        }
    }
    function logout() {
        fetch('/auth/logout', { method: 'GET', headers: { 'Content-Type': 'application/json' } })
            .catch(error => console.error('Error en logout:', error))
            .finally(() => {
                updateLoginButton(false);
                window.location.href = '/login';
            });
    }
    function initializeSidebarInteractions() {
        document.querySelectorAll('.sidebar-menu a').forEach(link => {
            link.addEventListener('click', function () {
                this.style.transform = 'scale(0.98)';
                setTimeout(() => this.style.transform = 'scale(1)', 150);
            });
        });
        createMobileToggle();
    }
    function createMobileToggle() {
        const existingToggle = document.querySelector('.sidebar-toggle');
        if (existingToggle) existingToggle.remove();

        if (window.innerWidth <= 768) {
            const toggleButton = document.createElement('button');
            toggleButton.className = 'sidebar-toggle';
            toggleButton.setAttribute('aria-label', 'Alternar menú lateral');
            toggleButton.innerHTML = '<i class="fas fa-bars"></i>';
            toggleButton.style.cssText = `
                position: fixed; top: 1rem; left: 1rem; z-index: 1001;
                background: #1e4d72; color: white; border: none;
                padding: 0.5rem; border-radius: 4px; font-size: 1.2rem;
                cursor: pointer; display: block;
            `;
            document.body.appendChild(toggleButton);
            toggleButton.addEventListener('click', () => {
                const sidebar = document.querySelector('.sidebar');
                if (sidebar) sidebar.classList.toggle('active');
            });
        }
    }
    function initializeFormAnimations() {
        document.querySelectorAll('.form-select, .form-textarea, .form-input').forEach(field => {
            field.addEventListener('focus', () => {
                if (field.parentNode) {
                    field.parentNode.style.transform = 'scale(1.02)';
                    field.parentNode.style.transition = 'transform 0.2s ease';
                }
            });
            field.addEventListener('blur', () => {
                if (field.parentNode) field.parentNode.style.transform = 'scale(1)';
            });
        });

        document.querySelectorAll('.btn').forEach(button => {
            ['mousedown', 'mouseup', 'mouseleave'].forEach(event => {
                button.addEventListener(event, () => {
                    button.style.transform = event === 'mousedown' ? 'scale(0.95)' : 'scale(1)';
                });
            });
        });
    }
    function initializeCharacterCounters() {
        document.querySelectorAll('textarea[maxlength]').forEach(textarea => {
            const maxLength = textarea.getAttribute('maxlength');
            const counterId = textarea.id + 'Count';
            let counter = document.getElementById(counterId) || (() => {
                const newCounter = document.createElement('span');
                newCounter.id = counterId;
                newCounter.className = 'character-count';
                textarea.parentNode.appendChild(newCounter);
                return newCounter;
            })();

            function updateCounter() {
                counter.textContent = `${textarea.value.length} / ${maxLength} caracteres`;
                counter.style.color = textarea.value.length > maxLength * 0.9 ? '#e74c3c' : '#666';
            }

            textarea.addEventListener('input', updateCounter);
            updateCounter();
        });
    }    function autoCloseAlerts() {
        document.querySelectorAll('.alert').forEach(alert => {
            if (!alert.querySelector('.alert-close')) {
                const closeButton = document.createElement('button');
                closeButton.className = 'alert-close';
                closeButton.innerHTML = '&times;';
                closeButton.setAttribute('aria-label', 'Cerrar alerta');
                closeButton.style.cssText = 'background: none; border: none; font-size: 1.2rem; cursor: pointer; margin-left: 10px;';
                closeButton.addEventListener('click', () => {
                    alert.style.opacity = '0';
                    alert.style.transform = 'translateY(-20px)';
                    setTimeout(() => alert.remove(), 300);
                });
                alert.appendChild(closeButton);
            }
            setTimeout(() => {
                alert.style.opacity = '0';
                alert.style.transform = 'translateY(-20px)';
                setTimeout(() => alert.remove(), 300);
            }, 5000);
        });
    }
    function showNotification(message, type = 'info') {
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.textContent = message;
        notification.style.cssText = `
            position: fixed; top: 20px; right: 20px; padding: 15px 20px;
            background-color: ${type === 'warning' ? '#f39c12' : type === 'error' ? '#e74c3c' : '#3498db'};
            color: white; border-radius: 4px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            z-index: 1000; transition: opacity 0.3s ease;
        `;

        document.body.appendChild(notification);
        setTimeout(() => {
            notification.style.opacity = '0';
            setTimeout(() => notification.remove(), 300);
        }, 5000);
    }    function initializeResponsiveElements() {
        window.addEventListener('resize', () => {
            if (window.innerWidth > 768) {
                const sidebar = document.querySelector('.sidebar');
                if (sidebar) sidebar.classList.remove('active');
                const toggleButton = document.querySelector('.sidebar-toggle');
                if (toggleButton) toggleButton.remove();
            } else {
                createMobileToggle();
            }
        });

        document.querySelectorAll('.results-table').forEach(table => {
            table.setAttribute('role', 'table');
            table.querySelectorAll('th').forEach(header => {
                header.setAttribute('role', 'columnheader');
                header.setAttribute('scope', 'col');
            });
            table.querySelectorAll('td').forEach(cell => cell.setAttribute('role', 'cell'));
        });
    }
    window.AppMessages = Messages;
})();