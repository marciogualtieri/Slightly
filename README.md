### TABLE OF CONTENTS
#### [TEST & BUILD THE APPLICATION](#test-and-build-the-application)
#### [AUTOMATED TESTS](#automated-tests)
#### [RUN THE WEB APP](#run-the-web-app)
#### [ON THE DESIGN CHOICES](#on-the-design-choices)
#### [OPTIONAL FEATURES](#optional-feature)
###### [TEMPLATE INCLUSION](#optional-feature-template-inclusion)
###### [LOCAL VARIABLE](#optional-feature-local-variable)

### <a name="test-and-build-the-application"></a> TEST & BUILD THE APPLICATION

     $ maven clean verify

JavaDoc generated documentation will be available in the following file:

     ./target/apidocs/index.html

This app has been developed using Maven 3 and Java 7.

### <a name="behavioral-driven-development"></a> AUTOMATED TESTS

This app has unit tests and integration tests, which are run from maven using Surefire and Failsafe plugins.

To run unit tests only:

     $ maven clean test
     
To run integration tests:

     $ maven clean verify

A local Jetty server instance is started before integration tests and stopped after integration tests.

### <a name="run-the-web-app"></a> RUN THE WEB APP

The following command will build, test, deploy and run the application in a local Jetty server:

    mvn clean test package jetty:run
    
You may open the following URLs for manual testing:

* [Simple Template](http://localhost:8080/Slightly/person.html?id=2) (from the requirements document).

* [Fancy Template](http://localhost:8080/Slightly/fancyPerson.html?id=2) (uses HTML tables and CSS).

### <a name="on-the-design-choices"></a> ON THE DESIGN CHOICES

The most important thing to mention is the use of the [Command Pattern](https://en.wikipedia.org/wiki/Command_pattern) to implement the document processor, which renders the template into the HTML output.

I decided to call my command classes "transformations" instead of calling them "commands" though.

Each of the transformations that need to be applied to the document template (such as evaluate and process data-if elements, expand data-for elements, render javascript expressions) is implemented as a command class (an specialization of the ```Transformation``` abstract class.

The ```DocumentProcessor``` simply initializes a list of these transformations (commands) on creation and apply every and each one of them to the document when ```DocumentProcessor.process()``` is called.



![](https://g.gravizo.com/source/svg/custom_mark1?https%3A%2F%2Fraw.githubusercontent.com%2Fmarciogualtieri%2FSlightly%2Fmaster%2FREADME.md)

<!---
custom_mark1
  digraph G {
    DataInclusionTransformation -> DataIfTransformation
    DataIfTransformation -> DataForTransformation
    DataForTransformation -> DataLocalVarTransformation
    DataLocalVarTransformation -> RenderingTransformation
  }
custom_mark1
-->

Using this pattern creates a open-closed design: If a new transformation is required (such as adding support to template-element for instance), we only need to implement a new transformation class (e.g. ```TemplateElementTransformation```) and add an instance of this new class to the list of transformations of the ```DocumentProcessor```.

The following frameworks used in this app are worth to mention:

* [JSoup](http://jsoup.org/) for parsing HTML, which does character escaping by default ([the default escape mode is base](http://jsoup.org/apidocs/org/jsoup/nodes/Document.OutputSettings.html)).

* [Selenium WebDriver](http://www.seleniumhq.org/projects/webdriver/) for integration tests.

* [JUnit](http://junit.org/) for unit tests.

* [Catch-Exception](https://github.com/Codearte/catch-exception) for unit tests.


I'm using several other auxiliary frameworks, including Guava and Apache Commons. Check the pom file for details.

### <a name="optional-features"></a> OPTIONAL FEATURES

##### <a name="optional-feature-template-inclusion"></a> TEMPLATE INCLUSION

As far as my understanding of the HTML5 template element goes, this feature is not meant to be used in the manner suggested in the requirements.

The template element is defined inside the main page as a reusable component that can be rendered over and over inside the main page by the use of javascript calls.

The "fancy template" includes the use of the HTML5 template element to build a header and footer for the page with the netcentric logo and a configurable slogan text underneath (serving as an example of its use).

You will find the template definition and the javascript code in ```fancyPerson.html```.

Therefore, I have decided to use a ```data-``` attribute for a new transformation class: ```data-inclusion```.

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

##### <a name="optional-feature-local-variable"></a> LOCAL VARIABLE

I have defined a new attribute for this purpose: ```data-local-var```.

For instance, given the following element:

    <h1 data-local-var='person = new Person("Lucy", "Rick", true, 0)'>
        ${person.name}
    </h1>

The expression contained in the ```data-local-var``` attribute will be evaluated before the expressions contained in its inner HTML are rendered.

The node will be transformed as follows:

    <h1>
        Lucy
    </h1>

It works even if there are nested HTML elements inside, e.g.:

    <div data-local-var='person = new Person("Lucy", "Rick", true, 0)'>
        <table>
            <tr>
                <td>Name</td><td>${person.name}</td>
            </tr>
            <tr>
                <td>Spouse</td><td>${person.spouse}</td>
            </tr>
        </table
     </div>

Which would be rendered to:

    <div>
        <table>
            <tr>
                <td>Name</td><td>Lucy</td>
            </tr>
            <tr>
                <td>Spouse</td><td>Rick</td>
            </tr>
        </table
     </div>

Note that the template's expressions are rendered in two phases. The first one by ```DataLocalVarTransformation```, which only affects the elements with the ```data-local-var``` attribute.

The second and last time by ```RenderingTransformation```, which renders all remaining expressions.

Even though both transformations use the same script engine, before an element with ```data-local-var``` is rendered the engine's bindings are saved.

After the element is rendered, the engine's bindings are recovered to the previous state. In this manner, the engine's bindings and the ```data-local-var``` elements bindings are isolated from one another.

The design and implementation of this feature comes from my interpretation of the requirements. Hopefully is not too far off from what the person who wrote the requirements had in mind.

Would be easier if these optional features were defined more precisely (like the mandatory ones), but I guess they were written in this way intentionally as part of the candidate evaluation process.
