<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@page isErrorPage="true" %>
<%@page session="false" %>

<%

    Integer errorcode = (java.lang.Integer) request.getAttribute("javax.servlet.error.status_code");
    if (errorcode != null && errorcode >= 400 && !response.isCommitted()) {
        response.setStatus(errorcode);
    }

    Boolean processd = (Boolean) request.getAttribute("net.tenplanets.errorpage.processd");
    if (processd != null && processd) {
        //Error page is processing twice when extention of page name is wrong.
        return;
    }
    request.setAttribute("net.tenplanets.errorpage.processd", true);

%>

<html>
    <head>
     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="description" content="Error page"/>
    <meta name="robots" content="noindex, nofollow"/>
    <meta name="author" content="Vadims Zemlanojs">
    </head>
    <body style="padding:0 5px;">

        <br><br>
        <h3>Error processin page. ErrorCode: (${pageContext.errorData.statusCode})</h3>
        <p>
            ${pageContext.errorData.throwable.message}         
        </p>
        <hr/>

        <script type="text/javascript">
        </script>
        <%--In order to prevent IE to show the internal error page, it is necessary to make the page with a size more than 512 bytes (Meta also is considered). --%>
    </body>
</html>



