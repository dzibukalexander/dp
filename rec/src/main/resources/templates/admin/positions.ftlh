<#assign title = "Специализации">
<#assign content>
    <div class="positions-header">
        <h2 class="mb-4">Список специализаций</h2>
    </div>

    <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addPositionModal">
        Добавить
    </button>

    <table class="table">
        <thead>
        <tr>
            <th>Название</th>
            <th>Связанная модель</th>
            <th>Действия</th>
        </tr>
        </thead>
        <tbody>
        <#list positions as position>
            <tr>
                <td>${position.name}</td>
                <td>
                    <#if position.aiAlgo??>
                        ${position.aiAlgo.name}
                    <#else>
                        Нет связи
                    </#if>
                </td>
                <td>
                    <div class="actions">
<#--                        <a href="/admin/positions/${position.id}/edit" class="btn-edit" onclick="openEditModal(event)">-->
<#--                            <i class="bi bi-pencil-square"></i>-->
<#--                        </a>-->
                        <form action="/admin/positions/del/${position.id}" method="post" style="display: inline;">
                            <button type="submit" class="btn-delete">
                                <i class="bi bi-trash"></i>
                            </button>
                        </form>
                    </div>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>

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