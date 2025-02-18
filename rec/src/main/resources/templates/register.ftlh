<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Регистрация</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        <#include "css/register.css">
    </style>
</head>
<body>
<div class="form-container">
    <h1>Регистрация</h1>
    <form action="/register" method="post">
        <div class="mb-3">
            <label for="firstName" class="form-label">Имя:</label>
            <input type="text" class="form-control" id="firstName" name="firstName" required>
        </div>
        <div class="mb-3">
            <label for="name" class="form-label">Фамилия:</label>
            <input type="text" class="form-control" id="name" name="name" required>
        </div>
        <div class="mb-3">
            <label for="secondName" class="form-label">Отчество:</label>
            <input type="text" class="form-control" id="secondName" name="secondName" required>
        </div>
        <div class="mb-3">
            <label for="email" class="form-label">Email:</label>
            <input type="email" class="form-control" id="email" name="email" required>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Пароль:</label>
            <input type="password" class="form-control" id="password" name="password" required>
        </div>
        <div class="mb-3">
            <label for="role" class="form-label">Роль:</label>
            <select class="form-select" id="role" name="role" required>
                <#list roles as role>
                    <option value="${role}">${role}</option>
                </#list>
            </select>
        </div>
        <div class="mb-3" id="positionField" style="display: none;">
            <label for="position" class="form-label">Позиция:</label>
            <input type="text" class="form-control" id="position" name="position">
        </div>
        <!-- Выбор позиции (только для докторов) -->
        <div class="mb-3" id="positionField" style="display: none;">
            <label class="form-label">Специализация:</label>
            <select class="form-select" name="positionId">
                <option value="">Выберите специализацию</option>
                <#list positions as position>
                    <option value="${position.id}">${position.name}</option>
                </#list>
            </select>
        </div>
        <button type="submit" class="btn btn-primary">Зарегистрироваться</button>
    </form>
</div>

<script>
    const roleSelect = document.getElementById('role');
    const positionField = document.getElementById('positionField');

    roleSelect.addEventListener('change', function() {
        if (roleSelect.value === 'Доктор') {
            positionField.style.display = 'block';
        } else {
            positionField.style.display = 'none';
        }
    });
</script>
</body>
</html>