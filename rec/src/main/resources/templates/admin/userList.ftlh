<#assign title = "Пользователи">
<#assign content>
    <h2 class="mb-4">Список пользователей</h2>
    <table class="table table-users">
        <thead>
        <tr>
            <th>Email</th>
            <th>Роли</th>
            <th>Активность</th>
            <th>Действия</th>
        </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <td>${user.email}</td>
                <td>
                    <#list user.roles as role>
                        <span class="badge bg-primary">${role}</span>
                    </#list>
                </td>
                <td>
                    <#if user.active>
                        <img width="35" height="35" src="https://img.icons8.com/laces/64/checked.png" alt="Активен"/>
                    <#else>
                        <img width="35" height="35" src="https://img.icons8.com/laces/64/close-window.png" alt="Не активен/>
                    </#if>
                </td>
                <td>
                    <a href="/admin/users/${user.id}" class="btn-edit">
                        <i class="bi bi-pencil-square"></i>
                    </a>
                    <form action="/admin/users/del/${user.id}" method="post" style="display: inline;">
                        <input type="hidden" name="_method" value="DELETE"/>
                        <button type="submit" class="btn-delete" style="border: none; background: none; cursor: pointer;">
                            <i class="bi bi-trash"></i>
                        </button>
                    </form>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</#assign>

<#include "layout.ftlh">