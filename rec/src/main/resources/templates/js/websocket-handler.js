function processPredictionResult(result) {
    console.log("processPredictionResult")
    const predictionResult = document.getElementById('prediction-result');
    const conclusionText = document.getElementById('conclusion-text');

    predictionResult.innerHTML = '';

    if (result.photoData) {
        try {
            // Проверяем, является ли photoData строкой base64 или массивом байт
            let imageData = result.photoData;

            // Если это массив чисел (байтов), конвертируем в base64
            if (Array.isArray(imageData)) {
                const byteArray = new Uint8Array(imageData);
                imageData = btoa(String.fromCharCode.apply(null, byteArray));
            }

            // Создаем элемент изображения
            const img = document.createElement('img');
            img.src = 'data:image/jpeg;base64,' + imageData;
            img.alt = 'Результат анализа';
            img.id = 'response-photo';
            img.style.maxWidth = '100%';
            img.style.height = 'auto';
            console.log(img);
            predictionResult.appendChild(img);

        } catch (error) {
            console.error('Error processing image:', error);
            const errorMsg = document.createElement('div');
            errorMsg.className = 'error-message';
            errorMsg.textContent = 'Ошибка при отображении изображения';
            predictionResult.appendChild(errorMsg);
        }
    }

    if (result.result) {
        const conclusionBox = document.createElement('div');
        conclusionBox.className = 'conclusion-box';
        conclusionBox.innerHTML = '<h4>Заключение:</h4><p>' + result.result + '</p>';
        predictionResult.appendChild(conclusionBox);

        if (conclusionText) {
            conclusionText.value = result.result;
        }

        // Обновляем форму, если она есть
        const responseForm = document.getElementById('response-form');
        if (responseForm) {
            responseForm.querySelector('textarea').value = result.result;
        }
    }

    updateUI(false)
}

let stompClient = null;
let currentRequestId = null;
let statusCheckInterval = null;

// Создание нового соединения
const establishNewConnection = (requestId, resolve, reject) => {
    console.log("establishNewConnection")
    const socket = new SockJS('/ws-status');
    stompClient = Stomp.over(socket);
    stompClient.debug = () => {}; // Отключаем логирование

    stompClient.connect({}, () => {
        currentRequestId = requestId;
        setupSubscriptions(requestId);
        resolve();
    }, (error) => {
        console.error('WebSocket connection error:', error);
        reject(error);
    });
};

export const connectWebSocket = (requestId) => {
    console.log("connectWebSocket")
    return new Promise((resolve, reject) => {
        // Если уже подключены к этому же запросу
        if (stompClient && stompClient.connected && currentRequestId === requestId) {
            resolve();
            return;
        }

        // Если подключены к другому запросу - отключаемся
        if (stompClient && stompClient.connected) {
            disconnectWebSocket().then(() => {
                establishNewConnection(requestId, resolve, reject);
            });
        } else {
            establishNewConnection(requestId, resolve, reject);
        }
    });
};



export const cleanup = () => {
    console.log("cleanup")
    if (statusCheckInterval) {
        clearInterval(statusCheckInterval);
        statusCheckInterval = null;
    }
    //disconnectWebSocket();
};

export const setupSubscriptions = (requestId) => {
    console.log("setupSubscriptions")
    stompClient.subscribe("/topic/request/" + requestId + "/status", (message) => {
        console.log("stompClient.subscribe(\"/topic/request/\" + requestId + \"/status\", (message)")

        const status = JSON.parse(message.body);
        updateUI(status.inProgress);
    });

    stompClient.subscribe("/topic/request/" + requestId + "/response", (message) => {
        console.log("stompClient.subscribe(\"/topic/request/\" + requestId + \"/response\", (message)")
        const response = JSON.parse(message.body);
        processPredictionResult(response);
        cleanup();
    });
};

export const updateUI = (inProgress) => {
    console.log("Updating UI with status:", inProgress);

    const loading = document.getElementById('prediction-loading');
    const predictBtn = document.getElementById('predict-btn');
    const submitBtn = document.querySelector('#response-form button[type="submit"]');
    // const submitBtn = document.getElementsByClassName('submit-btn');

    // Обновляем спиннер
    if (loading) {
        loading.style.display = inProgress ? 'flex' : 'none';
    }

    // Обновляем кнопку "Сделать прогноз"
    if (predictBtn) {
        predictBtn.disabled = inProgress;
        predictBtn.textContent = inProgress ? 'Прогноз выполняется...' : 'Сделать прогноз';
        predictBtn.classList.toggle('processing', inProgress);
    }

    // Обновляем кнопку "Отправить ответ"
    if (submitBtn) {
        console.log(submitBtn)
        submitBtn.disabled = inProgress;
        submitBtn.classList.toggle('processing', inProgress);
    }
};

export const startStatusChecking = (requestId) => {
    console.log("startStatusChecking")
    statusCheckInterval = setInterval(async () => {
        try {
            const response = await fetch("/request/" + requestId + "/status");
            const status = await response.json();
            updateUI(status.inProgress);

            // Если прогноз завершен, прекращаем проверку
            if (!status.inProgress) {
                cleanup();
            }
        } catch (error) {
            console.error('Status check failed:', error);
        }
    }, 1000);
};

export const disconnectWebSocket = () => {
    console.log("disconnectWebSocket")
    return new Promise((resolve) => {
        if (stompClient && stompClient.connected) {
            stompClient.disconnect(resolve);
        } else {
            resolve();
        }
    }).finally(() => {
        stompClient = null;
        currentRequestId = null;
    });
};