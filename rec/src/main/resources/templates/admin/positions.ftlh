<#assign title = "Специализации">
<#assign content>
    <div class="positions-header d-flex justify-content-between align-items-center mb-4">
        <h2>Список специализаций</h2>
        <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addPositionModal">
            <i class="bi bi-plus-circle"></i> Добавить
        </button>
    </div>

    <div class="card shadow-sm">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead class="table-light">
                    <tr>
                        <th>Название</th>
                        <th>Связанная модель</th>
                        <th>Действия</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list positions as position>
                        <tr class="clickable-row">
                            <td>${position.name}</td>
                            <td>
                                <#if position.aiAlgo??>
                                    <span class="badge bg-primary">${position.aiAlgo.name}</span>
                                <#else>
                                    <span class="badge bg-light text-dark">Нет связи</span>
                                </#if>
                            </td>
                            <td>
                                <div class="d-flex gap-2">
                                    <a href="/admin/positions/${position.id}/edit" class="btn btn-sm btn-outline-primary">
                                        <i class="bi bi-pencil"></i>
                                    </a>
                                    <form action="/admin/positions/del/${position.id}" method="post" class="d-inline">
                                        <button type="submit" class="btn btn-sm btn-outline-danger"
                                                onclick="return confirm('Удалить эту специализацию?')">
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


<#-- Модальное окно для добавления позиции -->
    <div class="modal fade" id="addPositionModal">-->
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Добавить специализацию</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form id="addPositionForm" action="/admin/positions" method="post">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="form-label">Название</label>
                            <input type="text" class="form-control" name="name" required>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Отмена</button>
                          <button type="submit" class="btn btn-primary">Сохранить</button>
                       </div>
                    </form>
          </div>
        </div>
     </div>
</#assign>

<#include "layout.ftlh">