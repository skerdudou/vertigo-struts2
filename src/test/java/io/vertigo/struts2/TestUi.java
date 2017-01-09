package io.vertigo.struts2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jetty.annotations.ServletContainerInitializersStarter;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.jsp.JettyJspServlet;
import org.eclipse.jetty.plus.annotation.ContainerInitializer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Timezone;

public class TestUi {

	private static List<ContainerInitializer> jspInitializers() {
		final JettyJasperInitializer sci = new JettyJasperInitializer();
		final ContainerInitializer initializer = new ContainerInitializer(sci, null);
		final List<ContainerInitializer> initializers = new ArrayList<>();
		initializers.add(initializer);
		return initializers;
	}

	private static ClassLoader getUrlClassLoader() {
		return new URLClassLoader(new URL[0], TestUi.class.getClassLoader());
	}

	/**
	 * Create JSP Servlet (must be named "jsp")
	 */
	private static ServletHolder jspServletHolder() {
		final ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
		holderJsp.setInitOrder(0);
		holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
		holderJsp.setInitParameter("fork", "false");
		holderJsp.setInitParameter("xpoweredBy", "false");
		holderJsp.setInitParameter("compilerTargetVM", "1.8");
		holderJsp.setInitParameter("compilerSourceVM", "1.8");
		holderJsp.setInitParameter("keepgenerated", "true");
		return holderJsp;
	}

	private static final int port = 18080;
	private final String baseUrl = "http://localhost:" + port;
	private static Server server;
	private static WebDriver driver;

	@BeforeClass
	public static void setUp() throws Exception {
		startServer();
		driver = new JBrowserDriver(Settings.builder()
				.timezone(Timezone.EUROPE_PARIS)
				.headless(true) //use false for debug purpose
				.build());
	}

	private static void startServer() throws IOException, Exception {
		server = new Server(port);
		final WebAppContext context = new WebAppContext(TestUi.class.getClassLoader().getResource("testWebApp/").getFile(), "/test");
		System.setProperty("org.apache.jasper.compiler.disablejsr199", "false");
		context.setAttribute("jacoco.exclClassLoaders", "*");

		context.setAttribute("javax.servlet.context.tempdir", getScratchDir());
		context.setAttribute("org.eclipse.jetty.containerInitializers", jspInitializers());
		context.addBean(new ServletContainerInitializersStarter(context), true);
		context.setClassLoader(getUrlClassLoader());
		context.addServlet(jspServletHolder(), "*.jsp");

		context.setClassLoader(new WebAppClassLoader(TestUi.class.getClassLoader(), context));

		server.setHandler(context);
		server.start();
	}

	private static File getScratchDir() throws IOException {
		final File tempDir = new File(System.getProperty("java.io.tmpdir"));
		final File scratchDir = new File(tempDir.toString(), "embedded-jetty-jsp");

		if (!scratchDir.exists()) {
			if (!scratchDir.mkdirs()) {
				throw new IOException("Unable to create scratch directory: " + scratchDir);
			}
		}
		return scratchDir;
	}

	@AfterClass
	public static void tearDown() throws Exception {
		if (server != null) {
			server.stop();
		}
		if (driver != null) {
			//driver.close();
		}
	}

	/*@Test
	public void testServer() throws IOException, Exception {
		server.join();
	}*/

	@Test
	public void testLoadLoginPage() {
		driver.get(baseUrl + "/test/");
		assertEquals(baseUrl + "/test/Login.do", driver.getCurrentUrl());
	}

	@Test
	public void testLogin() throws InterruptedException {
		driver.get(baseUrl + "/test/Login.do");

		waitElement(By.name("utilisateur.password")).clear();
		findElement(By.name("utilisateur.password")).sendKeys("!test!");
		findElement(By.name("action:loginLogin")).click();

		assertEquals("TeleDo - Accueil", driver.getTitle());
	}

