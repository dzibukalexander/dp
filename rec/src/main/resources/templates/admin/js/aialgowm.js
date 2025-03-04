// Открытие модального окна для редактирования
function openEditModal(event) {
    event.preventDefault();
    const url = event.target.closest('a').href;
    fetch(url)
        .then(response => response.text())
        .then(html => {
            const modal = document.createElement('div');
            modal.innerHTML = html;
            document.body.appendChild(modal);
            $(modal).modal('show');
        });
}

// Связывание AIAlgo с Position
document.querySelectorAll('.clickable-row').forEach(row => {
    row.addEventListener('click', () => {
        const aiAlgoId = row.dataset.id;
        $('#positionModal').modal('show');
    });
});

document.querySelectorAll('.btn-select-position').forEach(btn => {
    btn.addEventListener('click', () => {
        const positionId = btn.dataset.positionId;
        const aiAlgoId = // Получить ID AIAlgo из контекста
            fetch(`/admin/ai-algos/${aiAlgoId}/link-position/${positionId}`, {method: 'POST'})
                .then(() => window.location.reload());
    });
});