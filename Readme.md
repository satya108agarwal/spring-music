Spring Music
============

This is a sample application for using database services on [Cloud Foundry](http://cloudfoundry.org) with the [Spring Framework](http://spring.io) and [Spring Boot](http://projects.spring.io/spring-boot/).

This application has been built to store the same domain objects in one of a variety of different persistence technologies - relational, document, and key-value stores. This is not meant to represent a realistic use case for these technologies, since you would typically choose the one most applicable to the type of data you need to store, but it is useful for testing and experimenting with different types of services on Cloud Foundry.

The application use Spring Java configuration and [bean profiles](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html) to configure the application and the connection objects needed to use the persistence stores. It also uses the [Java CFEnv](https://github.com/pivotal-cf/java-cfenv/) library to inspect the environment when running on Cloud Foundry. See the [Cloud Foundry documentation](http://docs.cloudfoundry.org/buildpacks/java/spring-service-bindings.html) for details on configuring a Spring application for Cloud Foundry.

## Building

This project has been compiled an built with Java 17 and artifacts are checked in the repository


## Running the application locally

One Spring bean profile should be activated to choose the database provider that the application should use. The profile is selected by setting the system property `spring.profiles.active` when starting the app.

The application can be started locally using the following command:

~~~
 java -jar -Dspring.profiles.active=<profile> build/libs/spring-music-1.0.jar
~~~

**Note**: Edit the manifest.yml to have a unique app name. Append employeeId so the app name is unique across the foundation
~~~

# Push the app to the platform
cf push
~~~


## Alternate Java versions

By default, the application will be built and deployed using Java 17 compatibility.
If you want to use a more recent version of Java, you will need to update two things.

In `build.gradle`, change the `targetCompatibility` Java version from `JavaVersion.VERSION_17` to a different value from `JavaVersion`:

~~~
java {
  ...
  targetCompatibility = JavaVersion.VERSION_17
}
~~~

In `manifest.yml`, change the Java buildpack JRE version from `version: 17.+` to a different value:

~~~
    JBP_CONFIG_OPEN_JDK_JRE: '{ jre: { version: 17.+ } }'
~~~
