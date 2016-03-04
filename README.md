### TABLE OF CONTENTS
##### [TEST & BUILD THE APPLICATION](#test-and-build-the-application)
##### [AUTOMATED TESTS](#automated-tests)
##### [RUN THE WEB APP](#run-the-web-app)
##### [ON THE DESIGN CHOICES](#on-the-design-choices)

### <a name="test-and-build-the-application"></a> TEST & BUILD THE APPLICATION

     $ maven clean verify

JavaDoc generated documentation will be available in the following file:

     ./target/apidocs/index.html

This application has been developed using Maven 3 and Java 8.

### <a name="behavioral-driven-development"></a> AUTOMATED TESTS

This application has unit tests and integration tests, which are run from maven using Surefire and Failsafe plugins.

To run unit tests only:

     $ maven clean test
     
To run integration tests:

     $ maven clean verify

### <a name="run-the-web-app"></a> RUN THE WEB APP

The following command will build, test and run the application in a local tomcat server:

    mvn clean test package jetty:run
    
You may open the following URLs for manual testing:

* [Simple Template](http://localhost:18080/Slightly/person.html?id=2) (from the requirements document).

* [Fancy Template](http://localhost:18080/Slightly/fancyPerson.html?id=2) (uses HTML tables and CSS).

### <a name="on-the-design-choices"></a> ON THE DESIGN CHOICES

The most important thing to mention is the use of the [Command Pattern](https://en.wikipedia.org/wiki/Command_pattern) to implement the document processor.

I decided to call my command classes "transformations" instead of calling them "commands" though.

Each of the transformations that need to be applied to the document template (such as evaluate and process a data-if element, expand a data-for element, render javascript expressions) is implemented as a command class.

The ```DocumentProcessor``` simply initializes a list of these transformations (commands) on creation and apply every and each one of them to the document when ```DocumentProcessor.process()``` is called.

Using this pattern creates a open-closed design. If a new transformation is required (such as adding support to template-element for instance), we only need to implement a new transformer class (e.g. ```TemplateElementTransformation```) and add an instance of this new transformer to the list of transformations of the ```DocumentProcessor```.

The following frameworks are worth to mention:

* [JSoup](http://jsoup.org/) for parsing HTML.
* [Selenium WebDriver](http://www.seleniumhq.org/projects/webdriver/) for integration tests.
* [JUnit](http://junit.org/) for unit tests.
* [Catch-Exception](https://github.com/Codearte/catch-exception) for unit tests.

I'm using several other auxiliary frameworks, including Guava and Apache Commons. Check the pom file for details.