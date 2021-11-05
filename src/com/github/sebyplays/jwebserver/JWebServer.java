package com.github.sebyplays.jwebserver;

import com.github.sebyplays.jwebserver.utils.Utilities;
import com.github.sebyplays.jwebserver.utils.annotations.AccessController;
import com.github.sebyplays.jwebserver.api.Extension;
import com.github.sebyplays.jwebserver.utils.annotations.Register;
import com.github.sebyplays.jwebserver.utils.exceptions.ContextAlreadyAssignedException;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class JWebServer {

    @Getter private HttpServer httpServer;
    @Getter private int port;

    @Getter private HashMap<String, HttpContext> httpContexts = new HashMap<>();
    @Getter private HashMap<String, Extension> extensions = new HashMap<>();
    private File rootDir = new File(System.getProperty("user.dir") + "/data/www/class/");


    @SneakyThrows
    public JWebServer(int port){
        this.port = port;
        httpServer = httpServer.create(new InetSocketAddress(port), 0);
        httpServer.setExecutor(null);
        if(!rootDir.exists())
            rootDir.mkdirs();
    }

    public void start(){
        httpServer.start();
    }

    public void stop(){
        httpServer.stop(0);
    }

    public void registerContext(String name, AccessHandler accessHandler){
        if(!httpContexts.containsKey(name))
            httpContexts.put(name, httpServer.createContext("/" + name, accessHandler));
    }

    public void unregisterContext(String name){
        httpServer.removeContext("/" + name);
        if(httpContexts.containsKey(name))
            httpContexts.remove(name);
    }

    public void loadExtensions(){
        loadExtensions(rootDir, "");
    }

    private void loadExtensions(File startPath, String path){
        String paths = path;
        for(File file : startPath.listFiles()){
            if(file.isDirectory()){
                paths = paths + file.getName() + "/";
                loadExtensions(file, paths);
            } else {
                if(file.getName().toLowerCase().endsWith("class")){
                    Extension extension;
                    if(!extensions.containsKey(path + file.getName().split("\\.")[0])){
                        extension = new Extension(this, path + file.getName());
                        extensions.put(path + file.getName().split("\\.")[0], extension);
                        extension.register();
                    } else {
                        if(!(extension = extensions.get(path + file.getName().split("\\.")[0])).reload()){
                            extensions.remove(path + file.getName().split("\\.")[0]);
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        JWebServer jWebServer = new JWebServer(13141);
        jWebServer.registerControllers("com.github.sebyplays.jwebserver.handler");
        jWebServer.start();
    }

    public Annotation getAnnotation(Class clazz, Class annotation){
        if(clazz.isAnnotationPresent(annotation))
            return clazz.getAnnotation(annotation);
        return null;
    }

    @SneakyThrows
    public void registerControllers(String packageName){
        Class[] classes = Utilities.getClasses(packageName);
        for (Class clazz : classes){
            if(clazz.getSuperclass() == (AccessHandler.class) &&
                    clazz.isAnnotationPresent(Register.class) &&
                    clazz.isAnnotationPresent(AccessController.class)){
                AccessController accessController = (AccessController) getAnnotation(clazz, AccessController.class);
                if(!httpContexts.containsKey(accessController.index())){
                    registerContext(accessController.index(), (AccessHandler) clazz.newInstance());
                    return;
                }
                throw new ContextAlreadyAssignedException("We've encountered an error! Key already assigned " + accessController.index());
            }
        }
    }

}
