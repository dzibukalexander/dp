<#assign title = "Добавление новой модели AI">
<#assign content>
    <div class="models-header">
        <h2 class="mb-4">Добавление новой модели AI</h2>
    </div>

    <form action="/admin/ai-algos/new" method="post" class="edit-form">
        <div class="mb-3">
            <label class="form-label">Название модели:</label>
            <input type="text" class="form-control" name="name" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Описание:</label>
            <textarea class="form-control" name="description" rows="3"></textarea>
        </div>

        <div class="mb-3">
            <label class="form-label">Kaggle URL:</label>
            <input type="text" class="form-control" name="kaggleURL">
        </div>

        <div class="mb-3">
            <label class="form-label">Тип входных данных:</label>
            <select class="form-select" name="inputType" required>
                <option value="IMAGE">Изображение (image)</option>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">Тип выходных данных:</label>
            <select class="form-select" name="outputType" required>
                <option value="TEXT">Текст (text)</option>
                <option value="IMAGE">Изображение (image)</option>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">Статус:</label>
            <select class="form-select" name="status" required>
                <option value="ACTIVE">Активна</option>
                <option value="INACTIVE">Неактивна</option>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">API-url:</label>
            <input type="text" class="form-control" name="endpoint" required>
        </div>

        <div class="mb-3">
            <label class="form-label">По умолчанию:</label>
            <input type="checkbox" class="form-check-input" name="is_default">
        </div>

        <div class="mb-3">
            <label class="form-label">Связанная специализация:</label>
            <select class="form-select" name="positionId">
                <option value="">-- Выберите специализацию --</option>
                <#list positions as position>
                    <option value="${position.id}">${position.name}</option>
                </#list>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Сохранить</button>
        <a href="/admin/ai-algos" class="btn btn-secondary">Отмена</a>
    </form>
</#assign>

<#include "layout.ftlh">
