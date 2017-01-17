package io.vertigo.struts2.impl.servlet;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import io.vertigo.lang.Assertion;
import io.vertigo.vega.impl.servlet.filter.AbstractHttpServletResponseWrapper;

/**
 * Implémentation de HttpServletResponseWrapper qui fonctionne avec le CompressionServletResponseStream.
 * @author Amy Roh, Dmitri Valdin (Apache Software Foundation)
 */
class AjaxFragmentServletResponseWrapper extends AbstractHttpServletResponseWrapper {
	private final Set<String> fragmentIds;

	/**
	 * Constructeur qui crée un adapteur de ServletResponse wrappant la response spécifiée.
	 * @param response javax.servlet.HttpServletResponse
	 */
	AjaxFragmentServletResponseWrapper(final HttpServletResponse response, final Set<String> fragmentIds) {
		super(response);
		this.fragmentIds = fragmentIds;
	}

	/**
	 * Crée et retourne un ServletOutputStream pour écrire le contenu dans la response associée.
	 * @return javax.servlet.ServletOutputStream
	 */
	@Override
	public ServletOutputStream createOutputStream() {
		return new AjaxFragmentResponseStream();
	}

	/**
	 * Termine et ferme la response.
	 */
	public void finishResponse() {
		try {
			final StringBuilder result = new StringBuilder();
			final String temp = getStream().toString();
			for (final String fragmentId : fragmentIds) {
				final Pattern pattern = Pattern.compile("<([a-z]+)\\s[^>]*id=['\"]" + fragmentId.substring(1) + "['\"][^>]*>");
				final Matcher matcher = pattern.matcher(temp);
				if (matcher.find()) {
					final int start = matcher.start();
					final String tag = matcher.group(1);
					final int end = findEndTag(temp, start, tag, 0);
					result.append(temp.substring(start, end + 3 + tag.length()));
				} else {
					throw new IllegalArgumentException("Can't find " + fragmentId + " in \n" + temp);
				}
			}
			getResponse().getWriter().print(result.toString());
			close();
		} catch (final IOException e) {
			Logger.getRootLogger().trace(e.getMessage(), e);
		}
	}

	private int findEndTag(final String temp, final int from, final String tag, final int deep) {
		int end = temp.indexOf("</" + tag + ">", from);
		Assertion.checkArgument(end > 0, "Cant find en tag {0} after position {1}", "</" + tag + ">", from);
		final int innerStart = temp.indexOf("<" + tag, from + 1 + tag.length());
		if (innerStart != -1 && innerStart < end) {
			final int innerEnd = findEndTag(temp, innerStart, tag, deep + 1);
			end = findEndTag(temp, innerEnd + 3 + tag.length(), tag, deep);
			Assertion.checkArgument(end > 0, "Cant find en tag {0} after position {1} (deep", "</" + tag + ">", from);
		}
		return end;
	}

	/**
	 * Ne fait rien
	 * @param length int
	 */
	@Override
	public void setContentLength(final int length) {
		// ne fait rien
	}
}
