/* ============================================================
   Gestion Scolaire — Script global
   (toasts, sidebar mobile, confirmation suppression, password toggle)
   ============================================================ */

function showToast(message, type) {
    type = type || 'success';
    var container = document.getElementById('toast-container');
    if (!container) return;

    var icons = { success: 'bi-check-circle', error: 'bi-x-circle', warning: 'bi-exclamation-triangle' };
    var toast = document.createElement('div');
    toast.className = 'toast-item ' + type;
    toast.innerHTML = '<i class="bi ' + (icons[type] || icons.success) + '"></i><span>' + message + '</span>';
    container.appendChild(toast);

    setTimeout(function () {
        toast.style.transition = 'opacity 250ms ease, transform 250ms ease';
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(24px)';
        setTimeout(function () { toast.remove(); }, 260);
    }, 3500);
}

// Sidebar mobile toggle
document.addEventListener('DOMContentLoaded', function () {
    var toggleBtn = document.getElementById('sidebarToggle');
    var sidebar = document.getElementById('sidebar');
    var backdrop = document.getElementById('sidebarBackdrop');

    if (toggleBtn && sidebar && backdrop) {
        toggleBtn.addEventListener('click', function () {
            sidebar.classList.add('open');
            backdrop.classList.add('open');
        });
        backdrop.addEventListener('click', function () {
            sidebar.classList.remove('open');
            backdrop.classList.remove('open');
        });
    }

    // Password show/hide toggle
    document.querySelectorAll('.password-toggle').forEach(function (btn) {
        btn.addEventListener('click', function () {
            var input = document.getElementById(btn.dataset.target);
            if (!input) return;
            var isHidden = input.type === 'password';
            input.type = isHidden ? 'text' : 'password';
            btn.innerHTML = isHidden ? '<i class="bi bi-eye-slash"></i>' : '<i class="bi bi-eye"></i>';
        });
    });

    // Disable submit button + spinner on form submit (evite double-clic)
    document.querySelectorAll('form[data-submit-once]').forEach(function (form) {
        form.addEventListener('submit', function () {
            var btn = form.querySelector('button[type=submit]');
            if (btn && !btn.disabled) {
                btn.dataset.originalHtml = btn.innerHTML;
                btn.disabled = true;
                btn.innerHTML = '<span class="spinner-btn"></span> Connexion...';
            }
        });
    });

    // Auto-affichage des messages flash sous forme de toast
    var flashSuccess = document.getElementById('flash-succes');
    var flashError = document.getElementById('flash-erreur');
    if (flashSuccess) showToast(flashSuccess.dataset.message, 'success');
    if (flashError) showToast(flashError.dataset.message, 'error');

    // Shake sur le message d'erreur de login
    var loginError = document.getElementById('login-error-box');
    if (loginError) loginError.classList.add('shake');

    // Boutons de suppression : interception + ouverture de la modal de confirmation
    document.querySelectorAll('.btn-delete').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.preventDefault();
            var message = btn.getAttribute('data-delete-message') || 'Confirmer la suppression ?';
            var url = btn.getAttribute('href');
            confirmDelete(message, url);
        });
    });
});

/**
 * Ouvre une modal de confirmation de suppression générique.
 * @param {string} message  Le texte de confirmation
 * @param {string} deleteUrl L'URL vers laquelle rediriger si confirmé
 */
function confirmDelete(message, deleteUrl) {
    var modalEl = document.getElementById('confirmDeleteModal');
    if (!modalEl) {
        if (confirm(message)) window.location.href = deleteUrl;
        return;
    }
    document.getElementById('confirmDeleteMessage').textContent = message;
    document.getElementById('confirmDeleteBtn').setAttribute('href', deleteUrl);
    var modal = new bootstrap.Modal(modalEl);
    modal.show();
}
