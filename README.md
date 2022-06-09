# JWebServer
***
## A Java WebServer API

The JWebServer is a Java API that utilizes the integrated sun.httpserver package.
It simplifies the usage of the provided HttpServer.

***
## Features

- [x] Load extensions in form of .class files instead of .html
- [x] An event is being triggered, when accessing contexts.
- [x] Cookies!
- [x] Attach files to a response
- [x] Get a HashMap that contains the given query instead of the plain string, that hasn't been split
- [x] Automatic AccessHandler identification.
- [x] AccessHandler/Contoller Annotation example: ( @AccessContoller("/the/desired/index") )
- [x] Dynamic resource support
- [x] Custom 404 Page
- [x] Improve Cookie-support
- [x] Improve performance
- [x] GET/POST annotation support
- [x] Parameterize request-headers to methods 
- [x] Inbuilt session support
- [ ] Add SSL encryption support

## Used APIs

The JWebServer API is also using a number of apis to function properly:

- [JEvent] - An event api fully written in java.
- [lombok] - Automate your logging variables, and much more.
- [org.json] - A reference implementation of a JSON package in Java.
- [HttpServer] - The sun.httpserver provided by default.
- [JSoup] - A Java HTML parser.

## Usage
***
Classify a variable by the type of the "JWebServer" Object
and initialize it with an instanciated object of the class,
with the desired port inbetween the parentheses of the constructor parameters.

#### JWebServer (Object)
```java
//Here's how to create the object. 
//Remember, the port can vary. You can choose it.
JWebServer jWebServer = new JWebServer(9086);

// Register a context:
// the first parameter is a string, which will define the link under which the context is accessible.
// so to access this context, you'd have to open your brower on "http://localhost:9086/this/is/a"
// Once the link is being opened, the handle() method of the AccessHandler defined in the second parameter is triggered:
        jWebServer.registerContext("this/is/a", new TestContext());

// register annotated controllers:
        jWebServer.registerControllers("some.project.pkg");

// Unregister a context:
        jWebServer.unregisterContext("link/to/context");

// start the server:
        jWebServer.start();

// stop the server:
        jWebServer.stop();

// Get the hashmap containinh the registered contexts:
        jWebServer.getHttpContexts();

// Get the hasmap containing the loaded extensions:
        jWebServer.getExtensions();
        
//Set custom 404 page:
        jWebServer.setNotFoundPage(new File("/path/to/404.html"));

// The key to the value of both of the HashMaps mentioned 
// above is same the path for the browser to access the contexts
```

#### Extensions (Object)
You can load extensions with the loadExtensions(); method included in the JWebServer class.
The location of the extensions is being generated on application startup.
The extensions folder is located in the folder of the application file.

Path:
(user.dir/data/www/class/)

```java
// load all extensions located in the path:
        jWebServer.loadExtensions();

// the extension class contains methods for registering the extension, 
// removing and reloading the extension.


```


#### AccessHandler (Object)
The AccessHandler is the essential part, that is accessed in the end.
It includes the code, that is being executed, when calling the website.

```java
// To create an AccessHandler, that you can register in order to execute code,
// you simply have to create a class, that is inheriting from the mentioned class with the "extend" inheritance-keyword.
// Your IDE will prompt you to implement the "handle()" method. Do that.
// Once you have implemented the method, your class should look like in the example.
// Return a response object to send it.

import com.github.sebyplays.jwebserver.api.AccessHandler;
import com.github.sebyplays.jwebserver.api.Status;

public class YourCustomHandler extends AccessHandler {
    public Response handle(HttpExchange httpExchange, AccessHandler accessHandler) {
        return new Response(Status.OK, "IT WORKED!" + this.getQuery());
    }
}

    //Get the queryMap from the accessed site:
    getQueryMap();

//Example usecase:
    if(getQueryMap().containsKey("token")&&getQueryMap().get("token").equals("someAccessToken"))
            return new Response(Status.OK,"Your token is valid. yay. ok. now go on.");
            return new Response(Status.OK,"Your key is invalid..");

//Send a response:
// You basically start off with returning a newly created instance of the Response class.
// This class contains multiple constructors for different usecases, 
// choose the one that suits your needs.

            return new Response(Status.OK,"This is a Text-response");

// Prompts the user to download the file specified.
// The first parameter is defining the status of the response.
// The second parameter is deciding whether the file should be downloaded or not.
// The third parameter is the file to be downloaded.

            return new Response(Status.OK,true,new File("/path/to/file.txt"));

// can display the file specified in combination of the setContentType() method.  
            return new Response(Status.OK,false,new File(System.getProperty("user.dir")+"/some/image.png"));

// You can also get and set cookies to the response headers

// get cookies by key:
            getCookie("KeyOfCookie");

// set cookie:
            setCookie("KeyOfCookie","ValueOfCookie");

// remove cookie:
            removeCookie("KeyOfCookie");

// get a list of cookies:
            getCookies();

// set content-type of response:
            setContentType(ContentType contentType);




```


#### ContextAccessedEvent
Listeners of this event are being triggered,
whenever the setHttpHandler method is called.

Look up my project page of the [JEvent] on GitHub to see how to work with it.

***
### Annotations

#### @Register
```
//The @Register class annotation is used to identify classes, that shall be automatically registered,
//when the "registerControllers(String packageNameOfClasses)" has been invoked
//so it's not neccessary anymore to execute the "registerContext(String s, AccessHandler a)" 
//method in the JWebServer class for each class you want to register.



//This annotation must always come along with the following annotation.

```

#### @AccessController

```
//The @AccessContoller class annotation defines the path 
//under which the registered handler can be accessed.

//The default path is the name of the class 
//if there's not an index specified.

//The priority of the handler was specified by the @Register annotation in the past,
//but is now defined by the @AccessController annotation, 
//as it's simpler and a lot more logical.
```

##### Annotated Example

```java
package some.project.pkg;

import com.github.sebyplays.jwebserver.api.AccessHandler;
import com.github.sebyplays.jwebserver.api.Response;
import com.github.sebyplays.jwebserver.api.Status;
import com.github.sebyplays.jwebserver.api.annotations.ParamReqHeaders;
import com.github.sebyplays.jwebserver.api.annotations.methods.POST;
import com.github.sebyplays.jwebserver.utils.enums.Priority;
import com.github.sebyplays.jwebserver.api.annotations.AccessController;
import com.github.sebyplays.jwebserver.api.annotations.Register;

//Define the controllers link
@AccessController(index = "link/of/controller", priority = Priority.DEFAULT)
//Tell the method to register this class
@Register
public class YourCustomHandler extends AccessHandler {

    @Override
    public Response handle(HttpExchange httpExchange, AccessHandler accessHandler) {
        return new Response(Status.OK, "IT WORKED!" + this.getQuery());
    }

    //Methods annotated with "@RequestMethod" are executed before the handler is invoked,
    //providing you a simpler way to handle specific requests.
    //The @RequestParam("key") annotation is used to define the headers that are required for the request.
    //It will pass the desired values to the parameters of the method.
    @RequestMethod("ALL")
    public void handlePost(@RequestParam("email") String email, @RequestParam("password") String password) {
        //Do something
    }
}

//So in this example, the webpage can be opened by the browser of your choice with the link 
// http://localhost:8096/link/of/controller
```


## License

MIT

****


[JEvent]: <https://github.com/SebyPlays/JEvent>
[lombok]: <https://projectlombok.org/>
[org.json]: <https://github.com/stleary/JSON-java>
[HttpServer]: <https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpServer.html>
[JSoup]: <https://jsoup.org/>
