/**
 *
 */
package io.vertigo.struts2.boot.initializer;

import javax.inject.Inject;

import io.vertigo.core.locale.LocaleManager;
import io.vertigo.core.spaces.component.ComponentInitializer;
import io.vertigo.struts2.services.users.UserResources;

/**
 * Init ressources.
 * @author npiedeloup
 */
public class I18nResourcesInitializer implements ComponentInitializer {

	@Inject
	private LocaleManager localeManager;

	/** {@inheritDoc} */
	@Override
	public void init() {
		localeManager.add(UserResources.class.getName(), UserResources.values());
	}

}
