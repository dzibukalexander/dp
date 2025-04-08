document.querySelectorAll('.btn-diagnose').forEach(button => {
    button.addEventListener('click', () => {
        alert('Запуск анализа...');
    });
});

// Подключение к WebSocket
// function connectWebSocket() {
//     const socket = new SockJS('/ws-status');
//     const stompClient = Stomp.over(socket);
//
//     stompClient.connect({}, function(frame) {
//         console.log('Connected to WebSocket');
//
//         // Подписываемся на обновления статусов для всех открытых карточек
//         document.querySelectorAll('.request-card-link').forEach(card => {
//             const requestId = card.getAttribute('href').split('/')[2];
//             subscribeToRequestStatus(stompClient, requestId);
//         });
//
//         // Периодическая проверка (каждую секунду)
//         setInterval(() => {
//             refreshAllRequestStatuses();
//         }, 1000);
//     }, function(error) {
//         console.log('WebSocket error: ', error);
//         // Переподключение через 5 секунд при ошибке
//         setTimeout(connectWebSocket, 5000);
//     });
//
//     return stompClient;
// }
//
// // Подписка на обновления статуса конкретного запроса
// function subscribeToRequestStatus(stompClient, requestId) {
//     stompClient.subscribe("/topic/request/" + requestId + "/status", function(message) {
//         const status = JSON.parse(message.body);
//         updateStatusIndicators(requestId, status);
//     });
// }
//
// // Обновление всех статусов (периодический запрос)
// function refreshAllRequestStatuses() {
//     document.querySelectorAll('.request-card-link').forEach(card => {
//         const requestId = card.getAttribute('href').split('/')[2];
//         fetch("/request/" + requestId + "/status")
//             .then(response => response.json())
//             .then(status => {
//                 updateStatusIndicators(requestId, status);
//             })
//             .catch(error => console.error('Error fetching status:', error));
//     });
// }
//
// // Функция обновления индикаторов (у вас уже есть)
// const updateStatusIndicators = (requestId, status) => {
//     let link = "\"/request/" + requestId + "\"";
//     const requestCards = document.querySelectorAll("#open-tab .request-card-link[href=" + link + "]");
//
//     requestCards.forEach(card => {
//         const indicator = card.querySelector('.status-indicator');
//         if (!indicator) return;
//
//         indicator.classList.remove('pending', 'in-progress', 'completed');
//         console.log(status.inProgress, indicator)
//         if (status.inProgress) {
//             indicator.classList.add('in-progress');
//         } else if (status.response) {
//             indicator.classList.add('completed');
//         } else {
//             indicator.classList.add('pending');
//         }
//
//         const loadingIndicator = card.querySelector('.loading-indicator');
//         if (loadingIndicator) {
//             loadingIndicator.style.display = status.inProgress ? 'block' : 'none';
//         }
//     });
// };
//
// // Инициализация при загрузке страницы
// document.addEventListener('DOMContentLoaded', function() {
//     // Подключаемся к WebSocket
//     const stompClient = connectWebSocket();
//
//     // // Обработчик для новых карточек (если они динамически добавляются)
//     // const observer = new MutationObserver(function(mutations) {
//     //     mutations.forEach(function(mutation) {
//     //         mutation.addedNodes.forEach(function(node) {
//     //             if (node.nodeType === 1 && node.matches('.request-card-link')) {
//     //                 const requestId = node.getAttribute('href').split('/')[2];
//     //                 subscribeToRequestStatus(stompClient, requestId);
//     //             }
//     //         });
//     //     });
//     // });
//     //
//     // // Наблюдаем за контейнерами запросов
//     // document.querySelectorAll('.requests-container').forEach(container => {
//     //     observer.observe(container, { childList: true });
//     // });
//
//     // Очистка при закрытии страницы
//     window.addEventListener('beforeunload', function() {
//         if (stompClient) {
//             stompClient.disconnect();
//         }
//     });
// });