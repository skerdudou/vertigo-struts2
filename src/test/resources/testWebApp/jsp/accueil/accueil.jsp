<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%> 

<s:include value="/jsp/include/pageHeader.jsp">
	<s:param name="subtitle">${pageName}</s:param>
</s:include>

<s:form id="layoutTable">
<h1>Test div layout=table</h1>
	<s:div layout="table">
		<s:textfield name="movie.title" label="default" />
		<s:textfield name="movie.year" label="default" />
		<s:submit action="saveAccueil" />
	</s:div>
</s:form>
<s:form  id="displayTable">
<h1>Test display:table sur ContextList</h1>
<display:table name="movies" class="tableau" uid="item" export="false" requestURI="#" pagesize="20" defaultsort="0">
	<display:column property="title" title="Jour" sortable="true" />
	<display:column property="year" title="year" sortable="true"/>
</display:table>
</s:form>
<s:form id="selectContextList">
<h1>Test select sur ContextList</h1>
	<s:div layout="table">
		<s:select name="casting.movId" label="default" list="movies" listKey="movId" listValue="title"/>
		<s:submit action="saveCastingAccueil" />
	</s:div>
</s:form>
<s:form id="radioContextList">
<h1>Test radio sur ContextList</h1>
	<s:div layout="table">
		<s:radio name="casting.movId" label="default" list="movies" listKey="movId" listValue="title"/>
		<s:submit action="saveCastingAccueil" />
	</s:div>
</s:form>
<s:form id="selectContextMdl">
<h1>Test select sur ContextMdl</h1>
	<s:div layout="table">
		<s:select name="casting.movId" label="default" list="moviesMdl" listKey="movId" listValue="title"/>
		<s:submit action="saveCastingAccueil" />
	</s:div>
</s:form>
<s:form id="radioContextMdl">
<h1>Test radio sur ContextMdl</h1>
	<s:div layout="table">
		<s:radio name="casting.movId" label="default" list="moviesMdl" listKey="movId" listValue="title"/>
		<s:submit action="saveCastingAccueil" />
	</s:div>
</s:form>
<s:form id="autocompleteContextList">
<h1>Test sj:autocompleter sur ContextList</h1>
	<s:div layout="table">
		<sj:autocompleter name="casting.movId" list="movies" listKey="movId" listValue="title" label="default"  headerKey="" headerValue="" href="ListAutocomplete.do" />
		<s:submit action="saveCastingAccueil" />
	</s:div>
</s:form>
<s:form id="autocompleteContextMdl">
<h1>Test sj:autocompleter sur ContextMdl</h1>
	<s:div layout="table">
		<sj:autocompleter name="communeId" list="communesMdl"  label="commune"  href="ListAutocomplete.do" listKey="idInsee" listValue="commune" />
		<s:submit action="saveCommuneAccueil" />
	</s:div>
</s:form>
<s:form id="displayTableContextListModifiable">
<h1>Test display:table sur ContextListModifiable</h1>
<display:table name="moviesModifiable" class="tableau" uid="itemModifiable" export="false" requestURI="#" pagesize="20" defaultsort="0">
	<display:column title="Titre" >
		<s:textfield name="%{util.contextKey(#attr.itemModifiable)}.title" theme="simple" />
	</display:column>
	<display:column title="Année" >
		<s:textfield name="%{util.contextKey(#attr.itemModifiable)}.year" theme="simple" />
	</display:column>
</display:table>
<s:submit action="saveListAccueil" />
<s:submit action="addMovieListAccueil" value="Ajouter"/>
</s:form>
<s:form id="changeMode">
Change mode : 
<s:if test="%{modeReadOnly}">
	<s:submit action="toEditAccueil" value="Edit"/>
</s:if>
<s:else>
	<s:submit action="toReadAccueil" value="Read"/>
</s:else>
</s:form>

<s:form id="uploadFile" method="post" enctype="multipart/form-data" >
<s:file name="test" theme="simple"/>
<s:submit action="uploadFileAccueil" value="Upload"/>
</s:form>
<s:include value="/jsp/include/pageFooter.jsp" />