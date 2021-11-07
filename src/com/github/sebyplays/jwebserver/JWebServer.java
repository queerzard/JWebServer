package com.github.sebyplays.jwebserver;

import com.github.sebyplays.jwebserver.utils.Priority;
import com.github.sebyplays.jwebserver.utils.Type;
import com.github.sebyplays.jwebserver.utils.Utilities;
import com.github.sebyplays.jwebserver.utils.annotations.AccessController;
import com.github.sebyplays.jwebserver.api.Extension;
import com.github.sebyplays.jwebserver.utils.annotations.Register;
import com.github.sebyplays.jwebserver.utils.exceptions.ContextAlreadyAssignedException;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import  com.sun.net.httpserver.HttpExchange;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class JWebServer {

    @Getter private HttpServer httpServer;
    @Getter private int port;

    @Getter private SSLContext sslContext = SSLContext.getInstance("TLS");

    @Getter private HashMap<String, HttpContext> httpContexts = new HashMap<>();
    @Getter private HashMap<String, Extension> extensions = new HashMap<>();

    @Getter @Setter private static File notFoundPage = new File(JWebServer.class.getResource("/com/github/sebyplays/jwebserver/utils/html/404.html").getFile());

    public static final File rootClassDir = new File(System.getProperty("user.dir") + "/data/www/class/");
    public static final File rootHtmlDir = new File(System.getProperty("user.dir") + "/data/www/html/");

    //TODO Find the part of the native java code, where context access attempts are processed,
    // inherit from these classes and override the method with their basic functionality
    // and your specific improvements in addition.

    //TODO Inform yourself about the com.sun.net.httpserver.HTTPExchange and the javax.xml.ws.Endpoint Object.
    // Both classes contain the information of how the context processing works.
    // Prepare yourself for Mindf*ck once you returned!
    // Greets, You.

    //TODO Never mind, the step mentioned above wasn't necessary, we solved it in the example handler named "Index"
    // adding those features was easier than expected.
    @SneakyThrows
    public JWebServer(Type httpsOrHttp, int port) throws NoSuchAlgorithmException {
        this.port = port;
        if(httpsOrHttp == Type.HTTP){
            try {
                httpServer = httpServer.create(new InetSocketAddress(port), 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            httpServer = HttpsServer.create(new InetSocketAddress(port), 0);
            ((HttpsServer)httpServer).setHttpsConfigurator(new HttpsConfigurator(sslContext));
        }
        httpServer.setExecutor(Executors.newCachedThreadPool());
        if(!rootClassDir.exists())
            rootClassDir.mkdirs();

        if(!rootHtmlDir.exists())
            rootHtmlDir.mkdirs();
    }


    @SneakyThrows
    public static void main(String[] args) {
        JWebServer jWebServer = new JWebServer(Type.HTTP, 13141);
        jWebServer.loadExtensions();
        jWebServer.registerControllers("com.github.sebyplays.jwebserver.testHandler");
        jWebServer.start();
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
        if(httpContexts.containsKey(name)) {
            httpContexts.remove(name);
            httpServer.removeContext("/" + name);
        }
    }

    public void loadExtensions(){
        loadExtensions(rootClassDir, "");
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

    @SneakyThrows
    public void registerControllers(String packageName){
        Class[] classes = Utilities.getClasses(packageName);
        for (Class clazz : classes){
            if(clazz.getSuperclass() == (AccessHandler.class) &&
                    clazz.isAnnotationPresent(Register.class) &&
                    clazz.isAnnotationPresent(AccessController.class)){
                AccessController accessController = (AccessController) Utilities.getAnnotation(clazz, AccessController.class);
                Register register = (Register) Utilities.getAnnotation(clazz, Register.class);
                if(!httpContexts.containsKey(accessController.index()) || register.priority() == Priority.PRIORITIZED){
                    unregisterContext(accessController.index());
                    registerContext(accessController.index(), (AccessHandler) clazz.newInstance());
                    continue;
                }
                throw new ContextAlreadyAssignedException("We've encountered an error! Key already assigned: /" + accessController.index());
            }
        }
    }



}
