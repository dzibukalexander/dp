function showErrorMessage(message) {
    console.log("showErrorMessage")
    const errorElement = document.getElementById('prediction-error') || createErrorElement();
    errorElement.textContent = message;
    errorElement.style.display = 'block';
    setTimeout(() => errorElement.style.display = 'none', 5000);
}

function createErrorElement() {
    console.log("createErrorElement")
    const errorElement = document.createElement('div');
    errorElement.id = 'prediction-error';
    errorElement.className = 'error-message';
    document.body.appendChild(errorElement);
    return errorElement;
}

// Основная функция прогноза
window.makePrediction = async function(requestId, modelId) {
    console.log("makePrediction")
    try {
        // Блокируем UI
        updateUI(true);

        // Устанавливаем соединение
        await connectWebSocket(requestId);
        startStatusChecking(requestId);

        // Отправляем запрос на прогноз
        const response = await fetch("/api/ml/predict/" + modelId + "?request=" + requestId + "", {
            method: 'POST',
            headers: { 'Accept': 'application/json' }
        });

        if (!response.ok) {
            throw new Error(await response.text());
        }
    } catch (error) {
        console.error('Prediction error:', error);
        showErrorMessage('Ошибка прогноза: ' + error.message);
        cleanup();
        disconnectWebSocket();
    }
};

export function init(requestId) {
    console.log("init")
    // Инициализация UI при загрузке
    fetch("/request/" + requestId + "/status")
        .then(response => response.json())
        .then(async (status) => {
            updateUI(status.inProgress);

            // 2. Если запрос в процессе, устанавливаем соединение
            if (status.inProgress) {
                try {
                    await connectWebSocket(requestId);
                    startStatusChecking(requestId);
                } catch (error) {
                    console.error("Failed to connect WebSocket:", error);
                }
            }
        } )

    // Очистка при закрытии страницы
    window.addEventListener('beforeunload', cleanup);
    window.addEventListener('unload', cleanup);
}

window.init = init;