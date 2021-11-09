package com.github.sebyplays.jwebserver.testHandler;

import com.github.sebyplays.jwebserver.AccessHandler;
import com.github.sebyplays.jwebserver.JWebServer;
import com.github.sebyplays.jwebserver.api.Response;
import com.github.sebyplays.jwebserver.api.Status;
import com.github.sebyplays.jwebserver.utils.ContentType;
import com.github.sebyplays.jwebserver.utils.Priority;
import com.github.sebyplays.jwebserver.utils.annotations.AccessController;
import com.github.sebyplays.jwebserver.utils.annotations.Register;
import com.sun.net.httpserver.HttpExchange;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@AccessController(index = "")
@Register(priority = Priority.DEFAULT)
public class Index extends AccessHandler {

    private String[] supportedFileExtensions = new String[]{".html", ".css", ".txt", ".png", ".gif", ".jpg", ".mp4", ".js", ".ico", ".svg"};

    @SneakyThrows
    @Override
    public Response handle(HttpExchange httpExchange, AccessHandler accessHandler) {
        File file = new File(JWebServer.rootHtmlDir.getAbsolutePath() + getHttpExchange().getRequestURI().toString());
        File tmpFile = file;

        if(!file.exists()){
            setContentType(ContentType.TEXT_HTML);
            return new Response(Status.NOT_FOUND, false, JWebServer.getNotFoundPage());
        }

        if(file.getName().endsWith(".html") || file.isDirectory() && (tmpFile = containsFile(file, "index.html")) != null)
            return new Response(Status.OK, Files.readAllBytes(tmpFile.toPath()));

        if(!isSupported(file.getName()))
            return new Response(Status.OK, true, file);

        setType(file.getName());
        return new Response(Status.OK, false, file);
    }

    //set content type if file extension is contained in supported file extensions array

    public void setType(String fileName){
        if(isSupported(fileName.split("\\.")[fileName.split("\\.").length -1])){
            if(fileName.endsWith(".css"))
                setContentType(ContentType.TEXT_CSS);
            else if(fileName.endsWith(".txt"))
                setContentType(ContentType.TEXT_PLAIN);
            else if(fileName.endsWith(".png"))
                setContentType(ContentType.IMAGE_PNG);
            else if(fileName.endsWith(".gif"))
                setContentType(ContentType.IMAGE_GIF);
            else if(fileName.endsWith(".jpg"))
                setContentType(ContentType.IMAGE_JPEG);
            else if(fileName.endsWith(".mp4"))
                setContentType(ContentType.VIDEO_MP4);
            else if(fileName.endsWith(".js"))
                setContentType(ContentType.APPLICATION_X_JAVASCRIPT);
            else if(fileName.endsWith(".ico"))
                setContentType(ContentType.IMAGE_X_ICON);
            else if(fileName.endsWith(".svg"))
                setContentType(ContentType.IMAGE_SVG_XML);
            else if(fileName.endsWith(".html"))
                setContentType(ContentType.TEXT_HTML);
        }
    }
    //if array contains string

    public boolean isSupported(String fileName){
        for(String extension : supportedFileExtensions){
            if(fileName.endsWith(extension))
                return true;
        }
        return false;
    }
    //directory contains file with name

    public File containsFile(File directory, String fileName){
        for(File file : directory.listFiles()){
            if(file.getName().equalsIgnoreCase(fileName))
                return file;
        }
        return null;
    }
}