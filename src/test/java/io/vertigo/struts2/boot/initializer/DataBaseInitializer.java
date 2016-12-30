/**
 *
 */
package io.vertigo.struts2.boot.initializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Calendar;

import javax.inject.Inject;

import io.vertigo.core.resource.ResourceManager;
import io.vertigo.core.spaces.component.ComponentInitializer;
import io.vertigo.dynamo.database.SqlDataBaseManager;
import io.vertigo.dynamo.database.connection.SqlConnection;
import io.vertigo.dynamo.database.statement.SqlCallableStatement;
import io.vertigo.dynamo.transaction.VTransactionManager;
import io.vertigo.dynamo.transaction.VTransactionWritable;
import io.vertigo.lang.WrappedException;
import io.vertigo.struts2.dao.movies.MovieDAO;
import io.vertigo.struts2.domain.movies.Movie;

/**
 * Init masterdata list.
 * @author jmforhan
 */
public class DataBaseInitializer implements ComponentInitializer {

	@Inject
	private ResourceManager resourceManager;
	@Inject
	private VTransactionManager transactionManager;
	@Inject
	private SqlDataBaseManager sqlDataBaseManager;
	@Inject
	private MovieDAO movieDao;

	/** {@inheritDoc} */
	@Override
	public void init() {
		createDataBase();
		createInitialMovies(movieDao, transactionManager);
	}

	private void createDataBase() {
		SqlConnection connection;
		try {
			connection = sqlDataBaseManager.getConnectionProvider(SqlDataBaseManager.MAIN_CONNECTION_PROVIDER_NAME).obtainConnection();
		} catch (final SQLException e) {
			throw WrappedException.wrapIfNeeded(e, "Can't open connection");
		}
		execSqlScript(connection, "sqlgen/crebas.sql");
	}

