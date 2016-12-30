package io.vertigo.struts2.dao.users;

import javax.inject.Inject;

import io.vertigo.dynamo.impl.store.util.DAO;
import io.vertigo.dynamo.store.StoreManager;
import io.vertigo.dynamo.store.StoreServices;
import io.vertigo.dynamo.task.TaskManager;
import io.vertigo.struts2.domain.users.UserAuthentification;

/**
 * DAO : Accès à un object (DTO, DTC).
 * UserAuthentificationDAO
 */
public final class UserAuthentificationDAO extends DAO<UserAuthentification, java.lang.Long> implements StoreServices {

	/**
	 * Contructeur.
	 * @param storeManager Manager de persistance
	 * @param taskManager Manager de Task
	 */
	@Inject
	public UserAuthentificationDAO(final StoreManager storeManager, final TaskManager taskManager) {
		super(UserAuthentification.class, storeManager, taskManager);
	}

}
