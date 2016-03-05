### TABLE OF CONTENTS
##### [TEST & BUILD THE APPLICATION](#test-and-build-the-application)
##### [AUTOMATED TESTS](#automated-tests)
##### [RUN THE WEB APP](#run-the-web-app)
##### [ON THE DESIGN CHOICES](#on-the-design-choices)
##### [OPTIONAL FEATURES](#optional-feature)

### <a name="test-and-build-the-application"></a> TEST & BUILD THE APPLICATION

     $ maven clean verify

JavaDoc generated documentation will be available in the following file:

     ./target/apidocs/index.html

This app has been developed using Maven 3 and Java 8.

### <a name="behavioral-driven-development"></a> AUTOMATED TESTS

This app has unit tests and integration tests, which are run from maven using Surefire and Failsafe plugins.

To run unit tests only:

     $ maven clean test
     
To run integration tests:

     $ maven clean verify

### <a name="run-the-web-app"></a> RUN THE WEB APP

The following command will build, test and run the application in a local jetty server:

    mvn clean test package jetty:run
    
You may open the following URLs for manual testing:

* [Simple Template](http://localhost:8080/Slightly/person.html?id=2) (from the requirements document).

* [Fancy Template](http://localhost:8080/Slightly/fancyPerson.html?id=2) (uses HTML tables and CSS).

### <a name="on-the-design-choices"></a> ON THE DESIGN CHOICES

The most important thing to mention is the use of the [Command Pattern](https://en.wikipedia.org/wiki/Command_pattern) to implement the document processor.

I decided to call my command classes "transformations" instead of calling them "commands" though.

Each of the transformations that need to be applied to the document template (such as evaluate and process a data-if element, expand a data-for element, render javascript expressions) is implemented as a command class.

The ```DocumentProcessor``` simply initializes a list of these transformations (commands) on creation and apply every and each one of them to the document when ```DocumentProcessor.process()``` is called.

Using this pattern creates a open-closed design. If a new transformation is required (such as adding support to template-element for instance), we only need to implement a new transformation class (e.g. ```TemplateElementTransformation```) and add an instance of this new class to the list of transformations of the ```DocumentProcessor```.

The following frameworks used in this app are worth to mention:

* [JSoup](http://jsoup.org/) for parsing HTML.
* [Selenium WebDriver](http://www.seleniumhq.org/projects/webdriver/) for integration tests.
* [JUnit](http://junit.org/) for unit tests.
* [Catch-Exception](https://github.com/Codearte/catch-exception) for unit tests.

I'm using several other auxiliary frameworks, including Guava and Apache Commons. Check the pom file for details.

### <a name="optional-features"></a> OPTIONAL FEATURES

Regarding the template inclusion feature, as far as my understanding of the HTML5 template element goes, this feature is not meant to be used in this manner.

The template element is defined inside the main page as a reusable component that can be rendered over and over inside the main page by javascript calls.

The "fancy template" includes the use of the template element to build a header and footer for the page with the netcentric logo and a configurable slogan text underneath.

You will find the template definition and the javascript code in ```fancyPerson.html```.

Therefore I have decided to use a ```data-``` attribute for a new transformation class: ```data-inclusion```.

This new transformation will append the contents of the given template file to the element as child elements:

    <div id="footer" data-inclusion="templates/footer.html"/>

```footer.html``` contents: 

    <h4>Netcentric</h4>
    <h5>Code. Analyze. Build. Repair. Improve. Innovate.</h5>
    
After rendering:

    <div id="footer">
        <h4>Netcentric</h4>
         <h5>Code. Analyze. Build. Repair. Improve. Innovate.</h5>
    </div>