package io.vertigo.struts2.ui;

import java.util.Locale;

import io.vertigo.persona.security.UserSession;
import io.vertigo.struts2.domain.users.ApplicationUser;

/**
 * Session d'un utilisateur<br>
 * Un utilisateur possède une liste de profils correspondant à des rêgles au sein d'une ou plusieurs entités.<br>
 * On considère que toute session utilisateur créée implique que l'utilisateur est authentifié.
 *
 * @author cgodard
 */
public final class TestUserSession extends UserSession {

	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 2497388902473962429L;

	private ApplicationUser applicationUser;

	/**
	 * @return Default Locale.
	 */
	@Override
	public Locale getLocale() {
		return Locale.FRANCE;
	}

	public void setApplicationUser(final ApplicationUser applicationUser) {
		this.applicationUser = applicationUser;
	}

	public ApplicationUser getApplicationUser() {
		return applicationUser;
	}

}
