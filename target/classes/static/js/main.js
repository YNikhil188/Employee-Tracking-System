// Global utility functions for the application

document.addEventListener('DOMContentLoaded', () => {
    initTheme();
    setupSidebarToggle();
});

// Theme Management (Dark / Light Mode)
function initTheme() {
    const savedTheme = localStorage.getItem('theme') || 'light';
    document.documentElement.setAttribute('data-theme', savedTheme);
    updateThemeIcon(savedTheme);

    const themeToggleBtn = document.getElementById('theme-toggle');
    if (themeToggleBtn) {
        themeToggleBtn.addEventListener('click', () => {
            const currentTheme = document.documentElement.getAttribute('data-theme');
            const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
            document.documentElement.setAttribute('data-theme', newTheme);
            localStorage.setItem('theme', newTheme);
            updateThemeIcon(newTheme);
        });
    }
}

function updateThemeIcon(theme) {
    const icon = document.querySelector('#theme-toggle i');
    if (icon) {
        if (theme === 'dark') {
            icon.className = 'bi bi-sun-fill';
        } else {
            icon.className = 'bi bi-moon-fill';
        }
    }
}

// Mobile Sidebar Toggle
function setupSidebarToggle() {
    const toggleBtn = document.getElementById('sidebar-toggle');
    const sidebar = document.querySelector('.sidebar');
    const mainContent = document.querySelector('.main-content');
    
    if (toggleBtn && sidebar && mainContent) {
        toggleBtn.addEventListener('click', () => {
            sidebar.classList.toggle('show');
            mainContent.classList.toggle('sidebar-open');
        });
    }
}

// Loading Spinner Utility
const Spinner = {
    show: () => {
        const spinner = document.getElementById('loading-spinner');
        if (spinner) spinner.classList.remove('d-none');
    },
    hide: () => {
        const spinner = document.getElementById('loading-spinner');
        if (spinner) spinner.classList.add('d-none');
    }
};

// Bootstrap Toast Notifications
const Toast = {
    show: (message, type = 'success') => {
        const container = document.getElementById('toast-container');
        if (!container) {
            const newContainer = document.createElement('div');
            newContainer.id = 'toast-container';
            newContainer.className = 'toast-container';
            document.body.appendChild(newContainer);
        }

        const toastId = 'toast_' + Date.now();
        const iconClass = type === 'success' ? 'bi-check-circle-fill text-success' : 'bi-exclamation-triangle-fill text-danger';
        const title = type === 'success' ? 'Success' : 'Error';

        const toastHtml = `
            <div id="${toastId}" class="toast align-items-center border-0 shadow" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="d-flex">
                    <div class="toast-body d-flex align-items-center gap-2">
                        <i class="bi ${iconClass} fs-5"></i>
                        <div><strong>${title}</strong>: ${message}</div>
                    </div>
                    <button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
            </div>
        `;

        document.getElementById('toast-container').insertAdjacentHTML('beforeend', toastHtml);
        const toastElement = document.getElementById(toastId);
        
        // Add color accent border based on type
        if (type === 'success') {
            toastElement.classList.add('border-start', 'border-success', 'border-4');
        } else {
            toastElement.classList.add('border-start', 'border-danger', 'border-4');
        }

        const bsToast = new bootstrap.Toast(toastElement, { delay: 4000 });
        bsToast.show();

        // Remove element after it hides
        toastElement.addEventListener('hidden.bs.toast', () => {
            toastElement.remove();
        });
    }
};

// Global Fetch Wrapper
async function apiRequest(url, options = {}) {
    Spinner.show();
    
    // Inject headers
    options.headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };

    try {
        const response = await fetch(url, options);
        if (response.status === 204) {
            Spinner.hide();
            return null;
        }

        const contentType = response.headers.get('content-type');
        let data;
        if (contentType && contentType.includes('application/json')) {
            data = await response.json();
        } else {
            data = await response.text();
        }

        if (!response.ok) {
            const errorMsg = (data && data.message) || `HTTP error! Status: ${response.status}`;
            throw new Error(errorMsg);
        }

        Spinner.hide();
        return data;
    } catch (error) {
        Spinner.hide();
        logError(error.message);
        Toast.show(error.message, 'error');
        throw error;
    }
}

// Logging Utility
function logError(message) {
    console.error(`[App Error]: ${message}`);
}
