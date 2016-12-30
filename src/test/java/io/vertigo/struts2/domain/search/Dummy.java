package io.vertigo.struts2.domain.search;

import io.vertigo.dynamo.domain.model.DtObject;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;

/**
 * Attention cette classe est générée automatiquement !
 * Objet de données Dummy
 */
public final class Dummy implements DtObject {

	/** SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	private Long dummyLong;

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Id'.
	 * @return Long dummyLong
	 */
	@Field(domain = "DO_ID", label = "Id")
	public Long getDummyLong() {
		return dummyLong;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Id'.
	 * @param dummyLong Long
	 */
	public void setDummyLong(final Long dummyLong) {
		this.dummyLong = dummyLong;
	}

	//Aucune Association déclarée

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
