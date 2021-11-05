# JWebServer
## A Java WebServer API

The JWebServer is a Java API that utilizes the integrated sun.httpserver package.
It simplifies the usage of the provided HttpServer.


## Features

- [x] Load extensions in form of .class files instead of .html
- [x] An event is being triggered, when accessing contexts.
- [x] Cookies!
- [x] Attach files to a response
- [x] Get a HashMap that contains the given query instead of the plain string, that hasn't been split
- [ ] Automatic AccessHandler identification.
- [ ] AccessHandler/Contoller Annotation example: ( @AccessContoller("/the/desired/index") )
- [ ] .html support
- [ ] CSS support
- [ ] Resource support in general. example: ( Dragging images or other types of data into a folder. Automatically index resource )
- [ ] Set 404 Page
- [ ] Dynamic resources
- [ ] Improve Cookie-support

## Used APIs

The JWebServer API is also using a number of apis to function properly:

- [JEvent] - An event api fully written in java.
- [lombok] - Automate your logging variables, and much more.
- [org.json] - A reference implementation of a JSON package in Java.
- [HttpServer] - The sun.httpserver provided by default.

## Usage

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
// Once the link is being opened, the handle() method of the AccessHandler defined in the second parameter is triggered.
jWebServer.registerContext("this/is/a", new TestContext());

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
jWebServer.loadExtensions()

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
// The very first line in the handle method must call the method named
// "setHttpExchange" of the super-class, with the parameter shown in the example. otherwhise it won't work, because
// the code that is following up internally, uses the object.

public class YourCustomHandler extends AccessHandler {
    public void handle(HttpExchange httpExchange) throws IOException {
        setHttpExchange(httpExchange);
        respond(200, "IT WORKED!" + this.getQuery());
    }
}

//Get the queryMap from the accessed site:
getQueryMap();

//Example usecase:
if(getQueryMap().containsKey("token") && getQueryMap().get("token").equals("someAccessToken")){
    respond(200, "Your token is valid. yay. ok. now go on.");
    return;
} else {
    respond(200, "Your key is invalid..")
}

//Send a response:
// To send a response, you have to either call the normal respond() method 
// or the fileResponse() method depending on what you want to send.
// The normal respond() method has two parameters, 
// the first is for the http response codes 
// and the second is for the string you want to deliver.
// You can also send html source code through the string and it will be displayed as  
// it's supposed to be.

respond(200, "<html><h1>This is a header</h1></html>");

fileResponse(200, new File(System.getProperty("user.dir") + "/some/file.txt"));

// You can also get and set cookies to the response headers

// get cookies by key:
getCookie("KeyOfCookie");

// set cookie:
setCookie("KeyOfCookie", "ValueOfCookie");

// remove cookie:
removeCookie("KeyOfCookie");

// get a list of cookies:
getCookies();


```

#### ContextAccessedEvent
Listeners of this event are being triggered, 
whenever the setHttpHandler method is called.

Look up my project page of the [JEvent] on GitHub to see how to work with it.


## License

MIT

**Free Software, Hell Yeah!**

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

[JEvent]: <https://github.com/SebyPlays/JEvent>
[lombok]: <https://projectlombok.org/>
[org.json]: <https://github.com/stleary/JSON-java>
[HttpServer]: <https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpServer.html>
