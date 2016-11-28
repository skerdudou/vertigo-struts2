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
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.vertigo.core.spaces.definiton.DefinitionReference;
import io.vertigo.dynamo.domain.metamodel.DtDefinition;
import io.vertigo.dynamo.domain.model.DtList;
import io.vertigo.dynamo.domain.model.DtObject;
import io.vertigo.lang.Assertion;
import io.vertigo.vega.webservice.model.UiObject;
import io.vertigo.vega.webservice.validation.DtObjectValidator;
import io.vertigo.vega.webservice.validation.UiMessageStack;

/**
 * Version modifiable des UiList.
 * @author npiedeloup
 * @param <D> Type d'objet
 */
public final class UiListModifiable<D extends DtObject> extends AbstractList<UiObject<D>> implements UiList<D>, Serializable {

	private static final long serialVersionUID = -8398542301760300787L;
	private final DefinitionReference<DtDefinition> dtDefinitionRef;

	private final String inputKey;

	// Index
	private final Map<StrutsUiObject<D>, D> dtoByUiObject = new HashMap<>();

	// Buffer
	private final List<StrutsUiObject<D>> removedUiObjects = new ArrayList<>();
	private final List<StrutsUiObject<D>> addedUiObjects = new ArrayList<>();
	private final List<StrutsUiObject<D>> bufferUiObjects;

	// Data
	private final DtList<D> removedDtObjects;
	private final DtList<D> addedDtObjects;
	private final DtList<D> modifiedDtObjects;
	private final DtList<D> dtList;

	/**
	 * Constructor.
	 * @param dtList Inner DtList
	 */
	UiListModifiable(final DtList<D> dtList, final String inputKey) {
		Assertion.checkNotNull(dtList);
		//-----
		this.dtList = dtList;
		this.inputKey = inputKey;
		final DtDefinition dtDefinition = dtList.getDefinition();
		dtDefinitionRef = new DefinitionReference<>(dtDefinition);

		this.removedDtObjects = new DtList<>(dtDefinition);
		this.addedDtObjects = new DtList<>(dtDefinition);
		this.modifiedDtObjects = new DtList<>(dtDefinition);

		this.bufferUiObjects = new ArrayList<>(dtList.size());
		rebuildBuffer();
	}

	private void rebuildBuffer() {
		dtoByUiObject.clear();
		bufferUiObjects.clear();
		for (int row = 0; row < dtList.size(); row++) {
			final D dto = dtList.get(row);
			final StrutsUiObject<D> uiObjects = new StrutsUiObject<>(dto);
			bufferUiObjects.add(uiObjects);
			dtoByUiObject.put(uiObjects, dto);
		}
	}

	/**
	 * @return DtDefinition de l'objet métier
	 */
	@Override
	public DtDefinition getDtDefinition() {
		return dtDefinitionRef.get();
	}

