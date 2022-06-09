package com.github.sebyplays.jwebserver.utils.html.document.elements;

import com.github.sebyplays.jwebserver.utils.html.document.HtmlDocument;

public class Paragraph extends Element{
    public Paragraph(String text) {
        setText(text);
        tagName = "p";
    }

    public Paragraph(){
        tagName = "p";
    }

    public void setText(String text){
        this.setInnerText(text);
    }
}
