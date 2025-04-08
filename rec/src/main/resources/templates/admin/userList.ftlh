<#assign title = "Пользователи">
<#assign content>
    <div class="users-header d-flex justify-content-between align-items-center mb-4">
        <h2>Список пользователей</h2>
    </div>

    <div class="card shadow-sm">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead class="table-light">
                    <tr>
                        <th>Email</th>
                        <th>Роли</th>
                        <th>Активность</th>
                        <th>Действия</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list users as user>
                        <tr class="clickable-row">
                            <td>${user.email}</td>
                            <td>
                                <#list user.roles as role>
                                    <span class="badge bg-primary me-1">${role}</span>
                                </#list>
                            </td>
                            <td>
                                <#if user.active>
                                    <span class="badge bg-success">Активен</span>
                                <#else>
                                    <span class="badge bg-secondary">Не активен</span>
                                </#if>
                            </td>
                            <td>
                                <div class="d-flex gap-2">
                                    <a href="/admin/users/${user.id}" class="btn btn-sm btn-outline-primary">
                                        <i class="bi bi-pencil"></i>
                                    </a>
                                    <form action="/admin/users/del/${user.id}" method="post" class="d-inline">
                                        <button type="submit" class="btn btn-sm btn-outline-danger"
                                                onclick="return confirm('Удалить этого пользователя?')">
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

<#include "layout.ftlh">