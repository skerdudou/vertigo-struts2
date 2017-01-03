package io.vertigo.struts2.ui.controller.accueil;

import javax.inject.Inject;

import io.vertigo.dynamo.domain.model.DtListState;
import io.vertigo.struts2.core.ContextForm;
import io.vertigo.struts2.core.ContextList;
import io.vertigo.struts2.core.ContextListModifiable;
import io.vertigo.struts2.core.ContextMdl;
import io.vertigo.struts2.core.ContextRef;
import io.vertigo.struts2.domain.movies.Movie;
import io.vertigo.struts2.domain.people.Casting;
import io.vertigo.struts2.domain.reference.Commune;
import io.vertigo.struts2.services.movies.MovieServices;
import io.vertigo.struts2.ui.controller.AbstractTestActionSupport;

public class AccueilAction extends AbstractTestActionSupport {

	private static final long serialVersionUID = 1L;

	private final ContextForm<Movie> movie = new ContextForm<>("movie", this);
	private final ContextForm<Casting> casting = new ContextForm<>("casting", this);
	private final ContextList<Movie> movieList = new ContextList<>("movies", this);
	private final ContextListModifiable<Movie> movieListModifiables = new ContextListModifiable<>("moviesModifiable", this);
	private final ContextMdl<Movie> moviesListMdl = new ContextMdl<>("moviesMdl", this);
	private final ContextRef<String> communeId = new ContextRef<>("communeId", String.class, this);
	private final ContextMdl<Commune> communeListMdl = new ContextMdl<>("communesMdl", this);

	@Inject
	private MovieServices movieServices;

	@Override
	protected void initContext() {
		movie.publish(new Movie());
		casting.publish(new Casting());
		movieList.publish(movieServices.getMovies(new DtListState(200, 0, null, null)));
		movieListModifiables.publish(movieServices.getMovies(new DtListState(200, 0, null, null)));
		moviesListMdl.publish(Movie.class, null);
		communeListMdl.publish(Commune.class, null);
		toModeCreate();
	}

	public String doSave() {
		movie.readDto();
		return NONE;
	}

	public String doSaveCasting() {
		casting.readDto();
		return NONE;
	}

	public String doSaveCommune() {
		communeId.set(communeId.get());
		return NONE;
	}

	public String doAddMovieList() {
		movieListModifiables.getUiListModifiable().add(new Movie());
		return NONE;
	}

	public String doSaveList() {
		movieListModifiables.readDtList();
		return NONE;
	}

	public String doUploadFile() {
		getModel();
		return NONE;
	}

	public String toRead() {
		toModeReadOnly();
		return NONE;
	}

	public String toEdit() {
		toModeEdit();
		return NONE;
	}

	@Override
	public String getPageName() {
		return "Accueil";
	}
}
