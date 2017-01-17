package io.vertigo.struts2.impl.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.vertigo.vega.impl.servlet.filter.AbstractFilter;

public final class AjaxFragmentFilter extends AbstractFilter {

	/** {@inheritDoc} */
	@Override
	public void doInit() {
		//
	}

	@Override
	public void doMyFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
		if (!(req instanceof HttpServletRequest) || !(res instanceof HttpServletResponse)) {
			chain.doFilter(req, res);
			return;
		}

		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;
		final Set<String> headers = new HashSet<>(Collections.list(request.getHeaderNames()));
		if (headers.contains("x-request-target")) {
			final Set<String> fragmentIds = new HashSet<>(Collections.list(request.getHeaders("x-request-target")));
			final AjaxFragmentServletResponseWrapper wrappedResponse = new AjaxFragmentServletResponseWrapper(response, fragmentIds);
			try {
				chain.doFilter(request, wrappedResponse);
			} finally {
				wrappedResponse.finishResponse();
			}
		} else {
			chain.doFilter(request, response);
		}
	}
}
