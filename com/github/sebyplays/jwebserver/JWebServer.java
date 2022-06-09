package com.github.sebyplays.jwebserver;

import com.github.sebyplays.jwebserver.api.AccessHandler;
import com.github.sebyplays.jwebserver.api.HttpSession;
import com.github.sebyplays.jwebserver.utils.HtmlModel;
import com.github.sebyplays.jwebserver.utils.enums.Priority;
import com.github.sebyplays.jwebserver.utils.enums.Type;
import com.github.sebyplays.jwebserver.utils.Utilities;
import com.github.sebyplays.jwebserver.api.annotations.AccessController;
import com.github.sebyplays.jwebserver.api.Extension;
import com.github.sebyplays.jwebserver.api.annotations.Register;
import com.github.sebyplays.jwebserver.utils.exceptions.ContextAlreadyAssignedException;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.Executors;

public class JWebServer {

    @Getter private HttpServer httpServer;
    @Getter private int port;
    @Getter private Type protocol;

    @Getter private SSLContext sslContext = SSLContext.getInstance("TLS");

    @Getter private HashMap<String, HttpContext> httpContexts = new HashMap<>();
    @Getter private HashMap<String, Extension> extensions = new HashMap<>();

    @Getter @Setter private static File notFoundPage = new File(JWebServer.class.getResource("/com/github/sebyplays/jwebserver/utils/html/404.html").getFile());

    public static final File rootClassDir = new File(System.getProperty("user.dir") + "/data/www/class/");
    public static final File rootHtmlDir = new File(System.getProperty("user.dir") + "/data/www/html/");

    public static HashMap<String, HtmlModel> pageCache = new HashMap<>();

    // It's creating a new JWebServer instance.
    @SneakyThrows
    public JWebServer(Type httpsOrHttp, int port) throws NoSuchAlgorithmException {
        this.port = port;
        this.protocol = httpsOrHttp;
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

        HttpSession.sessionPurger();

    }


    @SneakyThrows
    public static void main(String[] args) {
        JWebServer jWebServer = new JWebServer(Type.HTTP, 13141);
        jWebServer.loadExtensions();
        jWebServer.registerControllers("com.github.sebyplays.jwebserver.test.testHandler");
        jWebServer.start();
    }

    public void start(){
        httpServer.start();
    }

    public void stop(){
        httpServer.stop(0);
    }

    /**
     * If the name of the context doesn't exist in the hashmap, then create a new context with the name and the access
     * handler.
     *
     * @param name The name of the context. This is the name that will be used in the URL.
     * @param accessHandler This is the class that will handle the requests.
     */
    public void registerContext(String name, AccessHandler accessHandler){
        if(!httpContexts.containsKey(name))
            httpContexts.put(name, httpServer.createContext("/" + name, accessHandler));
    }

    /**
     * If the context exists, remove it from the list of contexts and remove it from the server
     *
     * @param name The name of the context. This is the name that will be used in the URL.
     */
    public void unregisterContext(String name){
        if(httpContexts.containsKey(name)) {
            httpContexts.remove(name);
            httpServer.removeContext("/" + name);
        }
    }

    /**
     * > It loads all the classes in the rootClassDir directory and all its subdirectories
     */
    public void loadExtensions(){
        loadExtensions(rootClassDir, "");
    }

    /**
     * It loads all the extensions in the extensions folder
     *
     * @param startPath The directory to start searching for extensions in.
     * @param path The path to the extension.
     */
    private void loadExtensions(File startPath, String path){
        String paths = path;
        for(File file : startPath.listFiles()){
            if(file.isDirectory()){
                paths = paths + file.getName() + "/";
                loadExtensions(file, paths);
            } else {
                if(file.getName().toLowerCase().endsWith("class") || file.getName().toLowerCase().endsWith("java")){
                    File okFile = file;
                    if(file.getName().toLowerCase().endsWith("java"))
                        okFile = Utilities.compile(file);

                    Extension extension;
                    if(!extensions.containsKey(path + okFile.getName().split("\\.")[0])){
                        extension = new Extension(this, path + okFile.getName());
                        extensions.put(path + okFile.getName().split("\\.")[0], extension);
                        extension.register();
                    } else {
                        if(!(extension = extensions.get(path + okFile.getName().split("\\.")[0])).reload()){
                            extensions.remove(path + okFile.getName().split("\\.")[0]);
                        }
                    }
                }
            }
        }
    }

    /**
     * It gets all the classes in the package, checks if they're an AccessHandler, if they're annotated with Register and
     * AccessController, and if they are, it registers them
     *
     * @param packageName The package name of the controllers.
     */
    @SneakyThrows
    public void registerControllers(String packageName){
        Class[] classes = Utilities.getClasses(packageName);
        for (Class clazz : classes){
            if(clazz.getSuperclass() == (AccessHandler.class) && clazz.isAnnotationPresent(Register.class) &&
                    clazz.isAnnotationPresent(AccessController.class)){
                AccessController accessController = (AccessController) Utilities.getAnnotation(clazz, AccessController.class);
                if(!httpContexts.containsKey(accessController.index()) || accessController.priority() == Priority.PRIORITIZED){
                    unregisterContext(accessController.index());
                    registerContext(accessController.index().equals("{nameOfClass}") ? clazz.getSimpleName().toLowerCase() : accessController.index(), (AccessHandler) clazz.newInstance());
                    continue;
                }
                throw new ContextAlreadyAssignedException("We've encountered an error! Key already assigned: /" + accessController.index());
            }
        }
    }


}
