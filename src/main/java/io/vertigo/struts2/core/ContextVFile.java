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
package io.vertigo.struts2.core;

import java.io.File;

import io.vertigo.dynamo.file.model.VFile;
import io.vertigo.dynamo.impl.file.model.FSFile;
import io.vertigo.lang.Assertion;
import io.vertigo.lang.MessageText;
import io.vertigo.lang.VUserException;

/**
 * Liste des couples (clé, object) enregistrés.
 * @author npiedeloup
 */
public final class ContextVFile {
	private static final String VFILE_SUFFIX = "VFile";
	private static final String FILE_NAME_SUFFIX = "FileName";
	private static final String CONTENT_TYPE_SUFFIX = "ContentType";
	private final AbstractActionSupport action;
	private final String contextKeyVFile;
	private final String contextKeyFile;
	private final String contextKeyFileName;
	private final String contextKeyContentType;

	/**
	 * Constructeur.
	 * @param contextKey Clé dans le context
	 * @param action Action struts
	 */
	public ContextVFile(final String contextKey, final AbstractActionSupport action) {
		Assertion.checkArgNotEmpty(contextKey);
		Assertion.checkNotNull(action);
		//-----
		contextKeyVFile = contextKey + VFILE_SUFFIX;
		contextKeyFile = contextKey;
		contextKeyFileName = contextKey + FILE_NAME_SUFFIX;
		contextKeyContentType = contextKey + CONTENT_TYPE_SUFFIX;
		this.action = action;
	}

	/**
	 * @param file Fichier à mettre dans le context
	 */
	public void set(final VFile file) {
		Assertion.checkNotNull(file);
		action.getModel().put(contextKeyVFile, file);
	}

	/**
	 * @return Object du context
	 */
	public VFile get() {
		//TODO Assert !action.getModel().containsKey(contextKeyVFile)
		final File[] filesRef = File[].class.cast(action.getModel().get(contextKeyFile));
		if (filesRef == null || filesRef.length == 0) {
			throw new VUserException(new MessageText("Le fichier ({0}) n'a pas été envoyé", null, contextKeyFile));
		}
		final String[] fileNamesRef = String[].class.cast(action.getModel().get(contextKeyFileName));
		final String[] contentTypesRef = String[].class.cast(action.getModel().get(contextKeyContentType));
		final VFile vFile = new FSFile(fileNamesRef[0], contentTypesRef[0], filesRef[0]);
		action.getModel().put(contextKeyVFile, vFile);
		return vFile;
	}

	/**
	 * @return Si cet élément est dans le context.
	 */
	public boolean exists() {
		return action.getModel().containsKey(contextKeyFile) || action.getModel().containsKey(contextKeyVFile);
	}
}
