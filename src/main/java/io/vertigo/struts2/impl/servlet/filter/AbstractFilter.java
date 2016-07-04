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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import io.vertigo.lang.Option;

/**
 * @author npiedeloup
 */
public abstract class AbstractFilter implements Filter {
	/** Filter parameter name for exclude some url. */
	static final String EXCLUDE_PATTERN_PARAM_NAME = "url-exclude-pattern";
	private FilterConfig config;
	private Option<Pattern> pattern;

	/** {@inheritDoc} */
	@Override
	public final void init(final FilterConfig filterConfig) {
		config = filterConfig;
		pattern = parsePattern(config.getInitParameter(EXCLUDE_PATTERN_PARAM_NAME));
		doInit();
	}

	/** {@inheritDoc} */
	@Override
	public final void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
		if (isUrlMatch(req, pattern)) {
			chain.doFilter(req, res);
			return;
		}
		doMyFilter(req, res, chain);
	}

	/**
	 * @param urlExcludePattern Chaine d'exclusion du filtre à traduire en regExp.
	 * @return Pattern compilé
	 */
	protected static final Option<Pattern> parsePattern(final String urlExcludePattern) {
		if (urlExcludePattern != null) {
			String urlExcludePatternParamNormalized = urlExcludePattern.replaceAll("\\.", "\\\\."); // . devient \\. (pour matcher un .)
			urlExcludePatternParamNormalized = urlExcludePatternParamNormalized.replaceAll("\\*([^;])", "[^\\/]*$1"); //* en milieu de pattern devient tous char sauf /
			urlExcludePatternParamNormalized = urlExcludePatternParamNormalized.replaceAll("\\*(;|$)", ".*$1"); //* en fin de pattern devient tous char
			urlExcludePatternParamNormalized = urlExcludePatternParamNormalized.replaceAll(";", ")|(^"); //; devient un OR
			urlExcludePatternParamNormalized = "(^" + urlExcludePatternParamNormalized + ")";
			return Option.of(Pattern.compile(urlExcludePatternParamNormalized));
		}
		return Option.empty();
	}

	/**
	 * Test si l'url correspond au pattern.
	 * @param req Request
	 * @param pattern Pattern de test
	 * @return si l'url match le pattern, ou false si pas de pattern ou si pas httprequest.
	 */
	protected static final boolean isUrlMatch(final ServletRequest req, final Option<Pattern> pattern) {
		if (pattern.isPresent() && req instanceof HttpServletRequest) {
			final HttpServletRequest httpRequest = (HttpServletRequest) req;
			return isUrlMatch(httpRequest.getContextPath(), httpRequest.getRequestURI(), pattern.get());
		}
		return false;
	}

	/**
	 * Test si l'url (hors domain et context) correspond au pattern.
	 * @param context Context de la webapp
	 * @param requestUri uri complete de la request
	 * @param pattern Pattern de test
	 * @return si l'url match le pattern, ou false si pas de pattern ou si pas httprequest.
	 */
	protected static final boolean isUrlMatch(final String context, final String requestUri, final Pattern pattern) {
		String url = requestUri.substring(requestUri.indexOf(context) + context.length());
		if (url.contains(";")) { //pour les ;jsessionid qui ne doivent pas etre pris en compte par les patterns
			url = url.substring(0, url.indexOf(";"));
		}
		final Matcher matcher = pattern.matcher(url);
		return matcher.matches();
	}

	protected final FilterConfig getFilterConfig() {
		return config;
	}

	protected abstract void doInit();

	protected abstract void doMyFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException;

	/** {@inheritDoc} */
	@Override
	public final void destroy() {
		config = null;
	}
}
