<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>账号任务管理</title>
    <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js"></script>
    <script src="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/4.6.2/js/bootstrap.bundle.min.js"></script>
    <link href="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/4.6.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }

        td {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            max-width: 250px;
        }

        .filter-container {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            margin: 20px 0;
        }

        .filter-group {
            display: flex;
            align-items: center;
            gap: 5px;
        }

        .filter-group label {
            margin-right: 5px;
        }

        .filter-group input, .filter-group select {
            width: auto;
            display: inline-block;
        }

        .filter-group button {
            margin-left: 10px;
        }

        /* 新增任务表单样式 */
        .form-container {
            display: none;
            background-color: #f9f9f9;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
            margin: 20px 0;
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }

        .form-group input, .form-group select {
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .toggle-button {
            background-color: #007bff;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin: 10px 0;
        }

        .toggle-button:hover {
            background-color: #0056b3;
        }

        /* 截图样式 */
        .screenshot-thumbnail {
            max-width: 100px;
            max-height: 60px;
            cursor: pointer;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        .screenshot-thumbnail:hover {
            opacity: 0.8;
        }

        /* 模态框样式 */
        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0,0,0,0.9);
        }

        .modal-content {
            margin: auto;
            display: block;
            width: 80%;
            max-width: 700px;
            max-height: 80%;
            object-fit: contain;
        }

        .close {
            position: absolute;
            top: 15px;
            right: 35px;
            color: #f1f1f1;
            font-size: 40px;
            font-weight: bold;
            cursor: pointer;
        }

        .close:hover,
        .close:focus {
            color: #bbb;
            text-decoration: none;
            cursor: pointer;
        }

        .verification-btn {
            background-color: #28a745;
            color: white;
            padding: 5px 10px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            font-size: 12px;
        }

        .verification-btn:hover {
            background-color: #218838;
        }
    </style>
    <script>
        var clickFilterStatus = false;

        function search(filter) {
            var taskQuery = {
                startDt: $('#startDt').val(),
                endDt: $('#endDt').val(),
                accountSource: "${accountSource}",
                queryAll: false,
                queryStatus: null
            };

            if (filter) {
                taskQuery.queryStatus = $('#queryStatus').val();
                taskQuery.competitorCode = $('#queryCompetitorCode').val();
                taskQuery.deviceCode = $('#queryDeviceCode').val();
                taskQuery.imageCode = $('#queryImageCode').val();
                taskQuery.channelName = $('#queryChannelName').val();
            }

            $.ajax({
                type: "POST",
                url: "/api/smart/dig/accountTask/query",
                contentType: 'application/json',
                data: JSON.stringify(taskQuery),
                success: function (response) {
                    var taskListHtml = '';
                    response.data.forEach(function (task) {
                        taskListHtml += '<tr>' +
                            '<td class="task-id" style="display:none;">' + task.id + '</td>' +
                            '<td><input type="text" class="account" value="' + (task.account || '') + '"/><button class="save-btn" data-task-id="' + task.id + '">保存</button></td>' +
                            '<td><input type="password" class="password" value="' + (task.password || '') + '"/><button class="save-btn" data-task-id="' + task.id + '">保存</button></td>' +
                            '<td>' +
                                '<input type="text" class="verification-code" value="' + (task.verificationCode || '') + '"/>' +
                                '<button class="verification-btn" data-task-id="' + task.id + '">申请验证码</button>' +
                                '<button class="save-btn" data-task-id="' + task.id + '">保存</button>' +
                            '</td>' +
                            '<td class="login-status">' + (task.isLoggedIn ? '是' : '否') + '</td>' +
                            '<td>' +
                                (task.screenshot ?
                                    '<img src="' + task.screenshot + '" alt="截图" class="screenshot-thumbnail" onclick="showFullImage(\'' + task.screenshot + '\')">' :
                                    '无'
                                ) +
                            '</td>' +
                            '<td><button class="delete-btn" data-task-id="' + task.id + '">删除</button></td>' +
                            '</tr>';
                    });
                    $('#taskList').html(taskListHtml);
                }
            });
        }

        function search4Update() {
            if (clickFilterStatus) {
                return;
            }
            var taskQuery = {
                startDt: $('#startDt').val(),
                endDt: $('#endDt').val(),
                accountSource: "${accountSource}",
                queryAll: true
            };

            $.ajax({
                type: "POST",
                url: "/api/smart/dig/accountTask/query",
                contentType: 'application/json',
                data: JSON.stringify(taskQuery),
                success: function (response) {
                    var updatedTaskList = response.data;
                    var currentTasks = [];
                    $("#taskList tr").each(function () {
                        var taskId = $(this).find(".task-id").text();
                        if (taskId.includes(",")) {
                            taskId = taskId.replace(",", '');
                        }
                        currentTasks.push(parseInt(taskId));
                    })
                    updatedTaskList.forEach(function (updatedTask) {
                        var taskId = updatedTask.id;
                        var taskExits = $.inArray(taskId, currentTasks) > -1;
                        if (taskExits) {
                            var taskRow = $("#taskList tr").filter(function () {
                                return parseInt($(this).find(".task-id").text()) === taskId;
                            })
                            taskRow.find(".login-status").text(updatedTask.isLoggedIn ? '是' : '否');
                        } else {
                            if (updatedTask.needDeal) {
                                var newRow = '<tr>' +
                                    '<td class="task-id" style="display:none;">' + updatedTask.id + '</td>' +
                                    '<td><input type="text" class="account" value="' + (updatedTask.account || '') + '"/><button class="save-btn" data-task-id="' + updatedTask.id + '">保存</button></td>' +
                                    '<td><input type="password" class="password" value="' + (updatedTask.password || '') + '"/><button class="save-btn" data-task-id="' + updatedTask.id + '">保存</button></td>' +
                                    '<td>' +
                                        '<input type="text" class="verification-code" value="' + (updatedTask.verificationCode || '') + '"/>' +
                                        '<button class="verification-btn" data-task-id="' + updatedTask.id + '">申请验证码</button>' +
                                        '<button class="save-btn" data-task-id="' + updatedTask.id + '">保存</button>' +
                                    '</td>' +
                                    '<td class="login-status">' + (updatedTask.isLoggedIn ? '是' : '否') + '</td>' +
                                    '<td>' +
                                        (updatedTask.screenshot ?
                                            '<img src="' + updatedTask.screenshot + '" alt="截图" class="screenshot-thumbnail" onclick="showFullImage(\'' + updatedTask.screenshot + '\')">' :
                                            '无'
                                        ) +
                                    '</td>' +
                                    '<td><button class="delete-btn" data-task-id="' + updatedTask.id + '">删除</button></td>' +
                                    '</tr>';
                                $("#taskList").append(newRow);
                            }
                        }
                    })
                }
            });
        }

        function showFullImage(imageSrc) {
            var modal = document.getElementById('imageModal');
            var modalImg = document.getElementById('modalImage');
            modal.style.display = "block";
            modalImg.src = imageSrc;
        }

        setInterval(search4Update, 5000);

        $(document).ready(function () {
            var today = new Date();
            var year = today.getFullYear();
            var month = ("0" + (today.getMonth() + 1)).slice(-2);
            var day = ("0" + today.getDate()).slice(-2);
            var formattedDate = year + "-" + month + "-" + day;
            $("#startDt").val(formattedDate);
            $("#endDt").val(formattedDate);

            // 新增任务表单切换
            $('#taskToggleButton').click(function () {
                var taskFormContainer = $('#taskFormContainer');
                if (taskFormContainer.is(':visible')) {
                    taskFormContainer.hide();
                } else {
                    taskFormContainer.show();
                }
            });

            // 新增任务表单提交
            $('#taskForm').submit(function (event) {
                event.preventDefault();
                var formData = {
                    taskType: $('#taskType').val(),
                    loginName: $('#loginName').val(),
                    password: $('#taskPassword').val(),
                    verificationCode: $('#taskVerificationCode').val(),
                    price: parseFloat($('#price').val())
                };

                $.ajax({
                    url: '/api/smart/dig/accountTask/createTask',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(formData),
                    success: function (data) {
                        if (data.code === 0) {
                            alert('任务创建成功');
                            $('#taskForm')[0].reset();
                            search(false);
                        } else {
                            alert('任务创建失败: ' + data.message);
                        }
                    },
                    error: function (error) {
                        alert('任务创建失败: ' + error);
                    }
                });
            });

            $(document).on('click', '#search-to-deal', function () {
                clickFilterStatus = false;
                search(false);
            })

            $(document).on('click', '#search-by-status', function () {
                clickFilterStatus = true;
                search(true);
            })

            // 保存按钮事件
            $(document).on('click', '.save-btn', function () {
                var taskId = String($(this).data('task-id'));
                if (taskId.includes(",")) {
                    taskId = taskId.replace(",", '');
                }
                var taskRow = $(this).closest('tr');
                var task = {
                    taskId: taskId,
                    account: taskRow.find('.account').val(),
                    password: taskRow.find('.password').val(),
                    verificationCode: taskRow.find('.verification-code').val()
                };

                $.ajax({
                    url: '/api/smart/dig/accountTask/update',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(task),
                    success: function (response) {
                        if (response.code === 0) {
                            taskRow.find('.account').val(response.data.account);
                            taskRow.find('.password').val(response.data.password);
                            taskRow.find('.verification-code').val(response.data.verificationCode);
                            taskRow.find('.login-status').text(response.data.isLoggedIn ? '是' : '否');
                        } else {
                            alert('更新失败: ' + response.msg);
                        }
                    },
                    error: function (xhr, status, error) {
                        alert('更新失败: ' + error);
                    }
                });
            });

            // 申请验证码按钮事件
            $(document).on('click', '.verification-btn', function () {
                var taskId = String($(this).data('task-id'));
                if (taskId.includes(",")) {
                    taskId = taskId.replace(",", '');
                }

                $.ajax({
                    url: '/api/smart/dig/accountTask/requestVerificationCode',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({taskId: taskId}),
                    success: function (response) {
                        if (response.code === 0) {
                            alert('验证码申请成功');
                        } else {
                            alert('验证码申请失败: ' + response.msg);
                        }
                    },
                    error: function (xhr, status, error) {
                        alert('验证码申请失败: ' + error);
                    }
                });
            });

            // 删除按钮事件
            $(document).on('click', '.delete-btn', function () {
                var userConfirmed = confirm("确定要删除吗？");
                if (userConfirmed) {
                    var taskId = String($(this).data('task-id'));
                    if (taskId.includes(",")) {
                        taskId = taskId.replace(",", '');
                    }
                    var taskRow = $(this).closest('tr');

                    $.ajax({
                        url: '/api/smart/dig/accountTask/delete',
                        type: 'POST',
                        contentType: 'application/json',
                        data: JSON.stringify({taskId: taskId}),
                        success: function (response) {
                            if (response.code === 0) {
                                taskRow.remove();
                                alert("删除成功");
                            } else {
                                alert('删除失败: ' + response.msg);
                            }
                        },
                        error: function (xhr, status, error) {
                            alert('删除失败: ' + error);
                        }
                    });
                } else {
                    alert("删除取消");
                }
            });

            // 关闭模态框
            $('#imageModal').click(function(e) {
                if (e.target === this) {
                    this.style.display = "none";
                }
            });
        });
    </script>
