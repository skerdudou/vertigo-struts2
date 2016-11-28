/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2016, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.struts2.core;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import io.vertigo.dynamo.domain.metamodel.DataType;
import io.vertigo.dynamo.domain.metamodel.DtField;
import io.vertigo.dynamo.domain.model.DtObject;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.lang.Assertion;
import io.vertigo.vega.engines.webservice.json.RestUiObject;

/**
 * Objet d'IHM, fournit les valeurs formatés des champs de l'objet métier sous-jacent.
 * Implements Map<String, Object> car struts poste des String[] que l'on reconverti en String (on prend le premier).
 *
 * @author pchretien, npiedeloup
 * @param <D> Type de DtObject représenté par cet Input
 */
public final class StrutsUiObject<D extends DtObject> extends RestUiObject<D> implements Map<String, Serializable> {

	public StrutsUiObject(final D serverSideDto) {
		this(serverSideDto, (D) DtObjectUtil.createDtObject(DtObjectUtil.findDtDefinition(serverSideDto)), Collections.emptySet());
	}

	public StrutsUiObject(final D serverSideDto, final D inputDto, final Set<String> modifiedFields) {
		super(inputDto, modifiedFields);
		setServerSideObject(serverSideDto);
	}

	private static final long serialVersionUID = -4639050257543017072L;
	private static final String DOMAIN_MULTIPLE_IDS = "DO_MULTIPLE_IDS";

	/** {@inheritDoc} */
	@Override
	public Serializable get(final Object key) {
		final String keyFieldName = String.class.cast(key);
		Assertion.checkArgNotEmpty(keyFieldName);
		Assertion.checkArgument(Character.isLowerCase(keyFieldName.charAt(0)) && !keyFieldName.contains("_"), "Le nom du champs doit-être en camelCase ({0}).", keyFieldName);
		//-----
		final DtField dtField = getDtField(keyFieldName);
		if (isMultiple(dtField)) {
			final String strValue = getStringValue(keyFieldName);
			return parseMultipleValue(strValue);
		} else if (isBoolean(dtField)) {
			final Boolean value = getTypedValue(keyFieldName, Boolean.class);
			return value != null ? String.valueOf(value) : null;
		} else {
			return getStringValue(keyFieldName);
		}
	}

	/** {@inheritDoc} */
	@Override
	public String put(final String fieldName, final Serializable value) {
		Assertion.checkArgNotEmpty(fieldName);
		Assertion.checkNotNull(value, "La valeur formatée ne doit pas être null mais vide ({0})", fieldName);
		Assertion.checkState(value instanceof String || value instanceof String[], "Les données saisies doivent être de type String ou String[] ({0} : {1})", fieldName, value.getClass());
		//-----
		final DtField dtField = getDtField(fieldName);
		final String strValue;
		if (isMultiple(dtField)) {
			strValue = formatMultipleValue((String[]) value);
		} else {
			strValue = requestParameterToString(value);
		}
		setStringValue(fieldName, strValue);
		return null;// TODO voir comment faire autrement
	}

	private static String requestParameterToString(final Serializable value) {
		return value instanceof String[] ? ((String[]) value)[0] : (String) value;
	}

	private static String formatMultipleValue(final String[] values) {
		final StringBuilder sb = new StringBuilder();
		String sep = "";
		for (final String value : values) {
			sb.append(sep);
			sb.append(value);
			sep = ";";
		}
		return sb.toString();
	}

	private static String[] parseMultipleValue(final String strValue) {
		return strValue.split(";");
	}

	private static boolean isMultiple(final DtField dtField) {
		return DOMAIN_MULTIPLE_IDS.equals(dtField.getDomain().getName());
	}

	private static boolean isBoolean(final DtField dtField) {
		return dtField.getDomain().getDataType() == DataType.Boolean;
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsKey(final Object arg0) {
		return camel2ConstIndex.containsKey(arg0);
	}

	/** Non implémenté. */
	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	/** Non implémenté. */
	@Override
	public boolean containsValue(final Object arg0) {
		throw new UnsupportedOperationException();
	}

	/** Non implémenté. */
	@Override
	public Set<java.util.Map.Entry<String, Serializable>> entrySet() {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEmpty() {
		return camel2ConstIndex.isEmpty();
	}

	/** {@inheritDoc} */
	@Override
	public Set<String> keySet() {
		return camel2ConstIndex.keySet();
	}

	/** Non implémenté. */
	@Override
	public void putAll(final Map<? extends String, ? extends Serializable> arg0) {
		throw new UnsupportedOperationException();
	}

	/** Non implémenté. */
	@Override
	public String remove(final Object arg0) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc} */
	@Override
	public int size() {
		return camel2ConstIndex.size();
	}

	/** Non implémenté. */
	@Override
	public Collection<Serializable> values() {
		throw new UnsupportedOperationException();
	}
}
