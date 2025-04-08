<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Информация о модели</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        <#include "css/about_model.css">
    </style>
</head>
<body>
<div class="model-details-container">
    <div class="model-header">
        <h2>Информация о модели</h2>
        <#if !model??>
            <div class="alert alert-warning">
                Для вашей специализации не назначена модель
            </div>
        </#if>
    </div>

    <#if model??>
        <div class="model-card">
            <div class="model-card-header">
                <h3>${model.name}</h3>
                <#--                    <span class="badge ${model.status == 'ACTIVE' ? 'bg-success' : 'bg-secondary'}">-->
                <#--                        ${model.status}-->
                <#--                    </span>-->
            </div>

            <div class="model-card-body">
                <div class="model-property">
                    <h5>Описание:</h5>
                    <p>${model.description!''}</p>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="model-property">
                            <h5>Тип ввода:</h5>
                            <span class="badge bg-info">
                                    <i class="bi bi-arrow-down"></i> ${model.inputType}
                                </span>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="model-property">
                            <h5>Тип вывода:</h5>
                            <span class="badge bg-primary">
                                    <i class="bi bi-arrow-up"></i> ${model.outputType}
                                </span>
                        </div>
                    </div>
                </div>

                <#--                    <div class="model-property">-->
                <#--                        <h5>API Endpoint:</h5>-->
                <#--                        <code>${model.endpoint}</code>-->
                <#--                    </div>-->

                <#--                    <#if model.kaggleURL??>-->
                <#--                        <div class="model-property">-->
                <#--                            <h5>Kaggle URL:</h5>-->
                <#--                            <a href="${model.kaggleURL}" target="_blank" class="text-decoration-none">-->
                <#--                                <i class="bi bi-box-arrow-up-right"></i> Открыть в Kaggle-->
                <#--                            </a>-->
                <#--                        </div>-->
                <#--                    </#if>-->

                <#--                    <div class="model-property">-->
                <#--                        <h5>Статус:</h5>-->
                <#--                        <span class="badge ${model.isDefault ? 'bg-warning text-dark' : 'bg-light text-dark'}">-->
                <#--                            ${model.isDefault?then('Модель по умолчанию', 'Дополнительная модель')}-->
                <#--                        </span>-->
                <#--                    </div>-->
            </div>
        </div>
    </#if>
</div>
</body>
</html>

