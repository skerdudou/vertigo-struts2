package io.vertigo.struts2.boot;

import java.util.Properties;

import org.apache.log4j.Logger;

import io.vertigo.app.AutoCloseableApp;
import io.vertigo.app.Home;
import io.vertigo.app.config.xml.XMLAppConfigBuilder;
import io.vertigo.studio.mda.MdaManager;

public final class Struts2TestGen {

	public static void main(final String[] args) {
		final XMLAppConfigBuilder appConfigBuilder = new XMLAppConfigBuilder();
		appConfigBuilder.beginBoot().silently();
		appConfigBuilder.withModules(Struts2TestGen.class, new Properties(), "/managers-mda.xml");
		try (AutoCloseableApp app = new AutoCloseableApp(appConfigBuilder.build())) {
			Home.getApp().getComponentSpace().resolve(MdaManager.class)
					.generate()
					/* Impression du Rapport d'exécution. */
					.displayResultMessage(System.out);
		} catch (final Exception e) {
			e.printStackTrace();
			Logger.getLogger(Struts2TestGen.class).warn("an error occured when starting", e);
		}
	}
}
