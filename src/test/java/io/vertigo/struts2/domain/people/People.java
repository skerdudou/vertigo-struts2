package io.vertigo.struts2.domain.people;

import io.vertigo.dynamo.domain.model.KeyConcept;
import io.vertigo.dynamo.domain.model.URI;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;

/**
 * Attention cette classe est générée automatiquement !
 * Objet de données People
 */
public final class People implements KeyConcept {

	/** SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	private Long peoId;
	private String lastName;
	private String firstName;
	private String peoName;
	private String imdbid;

	/** {@inheritDoc} */
	@Override
	public URI<People> getURI() {
		return DtObjectUtil.createURI(this);
	}

	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'PEO_ID'.
	 * @return Long peoId <b>Obligatoire</b>
	 */
	@Field(domain = "DO_ID", type = "ID", required = true, label = "PEO_ID")
	public Long getPeoId() {
		return peoId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'PEO_ID'.
	 * @param peoId Long <b>Obligatoire</b>
	 */
	public void setPeoId(final Long peoId) {
		this.peoId = peoId;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Last Name'.
	 * @return String lastName
	 */
	@Field(domain = "DO_NAME", label = "Last Name")
	public String getLastName() {
		return lastName;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Last Name'.
	 * @param lastName String
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'First Name'.
	 * @return String firstName
	 */
	@Field(domain = "DO_FIRSTNAME", label = "First Name")
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'First Name'.
	 * @param firstName String
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Peo Name'.
	 * @return String peoName
	 */
	@Field(domain = "DO_LABEL_LONG", label = "Peo Name")
	public String getPeoName() {
		return peoName;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Peo Name'.
	 * @param peoName String
	 */
	public void setPeoName(final String peoName) {
		this.peoName = peoName;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'imdbID'.
	 * @return String imdbid
	 */
	@Field(domain = "DO_LABEL", label = "imdbID")
	public String getImdbid() {
		return imdbid;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'imdbID'.
	 * @param imdbid String
	 */
	public void setImdbid(final String imdbid) {
		this.imdbid = imdbid;
	}

	// Association : Casting non navigable

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
