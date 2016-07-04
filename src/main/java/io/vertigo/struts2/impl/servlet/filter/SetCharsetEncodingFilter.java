/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2016, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.struts2.impl.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import io.vertigo.lang.Assertion;

/**
 * Implémentation de javax.servlet.Filter utilisée affecter le charset de la request.
 * Doit-être le premier filter pour être efficace.
 * Le charset utilisé doit-être compatible avec la finalit� de la donnée (typiquement avec la BDD).
 * @author npiedeloup
 */
public final class SetCharsetEncodingFilter implements Filter {
	private String charset;

	/** {@inheritDoc} */
	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		charset = filterConfig.getInitParameter("charset");
		Assertion.checkArgNotEmpty(charset);
	}

	/** {@inheritDoc} */
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding(charset);
		chain.doFilter(request, response);
	}

	/** {@inheritDoc} */
	@Override
	public void destroy() {
		//rien
	}
}
