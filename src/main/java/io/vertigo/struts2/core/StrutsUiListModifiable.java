package io.vertigo.struts2.core;

import io.vertigo.dynamo.domain.model.DtList;
import io.vertigo.dynamo.domain.model.DtObject;
import io.vertigo.vega.engines.webservice.json.AbstractUiListModifiable;
import io.vertigo.vega.webservice.model.UiObject;

public class StrutsUiListModifiable<D extends DtObject> extends AbstractUiListModifiable<D> {

	private static final long serialVersionUID = -6612061761970992295L;

	StrutsUiListModifiable(final DtList<D> dtList, final String inputKey) {
		super(dtList, inputKey);

	}

	/* (non-Javadoc)
	 * @see io.vertigo.struts2.core.AbstractUiListModifiable#createUiObject(io.vertigo.dynamo.domain.model.DtObject)
	 */
	@Override
	protected UiObject<D> createUiObject(final D dto) {
		return new StrutsUiObject<>(dto);
	}

}
