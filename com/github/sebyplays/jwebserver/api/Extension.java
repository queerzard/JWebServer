package com.github.sebyplays.jwebserver.api;

import com.github.sebyplays.jwebserver.JWebServer;
import com.github.sebyplays.jwebserver.utils.Utilities;
import com.github.sebyplays.jwebserver.api.annotations.AccessController;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * It loads a class file from the class directory, and creates an instance of it
 */
public class Extension extends ClassLoader{

    @Getter private File file;
    @Getter private File rootDirectory = new File(System.getProperty("user.dir") + "/data/www/class/");
    @Getter private String context;
    @Getter private JWebServer jWebServer;

    @Getter private Class classFile;
    @Getter private String className;
    @Getter private String classPath;
    @Getter private AccessHandler accessHandler;

    @SneakyThrows
    public Extension(JWebServer jWebServer, String path){
        file = new File(System.getProperty("user.dir") + "/data/www/class/" + path);
        if(file.isDirectory())
            return;
        if(!file.getName().endsWith(".class"))
            return;
        context = path.replaceAll("\\.class", "");
        className = file.getName().replaceAll("\\.class", "");
        classPath = file.getParentFile().getAbsolutePath();
        System.out.println(context);
        this.jWebServer = jWebServer;
        initializeResource();
    }

    /**
     * "Register a context with the server, and associate it with an access handler."
     *
     * The context is a string that represents the context of the request. For example, if you want to handle requests to
     * the URL http://localhost:8080/hello, then the context would be "/hello"
     */
    public void register(){
        jWebServer.registerContext(context, accessHandler);
    }

    /**
     * Remove the context from the server.
     */
    public void remove(){
        jWebServer.unregisterContext(context);
    }

    /**
     * Remove the old resource, nullify all the variables, and then re-initialize the resource.
     *
     * @return A boolean value.
     */
    public boolean reload(){
        remove();
        accessHandler = null;
        classFile = null;
        className = null;
        System.gc();
        if(!file.exists())
            return false;
        initializeResource();
        register();
        return true;
    }

    /**
     * It loads the class file from the classpath, instantiates it, and then checks if the class is annotated with the
     * @AccessController annotation. If it is, it sets the context to the value of the index() method of the annotation
     */
    @SneakyThrows
    public void initializeResource(){
        classFile = Class.forName(context.replaceAll("/", "."), true, new URLClassLoader(new URL[]{rootDirectory.toURI().toURL()}, this.getClass().getClassLoader()));
        //classFile = Class.forName(className, false, new URLClassLoader(new URL[]{new File(classPath).toURI().toURL()}, this.getClass().getClassLoader()));
        accessHandler = (AccessHandler) classFile.newInstance();
        if(!classFile.isAnnotationPresent(AccessController.class))
            return;
        AccessController accessController = (AccessController) Utilities.getAnnotation(classFile, AccessController.class);
        context = accessController.index();
    }

}
