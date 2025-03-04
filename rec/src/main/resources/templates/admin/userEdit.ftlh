<#assign title = "Редактирование пользователя">
<#assign content>
    <h2 class="mb-4">Редактирование: ${user.email}</h2>
    <form action="/admin/users" method="post" class="edit-form">
        <div class="mb-3">
            <label class="form-label">Роли:</label>
            <div class="roles-list">
                <#list roles as role>
                    <div class="form-check">
                        <input class="form-check-input"
                               type="checkbox"
                               name="roles"
                               value="${role}"
                               id="role-${role}"
                                ${user.roles?seq_contains(role)?then('checked', '')}
                                <#if role != 'ADMIN'>disabled</#if>>
                        <label class="form-check-label" for="role-${role}">
                            ${role.getDisplayName()}
                        </label>
                    </div>
                </#list>
            </div>
        </div>

        <#-- Показывать выбор позиции только если пользователь уже доктор -->
        <#if userIsDoctor>
            <div class="mb-3">
                <label class="form-label">Позиция доктора:</label>
                <select class="form-select" name="positionId">
                    <option value="">-- Нет позиции --</option>
                    <#list positions as position>
                        <option value="${position.id}"
                                ${(user.doctor?? && user.doctor.position?? && user.doctor.position.id == position.id)?then('selected', '')}>
                            ${position.name}
                        </option>
                    </#list>
                </select>
            </div>
        </#if>

        <input type="hidden" name="userId" value="${user.id}">
        <button type="submit" class="btn btn-primary">Сохранить</button>
    </form>
</#assign>

<#include "layout.ftlh">