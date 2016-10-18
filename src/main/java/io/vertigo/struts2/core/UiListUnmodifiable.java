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

import java.util.stream.Collectors;

import io.vertigo.dynamo.domain.model.DtList;
import io.vertigo.dynamo.domain.model.DtObject;

/**
 * Wrapper d'affichage des listes d'objets métier.
 * @author npiedeloup
 * @param <O> the type of entity
 */
public final class UiListUnmodifiable<O extends DtObject> extends AbstractUiList<O> implements UiList<O> {
	private static final long serialVersionUID = 5475819598230056558L;

	private final DtList<O> dtList;

	/**
	 * Constructeur.
	 * @param dtList Liste à encapsuler
	 */
	public UiListUnmodifiable(final DtList<O> dtList) {
		super(dtList.getDefinition());
		//-----
		this.dtList = dtList;
		if (dtList.size() < 1000) {
			initUiObjectByIdIndex();
		}
	}

	// ==========================================================================

	/** {@inheritDoc} */
	@Override
	protected DtList<O> obtainDtList() {
		return dtList;
	}

	/**
	 * Vérifie les UiObjects de la liste, met à jour les objets métiers et retourne la liste.
	 * @param validator Validateur à utilisé, peut-être spécifique à l'objet.
	 * @param uiMessageStack Pile des messages qui sera mise à jour
	 * @return Liste métier validée.
	 */
	@Override
	public DtList<O> validate(final UiObjectValidator<O> validator, final UiMessageStack uiMessageStack) {
		check(validator, uiMessageStack);
		return flush();
	}

	/**
	 * Vérifie les UiObjects de la liste et remplis la pile d'erreur.
	 * @param validator Validateur à utilisé
	 * @param uiMessageStack Pile des messages qui sera mise à jour
	 */
	@Override
	public void check(final UiObjectValidator<O> validator, final UiMessageStack uiMessageStack) {
		//1. check Error => KUserException
		//on valide les éléments internes
		getUiObjectBuffer()
				.stream()
				.forEach(uiObject -> uiObject.check(validator, uiMessageStack));
	}

	/**
	 * @return met à jour les objets métiers et retourne la liste.
	 */
	@Override
	public DtList<O> flush() {
		//1. check Error => KUserException
		//on valide les éléments internes
		getUiObjectBuffer()
				.stream()
				.forEach(uiObject -> uiObject.flush());

		clearUiObjectBuffer(); //on purge le buffer
		return dtList;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return dtList
				.stream()
				.limit(50) //we consider only the first 50 elements
				.map(dto -> dto.toString())
				.collect(Collectors.joining("; ",
						"uiList(" + dtList.size() + " element(s) :",
						")"));
	}
}
