<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><c:if test="${commitUrl != null}">
<c:choose>
    <c:when test="${compactMode}">
        <a href="${commitUrl}" title="Click to view commit on BitBucket Cloud" target="_blank" style="text-decoration: none; color: #151515"><i class="icon-bitbucket"></i></a>
    </c:when>
    <c:otherwise>
        <dt><i class="icon-bitbucket"></i> <a href="${commitUrl}" target="_blank" title="View commit on GitHub">View commit on BitBucket Cloud</a></dt>
    </c:otherwise>
</c:choose>
</c:if>