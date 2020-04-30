# Task Notifier

This repository contains a project that should be compiled as a jar and used by Bonita for task notifications


## How to use this jar

1. Get it from the repo.
1. Build it with Gradle : gradlew.bat build
1. Put the jar in the libs folder server/webapps/bonita/WEB-INF/lib
1. Get the setup files by executing a setup/setup pull
1. Modify the platform_conf/current/tenants/1/tenant_portal/authenticationManager-config.properties file
1. Modify the platform_conf/current/tenants/1/tenant_engine/bonita-tenant-sp-custom.xml file
1. Modify the platform_conf/current/tenants/1/tenant_engine/bonita-tenant-sp-custom.properties file
1. Commit the file by executing a setup/setup push
1. Restart the server and check that it works as expected.

NB : To run it locally with Bonita Studio, it's the same process. But you need to change the file setup/database.properties

## database.properties (local only)
``` properties
	 h2.database.dir=../../MyProject/h2_database
```

## authenticationManager-config.properties
``` properties
	auth.AuthenticationManager = org.bonitasoft.ca.auth.CustomAuthenticationManager
	ca.logout.global = false
	auth.passphrase = Bonita
```

## bonita-tenant-sp-custom.xml
``` xml
	<bean id="passphraseOrPasswordAuthenticationService" class="com.bonitasoft.engine.authentication.impl.PassphraseOrPasswordAuthenticationService" lazy-init="true">
       <constructor-arg name="logger" ref="tenantTechnicalLoggerService" />
       <constructor-arg name="identityService" ref="identityService" />
       <constructor-arg name="configuredPassphrase" value="${authentication.service.ref.passphrase}" />
	</bean>
```

## bonita-tenant-sp-custom.properties
``` properties
	authentication.service.ref.name=passphraseOrPasswordAuthenticationService
	#
	## If authentication.service.ref.name equals "passphraseOrPasswordAuthenticationService",
	## you need to configure the following passphrase 
	authentication.service.ref.passphrase=Bonita
```
