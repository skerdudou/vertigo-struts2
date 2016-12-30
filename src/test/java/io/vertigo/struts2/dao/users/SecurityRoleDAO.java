package io.vertigo.struts2.dao.users;

import javax.inject.Inject;

import io.vertigo.dynamo.impl.store.util.DAO;
import io.vertigo.dynamo.store.StoreManager;
import io.vertigo.dynamo.store.StoreServices;
import io.vertigo.dynamo.task.TaskManager;
import io.vertigo.struts2.domain.users.SecurityRole;

/**
 * DAO : Accès à un object (DTO, DTC).
 * SecurityRoleDAO
 */
public final class SecurityRoleDAO extends DAO<SecurityRole, java.lang.String> implements StoreServices {

	/**
	 * Contructeur.
	 * @param storeManager Manager de persistance
	 * @param taskManager Manager de Task
	 */
	@Inject
	public SecurityRoleDAO(final StoreManager storeManager, final TaskManager taskManager) {
		super(SecurityRole.class, storeManager, taskManager);
	}

}
