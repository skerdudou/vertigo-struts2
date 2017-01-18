package io.vertigo.struts2.impl.servlet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import io.vertigo.vega.impl.servlet.filter.AbstractHttpServletResponseWrapper;

/**
 * Impl of HttpServletResponseWrapper to intercept and return content as String.
 * @author npiedeloup
 */
class CropHtmlServletResponseWrapper extends AbstractHttpServletResponseWrapper implements AutoCloseable {

	/**
	 * Constructeur qui crée un adapteur de ServletResponse wrappant la response spécifiée.
	 * @param response javax.servlet.HttpServletResponse
	 */
	CropHtmlServletResponseWrapper(final HttpServletResponse response) {
		super(response);
	}

	/**
	 * Crée et retourne un ServletOutputStream pour écrire le contenu dans la response associée.
	 * @return javax.servlet.ServletOutputStream
	 */
	@Override
	public ServletOutputStream createOutputStream() {
		return new CropHtmlResponseStream();
	}

	/**
	 * Get the contents of the outputStream.
	 * @return contents of the outputStream
	 */
	public String getAsString() {
		return getStream().toString();
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
