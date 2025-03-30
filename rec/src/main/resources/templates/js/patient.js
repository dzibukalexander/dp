// Объявляем функции в глобальной области видимости
window.sendPhoto = function (doctorId) {
    console.log("sendPhoto func called for doctor:", doctorId);
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = 'image/*';
    fileInput.onchange = async (e) => {
        const file = e.target.files[0];
        if (file) {
            // Показываем экран загрузки
            document.getElementById('loading-overlay').style.display = 'flex';

            // Блокируем весь интерфейс
            document.body.style.overflow = 'hidden';
            const formData = new FormData();
            formData.append('file', file);
            formData.append('doctorId', doctorId);

            await fetch('/send-photo', {
                method: 'POST',
                body: formData
            }).then(response => {
                if (response.ok) {
                    console.log("Photo sent successfully");
                    location.reload();
                    // document.querySelector('[data-tab-target="open-tab"]').click();
                } else {
                    alert('Ошибка при отправке фото');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ошибка при отправке фото');
            })
            .finally(() => {
                document.getElementById('loading-overlay').style.display = 'none';
                document.body.style.overflow = '';
            });
        }
    };
    fileInput.click();
};


window.filterDoctors = function () {
    const input = document.getElementById('specializationFilter');
    const filter = input.value.toUpperCase();
    const cards = document.querySelectorAll('.card');

    cards.forEach(card => {
        const specialization = card.getAttribute('data-specialization').toUpperCase();
        card.style.display = specialization.includes(filter) ? '' : 'none';
    });
};

// Инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', function () {
    // Плавная прокрутка
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();
            const href = this.getAttribute('href');
            const target = document.querySelector(href);
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth'
                });
            }
        });
    });

    // Инициализация фильтра при загрузке
    const filterInput = document.getElementById('specializationFilter');
    if (filterInput) {
        filterInput.addEventListener('input', filterDoctors);
    }
});