	/**
	 * @param dto Element to removed
	 * @return If element was removed
	 */
	public boolean remove(final StrutsUiObject<D> dto) {
		final boolean result = bufferUiObjects.remove(dto);
		if (result) {
			if (addedUiObjects.contains(dto)) {
				//Si on supprime (remove) un objet déjà ajouté (add),
				//alors il suffit de l'enlever de la liste des éléments ajoutés.
				addedUiObjects.remove(dto);
			} else {
				//Sinon on l'ajoute à la liste des éléments supprimés.
				removedUiObjects.add(dto);
			}
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public StrutsUiObject<D> remove(final int index) {
		final StrutsUiObject<D> dto = get(index);
		final boolean result = remove(dto);
		Assertion.checkState(result, "Erreur de suppression i={0}", index);
		return dto;
	}

	/**
	 * @param dto Element to add
	 * @return true (as specified by Collection.add)
	 */
	public boolean add(final D dto) {
		final StrutsUiObject<D> uiObject = new StrutsUiObject<>(dto);
		final boolean result = bufferUiObjects.add(uiObject);
		if (result) {
			if (removedUiObjects.contains(uiObject)) {
				//Si on ajoute (add) un objet précédemment supprimé (add),
				//alors il suffit de l'enlever de la liste des éléments supprimés.
				removedUiObjects.remove(uiObject);
			} else {
				addedUiObjects.add(uiObject);
			}
		}
		return result;
	}

	/**
	 * @return List des objets supprimés
	 */
	public DtList<D> getRemovedList() {
		Assertion.checkState(removedUiObjects.isEmpty(), "La UiList doit être validé, pour avoir la liste des éléments supprimés.");
		//-----
		return removedDtObjects;
	}

	/**
	 * @return List des objets ajoutés
	 */
	public DtList<D> getAddedList() {
		Assertion.checkState(removedUiObjects.isEmpty(), "La UiList doit être validé, pour avoir la liste des éléments ajoutés.");
		//-----
		return addedDtObjects;
	}

	/**
	 * @return List des objets modifiés
	 */
	public DtList<D> getModifiedList() {
		Assertion.checkState(removedUiObjects.isEmpty(), "La UiList doit être validé, pour avoir la liste des éléments ajoutés.");
		//-----
		return modifiedDtObjects;
	}

	/** {@inheritDoc} */
	@Override
	public StrutsUiObject<D> get(final int row) {
		//id>=0 : par index dans la UiList (pour boucle, uniquement dans la même request)
		Assertion.checkState(row >= 0, "Le getteur utilisé n'est pas le bon: utiliser getByRowId");
		final StrutsUiObject<D> uiObject = bufferUiObjects.get(row);
		Assertion.checkNotNull(uiObject);
		return uiObject;
	}

	/** {@inheritDoc} */
	@Override
	public int indexOf(final Object o) {
		if (o instanceof DtObject) {
			return indexOf((DtObject) o);
		} else if (o instanceof StrutsUiObject) {
			return indexOf((StrutsUiObject<D>) o);
		}
		return super.indexOf(o);
	}

	/**
	 * @param uiObject UiObject recherché
	 * @return index de l'objet dans la liste
	 */
	private int indexOf(final StrutsUiObject<D> uiObject) {
		Assertion.checkNotNull(uiObject);
		//-----
		return bufferUiObjects.indexOf(uiObject);
	}

	/**
	 * @param dtObject DtObject recherché
	 * @return index de l'objet dans la liste
	 */
	private int indexOf(final DtObject dtObject) {
		Assertion.checkNotNull(dtObject);
		//-----
		for (int i = 0; i < bufferUiObjects.size(); i++) {
			if (bufferUiObjects.get(i).getServerSideObject().equals(dtObject)) {
				return i;
			}
		}
		return -1;
	}

	/** {@inheritDoc} */
	@Override
	public int size() {
		return bufferUiObjects.size();
	}

	/** {@inheritDoc} */
	@Override
	public void checkFormat(final UiMessageStack uiMessageStack) {
		//1. check Error => KUserException
		//on valide les éléments internes
		for (final StrutsUiObject<D> uiObject : bufferUiObjects) {
			uiObject.setInputKey(inputKey + ".get(" + indexOf(uiObject) + ")");
			uiObject.checkFormat(uiMessageStack);
		}
	}

	/** {@inheritDoc} */
	@Override
	public DtList<D> mergeAndCheckInput(final DtObjectValidator<D> validator, final UiMessageStack uiMessageStack) {
		checkFormat(uiMessageStack);
		removedDtObjects.clear();
		addedDtObjects.clear();
		modifiedDtObjects.clear();

		//1. check Error => KUserException
		//on valide les éléments internes
		for (final UiObject<D> uiObject : bufferUiObjects) {
			if (uiObject.isModified()) {
				final D validatedDto = uiObject.mergeAndCheckInput(Collections.singletonList(validator), uiMessageStack);
				if (!addedUiObjects.contains(uiObject)) {
					modifiedDtObjects.add(validatedDto);
				} else {
					addedDtObjects.add(validatedDto);
				}
			}
		}

		//2. Opérations
		for (final StrutsUiObject<D> uiObject : removedUiObjects) {
			final D dto = dtoByUiObject.get(uiObject);
			if (dto != null) {//on ne garde que les dto qui ETAIENT dans la dtc
				removedDtObjects.add(dto);
				//on ne supprime pas tout de suite de la dtc, car cela invalidera les index de originIndexByUiObject
			}
		}

		//on vérifie avant s'il y a des elements pour le cas des listes non modifiable
		//il faudrait plutot que la DtListInput soit non modifiable aussi
		if (!addedDtObjects.isEmpty()) {
			dtList.addAll(addedDtObjects);
		}
		if (!removedDtObjects.isEmpty()) {
			dtList.removeAll(removedDtObjects);
		}
		//-----
		Assertion.checkState(bufferUiObjects.size() == dtList.size(), "bufferList.size() <> dtList.size() : mauvaise synchronisation dtList / bufferList");

		//3. On reconstruit buffer et indexes
		removedUiObjects.clear();//on purge le buffer
		addedUiObjects.clear(); //on purge le buffer
		rebuildBuffer();
		return dtList;
	}

	/** {@inheritDoc} */
	@Override
	public Iterator<UiObject<D>> iterator() {
		return new UiListModifiableIterator();
	}

	/** innerclass, volontairement non static */
	class UiListModifiableIterator implements Iterator<UiObject<D>> {
		private final int expectedSize;
		private int currentIndex; //init a 0

		/**
		 * Constructor.
		 */
		UiListModifiableIterator() {
			expectedSize = size();
		}

		/** {@inheritDoc} */
		@Override
		public boolean hasNext() {
			checkForComodification();
			return currentIndex < size();
		}

		/** {@inheritDoc} */
		@Override
		public StrutsUiObject<D> next() {
			checkForComodification();
			final StrutsUiObject<D> next = get(currentIndex);
			currentIndex++;
			return next;
		}

		/** {@inheritDoc} */
		@Override
		public void remove() {
			UiListModifiable.this.remove(get(currentIndex - 1));
		}

		private void checkForComodification() {
			if (expectedSize != size()) {
				throw new ConcurrentModificationException();
			}
		}
	}

}
