<!-- Контент для доктора. Файл index-doctor -->
<div class="cards-container">
    <#list patientPages as dp>
        <div class="card">
            <h3>${dp.patient.user.firstName} ${dp.patient.user.secondName}</h3>
            <#if dp.page??>
                <img src="/avatar/${dp.page.id}" alt="Фото пациента" class="avatar">
            <#else>
                <div class="no-image">🖼️ Нет изображения</div>
            </#if>
            <p>Время отправки: ${dp.formattedDateCreated}</p>
        </div>
    </#list>
</div>