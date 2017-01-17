package io.vertigo.struts2.impl.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

/**
 * Implémentation de ServletOutputStream qui fonctionne avec le AjaxFragmentResponseStream.
 * @author npiedeloup
 */
class AjaxFragmentResponseStream extends ServletOutputStream {
	private final OutputStream stream;

	/**
	 * Construit un servlet output stream associé avec la réponse spécifiée.
	 * @param response javax.servlet.http.HttpServletResponse
	 * @param compressionThreshold int
	 */
	AjaxFragmentResponseStream() {
		super();
		stream = new ByteArrayOutputStream();
	}

	/**
	 * Ferme cet output stream (et flushe les données bufferisées).
	 * @throws java.io.IOException   Erreur d'entrée/sortie
	 */
	@Override
	public void close() throws IOException {
		stream.close();
	}

	/**
	 * Flushe les données bufferisées de cet output stream.
	 * @throws java.io.IOException   Erreur d'entrée/sortie
	 */
	@Override
	public void flush() throws IOException {
		stream.flush();
	}

	/**
	 * Ecrit l'octet spécifié dans l'output stream
	 * @param i int
	 * @throws java.io.IOException   Erreur d'entrée/sortie
	 */
	@Override
	public void write(final int i) throws IOException {
		stream.write(i);
	}

	/**
	 * Ecrit les octets spécifiés dans l'output stream.
	 * @param bytes bytes[]
	 * @throws java.io.IOException   Erreur d'entrée/sortie
	 */
	@Override
	public void write(final byte[] bytes) throws IOException {
		write(bytes, 0, bytes.length);
	}

	/**
	 * Ecrit <code>len</code> octets du tableau d'octets spécifiés, en commençant à la position spécifiée,
	 * dans l'output stream.
	 * @param bytes bytes[]
	 * @param off int
	 * @param len int
	 * @throws java.io.IOException   Erreur d'entrée/sortie
	 */
	@Override
	public void write(final byte[] bytes, final int off, final int len) throws IOException {
		if (len == 0) {
			return;
		}
		// write the content to the buffer
		stream.write(bytes, off, len);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isReady() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public void setWriteListener(final WriteListener writeListener) {
		throw new UnsupportedOperationException("Can't setWriteListener on this ResponseStream");
	}

	/**
	 * Get the contents of the outputStream.
	 * @return contents of the outputStream
	 */
	@Override
	public String toString() {
		return stream.toString();
	}
}
