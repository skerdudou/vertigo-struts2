package io.vertigo.struts2.dao.users;

import javax.inject.Inject;

import io.vertigo.dynamo.impl.store.util.DAO;
import io.vertigo.dynamo.store.StoreManager;
import io.vertigo.dynamo.store.StoreServices;
import io.vertigo.dynamo.task.TaskManager;
import io.vertigo.struts2.domain.users.ApplicationUser;

/**
 * DAO : Accès à un object (DTO, DTC).
 * ApplicationUserDAO
 */
public final class ApplicationUserDAO extends DAO<ApplicationUser, java.lang.Long> implements StoreServices {

	/**
	 * Contructeur.
	 * @param storeManager Manager de persistance
	 * @param taskManager Manager de Task
	 */
	@Inject
	public ApplicationUserDAO(final StoreManager storeManager, final TaskManager taskManager) {
		super(ApplicationUser.class, storeManager, taskManager);
	}

}
