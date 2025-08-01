<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>index</title>
    <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js"></script>
    <script src="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/4.6.2/js/bootstrap.bundle.min.js"></script>
    <link href="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/4.6.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }

        td {

            overflow: hidden; /* 隐藏超出内容 */
            text-overflow: ellipsis; /* 显示省略号 */
            white-space: nowrap; /* 不换行 */
            max-width: 250px; /* 控制单元格最大宽度 */
        }
    </style>
    <script>
        var clickFilterStatus = false;

        function search(filter) {
            // 获取表单数据
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

            // 使用Ajax发送POST请求
            $.ajax({
                type: "POST",
                url: "/api/smart/dig/accountTask/query",
                contentType: 'application/json', // 设置请求头以发送JSON数据
                data: JSON.stringify(taskQuery), // 将对象转换为JSON字符串,
                success: function (response) {
                    var taskListHtml = '';
                    // 更新页面的内容
                    response.data.forEach(function (task) {
                        taskListHtml += '<tr>' +
                            '<td class="task-id">' + task.id + '</td>' +
                            '<td>' + task.deviceCode + '</td>' +
                            '<td>' + task.imageCode + '</td>' +
                            '<td><input type="text" class="phone-num" value="' + task.phoneNum + '"/><button class="save-btn" data-task-id="' + task.id + '">保存</button></td>' +
                            '<td><input type="text" class="v-code" value="' + task.verificationCode + '"/><button class="save-btn" data-task-id="' + task.id + '">保存</button></td>' +
                            '<td>' + task.competitorCode + '</td>' +
                            '<td>' + task.accountSource + '</td>' +
                            '<td><input type="text" class="channel-name" value="' + task.channelName + '"/><button class="channel-save-btn" data-task-id="' + task.id + '">保存</button></td>' +

                            '<td class="task-status">' + task.statusValue + '</td>' +
                            '<td class="create-desc">' + task.desc + '</td>' +
                            '<td class="create-time">' + task.createTime + '</td>' +
                            '<td class="update-time">' + task.updateTime + '</td>' +
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
            // 获取表单数据
            var taskQuery = {
                startDt: $('#startDt').val(),
                endDt: $('#endDt').val(),
                accountSource: "${accountSource}",
                queryAll: true
            };

            // 使用Ajax发送POST请求
            $.ajax({
                type: "POST",
                url: "/api/smart/dig/accountTask/query",
                contentType: 'application/json', // 设置请求头以发送JSON数据
                data: JSON.stringify(taskQuery), // 将对象转换为JSON字符串,
                success: function (response) {
                    var updatedTaskList = response.data;
                    var currentTasks = []; // 保存当前页面的任务列表
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
                            taskRow.find(".task-status").text(updatedTask.statusValue)
                            taskRow.find(".update-time").text(updatedTask.updateTime)
                        } else {
                            if (updatedTask.needDeal) {
                                var newRow = '<tr>' +
                                    '<td class="task-id">' + updatedTask.id + '</td>' +
                                    '<td>' + updatedTask.deviceCode + '</td>' +
                                    '<td>' + updatedTask.imageCode + '</td>' +
                                    '<td><input type="text" class="phone-num" value="' + updatedTask.phoneNum + '"/><button class="save-btn" data-task-id="' + updatedTask.id + '">保存</button></td>' +
                                    '<td><input type="text" class="v-code" value="' + updatedTask.verificationCode + '"/><button class="save-btn" data-task-id="' + updatedTask.id + '">保存</button></td>' +
                                    '<td>' + updatedTask.competitorCode + '</td>' +
                                    '<td>' + updatedTask.accountSource + '</td>' +
                                    '<td><input type="text" class="channel-name" value="' + updatedTask.channelName + '"/><button class="channel-save-btn" data-task-id="' + updatedTask.id + '">保存</button></td>' +

                                    '<td class="task-status">' + updatedTask.statusValue + '</td>' +
                                    '<td class="create-desc">' + updatedTask.desc + '</td>' +
                                    '<td class="create-time">' + updatedTask.createTime + '</td>' +
                                    '<td class="update-time">' + updatedTask.updateTime + '</td>' +
                                    '<td><button class="delete-btn" data-task-id="' + updatedTask.id + '">删除</button></td>' +
                                    '</tr>';
                                $("#taskList").append(newRow);
                            }
                        }
                    })
                }
            });
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

            $(document).on('click', '#search-to-deal', function () {
                clickFilterStatus = false;
                search(false);
            })

            $(document).on('click', '#search-by-status', function () {
                clickFilterStatus = true;
                search(true);
            })


            // 绑定保存按钮事件处理程序
            $(document).on('click', '.save-btn', function () {
                var taskId = String($(this).data('task-id'));
                if (taskId.includes(",")) {
                    taskId = taskId.replace(",", '');
                }
                var taskRow = $(this).closest('tr');
                var task = {
                    taskId: taskId,
                    phoneNum: taskRow.find('.phone-num').val(),
                    verificationCode: taskRow.find('.v-code').val(),
                };

                // 发送更新请求
                $.ajax({
                    url: '/api/smart/dig/accountTask/update',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(task),
                    success: function (response) {
                        if (response.code === 0) {
                            taskRow.find('.phone-num').val(response.data.phoneNum);
                            taskRow.find('.v-code').val(response.data.verificationCode);
                            taskRow.find('.task-status').text(response.data.statusValue);
                        } else {
                            alert('更新失败: ' + response.msg);
                        }
                    },
                    error: function (xhr, status, error) {
                        // 错误处理
                        alert('更新失败: ' + error);
                    }
                });
            });

            // 绑定渠道保存按钮事件处理程序
            $(document).on('click', '.channel-save-btn', function () {
                var taskId = String($(this).data('task-id'));
                if (taskId.includes(",")) {
                    taskId = taskId.replace(",", '');
                }
                var taskRow = $(this).closest('tr');
                var task = {
                    taskId: taskId,
                    channelName: taskRow.find('.channel-name').val()
                };

                // 发送更新请求
                $.ajax({
                    url: '/api/smart/dig/accountTask/updateChannel',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(task),
                    success: function (response) {
                        if (response.code === 0) {
                            taskRow.find('.channel-name').val(response.data.channelName);
                        } else {
                            alert('更新失败: ' + response.msg);
                        }
                    },
                    error: function (xhr, status, error) {
                        // 错误处理
                        alert('更新失败: ' + error);
                    }
                });
            });

            // 绑定删除按钮事件处理程序
            $(document).on('click', '.delete-btn', function () {
                var userConfirmed = confirm("确定要删除吗？");
                if (userConfirmed) {
                    var taskId = String($(this).data('task-id'));
                    if (taskId.includes(",")) {
                        taskId = taskId.replace(",", '');
                    }
                    var taskRow = $(this).closest('tr');
                    var task = {
                        taskId: taskId,
                        phoneNum: "delete",
                        verificationCode: "delete"
                    };

                    // 发送更新请求
                    $.ajax({
                        url: '/api/smart/dig/accountTask/update',
                        type: 'POST',
                        contentType: 'application/json',
                        data: JSON.stringify(task),
                        success: function (response) {
                            if (response.code === 0) {
                                // taskRow.find('.phone-num').val(response.data.phoneNum);
                                // taskRow.find('.v-code').val(response.data.verificationCode);
                                // taskRow.find('.task-status').text(response.data.statusValue);
                            } else {
                                alert('更新失败: ' + response.msg);
                            }
                        },
                        error: function (xhr, status, error) {
                            // 错误处理
                            alert('更新失败: ' + error);
                        }
                    });

                    alert("删除成功");
                } else {
                    alert("删除取消");
                }
            });
        });
    </script>
    <style>
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
    </style>
</head>
<body>
<script src="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/4.6.2/js/bootstrap.bundle.min.js"></script>

<div class="container" style="margin-left: 50px;">
    <h1 style="text-align: center">接码任务</h1>
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
                    <option value="1">待接码</option>
                    <option value="2">已录手机号</option>
                    <option value="3">验证码已发送</option>
                    <option value="4">已录验证码</option>
                    <option value="5">接码成功</option>
                    <option value="6">接码失败(需要更换手机号)</option>
                    <option value="7">该接码任务放弃</option>
                    <option value="8">一直收不到验证码</option>
                    <option value="9">人工已删除</option>
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
            <#--            <div class="filter-group">-->
            <#--                <label for="queryAccountSource">码商渠道:</label>-->
            <#--                <select id="queryAccountSource" style="width: 120px;">-->
            <#--                    <option value="-1">全部</option>-->
            <#--                    <option value="orange">orange</option>-->
            <#--                    <option value="sf">sf</option>-->
            <#--                </select>-->
            <#--            </div>-->
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
        <#--    class="table table-sm table-striped"-->
<#--        <table width="1400" style="margin-top: 50px;">-->
<#--            <div class="form-group row">-->
<#--                <label for="fileInput" class="col-sm-2 col-form-label">供应商账号文件上传:</label>-->
<#--                <div class="col-sm-10">-->
<#--                    <input type="file" style="display: none;" id="fileInput" name="file">-->
<#--                    <button type="button" id="fileInputBtn">选择文件</button>-->
<#--                    <span class="ml-2" id="fileNameDisplay">未选择文件</span>-->
<#--                    <button type="button" id="supplier-phone-upload">上传</button>-->
<#--                </div>-->
<#--            </div>-->
<#--            <div id="uploadStatus" class="mt-2"></div>-->
<#--            <label>状态：</label>-->
<#--            <select id="queryStatus">-->
<#--                <option value="-1">全部</option>-->
<#--                <option value="1">待接码</option>-->
<#--                <option value="2">已录手机号</option>-->
<#--                <option value="3">验证码已发送</option>-->
<#--                <option value="4">已录验证码</option>-->
<#--                <option value="5">接码成功</option>-->
<#--                <option value="6">接码失败(需要更换手机号)</option>-->
<#--                <option value="7">该接码任务放弃</option>-->
<#--                <option value="8">一直收不到验证码</option>-->
<#--                <option value="9">人工已删除</option>-->
<#--            </select>-->
<#--            &nbsp;&nbsp;&nbsp;<label>项目：</label>-->
<#--            <select id="queryCompetitorCode" style="width: 200px;">-->
<#--                <option value="-1">全部</option>-->
<#--                <option value="dd">dd</option>-->
<#--                <option value="pp">pp</option>-->
<#--            </select>-->
<#--            &nbsp;&nbsp;&nbsp;<label>码商：</label>-->
<#--            <select id="queryAccountSource" style="width: 200px;">-->
<#--                <option value="-1">全部</option>-->
<#--                <option value="orange">orange</option>-->
<#--                <option value="sf">sf</option>-->
<#--            </select>-->
<#--            <label for="queryChannelName">渠道:</label>-->
<#--            <input type="text" id="queryChannelName" name="queryChannelName">-->
<#--            <button id="search-by-status">查询</button>-->
<#--    </div>-->
    <#--    class="table table-sm table-striped"-->
    <table width="1400" style="margin-top: 50px;width:1600px;">
        <thead>
        <tr>
            <th>id</th>
            <th>编码1</th>
            <th>编码2</th>
            <th>手机号</th>
            <th>验证码</th>
            <th>项目</th>
            <th>码商</th>
            <th>渠道</th>
            <th>状态</th>
            <th>描述</th>
            <th>创建时间</th>
            <th>更新时间</th>
            <th>删除操作</th>
        </tr>
        </thead>
        <tbody id="taskList">
        <#list list as task>
            <tr>
                <td class="task-id">${task.id}</td>
                <td>${task.deviceCode}</td>
                <td>${task.imageCode}</td>
                <td>
                    <input type="text" class="phone-num" value="${task.phoneNum}"/>
                    <button class="save-btn" data-task-id="${task.id}">保存</button>
                </td>
                <td>
                    <input type="text" class="v-code" value="${task.verificationCode}"/>
                    <button class="save-btn" data-task-id="${task.id}">保存</button>
                </td>
                <td>${task.competitorCode}</td>
                <td>${task.accountSource}</td>
                <td>
                    <input type="text" class="channel-name" value="${task.channelName}"/>
                    <button class="channel-save-btn" data-task-id="${task.id}">保存</button>
                </td>
                <td class="task-status">${task.statusValue}</td>
                <td class="create-desc">${task.desc}</td>
                <td class="create-time">${task.createTime}</td>
                <td class="update-time">${task.updateTime}</td>
                <td>
                    <button class="delete-btn" data-task-id="${task.id}">删除</button>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>
</body>
</html>