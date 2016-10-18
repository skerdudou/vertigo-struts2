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

import io.vertigo.dynamo.domain.model.DtList;
import io.vertigo.dynamo.domain.model.DtListURIForMasterData;
import io.vertigo.dynamo.domain.model.Entity;
import io.vertigo.dynamo.transaction.VTransactionWritable;
import io.vertigo.lang.Assertion;

/**
 * Wrapper d'affichage des listes d'objets métier.
 * @author npiedeloup
 * @param <E> the type of entity
 */
final class UiMdList<E extends Entity> extends AbstractUiList<E> implements UiList<E> {
	private static final long serialVersionUID = 5475819598230056558L;

	private final DtListURIForMasterData dtListURIForMasterData;
	private transient DtList<E> lazyDtList;

	/**
	 * Constructeur.
	 *
	 * @param dtListUri Uri de la Liste à encapsuler
	 */
	public UiMdList(final DtListURIForMasterData dtListURIForMasterData) {
		super(dtListURIForMasterData.getDtDefinition());
		Assertion.checkArgument(storeManager.get().getMasterDataConfig().containsMasterData(dtListURIForMasterData.getDtDefinition()), "UiMdList can't be use with {0}, it's not a MasterDataList.",
				dtListURIForMasterData.getDtDefinition().getName());
		// -------------------------------------------------------------------------
		this.dtListURIForMasterData = dtListURIForMasterData;
	}

	// ==========================================================================

	/**
	 * @return Liste des données
	 */
	@Override
	public DtList<E> obtainDtList() {
		if (lazyDtList == null) {
			try (final VTransactionWritable transaction = transactionManager.get().createCurrentTransaction()) {
				lazyDtList = storeManager.get().getDataStore().<E> findAll(dtListURIForMasterData);
			}
		}
		return lazyDtList;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "uiMdList(" + dtListURIForMasterData.toString() + (lazyDtList != null ? ", loaded:" + lazyDtList.size() : "") + " )";
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object o) {
		//on surcharge equals pour éviter un appel à super.equals non désiré et qui forcerai le chargement de la liste
		if (o instanceof UiMdList) {
			return dtListURIForMasterData.equals(((UiMdList<?>) o).dtListURIForMasterData);
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		//on surcharge hashCode pour eviter un appel à super.hashCode non désiré et qui forcerai le chargement de la liste
		return dtListURIForMasterData.hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public DtList<E> validate(final UiObjectValidator<E> validator, final UiMessageStack uiMessageStack) {
		return obtainDtList();
	}

	/** {@inheritDoc} */
	@Override
	public void check(final UiObjectValidator<E> validator, final UiMessageStack uiMessageStack) {
		//rien
	}

	/** {@inheritDoc} */
	@Override
	public DtList<E> flush() {
		return obtainDtList();
	}
}
