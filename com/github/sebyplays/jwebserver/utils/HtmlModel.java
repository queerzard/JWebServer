package com.github.sebyplays.jwebserver.utils;

import com.github.sebyplays.jwebserver.api.Response;
import com.github.sebyplays.jwebserver.api.Status;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;

/**
 * It takes an HTML file, and replaces all the keys in the model with their values
 */
public class HtmlModel {

    @Getter private HashMap<String, String> model = new HashMap<>();

    @Getter private File file;

    private String htmlContent;

    @Getter private Document document;

    @SneakyThrows
    public HtmlModel(File file){
        this.file = file;
        this.htmlContent = new String(Files.readAllBytes(file.toPath()), "UTF-8");
        this.document = Jsoup.parse(htmlContent);
    }

    public HtmlModel(String htmlSource){
        this.htmlContent = htmlSource;
        this.document = Jsoup.parse(htmlSource);
    }

    /**
     * This function clears the attributes of the model
     */
    public void clearAttributes(){
        model.clear();
    }

    /**
     * This function adds a key-value pair to the model.
     *
     * @param key The name of the attribute to bind to.
     * @param value The value to be added to the model.
     */
    public void addModelAttribute(String key, String value){
        model.put(key, value);
    }

    /**
     * Removes the attribute with the given name from the model
     *
     * @param key The key of the model attribute to remove.
     */
    public void removeModelAttribute(String key){
        model.remove(key);
    }

    /**
     * It takes the HTML source code of the document, and replaces all the keys in the model with their values
     *
     * @return The HTML content of the document with the model values replaced.
     */
    public String getHtmlContent(){
        String source = document.html();
        for(String key : model.keySet()){
            source = source.replace("{" + key + "}", model.get(key));
        }
        return source;
    }

    /**
     * This function returns the raw HTML content of the document.
     *
     * @return The raw html content of the page.
     */
    public String getRawHtmlContent(){
        return htmlContent;
    }

    public Response getProcessedResponse(Status... status){
        return new Response(status.length == 1 ? status[0] : Status.OK, this, false);
    }

    /**
     * This function sets the text of the element with the given id to the given text.
     *
     * @param elementId The id of the element you want to change the text of.
     * @param text The text to be set.
     */
    public void setElementText(String elementId, String text){
        document.getElementById(elementId).text(text);
    }

}
