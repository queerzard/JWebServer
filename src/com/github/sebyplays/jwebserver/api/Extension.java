package com.github.sebyplays.jwebserver.api;

import com.github.sebyplays.jwebserver.AccessHandler;
import com.github.sebyplays.jwebserver.JWebServer;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.commons.Remapper;
import jdk.internal.org.objectweb.asm.commons.RemappingClassAdapter;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

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

    public void register(){
        jWebServer.registerContext(context, accessHandler);
    }

    public void remove(){
        jWebServer.unregisterContext(context);
    }

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

    @SneakyThrows
    public void initializeResource(){
        //classFile = Class.forName(context.replaceAll("/", "."), true, new URLClassLoader(new URL[]{rootDirectory.toURI().toURL()}, this.getClass().getClassLoader()));
        classFile = Class.forName(className, true, new URLClassLoader(new URL[]{new File(classPath).toURI().toURL()}, this.getClass().getClassLoader()));
        accessHandler = (AccessHandler) classFile.newInstance();

    }

}
