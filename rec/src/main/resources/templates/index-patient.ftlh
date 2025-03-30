<!-- Контент для пациента. Файл index-patient -->

<div class="patient-tabs">
    <!-- Вкладки -->
    <div class="tab-buttons">
        <button class="tab-button active" data-tab-target="send-tab">Отправить фото</button>
        <button class="tab-button" data-tab-target="open-tab">Открытые запросы</button>
        <button class="tab-button" data-tab-target="closed-tab">Закрытые запросы</button>
    </div>

    <!-- Содержимое вкладок -->
    <div id="send-tab" class="tab-content active">
        <div class="filter-section">
            <label for="specializationFilter">Фильтр по специализации:</label>
            <input type="text" id="specializationFilter" oninput="filterDoctors()">
        </div>

        <div class="cards-container" id="doctorsContainer">
            <#list doctors as doctor>
                <div class="card" data-specialization="${doctor.position.name}">
                    <#if doctor.user.avatar??>
                        <img src="/avatar/${doctor.user.avatar.id}" alt="Аватар доктора" class="avatar">
                    <#else>
                        <div class="avatar-placeholder">Нет аватара</div>
                    </#if>
                    <h3>${doctor.user.firstName} ${doctor.user.name}</h3>
                    <p>Специализация: ${doctor.position.name}</p>
                    <button onclick="sendPhoto(${doctor.id})" class="send-photo-btn">Отправить фото</button>
                </div>
            </#list>
        </div>
    </div>

    <div id="open-tab" class="tab-content">
        <h3>Открытые запросы (ожидают ответа доктора)</h3>
        <div class="requests-container">
            <#if openRequests?? && openRequests?size gt 0>
                <#list openRequests as request>
                    <div class="request-card">
                        <div class="doctor-info">
                            <h4>Доктор: ${request.doctor.user.firstName} ${request.doctor.user.name}</h4>
                            <p>Специализация: ${request.doctor.position.name}</p>
                            <p>Дата отправки: ${request.formattedDateCreated}</p>
                        </div>
                        <#if request.page??>
                            <img src="/photo/${request.page.id}" alt="Отправленное фото" class="request-photo">
                        </#if>
                    </div>
                </#list>
            <#else>
                <p class="no-requests">Нет открытых запросов</p>
            </#if>
        </div>
    </div>

<#--    <div id="closed-tab" class="tab-content">-->
<#--        <h3>Закрытые запросы (с ответом доктора)</h3>-->
<#--        <div class="requests-container">-->
<#--            <#if closedRequests?? && closedRequests?size gt 0>-->
<#--                <#list closedRequests as request>-->
<#--                    <div class="request-card">-->
<#--                        <!-- Информация о докторе &ndash;&gt;-->
<#--                        <div class="doctor-info">-->
<#--                            <h4>Доктор: ${request.doctor.user.firstName} ${request.doctor.user.name}</h4>-->
<#--                            <p>Специализация: ${request.doctor.position.name}</p>-->
<#--                            <p>Дата: ${request.formattedDateCreated}</p>-->
<#--                        </div>-->

<#--                        <!-- Отправленное фото &ndash;&gt;-->
<#--                        <#if request.page??>-->
<#--                            <div class="photo-section">-->
<#--                                <h5>Отправленное фото:</h5>-->
<#--                                <img src="/photo/${request.page.id}" alt="Отправленное фото">-->
<#--                            </div>-->
<#--                        </#if>-->

<#--                        <!-- Ответное фото &ndash;&gt;-->
<#--                        <#if request.response?? && request.response.page??>-->
<#--                            <div class="photo-section">-->
<#--                                <h5>Ответ доктора:</h5>-->
<#--                                <img src="/photo/${request.response.page.id}" alt="Ответное фото">-->
<#--                            </div>-->

<#--                            <#if request.response.conclusion??>-->
<#--                                <div class="conclusion-box">-->
<#--                                    <h5>Заключение:</h5>-->
<#--                                    <p>${request.response.conclusion}</p>-->
<#--                                </div>-->
<#--                            </#if>-->
<#--                        </#if>-->
<#--                    </div>-->
<#--                </#list>-->
<#--            </#if>-->
<#--        </div>-->
<#--    </div>-->

    <div id="closed-tab" class="tab-content">
        <h3>Закрытые запросы (с ответом доктора)</h3>
        <div class="requests-container">
            <#if closedReqpuests?? && closedRequests?size gt 0>
                <#list closedRequests as request>
                    <a href="/request/${request.id}" class="request-card-link">
                        <div class="request-card">
                            <div class="doctor-info">
                                <#if request.doctor.user.avatar??>
                                    <img src="/avatar/${request.doctor.user.avatar.id}"
                                         alt="Аватар доктора"
                                         class="profile-avatar">
                                <#else>
                                    <div class="avatar-placeholder">Нет аватара</div>
                                </#if>
                                <h4>${request.doctor.user.firstName} ${request.doctor.user.secondName}</h4>
                                <p>Дата: ${request.formattedDateCreated}</p>
                            </div>
                            <div class="photos-container">
                                <#if request.page??>
                                    <div class="photo-section">
                                        <h5>Фото пациента:</h5>
                                        <img src="/photo/${request.page.id}"
                                             alt="Фото пациента"
                                             class="request-photo">
                                    </div>
                                </#if>
                                <#if request.response?? && request.response.page??>
                                    <div class="photo-section">
                                        <h5>Ответ доктора:</h5>
                                        <img src="/photo/${request.response.page.id}"
                                             alt="Ответное фото"
                                             class="response-photo">
                                        <#if request.response.conclusion??>
                                            <div class="conclusion-preview">
                                                <p>${request.response.conclusion?substring(0, [request.response.conclusion?length, 50]?min)}<#if request.response.conclusion?length gt 50>...</#if></p>
                                            </div>
                                        </#if>
                                    </div>
                                </#if>
                            </div>
                        </div>
                    </a>
                </#list>
            <#else>
                <p class="no-requests">Нет закрытых запросов</p>
            </#if>
        </div>
    </div>
    <!-- Элемент загрузки -->
    <div id="loading-overlay" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.7); z-index: 9999; justify-content: center; align-items: center; flex-direction: column;">
        <div style="color: white; font-size: 24px; margin-bottom: 20px;">Отправка фото...</div>
        <div class="spinner" style="width: 50px; height: 50px; border: 5px solid #f3f3f3; border-top: 5px solid #3498db; border-radius: 50%; animation: spin 1s linear infinite;"></div>
    </div>

    <style>
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
    </style>
</div>