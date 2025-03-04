// Обработка отправки формы через AJAX
document.getElementById('addPositionForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const formData = new FormData(this);

    fetch(this.action, {
        method: 'POST',
        body: formData,
        headers: {
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
        .then(response => {
            if (response.ok) {
                $('#addPositionModal').modal('hide');
                window.location.reload(); // Обновляем страницу
            } else {
                alert('Ошибка при сохранении');
            }
        });
});