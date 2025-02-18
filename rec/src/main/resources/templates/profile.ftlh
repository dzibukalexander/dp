<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Профиль пользователя</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        <#include "css/profile.css">
    </style>
</head>
<body>
<div class="profile-container">
    <h1 class="text-center">Профиль пользователя</h1>
    <form action="/profile" method="post" enctype="multipart/form-data">
        <input type="hidden" name="id" value="${user.id}">

        <!-- Аватар -->
        <div class="text-center">
            <#if user.avatar??> <!-- Проверка наличия аватара и его id -->
                <img src="/avatar/${user.avatar.id}" class="avatar" alt="Аватар">
            <#else>
                <div class="avatar bg-secondary text-white d-flex align-items-center justify-content-center">
                    <span>Нет аватара</span>
                </div>
            </#if>
            <div class="mt-3">
                <label for="avatar" class="form-label">Изменить аватар:</label>
                <input type="file" class="form-control" id="avatar" name="avatar">
            </div>
        </div>

        <!-- Основные данные -->
        <div class="mb-3">
            <label for="firstName" class="form-label">Имя:</label>
            <input type="text" class="form-control" id="firstName" name="firstName" value="${user.firstName}" required>
        </div>
        <div class="mb-3">
            <label for="name" class="form-label">Фамилия:</label>
            <input type="text" class="form-control" id="name" name="name" value="${user.name}" required>
        </div>
        <div class="mb-3">
            <label for="secondName" class="form-label">Отчество:</label>
            <input type="text" class="form-control" id="secondName" name="secondName" value="${user.secondName}" required>
        </div>

        <!-- Специализация (для докторов) -->
        <#list roles as role>
            <#if role == 'DOCTOR'>
                <div class="mb-3">
                    <label for="positionName" class="form-label">Специализация:</label>
                    <input type="text" class="form-control" id="positionName" name="positionName" value="${position!''}">
                </div>
            </#if>
        </#list>

        <button type="submit" class="btn btn-primary">Сохранить</button>
    </form>
</div>
</body>
</html>