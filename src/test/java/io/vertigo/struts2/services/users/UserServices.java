package io.vertigo.struts2.services.users;

import io.vertigo.lang.Component;
import io.vertigo.struts2.domain.users.ApplicationUser;

public interface UserServices extends Component {

	ApplicationUser loginUser(final String login, final String password);

}