	@Test
	public void testBadLogin() throws InterruptedException {
		driver.get(baseUrl + "/test/Login.do");
		waitElement(By.name("action:loginLogin")).click();
		assertEquals("Échec de la connexion : nom d'utilisateur inconnu ou mot de passe incorrect.", findElement(By.cssSelector("span.message")).getText());

		findElement(By.name("utilisateur.password")).clear();
		findElement(By.name("utilisateur.password")).sendKeys("test");
		findElement(By.name("action:loginLogin")).click();
		assertEquals("Échec de la connexion : nom d'utilisateur inconnu ou mot de passe incorrect.", findElement(By.cssSelector("span.message")).getText());

		findElement(By.name("utilisateur.password")).clear();
		findElement(By.name("utilisateur.password")).sendKeys("!test!");
		findElement(By.name("action:loginLogin")).click();
		assertEquals("TeleDo - Accueil", driver.getTitle());
	}

	@Test
	public void testPostSimpleForm() throws InterruptedException {
		testLogin();

		assertEquals("Test div layout=table", waitElement(By.cssSelector("h1")).getText());
		findElement(By.name("movie.title")).clear();
		findElement(By.name("movie.title")).sendKeys("Test 1");
		findElement(By.name("movie.year")).clear();
		findElement(By.name("movie.year")).sendKeys("2020");
		findElement(By.name("action:saveAccueil")).click();

		assertEquals("Test 1", findElement(By.name("movie.title")).getAttribute("value"));
		assertEquals("2 020", findElement(By.name("movie.year")).getAttribute("value"));
	}

	@Test
	public void testSimpleDisplayTable() throws InterruptedException {
		testLogin();
		driver.get(baseUrl + "/test/Accueil.do");
		assertEquals("Test display:table sur ContextList", waitElement(By.xpath("(//form[@id='displayTable']/h1)")).getText());

		findElement(By.linkText("Jour")).click();
		assertEquals("Full metal jacket", findElement(By.xpath("//table[@id='item']/tbody/tr/td")).getText());
		assertEquals("The Good, the Bad and the Ugly", findElement(By.xpath("//table[@id='item']/tbody/tr[7]/td")).getText());

		findElement(By.linkText("Jour")).click();
		assertEquals("The Good, the Bad and the Ugly", findElement(By.xpath("//table[@id='item']/tbody/tr/td")).getText());
		assertEquals("Full metal jacket", findElement(By.xpath("//table[@id='item']/tbody/tr[7]/td")).getText());

		findElement(By.linkText("year")).click();
		assertEquals("1 966", findElement(By.xpath("//table[@id='item']/tbody/tr/td[2]")).getText());
		assertEquals("1 994", findElement(By.xpath("//table[@id='item']/tbody/tr[7]/td[2]")).getText());

		findElement(By.linkText("year")).click();
		assertEquals("1 994", findElement(By.xpath("//table[@id='item']/tbody/tr/td[2]")).getText());
		assertEquals("1 966", findElement(By.xpath("//table[@id='item']/tbody/tr[7]/td[2]")).getText());
	}

	@Test
	public void testSelectOnContextList() throws InterruptedException {
		testLogin();
		driver.get(baseUrl + "/test/Accueil.do");
		assertEquals("Test select sur ContextList", waitElement(By.xpath("(//form/h1)[3]")).getText());
		assertTrue(findElement(By.xpath("//form[@id='selectContextList']/table/tbody/tr/th/label")).getText().matches("^Movie\\*$"));
		final Select select = new Select(findElement(By.xpath("//form[@id='selectContextList']/table/tbody/tr/td/select")));
		assertEquals("Pulp Fiction, The Good, the Bad and the Ugly, The Godfather, Full metal jacket, Shinning, Misery, L'exorciste", getWebElementsAsString(select.getOptions()));
		assertEquals("Pulp Fiction", select.getFirstSelectedOption().getText());
		select.selectByVisibleText("Misery");
		assertEquals("Misery", select.getFirstSelectedOption().getText());
		findElement(By.name("action:saveCastingAccueil")).click();

		final Select select2 = new Select(findElement(By.xpath("//form[@id='selectContextList']/table/tbody/tr/td/select")));
		assertEquals("Misery", select2.getFirstSelectedOption().getText());
		findElement(By.name("action:toReadAccueil")).click();

		assertEquals("Misery", findElement(By.xpath("//form[3]/table/tbody/tr/td/span")).getText());
	}

