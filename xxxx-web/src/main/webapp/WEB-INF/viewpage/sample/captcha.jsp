<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE>
<html>
<head>
<meta name="decorator" content="none">
<title><spring:message code="captcha.title">
        <!--語系-->
    </spring:message></title>
<style>
</style>
</head>
<body>
    <script>
		loadScript('js/sample/captcha');
	</script>




    <h1>Audio Captcha Demo</h1>

    <br />



    <form method="post" autocomplete="off" onsubmit="return false;">
        <input id="audioCaptcha" name="audioCaptcha" class="audioCaptcha"/>

        <br/>

        <%-- <input id="captcha" name="captcha" class="captcha" type="text" /> --%>
        <button id="submit">
            <spring:message code="captcha.001">
                <!-- 驗証 -->
            </spring:message>
        </button>
    </form>



</body>
</body>
</html>
