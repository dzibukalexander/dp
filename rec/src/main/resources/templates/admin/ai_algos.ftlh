<#assign title = "AI-Модели">
<#assign content>
    <div class="models-header">
        <h2 class="mb-4">Список моделей</h2>
        <a href="/admin/ai-algos/new" class="btn btn-success">Добавить</a>
    </div>

    <table class="table">
        <thead>
        <tr>
            <th>Название</th>
            <th>Описание</th>
            <th>Kaggle URL</th>
            <th>Статус</th>
            <th>По умолчанию</th>
            <th>Связанная специализация</th>
            <th>Действия</th>
        </tr>
        </thead>
        <tbody>
        <#list aiAlgos as aiAlgo>
            <tr class="clickable-row" data-id="${aiAlgo.id}">
                <td>${aiAlgo.name}</td>
                <td>${aiAlgo.description!''}</td>
                <td>${aiAlgo.kaggleURL!''}</td>
                <td>${aiAlgo.status!''}</td>
                <td>${aiAlgo.is_default?then('Да', 'Нет')}</td>
                <td>
                    <#if aiAlgo.position??>
                        ${aiAlgo.position.name}
                    <#else>
                        Нет связи
                    </#if>
                </td>
                <td>
                    <!-- Кнопки действий, например, удаление или редактирование -->
                    <form action="/admin/ai-algos/${aiAlgo.id}/delete" method="post" style="display: inline;">
                        <input type="hidden" name="_method" value="DELETE">
                        <button type="submit" class="btn-delete">
                            <i class="bi bi-trash"></i>
                        </button>
                    </form>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>

    <!-- Модальное окно для редактирования AIAlgo -->
<#--    <div class="modal fade" id="editAIAlgoModal">-->
<#--        <div class="modal-dialog">-->
<#--            <div class="modal-content">-->
<#--                <div class="modal-header">-->
<#--                    <h5 class="modal-title">Редактирование AI-Модели</h5>-->
<#--                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>-->
<#--                </div>-->
<#--                <div class="modal-body">-->
<#--                    <form id="editAIAlgoForm" method="post">-->
<#--                        <div class="mb-3">-->
<#--                            <label class="form-label">Название</label>-->
<#--                            <input type="text" class="form-control" name="name" required>-->
<#--                        </div>-->
<#--                        <div class="mb-3">-->
<#--                            <label class="form-label">Описание</label>-->
<#--                            <textarea class="form-control" name="description"></textarea>-->
<#--                        </div>-->
<#--                        <div class="mb-3">-->
<#--                            <label class="form-label">Kaggle URL</label>-->
<#--                            <input type="text" class="form-control" name="kaggleURL">-->
<#--                        </div>-->
<#--                        <div class="mb-3">-->
<#--                            <label class="form-label">Статус</label>-->
<#--                            <select class="form-select" name="status">-->
<#--                                <#list statuses as status>-->
<#--                                    <option value="${status}">${status}</option>-->
<#--                                </#list>-->
<#--                            </select>-->
<#--                        </div>-->
<#--                        <div class="mb-3">-->
<#--                            <label class="form-label">По умолчанию</label>-->
<#--                            <input type="checkbox" class="form-check-input" name="is_default">-->
<#--                        </div>-->
<#--                        <div class="mb-3">-->
<#--                            <label class="form-label">Специализация</label>-->
<#--                            <select class="form-select" name="positionId">-->
<#--                                <option value="">-- Выберите специализацию --</option>-->
<#--                                <#list positions as position>-->
<#--                                    <option value="${position.id}">${position.name}</option>-->
<#--                                </#list>-->
<#--                            </select>-->
<#--                        </div>-->
<#--                        <input type="hidden" name="id">-->
<#--                        <button type="submit" class="btn btn-primary">Сохранить</button>-->
<#--                    </form>-->
<#--                </div>-->
<#--            </div>-->
<#--        </div>-->
<#--    </div>-->
</#assign>

<#include "layout.ftlh">