<!DOCTYPE html>
<html>
<head>
    <meta charset="utf‐8">
    <title>Hello World!</title>
</head>
<body>
    <table>
        <tr>
            <td>序号</td>
            <td>姓名</td>
            <td>年龄</td>
            <td>钱包</td>
            <td>日期</td>
        </tr>
        <#if stus??>
            <#list stus as stu>
            <tr>
                <td>${stu_index + 1}</td>
                <td <#if stu.name =='小明'>style="background:red;"</#if>>${stu.name}</td>
                <td>${stu.age}</td>
                <td>${stu.money}</td>
                <td>${stu.birthday?date}</td>
            </tr>
            </#list>
        </#if>
    </table>
    第一种方法
    输出stu1的学生信息：<br/>
    姓名：${stuMap['stu1'].name}<br/>
    年龄：${stuMap['stu1'].age}<br/>
    第二中方法
    输出stu1的学生信息：<br/>
    姓名：${stuMap.stu1.name}<br/>
    年龄：${stuMap.stu1.age}<br/>
    <table>
        <tr>
            <td>序号</td>
            <td>姓名</td>
            <td>年龄</td>
            <td>钱包</td>
        </tr>
        <#list stuMap?keys as k>
            <tr>
                <td>${k_index + 1}</td>
                <td>${stuMap[k].name}</td>
                <td>${stuMap[k].age}</td>
                <td >${stuMap[k].money}</td>
            </tr>
        </#list>
    </table>

</body>
</html>