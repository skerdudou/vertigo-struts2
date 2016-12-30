package io.vertigo.struts2.domain.users;

import io.vertigo.dynamo.domain.model.Entity;
import io.vertigo.dynamo.domain.model.URI;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;

/**
 * Attention cette classe est générée automatiquement !
 * Objet de données SecurityRole
 */
public final class SecurityRole implements Entity {

	/** SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	private String sroCd;
	private String label;

	/** {@inheritDoc} */
	@Override
	public URI<SecurityRole> getURI() {
		return DtObjectUtil.createURI(this);
	}

	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'SRO_CD'.
	 * @return String sroCd <b>Obligatoire</b>
	 */
	@Field(domain = "DO_CODE", type = "ID", required = true, label = "SRO_CD")
	public String getSroCd() {
		return sroCd;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'SRO_CD'.
	 * @param sroCd String <b>Obligatoire</b>
	 */
	public void setSroCd(final String sroCd) {
		this.sroCd = sroCd;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Label'.
	 * @return String label
	 */
	@Field(domain = "DO_LABEL", label = "Label")
	public String getLabel() {
		return label;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Label'.
	 * @param label String
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	// Association : Profil non navigable

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
