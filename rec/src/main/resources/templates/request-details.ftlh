<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Детали запроса</title>
    <style>
        <#include "css/main.css">
        <#include "css/request-details.css">
    </style>
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
                <p>${request.doctor.position.name}</p>
            </div>
        </#if>
    </div>

    <div class="photos-grid">
        <!-- Отправленное фото -->
        <div class="photo-card">
            <h3><#if isDoctor>Фото пациента<#else>Ваше фото</#if></h3>
            <#if request.page??>
                <img src="/photo/${request.page.id}" alt="Отправленное фото">
            <#else>
                <div style="padding: 20px; text-align: center;">Фото не найдено</div>
            </#if>
        </div>

        <!-- Ответное фото (если есть) -->
        <#if request.response?? && request.response.page??>
            <div class="photo-card">
                <h3><#if isDoctor>Ваш ответ<#else>Ответ доктора</#if></h3>
                <img src="/photo/${request.response.page.id}" alt="Ответное фото">
                <#if request.response.conclusion??>
                    <div class="conclusion-box">
                        <h4>Заключение:</h4>
                        <p>${request.response.conclusion}</p>
                    </div>
                </#if>
            </div>
        </#if>
    </div>

    <!-- Форма ответа для доктора -->
    <#if isDoctor && request.status.name() == "OPEN">
        <div class="response-form">
            <h3>Отправить ответ</h3>
            <form action="/request/${request.id}/respond"
                  method="post"
                  enctype="multipart/form-data">
                <textarea name="conclusion"
                          placeholder="Ваше заключение..."
                          rows="4"></textarea>
                <input type="file" name="responsePhoto" accept="image/*">
                <button type="submit" class="submit-btn">Отправить ответ</button>
            </form>
        </div>
    </#if>
</main>

</body>
</html>