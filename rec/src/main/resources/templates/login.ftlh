<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Вход</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        <#include "css/login.css">
    </style>
</head>
<body>
<div class="form-container">
    <h1>Вход</h1>
    <#if error??>
        <div class="error-message">Неверный email или пароль</div>
    </#if>
    <form action="/login" method="post">
        <div class="mb-3">
            <label for="email" class="form-label">Email:</label>
            <input type="email" class="form-control" id="username" name="username" required>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Пароль:</label>
            <input type="password" class="form-control" id="password" name="password" required>
        </div>
        <button type="submit" class="btn btn-primary">Войти</button>
    </form>
    <div class="mt-3 text-center">
        <p>Нет аккаунта? <a href="/register">Зарегистрируйтесь</a></p>
    </div>
</div>
</body>
</html>