	@Test
	public void testRadioOnContextList() throws InterruptedException {
		testLogin();
		driver.get(baseUrl + "/test/Accueil.do");
		assertEquals("Test radio sur ContextList", waitElement(By.cssSelector("#radioContextList > h1")).getText());
		findElement(By.id("radioContextList_casting_movId1002")).click();
		assertEquals("1002", findElement(By.id("radioContextList_casting_movId1002")).getAttribute("value"));
		findElement(By.cssSelector("#radioContextList_saveCastingAccueil")).click();

		assertEquals("1002", findElement(By.id("radioContextList_casting_movId1002")).getAttribute("value"));
		findElement(By.id("changeMode_toReadAccueil")).click();

		assertEquals("The Godfather", findElement(By.id("radioContextList_casting_movId")).getText());
	}

	@Test
	public void testSelectOnContextMdl() throws InterruptedException {
		testLogin();
		driver.get(baseUrl + "/test/Accueil.do");
		assertEquals("Test select sur ContextMdl", waitElement(By.cssSelector("#selectContextMdl > h1")).getText());
		assertTrue(findElement(By.cssSelector("#selectContextMdl  label")).getText().matches("^Movie\\*$"));

		final Select select = new Select(findElement(By.id("selectContextMdl_casting_movId")));
		select.selectByVisibleText("Shinning");
		assertEquals("Shinning", select.getFirstSelectedOption().getText());
		findElement(By.id("selectContextMdl_saveCastingAccueil")).click();

		final Select select2 = new Select(findElement(By.id("selectContextMdl_casting_movId")));
		assertEquals("Shinning", select2.getFirstSelectedOption().getText());
		findElement(By.id("changeMode_toReadAccueil")).click();

		assertEquals("Shinning", findElement(By.id("selectContextMdl_casting_movId-1")).getText());
	}

	@Test
	public void testRadioOnContextMdl() throws InterruptedException {
		testLogin();
		driver.get(baseUrl + "/test/Accueil.do");
		assertEquals("Test radio sur ContextMdl", waitElement(By.cssSelector("#radioContextMdl > h1")).getText());
		findElement(By.id("radioContextMdl_casting_movId1002")).click();
		assertEquals("1002", findElement(By.id("radioContextMdl_casting_movId1002")).getAttribute("value"));
		findElement(By.cssSelector("#radioContextMdl_saveCastingAccueil")).click();

		assertEquals("1002", findElement(By.id("radioContextMdl_casting_movId1002")).getAttribute("value"));
		findElement(By.id("changeMode_toReadAccueil")).click();

		assertEquals("The Godfather", findElement(By.id("radioContextMdl_casting_movId")).getText());
	}

	@Test
	public void testAutocompleterOnContextList() throws InterruptedException {
		testLogin();

		driver.get(baseUrl + "/test/Accueil.do");
		assertEquals("Test sj:autocompleter sur ContextList", waitElement(By.cssSelector("#autocompleteContextList > h1")).getText());
		assertTrue(findElement(By.cssSelector("#autocompleteContextList > table.grid > tbody > tr > th.tdLabel > label")).getText().matches("^Movie\\*$"));

		findElement(By.xpath("//form[@id='autocompleteContextList']/table/tbody/tr/td/input[2]")).clear();
		findElement(By.xpath("//form[@id='autocompleteContextList']/table/tbody/tr/td/input[2]")).sendKeys("the");

		assertEquals("The Godfather", waitElement(By.cssSelector("ul.ui-autocomplete span.col"), 5000).getText());

		findElement(By.cssSelector("ul.ui-autocomplete span.col")).click();
		assertEquals("The Godfather", findElement(By.xpath("//form[@id='autocompleteContextList']/table/tbody/tr/td/input[2]")).getAttribute("value"));

		findElement(By.id("autocompleteContextList_saveCastingAccueil")).click();

		assertEquals("The Godfather", findElement(By.xpath("//form[@id='autocompleteContextList']/table/tbody/tr/td/input[2]")).getAttribute("value"));
		findElement(By.id("changeMode_toReadAccueil")).click();
		assertEquals("The Godfather", findElement(By.xpath("//form[@id='autocompleteContextList']/table/tbody/tr/td/span")).getText());
	}

