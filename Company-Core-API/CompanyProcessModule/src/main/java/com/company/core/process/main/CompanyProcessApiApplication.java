package com.company.core.process.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@ComponentScan("com.company")
@EnableAsync
public class CompanyProcessApiApplication {

	private static final String MAXIMUM_POOL_SIZE = "maximumPoolSize";
	private static final String MINIMUM_IDLE = "minimumIdle";

	@Value("${spring.profiles.active:}")
	private String activeProfile;

	@Value("${maxPoolSize}")
	private int maxPoolSize;

	@Value("${minPoolSize}")
	private int minPoolSize;

	@Value("${configfileLoc}")
	public void setConfigFileLoc(String fileLoc) {
		configFileLoc = fileLoc;
	}

	public static String configFileLoc;
	private static DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS");

	public static void main(String[] args) {
		SpringApplication.run(CompanyProcessApiApplication.class, args);
	}

	// ----
	/**
	 * Load tenant datasource properties from the folder 'tenants/onStartUp` when
	 * the app has started.
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@EventListener
	public void onReady(ApplicationReadyEvent event) throws IOException {

		var datetime = LocalDateTime.now();

		log.info(datetime.format(newPattern) + " API application main configFileLoc>>>>" + configFileLoc + "tenant"
				+ File.separator);

		File[] files = Paths.get(configFileLoc + "tenant" + File.separator).toFile().listFiles();

		if (files == null) {

			log.error(datetime.format(newPattern)
					+ " API application main method onReady Tenant property files not found at >>>" + configFileLoc
					+ "tenant" + File.separator);

			return;
		}


		log.info(" API application main Loading of all tenant datasource completed");

	}

	/**
	 * Example of the tenant resolver - load the given tenant data source properties
	 * from the folder 'tenants/atRuntime'
	 *
	 * @param tenantId tenant id
	 * @return tenant DataSource
	 */
	private static DataSourceProperties tenantResolver(String tenantId) {

		File[] files = Paths.get(configFileLoc + "tenant" + File.separator).toFile().listFiles();

		if (files == null) {

			String msg = " API application main method tenantResolver Tenant property files not found at >>>"
					+ configFileLoc + "tenant" + File.separator;
			var datetime = LocalDateTime.now();

			log.error(datetime.format(newPattern) + msg);

			throw new RuntimeException(msg);
		}

		for (File propertyFile : files) {

			var tenantProperties = new Properties();

			try (var inputFile = new FileInputStream(propertyFile)) {
				tenantProperties.load(inputFile);

			} catch (IOException e) {

				String msg = " API application method tenantResolver Could not read tenant property file at "
						+ configFileLoc + "tenant" + File.separator;
				var datetime1 = LocalDateTime.now();
				log.error(datetime1.format(newPattern) + msg);

				throw new RuntimeException(msg, e);
			}

			String id = tenantProperties.getProperty("id");

			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + id + "====" + tenantId + "====" + tenantId.equals(id));

			if (tenantId.equals(id)) {

				var properties = new DataSourceProperties();
				properties.setUrl(tenantProperties.getProperty("url"));
				properties.setUsername(tenantProperties.getProperty("username"));
				properties.setPassword(tenantProperties.getProperty("password"));

				return properties;
			}
		}
		String msg = " Application main method tenantResolver Any tenant property files not found at " + configFileLoc;

		var datetime2 = LocalDateTime.now();
		log.error(datetime2.format(newPattern) + msg);

		throw new RuntimeException(msg);
	}

//	/**
//	 * Below method is use to setup the DB configuration using Flyway.
//	 * 
//	 * @param tenantObj
//	 */
//	public void setupDBConfig(TenantsDTO tenantObj) {
//
//		var scriptLocation = "classpath:db/migration/common/";
//
//		try {
//
//			var flyway = Flyway.configure().table("flyway_schema_history_UPM").ignoreMissingMigrations(true)
//					.locations(scriptLocation).baselineOnMigrate(Boolean.TRUE)
//					.dataSource((DataSource) tenantManager.tenantDataSources.get(tenantObj.getTenantId())).load();
//
//			log.info("activeProfile >>> " + activeProfile);
//
//			if (StringUtils.hasText(activeProfile) && !"default".equalsIgnoreCase(activeProfile)) { // do not execute
//																									// this in Dev or
//																									// SQA
//
//				flywayRepair(tenantObj, flyway);
//
//			}
//
//			flyway.migrate();
//
//		} catch (Exception excp) {
//
//			log.error("Eception occured while running Flyway script for Tenant >>" + tenantObj.getTenantId() + ">>>>>"
//					+ excp.getLocalizedMessage());
//		}
//
//		log.debug("All DB Configuration Script run is completed successfully!!!");
//
//	}
//
////	@Profile("!default, !unknown")
//	public void flywayRepair(TenantsDTO tenantObj, Flyway flyway) {
//
//		try {
//
//			flyway.repair();
//
//		} catch (Exception excp) {
//
//			log.error("Eception occured while running flywayRepair for Tenant >>" + tenantObj.getTenantId() + ">>>>>"
//					+ excp.getLocalizedMessage());
//		}
//
//		log.debug("All DB Configuration flywayRepair Script run is completed successfully!!!");
//
//	}

}
