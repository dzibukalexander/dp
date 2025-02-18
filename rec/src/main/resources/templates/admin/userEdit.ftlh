<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title></title>
</head>
<body>
<main>
    User editor
    <form action="/user" method="post">
        <#list roles as role>

            <div>
                <label><input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>${role}</label>
            </div>
        </#list>
        <input type="hidden" value="${user.id}" name="userId">
<#--        <input type="hidden" value="${_csrf.token}" name="_csrf">-->
        <button type="submit">Save</button>
    </form>
</main>
</body>
</html>