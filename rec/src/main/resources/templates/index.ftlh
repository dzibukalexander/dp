<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Распознавание медицинских патологий пациента</title>
    <style>
        <#include "css/main.css">
        <#include "css/tabs.css">
        <#include "css/patient.css">
    </style>

<#--    web-socket-->
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1.6.1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
</head>
<body>


<!-- Шапка -->
<header>
    <h1>Распознавание медицинских патологий пациента</h1>
    <nav>
        <a href="/">Главная</a>
        <a href="/profile?id=${id}">Профиль</a>
        <#if isDoctor>
            <a href="/about/model/${model.id}">О модели</a>
        </#if>
        <a href="/logout">Выйти</a>
    </nav>
</header>

<!-- Тело -->
<main>
    <h2>Добро пожаловать, <#if isDoctor>Доктор<#else>Пациент</#if>!</h2>

    <#if isDoctor>
        <#include "index-doctor.ftlh">
    <#else>
        <#include "index-patient.ftlh">
    </#if>
</main>

<#--<!-- Подвал &ndash;&gt;-->
<#--<footer>-->
<#--    <p>&copy; 2025 Распознавание медицинских патологий пациента. Все права защищены.</p>-->
<#--</footer>-->

<script>
    <#include "js/tabs.js">
    <#if isDoctor>
        <#include "js/doctor.js">
    <#else>
        <#include "js/patient.js">
    </#if>
</script>

</body>
</html>