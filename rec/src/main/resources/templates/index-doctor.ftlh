<!-- Контент для доктора. Файл index-doctor -->
<div class="doctor-tabs">
    <!-- Вкладки -->
    <div class="tab-buttons">
        <button class="tab-button active" data-tab-target="open-tab">Открытые запросы</button>
        <button class="tab-button" data-tab-target="closed-tab">Закрытые запросы</button>
    </div>

    <!-- Содержимое вкладок -->
    <div id="open-tab" class="tab-content active">
        <h3>Открытые запросы (требуют ответа)</h3>
        <div class="requests-container">
            <#if openRequests?? && openRequests?size gt 0>
                <#list openRequests as request>
                    <a href="/request/${request.id}" class="request-card-link">
                        <div class="request-card">
                            <div class="patient-info">
                                <#if request.patient.user.avatar??>
                                    <img src="/avatar/${request.patient.user.avatar.id}"
                                         alt="Аватар пациента"
                                         class="profile-avatar">
                                </#if>
                                <h4>${request.patient.user.firstName} ${request.patient.user.secondName}</h4>
                                <p>Дата: ${request.formattedDateCreated}</p>
                            </div>
                            <#if request.page??>
                                <img src="/photo/${request.page.id}"
                                     alt="Фото пациента"
                                     class="request-photo">
                            </#if>
                        </div>
                    </a>
                </#list>
            <#else>
                <p class="no-requests">Нет открытых запросов</p>
            </#if>
        </div>
    </div>

    <div id="closed-tab" class="tab-content">
        <h3>Закрытые запросы</h3>
        <div class="requests-container">
            <#if closedRequests?? && closedRequests?size gt 0>
                <#list closedRequests as request>
                    <a href="/request/${request.id}" class="request-card-link">
                        <div class="request-card">
                            <div class="patient-info">
                                <#if request.patient.user.avatar??>
                                    <img src="/avatar/${request.patient.user.avatar.id}"
                                         alt="Аватар пациента"
                                         class="profile-avatar">
                                <#else>
                                    <div class="avatar-placeholder">Нет аватара</div>
                                </#if>
                                <h4>${request.patient.user.firstName} ${request.patient.user.secondName}</h4>
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
                                        <h5>Ваш ответ:</h5>
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
</div>