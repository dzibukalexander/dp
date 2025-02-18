function filterDoctors() {
    const filter = document.getElementById('specializationFilter').value.toLowerCase();
    const cards = document.querySelectorAll('.card');

    cards.forEach(card => {
        const specialization = card.getAttribute('data-specialization').toLowerCase();
        if (specialization.includes(filter)) {
            card.style.display = 'block';
        } else {
            card.style.display = 'none';
        }
    });
}

function sendPhoto(doctorId) {
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = 'image/*';
    fileInput.onchange = (e) => {
        const file = e.target.files[0];
        if (file) {
            const formData = new FormData();
            formData.append('file', file);
            formData.append('doctorId', doctorId);

            fetch('/send-photo', {
                method: 'POST',
                body: formData
            }).then(response => {
                if (response.ok) {
                    alert('Фото отправлено!');
                } else {
                    alert('Ошибка при отправке фото.');
                }
            });
        }
    };
    fileInput.click();
}

// Плавная прокрутка
document.querySelectorAll('.nav-link').forEach(link => {
    link.addEventListener('click', (e) => {
        e.preventDefault();
        const href = link.getAttribute('href');
        document.querySelector(href).scrollIntoView({
            behavior: 'smooth'
        });
    });
});