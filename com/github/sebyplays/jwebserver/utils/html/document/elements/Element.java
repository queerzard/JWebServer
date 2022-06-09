package com.github.sebyplays.jwebserver.utils.html.document.elements;

import com.github.sebyplays.jwebserver.utils.html.document.HtmlDocument;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

public class Element {

    @Getter protected String tagName;
    @Getter protected String id = null;
    @Getter protected HtmlDocument document;

    @Getter protected String innerText = "";

    @Getter protected String originalTag = "";

    @Getter protected ArrayList<Element> children = new ArrayList<>();

    @Getter protected HashMap <String, String> attributes = new HashMap<>();


   /* public Element(HtmlDocument document, String id) {
        this.document = document;
        this.id = id;
        document.getFileContents().indexOf(id);
    }*/

    public Element(){}

    public Element(HtmlDocument document, String originalTag) {
        this.document = document;
        this.originalTag = originalTag;
        //<label for="username" id="test">Username</label>
        this.tagName = originalTag.split(" ")[0].replaceAll("<", "").replaceAll(">", "");
        if(originalTag.contains("id="))
            this.id = originalTag.split("id=")[1].split("\"")[1];
    }

    public void setInnerText(String innerText){
        this.innerText = innerText;
    }

    public String getValue(){
        return getAttribute("value");
    }

    public void setValue(String value){
        setAttribute("value", value);
        if(value.isEmpty())
            removeAttribute("value");
    }

    public void addAttribute(String attribute, String value){
        attributes.put(attribute, value);
    }

    public void setAttribute(String attribute, String value){
        addAttribute(attribute, value);
    }

    public String getAttribute(String attribute){
        return attributes.get(attribute);
    }

    public void removeAttribute(String attribute){
        attributes.remove(attribute);
    }

    public void addChild(Element child){
        children.add(child);
    }

    public void removeChild(Element child){
        children.remove(child);
    }

    public String toString(){
        String attributes = "";
        String children = "";

        for(String key : this.attributes.keySet())
            attributes += " " + key + "=\"" + this.attributes.get(key) + "\"";

        for(Element child : this.children)
            children += child.toString();

        return "<" + tagName + attributes + ">" + getInnerText() + children + "</" + tagName + ">";
    }


}
