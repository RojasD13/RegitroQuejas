(function () {
    'use strict';
    document.addEventListener('DOMContentLoaded', function () {
        console.log('🏛️ Centro de Quejas (shared) cargado correctamente');
        initializeSidebarInteractions();
        initializeFormAnimations();
        autoCloseAlerts();
        initializeCharacterCounters();
        initializeResponsiveElements();
        initializeLoginState();
    });
    function initializeLoginState() {
        checkLoginStatus();
        window.addEventListener('storage', function (e) {
            if (e.key === 'userLoggedIn') {
                checkLoginStatus();
            }
        });
    }
    function checkLoginStatus() {
        fetch('/api/user/status')
            .then(response => response.json())
            .then(data => {
                if (data.loggedIn) {
                    updateLoginButton(true, data.username);
                } else {
                    updateLoginButton(false);
                }
            })
            .catch(error => {
                console.error('Error al verificar el estado de inicio de sesión:', error);
                updateLoginButton(false);
            });
    }
    function updateLoginButton(isLoggedIn, username = '') {
        const loginButton = document.getElementById('loginButton');
        const userInfo = document.getElementById('userInfo');
        const userName = document.getElementById('userName');

        if (isLoggedIn) {
            loginButton.innerHTML = '<i class="fas fa-sign-out-alt"></i><span>Cerrar Sesión</span>';
            loginButton.onclick = function (e) {
                e.preventDefault();
                logout();
            };
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
        fetch('/auth/logout', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    updateLoginButton(false);
                    window.location.href = '/login';
                } else {
                    console.error('Error al cerrar sesión');
                }
            })
            .catch(error => {
                console.error('Error en la solicitud de logout:', error);
                updateLoginButton(false);
                window.location.href = '/login';
            });
    }
    function initializeSidebarInteractions() {
        const sidebarLinks = document.querySelectorAll('.sidebar-menu a');
        sidebarLinks.forEach(link => {
            link.addEventListener('click', function () {
                this.style.transform = 'scale(0.98)';
                setTimeout(() => {
                    this.style.transform = 'scale(1)';
                }, 150);
            });
        });
        createMobileToggle();
    }
    function createMobileToggle() {
        const existingToggle = document.querySelector('.sidebar-toggle');
        if (existingToggle) {
            existingToggle.remove();
        }
        if (window.innerWidth <= 768) {
            const toggleButton = document.createElement('button');
            toggleButton.className = 'sidebar-toggle';
            toggleButton.setAttribute('aria-label', 'Alternar menú lateral');
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
                if (sidebar) sidebar.classList.toggle('active');
            });
        }
    }
    function initializeFormAnimations() {
        const formFields = document.querySelectorAll('.form-select, .form-textarea, .form-input');
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
        const buttons = document.querySelectorAll('.btn');
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
    function initializeCharacterCounters() {
        const textareas = document.querySelectorAll('textarea[maxlength]');
        textareas.forEach(textarea => {
            const maxLength = textarea.getAttribute('maxlength');
            const counterId = textarea.id + 'Count';
            let counter = document.getElementById(counterId);
            if (!counter) {
                counter = document.createElement('span');
                counter.id = counterId;
                counter.className = 'character-count';
                textarea.parentNode.appendChild(counter);
            }
            function updateCounter() {
                counter.textContent = `${textarea.value.length} / ${maxLength} caracteres`;
                counter.style.color = textarea.value.length > maxLength * 0.9 ? '#e74c3c' : '#666';
            }
            textarea.addEventListener('input', updateCounter);
            updateCounter();
        });
    }
    function autoCloseAlerts() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(alert => {
            if (!alert.querySelector('.alert-close')) {
                const closeButton = document.createElement('button');
                closeButton.className = 'alert-close';
                closeButton.innerHTML = '&times;';
                closeButton.setAttribute('aria-label', 'Cerrar alerta');
                closeButton.style.cssText = `
                    background: none;
                    border: none;
                    font-size: 1.2rem;
                    cursor: pointer;
                    margin-left: 10px;
                `;
                closeButton.addEventListener('click', () => {
                    alert.style.opacity = '0';
                    alert.style.transform = 'translateY(-20px)';
                    setTimeout(() => {
                        if (alert.parentNode) alert.remove();
                    }, 300);
                });
                alert.appendChild(closeButton);
            }
            setTimeout(() => {
                if (alert.parentNode) {
                    alert.style.opacity = '0';
                    alert.style.transform = 'translateY(-20px)';
                    setTimeout(() => {
                        if (alert.parentNode) alert.remove();
                    }, 300);
                }
            }, 5000);
        });
    }
    function initializeResponsiveElements() {
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
        const tables = document.querySelectorAll('.results-table');
        tables.forEach(table => {
            table.setAttribute('role', 'table');
            const headers = table.querySelectorAll('th');
            headers.forEach(header => {
                header.setAttribute('role', 'columnheader');
                header.setAttribute('scope', 'col');
            });
            const cells = table.querySelectorAll('td');
            cells.forEach(cell => {
                cell.setAttribute('role', 'cell');
            });
        });
    }
})();