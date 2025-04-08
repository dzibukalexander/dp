<!DOCTYPE html>
<html lang="ru" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>Детали запроса</title>
    <style>
        <#include "css/main.css">
        <#include "css/request-details.css">
    </style>

    <!-- Альтернативные CDN -->
    <head>
        <!-- Подключаем SockJS и Stomp -->
        <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1.6.1/dist/sockjs.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
    </head>
</head>
<body>

<header>
    <a href="/" class="back-button">← Назад</a>
    <h1>Детали запроса</h1>
</header>

<main class="request-details-container">
    <div class="profile-section">
        <#if isDoctor>
            <!-- Для доктора: информация о пациенте -->
            <div class="profile-info">
                <#if request.patient.user.avatar??>
                    <img src="/avatar/${request.patient.user.avatar.id}"
                         alt="Аватар пациента"
                         class="profile-avatar">
                <#else>
                    <div class="avatar-placeholder">Нет аватара</div>
                </#if>
                <h2>${request.patient.user.firstName} ${request.patient.user.secondName}</h2>
                <p>Пациент</p>
            </div>
        <#else>
            <!-- Для пациента: информация о докторе -->
            <div class="profile-info">
                <#if request.doctor.user.avatar??>
                    <img src="/avatar/${request.doctor.user.avatar.id}"
                         alt="Аватар доктора"
                         class="profile-avatar">
                <#else>
                    <div class="avatar-placeholder">Нет аватара</div>
                </#if>
                <h2>${request.doctor.user.firstName} ${request.doctor.user.name}</h2>
                <p>Специализация: ${request.doctor.position.name}</p>
            </div>
        </#if>
    </div>

    <div class="photos-grid">
        <!-- Отправленное фото -->
        <div class="photo-card">
            <h3><#if isDoctor>Фото пациента<#else>Ваше фото</#if></h3>
            <#if request.requestPage??>
                <img src="/photo/${request.requestPage.id}" alt="Отправленное фото" id="patient-photo">
            <#else>
                <div style="padding: 20px; text-align: center;">Фото не найдено</div>
            </#if>
        </div>

        <!-- Блок для ответа/предсказания -->
        <div class="photo-card" id="response-section">
            <h3>Результат анализа</h3>
            <div id="prediction-result">
                <#if request?? && (request.responsePage?? || request.conclusion??)>
                    <#if request.responsePage??>
                        <img src="/photo/${request.responsePage.id}" alt="Ответное фото" id="response-photo">
                    </#if>
                    <#if request.conclusion??>
                        <div class="conclusion-box">
                            <h4>Заключение:</h4>
                            <p>${request.conclusion}</p>
                        </div>
                    </#if>
                <#else>
                    <div id="empty-response" class="empty-state">
                        <p>Нажмите "Сделать прогноз" для анализа</p>
                    </div>
                </#if>
            </div>
            <div id="prediction-loading" class="loading-indicator"
                 style="display: ${request.inProgress?then('block', 'none')};">
                <div class="spinner"></div>
                <p>Идет анализ изображения...</p>
            </div>
        </div>
    </div>

    <!-- Форма ответа для доктора -->
    <#if isDoctor && request.status.name() == "OPEN">
        <div class="response-form">
            <h3>Отправить ответ</h3>
            <div class="action-buttons">
                <#if model??>
                    <button onclick="makePrediction(${request.id}, ${model.id})"
                            id="predict-btn"
                            class="action-btn predict-btn"/>
                <#else>
                    <button class="action-btn predict-btn" disabled title="Нет доступной модели для вашей специализации">
                        Сделать прогноз (недоступно)
                    </button>
                </#if>
            </div>

            <form action="/request/${request.id}/respond"
                  method="post"
                  enctype="multipart/form-data"
                  id="response-form">

                <div class="form-group">
                    <label for="conclusion-text">Заключение:</label>
                    <textarea name="conclusion"
                              id="conclusion-text"
                              placeholder="Введите ваше заключение..."
<#--                              rows="6">${(predictionResult.result)!''}</textarea>-->
                              rows="6">${(request.conclusion)!''}</textarea>
                </div>

                <#if model?? && model.outputType == 'TEXT'>
                    <div class="form-group">
                        <label for="response-photo-input">Ответное фото (если требуется):</label>
                        <input type="file"
                               name="responsePhoto"
                               id="response-photo-input"
                               accept="image/*">
                        <div id="photo-preview" class="photo-preview" style="display: none;">
                            <img id="preview-image" src="" alt="Предпросмотр фото">
                            <button type="button" id="remove-photo" class="remove-photo-btn">×</button>
                        </div>
                    </div>
                </#if>
                <button type="submit" class="action-btn submit-btn">
                    Отправить ответ
                </button>
            </form>
        </div>
    </#if>
</main>

<script type="module">
    <#include "./js/websocket-handler.js">
    <#include "./js/request-details.js">
    document.addEventListener('DOMContentLoaded', function() {
        init(${request.id});
    });
</script>

</body>
</html>