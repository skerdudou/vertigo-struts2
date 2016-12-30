package io.vertigo.struts2.dao.people;

import javax.inject.Inject;

import io.vertigo.dynamo.impl.store.util.DAO;
import io.vertigo.dynamo.store.StoreManager;
import io.vertigo.dynamo.store.StoreServices;
import io.vertigo.dynamo.task.TaskManager;
import io.vertigo.struts2.domain.people.Casting;

/**
 * DAO : Accès à un object (DTO, DTC).
 * CastingDAO
 */
public final class CastingDAO extends DAO<Casting, java.lang.Long> implements StoreServices {

	/**
	 * Contructeur.
	 * @param storeManager Manager de persistance
	 * @param taskManager Manager de Task
	 */
	@Inject
	public CastingDAO(final StoreManager storeManager, final TaskManager taskManager) {
		super(Casting.class, storeManager, taskManager);
	}

}
