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
import io.vertigo.dynamo.domain.model.DtObject;
import io.vertigo.lang.Assertion;

/**
 * Liste des couples (clé, object) enregistrés.
 * @author npiedeloup
 * @param <O> Type d'objet
 */
public final class ContextListModifiable<O extends DtObject> {
	private final AbstractActionSupport action;
	private final UiMessageStack uiMessageStack;
	private final String contextKey;
	private final UiObjectValidator validator;

	//	public static <O extends DtObject> ContextForm<O> create(final String contextKey, final AbstractActionSupport action) {
	//		return new ContextForm<O>(contextKey, action);
	//	}

	/**
	 * Constructeur.
	 * @param contextKey Clé dans le context
	 * @param action Action struts
	 */
	public ContextListModifiable(final String contextKey, final AbstractActionSupport action) {
		this(contextKey, new UiObjectValidator(), action);
	}

	/**
	 * Constructeur.
	 * @param contextKey Clé dans le context
	 * @param validator Validator a utiliser
	 * @param action Action struts
	 */
	public ContextListModifiable(final String contextKey, final UiObjectValidator validator, final AbstractActionSupport action) {
		Assertion.checkArgNotEmpty(contextKey);
		Assertion.checkNotNull(action);
		Assertion.checkNotNull(validator);
		//-----
		this.contextKey = contextKey;
		this.action = action;
		this.uiMessageStack = action.getUiMessageStack();
		this.validator = validator;
	}

	/**
	 * Ajoute une liste au context.
	 * @param dtList List à publier
	 */
	public void publish(final DtList<O> dtList) {
		action.getModel().put(contextKey, new UiListModifiable<>(dtList));
	}

	/**
	 * Vérifie les erreurs de la liste. Celles-ci sont ajoutées à l'uiMessageStack si nécessaire.
	 */
	public void checkErrors() {
		action.getModel().getUiList(contextKey).check(validator, uiMessageStack);
	}

	/**
	 * @return List des objets métiers validée. Lance une exception si erreur.
	 */
	public DtList<O> readDtList() {
		return action.getModel().<O> getUiList(contextKey).validate(validator, uiMessageStack);
	}

	/**
	 * @return List des objets d'IHM. Peut contenir des erreurs.
	 */
	public UiListModifiable<O> getUiListModifiable() {
		return action.getModel().getUiListModifiable(contextKey);
	}
}
