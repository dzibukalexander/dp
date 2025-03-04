<!-- Контент для пациента. Файл index-patient -->
<div>
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
            <button onclick="sendPhoto(${doctor.id})">Отправить фото</button>
        </div>
    </#list>
</div>