	@Test
	public void testAutocompleterOnContextMdl() throws InterruptedException {
		testLogin();

		driver.get(baseUrl + "/test/Accueil.do");
		assertEquals("Test sj:autocompleter sur ContextMdl", waitElement(By.cssSelector("#autocompleteContextMdl > h1")).getText());
		assertTrue(findElement(By.cssSelector("#autocompleteContextMdl > table.grid > tbody > tr > th.tdLabel > label")).getText().matches("^commune$"));

		findElement(By.xpath("//form[@id='autocompleteContextMdl']/table/tbody/tr/td/input[2]")).clear();
		findElement(By.xpath("//form[@id='autocompleteContextMdl']/table/tbody/tr/td/input[2]")).sendKeys("blai");

		assertEquals("BLAISE", waitElement(By.cssSelector("ul.ui-autocomplete span.col"), 5000).getText());

		findElement(By.cssSelector("ul.ui-autocomplete span.col")).click();
		assertEquals("BLAISE", findElement(By.xpath("//form[@id='autocompleteContextMdl']/table/tbody/tr/td/input[2]")).getAttribute("value"));

		findElement(By.id("autocompleteContextMdl_saveCommuneAccueil")).click();

		assertEquals("BLAISE", findElement(By.xpath("//form[@id='autocompleteContextMdl']/table/tbody/tr/td/input[2]")).getAttribute("value"));
		findElement(By.id("changeMode_toReadAccueil")).click();
		assertEquals("BLAISE", findElement(By.xpath("//form[@id='autocompleteContextMdl']/table/tbody/tr/td/span")).getText());
	}

	@Test
	public void testDownload() throws Exception {
		testLogin();

		final FileDownloader4Tests fileDownloader4Tests = new FileDownloader4Tests(driver);
		final WebElement downloadLink = findElement(By.linkText("insee.csv"));
		final String downloadedFileAbsoluteLocation = fileDownloader4Tests.downloadFile(downloadLink);

		assertTrue(new File(downloadedFileAbsoluteLocation).exists());
		assertEquals(fileDownloader4Tests.getHTTPStatusOfLastDownloadAttempt(), 200);
	}

	@Test
	public void testUpload() throws InterruptedException {
		testLogin();

		final String fullPath = getClass().getResource("/data/insee.csv").getFile();
		findElement(By.id("uploadFile_fileTest")).clear();
		findElement(By.id("uploadFile_fileTest")).sendKeys(fullPath);
		findElement(By.id("uploadFile_uploadFileAccueil")).click();

		assertEquals("Fichier recu : insee.csv (application/octet-stream)", findElement(By.cssSelector("span")).getText());
		assertEquals("Previous file : insee.csv (application/octet-stream)", findElement(By.id("uploadFile")).getText());
	}

	private String getWebElementsAsString(final List<WebElement> webElements) {
		return webElements.stream()
				.map(WebElement::getText)
				.collect(Collectors.joining(", "));
	}

	private WebElement waitElement(final By byElement) throws InterruptedException {
		return waitElement(byElement, 1000);
	}

	private WebElement waitElement(final By byElement, final long timeout) throws InterruptedException {
		final long start = System.currentTimeMillis();
		do {
			try {
				if (isElementPresent(byElement)) {
					return driver.findElement(byElement);
				}
			} catch (final Exception e) {
				//do nothing
			}
			Thread.sleep(100);
		} while (System.currentTimeMillis() - start < timeout);
		System.out.println(driver.getPageSource());
		throw new AssertionError("Element non trouvé en " + timeout + "ms : " + byElement.toString());
	}

	private static boolean isElementPresent(final By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (final org.openqa.selenium.NoSuchElementException e) {
			return false;
		}
	}

	private static WebElement findElement(final By by) {
		try {
			return driver.findElement(by);
		} catch (final org.openqa.selenium.NoSuchElementException e) {
			System.out.println(driver.getPageSource());
			throw new NoSuchElementException(by.toString(), e);
		}
	}

}
