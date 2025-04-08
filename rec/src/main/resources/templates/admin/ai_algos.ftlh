<#assign title = "AI-Модели">
<#assign content>
    <div class="models-header d-flex justify-content-between align-items-center mb-4">
        <h2>Список моделей</h2>
        <a href="/admin/ai-algos/new" class="btn btn-success">
            <i class="bi bi-plus-circle"></i> Добавить модель
        </a>
    </div>

    <div class="card shadow-sm">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead class="table-light">
                    <tr>
<#--                        <th>ID</th>-->
                        <th>Название</th>
                        <th>Тип ввода</th>
                        <th>Тип вывода</th>
                        <th>API Endpoint</th>
                        <th>Описание</th>
                        <th>Kaggle URL</th>
                        <th>Статус</th>
                        <th>По умолчанию</th>
                        <th>Специализация</th>
                        <th>Действия</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list aiAlgos as aiAlgo>
                        <tr class="clickable-row" data-id="${aiAlgo.id}">
<#--                            <td>${aiAlgo.id}</td>-->
                            <td>${aiAlgo.name}</td>
                            <td>
                                <span class="badge bg-info">
                                    ${aiAlgo.inputType.toString()}
                                </span>
                            </td>
                            <td>
                                <span class="badge bg-primary">
                                    ${aiAlgo.outputType.toString()}
                                </span>
                            </td>
                            <td>
                                <code>${aiAlgo.endpoint}</code>
                            </td>
                            <td class="text-truncate" style="max-width: 200px;" title="${aiAlgo.description!''}">
                                ${aiAlgo.description!''}
                            </td>
                            <td>
                                <#if aiAlgo.kaggleURL??>
                                    <a href="${aiAlgo.kaggleURL}" target="_blank" class="text-decoration-none">
                                        <i class="bi bi-box-arrow-up-right"></i> Ссылка
                                    </a>
                                <#else>
                                    -
                                </#if>
                            </td>
                            <td>
                                <span class="badge <#if aiAlgo.status == 'ACTIVE'>bg-success<#else>bg-secondary</#if>">
                                    ${aiAlgo.status.toString()}
                                </span>
                            </td>
                            <td>
                                <span class="badge <#if aiAlgo.isDefault>bg-warning text-dark<#else>bg-light text-dark</#if>">
                                    ${aiAlgo.isDefault?then('Да', 'Нет')}
                                </span>
                            </td>
                            <td>
                                <#if aiAlgo.position??>
                                    <span class="badge bg-dark">${aiAlgo.position.name}</span>
                                <#else>
                                    <span class="badge bg-light text-dark">Не назначено</span>
                                </#if>
                            </td>
                            <td>
                                <div class="d-flex gap-2">
                                    <a href="/admin/ai-algos/edit/${aiAlgo.id}" class="btn btn-sm btn-outline-primary">
                                        <i class="bi bi-pencil"></i>
                                    </a>
                                    <form action="/admin/ai-algos/del/${aiAlgo.id}" method="post" class="d-inline">
                                        <input type="hidden" name="_method" value="DELETE">
                                        <button type="submit" class="btn btn-sm btn-outline-danger"
                                                onclick="return confirm('Вы уверены, что хотите удалить эту модель?')">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</#assign>

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
<#--</#assign>-->

<#include "layout.ftlh">