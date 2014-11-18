<!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>User Login</title>
</head>
<body>
<div>
  <form action="#" th:action="@{/doLogin}" th:object="${user}" method="post">
    <input type="email" th:field="*{email}"/>
    <br><br>
    <input type="password" th:field="*{pwd}"/>
    <br><br>
    <input type="submit"/>
  </form>
</div>
</body>
</html>
