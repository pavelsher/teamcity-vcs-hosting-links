<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><c:if test="${commitUrl != null}"
><a href="${commitUrl}" title="Click to open commit on GitHub" target="_blank" class="githubLink"><i class="icon-github"></i></a
></c:if>