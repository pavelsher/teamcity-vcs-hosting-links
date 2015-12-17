<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><c:if test="${commitUrl != null}"
><a href="${commitUrl}" title="Click to open commit on GitHub" target="_blank" style="text-decoration: none; color: #151515"><i class="icon-github"></i></a
></c:if>