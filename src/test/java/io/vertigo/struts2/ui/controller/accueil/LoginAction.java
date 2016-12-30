package io.vertigo.struts2.ui.controller.accueil;

import javax.inject.Inject;

import io.vertigo.struts2.core.ContextForm;
import io.vertigo.struts2.core.UiRequestUtil;
import io.vertigo.struts2.domain.users.UserAuthentification;
import io.vertigo.struts2.services.users.UserServices;
import io.vertigo.struts2.ui.controller.AbstractTestActionSupport;

/**
 * @author npiedeloup
 */
public final class LoginAction extends AbstractTestActionSupport {
	private static final long serialVersionUID = 3517185648660870776L;

	@Inject
	private UserServices userServices;

	private final ContextForm<UserAuthentification> utilisateurLoginRef = new ContextForm<>("utilisateur", this);

	/** {@inheritDoc} */
	@Override
	public void initContext() {
		final UserAuthentification utilisateurLogin = new UserAuthentification();
		utilisateurLogin.setLogin("test");
		utilisateurLoginRef.publish(utilisateurLogin);
		toModeEdit();
	}

	/**
	 * Connexion.
	 * @return outcome du login
	 */
	public String doLogin() {
		final UserAuthentification userAuthentification = utilisateurLoginRef.readDto();
		userServices.loginUser(userAuthentification.getLogin(), userAuthentification.getPassword());
		return SUCCESS; //success va sur accueil
	}

	/**
	 * Déconnexion.
	 * @return outcome du logout
	 */
	public String doLogout() {
		UiRequestUtil.invalidateHttpSession();
		return "reload"; // reload la page
	}

	/** {@inheritDoc} */
	@Override
	public String getPageName() {
		return "Page de connexion";
	}
}
