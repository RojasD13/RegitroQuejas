(function () {
    'use strict';
    document.addEventListener('DOMContentLoaded', function () {
        console.log('🏛️ Centro de Quejas (shared) cargado correctamente');
        initializeSidebarInteractions();
        initializeFormAnimations();
        autoCloseAlerts();
    });
    // Sidebar / navegación
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
        if (window.innerWidth <= 768) {
            let toggleButton = document.querySelector('.sidebar-toggle');
            if (!toggleButton) {
                toggleButton = document.createElement('button');
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
    }
    // Animaciones y efectos 
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
    // Auto-cerrar alertas
    function autoCloseAlerts() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(alert => {
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
})();
