package io.vertigo.struts2.dao.people;

import javax.inject.Inject;

import io.vertigo.dynamo.domain.model.URI;
import io.vertigo.dynamo.impl.store.util.DAO;
import io.vertigo.dynamo.store.StoreManager;
import io.vertigo.dynamo.store.StoreServices;
import io.vertigo.dynamo.task.TaskManager;
import io.vertigo.struts2.domain.people.People;

/**
 * DAO : Accès à un object (DTO, DTC).
 * PeopleDAO
 */
public final class PeopleDAO extends DAO<People, java.lang.Long> implements StoreServices {

	/**
	 * Contructeur.
	 * @param storeManager Manager de persistance
	 * @param taskManager Manager de Task
	 */
	@Inject
	public PeopleDAO(final StoreManager storeManager, final TaskManager taskManager) {
		super(People.class, storeManager, taskManager);
	}

	/**
	 * Indique que le keyConcept associé à cette uri va être modifié.
	 * Techniquement cela interdit les opérations d'ecriture en concurrence
	 * et envoie un évenement de modification du keyConcept (à la fin de transaction eventuellement)
	 * @param uri URI du keyConcept modifié
	 * @return KeyConcept à modifier
	 */
	public People readOneForUpdate(final URI<People> uri) {
		return dataStore.readOneForUpdate(uri);
	}

	/**
	 * Indique que le keyConcept associé à cet id va être modifié.
	 * Techniquement cela interdit les opérations d'ecriture en concurrence
	 * et envoie un évenement de modification du keyConcept (à la fin de transaction eventuellement)
	 * @param id Clé du keyConcept modifié
	 * @return KeyConcept à modifier
	 */
	public People readOneForUpdate(final java.lang.Long id) {
		return readOneForUpdate(createDtObjectURI(id));
	}

}
