<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--因为在进入首页面时需要取到页面信息所以向控制器发送请求--%>
<c:redirect url="qiantai/goodSvl?reqType=main&pageNow=1"/>