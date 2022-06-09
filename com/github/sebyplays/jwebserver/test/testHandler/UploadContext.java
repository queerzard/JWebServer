package com.github.sebyplays.jwebserver.test.testHandler;

import com.github.sebyplays.jwebserver.api.AccessHandler;
import com.github.sebyplays.jwebserver.JWebServer;
import com.github.sebyplays.jwebserver.api.Response;
import com.github.sebyplays.jwebserver.api.Status;
import com.github.sebyplays.jwebserver.api.annotations.AccessController;
import com.github.sebyplays.jwebserver.api.annotations.Register;
import com.sun.net.httpserver.HttpExchange;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;


@Register
@AccessController(index = "upload")
public class UploadContext extends AccessHandler {
    @Override
    public Response handle(HttpExchange httpExchange, AccessHandler accessHandler, HashMap<String, Object> args) {
        try {
            String html = new String(Files.readAllBytes(new File(JWebServer.rootHtmlDir.getAbsolutePath() + "/upload.html").toPath()));
            Document document = Jsoup.parse(html);
            if(httpExchange.getRequestMethod().equals("POST")) {
                byte[] bytes = readAllBytes(httpExchange.getRequestBody());


                System.out.println("File uploaded - " + bytes.length + " bytes total.");
                //Files.copy(out, fileToSave.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }

            return new Response(Status.OK, document.html());
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @SneakyThrows
    public byte[] readAllBytes(InputStream inputStream) {
        byte[] bytes = new byte[inputStream.available()];
        int read = 0;

        while (inputStream.available() > 0) {
            bytes[read] = (byte) inputStream.read();
            read++;
        }
        return bytes;
    }

}
