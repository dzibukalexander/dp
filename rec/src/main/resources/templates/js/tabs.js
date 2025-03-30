document.addEventListener('DOMContentLoaded', function() {
    // Обработка переключения вкладок
    document.querySelectorAll('.tab-button').forEach(button => {
        button.addEventListener('click', function() {
            const tabName = this.getAttribute('data-tab-target');
            openTab(tabName);

            // Обновляем активное состояние кнопок
            document.querySelectorAll('.tab-button').forEach(btn => {
                btn.classList.remove('active');
            });
            this.classList.add('active');
        });
    });

    function openTab(tabName) {
        // Скрыть все вкладки
        document.querySelectorAll('.tab-content').forEach(tab => {
            tab.classList.remove('active');
        });

        // Показать выбранную вкладку
        document.getElementById(tabName).classList.add('active');
    }

    // Инициализация - показать первую вкладку по умолчанию
    document.querySelector('.tab-button.active').click();
});