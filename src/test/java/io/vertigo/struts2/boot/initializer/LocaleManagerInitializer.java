package io.vertigo.struts2.boot.initializer;

import javax.inject.Inject;

import io.vertigo.core.locale.LocaleManager;
import io.vertigo.core.spaces.component.ComponentInitializer;

/**
 * Initializer de LocaleManager.
 * @author dchallas
 * @version $Id: LocaleManagerInitializer.java,v 1.4 2014/02/07 16:48:27 npiedeloup Exp $
 */
public final class LocaleManagerInitializer implements ComponentInitializer {

	@Inject
	private LocaleManager localeManager;

	/** {@inheritDoc} */
	@Override
	public void init() {
		localeManager.add("io.vertigo.dynamox.domain.constraint.Constraint", io.vertigo.dynamox.domain.constraint.Resources.values());
		localeManager.add("io.vertigo.dynamox.domain.formatter.Formatter", io.vertigo.dynamox.domain.formatter.Resources.values());

		// Messages Ui vertigo
		localeManager.add("io.vertigo.struts2.resources.Resources", io.vertigo.struts2.resources.Resources.values());

		// Messages Demo
		//localeManager.add(io.vertigo.struts2.ui.util.Resources.class.getName(), io.vertigo.struts2.ui.util.Resources.values());
	}
}