	private void execSqlScript(final SqlConnection connection, final String scriptPath) {
		try {
			final StringBuilder crebaseSql = new StringBuilder();
			final BufferedReader in = new BufferedReader(new InputStreamReader(resourceManager.resolve(scriptPath).openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				final String adaptedInputLine = inputLine.replaceAll("-- .*", "");//removed comments
				if (!"".equals(adaptedInputLine)) {
					crebaseSql.append(adaptedInputLine).append('\n');
				}
				if (inputLine.trim().endsWith(";")) {
					execCallableStatement(connection, sqlDataBaseManager, crebaseSql.toString());
					crebaseSql.setLength(0);
				}
			}
			in.close();
		} catch (final IOException e) {
			throw WrappedException.wrapIfNeeded(e, "Can't exec script {0}", scriptPath);
		}
	}

	private static void execCallableStatement(final SqlConnection connection, final SqlDataBaseManager sqlDataBaseManager, final String sql) {
		try (final SqlCallableStatement callableStatement = sqlDataBaseManager.createCallableStatement(connection, sql)) {
			callableStatement.init();
			callableStatement.executeUpdate();
		} catch (final SQLException e) {
			throw WrappedException.wrapIfNeeded(e, "Can't exec command {0}", sql);
		}
	}

	private static void createInitialMovies(final MovieDAO movieDao, final VTransactionManager transactionManager) {
		try (VTransactionWritable tx = transactionManager.createCurrentTransaction()) {
			movieDao.create(createMovie("Pulp Fiction", 1994, 154, "http://ia.media-imdb.com/images/M/MV5BMTkxMTA5OTAzMl5BMl5BanBnXkFtZTgwNjA5MDc3NjE@._V1_SX214_AL_.jpg", "L'odyssée sanglante et burlesque de petits malfrats dans la jungle de Hollywood à travers trois histoires qui s'entremêlent."));
			movieDao.create(createMovie("The Good, the Bad and the Ugly", 1966, 177, "https://upload.wikimedia.org/wikipedia/en/4/45/Good_the_bad_and_the_ugly_poster.jpg", "Pendant la Guerre de Sécession, trois hommes, préférant s'intéresser à leur profit personnel, se lancent à la recherche d'un coffre contenant 200 000 dollars en pièces d'or volés à l'armée sudiste. Tuco sait que le trésor se trouve dans un cimetière, tandis que Joe connaît le nom inscrit sur la pierre tombale qui sert de cache. Chacun a besoin de l'autre. Mais un troisième homme entre dans la course : Setenza, une brute qui n'hésite pas à massacrer femmes et enfants pour parvenir à ses fins."));
			movieDao.create(createMovie("The Godfather", 1972, 175, "http://www.avoir-alire.com/IMG/jpg/le_parrain_1.jpg", "En 1945, à New York, les Corleone sont une des cinq familles de la mafia. Don Vito Corleone, \"parrain\" de cette famille, marie sa fille à un bookmaker. Sollozzo, \" parrain \" de la famille Tattaglia, propose à Don Vito une association dans le trafic de drogue, mais celui-ci refuse. Sonny, un de ses fils, y est quant à lui favorable.\nAfin de traiter avec Sonny, Sollozzo tente de faire tuer Don Vito, mais celui-ci en réchappe. Michael, le frère cadet de Sonny, recherche alors les commanditaires de l'attentat et tue Sollozzo et le chef de la police, en représailles.\nMichael part alors en Sicile, où il épouse Apollonia, mais celle-ci est assassinée à sa place. De retour à New York, Michael épouse Kay Adams et se prépare à devenir le successeur de son père..."));
			movieDao.create(createMovie("Full metal jacket", 1987, 116, "http://t3.gstatic.com/images?q=tbn:ANd9GcRvS0gpcmYItYpYqNswzvlibugwezaH-13M8y4kiJnCthNS9nX-", "Pendant la guerre du Vietnam, la préparation et l'entrainement d'un groupe de jeunes marines, jusqu'au terrible baptême du feu et la sanglante offensive du Tet a Hue, en 1968."));
			movieDao.create(createMovie("Shinning", 1980, 119, "http://image.toutlecine.com/photos/s/h/i/shining-1980-15-g.jpg", "Jack Torrance, gardien d'un hôtel fermé l'hiver, sa femme et son fils Danny s'apprêtent à vivre de longs mois de solitude. Danny, qui possède un don de médium, le \"Shining\", est effrayé à l'idée d'habiter ce lieu, théâtre marqué par de terribles évènements passés..."));
			movieDao.create(createMovie("Misery", 1990, 107, "http://www.gstatic.com/tv/thumb/movieposters/12891/p12891_p_v8_aa.jpg", "Paul Sheldon, romancier et créateur du personnage de Misery dont il a écrit la saga est satisfait. Il vient enfin de faire mourir son héroïne et peut passer à autre chose. Il quitte l'hôtel de montagne où il a l'habitude d'écrire et prend la route de New York. Pris dans un violent blizzard, sa voiture dérape dans la neige et tombe dans un ravin. Paul Sheldon doit son salut à Annie Wilkes, infirmière retraitée qui vit dans un chalet isolé. Annie est justement une supporter inconditionnelle de la belle Misery."));
			movieDao.create(createMovie("L'exorciste", 1973, 122, "http://www.dvdclassik.com/upload/images/affiches/l-exorciste.jpeg", "En Irak, le Père Merrin est profondément troublé par la découverte d'une figurine du démon Pazuzu et les visions macabres qui s'ensuivent.\nParallèlement, à Washington, la maison de l'actrice Chris MacNeil est troublée par des phénomènes étranges : celle-ci est réveillée par des grattements mystérieux provenant du grenier, tandis que sa fille Regan se plaint que son lit bouge.\nQuelques jours plus tard, une réception organisée par Chris est troublée par l'arrivée de Regan, qui profère des menaces de mort à l'encontre du réalisateur Burke Dennings. Les crises se font de plus en plus fréquentes. En proie à des spasmes violents, l'adolescente devient méconnaissable.\nChris fait appel à un exorciste. L'Eglise autorise le Père Damien Karras à officier en compagnie du Père Merrin. Une dramatique épreuve de force s'engage alors pour libérer Regan."));
			tx.commit();
		}
	}

	private static Movie createMovie(final String title, final int year, final int runtime, final String poster, final String description) {
		final Movie movie = new Movie();
		movie.setTitle(title);
		movie.setYear(year);
		final Calendar calendar = Calendar.getInstance();
		calendar.set(year, 1, 1, 0, 0, 0);
		movie.setReleased(calendar.getTime());
		movie.setRuntime(runtime);
		movie.setPoster(poster);
		movie.setDescription(description);
		return movie;
	}
}