</head>
<body>

<div class="container" style="margin-left: 50px;">
    <h1 style="text-align: center">账号任务管理</h1>

    <!-- 新增任务表单按钮 -->
    <button class="toggle-button" id="taskToggleButton">新增任务</button>
    <div class="form-container" id="taskFormContainer">
        <form id="taskForm">
            <div class="form-group">
                <label for="taskType">任务类型:</label>
                <select id="taskType" name="taskType" required>
                    <option value="">请选择任务类型</option>
                    <option value="login">登录任务</option>
                    <option value="data_collection">数据采集</option>
                    <option value="verification">验证任务</option>
                    <option value="monitoring">监控任务</option>
                </select>
            </div>
            <div class="form-group">
                <label for="loginName">登陆名:</label>
                <input type="text" id="loginName" name="loginName" required>
            </div>
            <div class="form-group">
                <label for="taskPassword">密码:</label>
                <input type="password" id="taskPassword" name="taskPassword" required>
            </div>
            <div class="form-group">
                <label for="taskVerificationCode">验证码:</label>
                <input type="text" id="taskVerificationCode" name="taskVerificationCode" placeholder="如有验证码请输入">
            </div>
            <div class="form-group">
                <label for="price">价格:</label>
                <input type="number" id="price" name="price" step="0.01" min="0" required placeholder="请输入价格">
            </div>
            <button type="submit">创建任务</button>
        </form>
    </div>

    <div>
        <label for="startDt">开始日期:</label>
        <input type="date" id="startDt" name="startDt">
        <label for="endDt">结束日期:</label>
        <input type="date" id="endDt" name="endDt">
        <button id="search-to-deal">查询待处理</button>
        <br><br>
        <div class="filter-container">
            <div class="filter-group">
                <label for="queryStatus">状态:</label>
                <select id="queryStatus">
                    <option value="-1">全部</option>
                    <option value="1">待处理</option>
                    <option value="2">已登录</option>
                    <option value="3">已完成</option>
                    <option value="4">失败</option>
                </select>
            </div>
            <div class="filter-group">
                <label for="queryCompetitorCode">项目:</label>
                <select id="queryCompetitorCode" style="width: 120px;">
                    <option value="-1">全部</option>
                    <option value="dd">dd</option>
                    <option value="pp">pp</option>
                </select>
            </div>
            <div class="filter-group">
                <label for="queryChannelName">渠道:</label>
                <input type="text" id="queryChannelName" name="queryChannelName">
            </div>
            <div class="filter-group">
                <label for="queryDeviceCode">设备ID:</label>
                <input type="text" id="queryDeviceCode" name="queryDeviceCode" style="width: 100px;">
            </div>
            <div class="filter-group">
                <label for="queryImageCode">镜像ID:</label>
                <input type="text" id="queryImageCode" name="queryImageCode" style="width: 100px;">
            </div>
            <div class="filter-group">
                <button id="search-by-status">查询</button>
            </div>
        </div>

        <table width="1400" style="margin-top: 50px;width:1600px;">
            <thead>
            <tr>
                <th>账号</th>
                <th>密码</th>
                <th>验证码/令牌</th>
                <th>已登陆</th>
                <th>已完成（截图）</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="taskList">
            <#list list as task>
                <tr>
                    <td class="task-id" style="display:none;">${task.id}</td>
                    <td>
                        <input type="text" class="account" value="${task.account!''}"/>
                        <button class="save-btn" data-task-id="${task.id}">保存</button>
                    </td>
                    <td>
                        <input type="password" class="password" value="${task.password!''}"/>
                        <button class="save-btn" data-task-id="${task.id}">保存</button>
                    </td>
                    <td>
                        <input type="text" class="verification-code" value="${task.verificationCode!''}"/>
                        <button class="verification-btn" data-task-id="${task.id}">申请验证码</button>
                        <button class="save-btn" data-task-id="${task.id}">保存</button>
                    </td>
                    <td class="login-status">${task.isLoggedIn?string('是', '否')}</td>
                    <td>
                        <#if task.screenshot??>
                            <img src="${task.screenshot}" alt="截图" class="screenshot-thumbnail" onclick="showFullImage('${task.screenshot}')">
                        <#else>
                            无
                        </#if>
                    </td>
                    <td>
                        <button class="delete-btn" data-task-id="${task.id}">删除</button>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</div>

<!-- 图片模态框 -->
<div id="imageModal" class="modal">
    <span class="close">&times;</span>
    <img class="modal-content" id="modalImage">
</div>

</body>
</html>