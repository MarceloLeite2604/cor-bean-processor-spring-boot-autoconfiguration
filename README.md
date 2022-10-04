# Chain Of Responsibility (COR) Bean Processor Spring Boot Starter

⚠️This is a working in progress. It is not ready for usage yet!

A library to assist creating Chain Of Responsibility design patterns on Spring
Boot projects. You develop the responsibility logic and the library manages to
create the chain linking the beans automatically.

# Usage

1. Be sure to add [Spring Boot](https://spring.io/guides/gs/spring-boot/) to
   your project.
2. Add COR Bean Processor Spring Boot Starter on your project.
    1. For Maven projects add the following on your `pom.xml` file
       under `<dependencies>` tag.
   ```xml
   <dependency>
     <groupId>com.figtreelake</groupId>
     <artifactId>cor-bean-processor-spring-boot-starter</artifactId>
     <version>1.0</version>
   </dependency>
   ```

    2. For Gradle projects add the following on your `build.gradle` file
       under `dependencies` declaration.
   ```groovy
   implementation 'com.figtreelake:cor-bean-processor-spring-boot-starter:1.0'
   ```
    3. You can check the latest version available
       on [Maven Central repository](https://mvnrepository.com/repos/central).
3. Make your Chain Of Responsibility link classes implement [ChainLink]()
   interface.
4. That is it! You can now autowire the first link of your Chain Of
   Responsibility anywhere in your project.

   You can also check [cor-bean-processor-examples](https://github.com/MarceloLeite2604/cor-bean-processor-examples)
   repository to find more examples about how to use this library.

## Sources

This library was implemented after
reading [Spring Boot - Auto Configuration](https://docs.spring.io/spring-boot/docs/1.3.8.RELEASE/reference/html/using-boot-auto-configuration.html)
and [Spring Boot Features - Developing Auto Configuration](https://docs.spring.io/spring-boot/docs/2.0.0.M3/reference/html/boot-features-developing-auto-configuration.html#boot-features-custom-starter)
documentation sections.

Also, thanks to [Stéphane Nicoll](https://github.com/snicoll) and
his [spring-boot-master-auto-configuration](https://github.com/snicoll/spring-boot-master-auto-configuration)
repository which helped me understand the minor details necessary to create a
Spring starter library.



