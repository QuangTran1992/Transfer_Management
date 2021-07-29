<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: mebemint
  Date: 2021/07/27
  Time: 15:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div align="center">
    <form method="post" action="">
        <table style="width: 600px; height: 300px" border="1" cellpadding="10">
            <caption>
                <h2>
                    Transfer Information
                </h2>
                <a href="users">back to main</a>

            </caption>
            <c:if test="${user != null}">
                <input type="hidden" name="id" value="<c:out value='${user.id}' />"/>
            </c:if>
            <tr>
                <th>Transfer:</th>
                <td>
                    <input type="text" name="name" size="45"
                           value="<c:out value='${user.name}' />"
                    />
                </td>
            </tr>
            <tr>
                <th>Receiver</th>
                <td>
                    <select name="receiverId" id="receiver">
                        <c:forEach items="${users}" var="user">
                        <option>${user.id}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <th>Amout:</th>
                <td>
                    <input type="text" name="amount" size="15"
                           value=""
                    />
                </td>
            </tr>


            <tr>
                <td colspan="2" align="center">
                    <input type="submit" value="Transfer"/>
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
