// Динамическое обновление чекбоксов
document.querySelectorAll('.form-check-input').forEach(checkbox => {
    checkbox.addEventListener('change', function() {
        const label = this.nextElementSibling;
        if (this.checked) {
            label.style.color = '#2A6FDB';
        } else {
            label.style.color = '#333';
        }
    });
});