package com.github.sebyplays.jwebserver.utils.html.document;

import com.github.sebyplays.jwebserver.utils.html.document.elements.Element;
import com.github.sebyplays.jwebserver.utils.html.document.elements.Paragraph;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class HtmlDocument extends Element{

    @Getter private String fileContents = "";
    @Getter private File file;

    @Getter private ArrayList<Element> elements = new ArrayList<>();

    public HtmlDocument(){
        tagName = "html";
    }
    public HtmlDocument(File file){
        loadDocument(file);
    }


    @SneakyThrows
    public void loadDocument(File file){
        this.file = file;
        this.fileContents = new String(Files.readAllBytes(file.toPath()));
        //parseHtml();
    }



    //I ended up using JSoup
  /*  public void parseHtml(){
        //split elements by tags
        String[] elements = fileContents.replace("<!DOCTYPE html>", "").split("<");
        int i = 0;
        int i2 = 0;
        ArrayList<String> elements2 = new ArrayList<>();
        ArrayList<String> elements3 = new ArrayList<>();

        for (String element : elements){
            elements2.add("<" + element);
            if(i < elements.length) i++;
        }
        elements = elements2.toArray(new String[elements2.size()]);

        i = 0;
        for(String element : elements){
            System.out.println(element);
            if(element.contains("</")){

                String[] elementParts = element.split("</");
                for (String element2 : elementParts) elements3.add("<" + element2); if(i < elementParts.length) i++;
                elementParts = elements3.toArray(new String[elements3.size()]);

                String tagName = elementParts[0];
                String content = elementParts[1];
                Element element1 = new Element(this, tagName + content);
            //    System.out.println("TT" + element1);
                this.elements.add(new Element(this, tagName + content));
            }
        }
    }*/


    public Element getElementById(String id){
        for(Element e : elements){
            if(e.getId() != null && e.getId().equals(id)){
                return e;
            }
        }
        return null;
    }

    public Element[] getElementsByTagName(String tagName){
        ArrayList<Element> elements = new ArrayList<>();
        for(Element e : this.elements){
            if(e.getTagName().toLowerCase().contains(tagName.toLowerCase())){
                System.out.println(e.getTagName());
                elements.add(e);
            }
        }
        if(elements.size() == 1)
            return new Element[]{elements.get(0)};
        if(elements.size() == 0)
            return null;
        return elements.toArray(new Element[elements.size()]);
    }

    //Testing stuff
    @SneakyThrows
    public static void main(String[] args) {
        Document document = Jsoup.parse(new String(Files.readAllBytes(new File(System.getProperty("user.dir") + "/data/www/html/login.html").toPath())));
        document.toString();
        HtmlDocument htmlDocument = new HtmlDocument(new File(System.getProperty("user.dir") + "/data/www/html/login.html"));;
        //System.out.println(Arrays.toString(htmlDocument.getElementsByTagName("title")));
        Paragraph p = new Paragraph("Hello World");
        p.addAttribute("style", "background-color: green;");
        System.out.println(p);
    }




}
