<!DOCTYPE html>
<html lang="ru" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Админ-панель | [[${title}]]</title>
    <style>
        <#include "css/admin.css">
    </style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body>
<!-- Шапка -->
<header class="admin-header">
    <div class="container">
        <nav class="admin-nav">
            <a href="/admin/users" class="logo">Админ-панель</a>
            <div class="nav-links">
                <a href="/admin/users" class="nav-link">Пользователи</a>
                <a href="/admin/positions" class="nav-link">Специализации</a>
                <a href="/admin/ai-algos" class="nav-link">ИИ-модели</a>
                <a href="/logout" class="nav-link">Выйти</a>
            </div>
        </nav>
    </div>
</header>

<!-- Контент -->
<main class="admin-main">
    <div class="container">
        ${content}
    </div>
</main>

<!-- Скрипты -->
<script>
    <#include "js/admin.js">
<#--    <#include "js/aialgowm.js">-->
    <#include "js/position.js">
</script>
<!-- Подключение Bootstrap JS и jQuery -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<#--//<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>-->
</body>
</html>