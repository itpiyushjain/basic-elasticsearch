package com.piyush.rssfeedreader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jain06 on 4/4/15.
 */
public abstract class FeedReader {

    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String SOURCE = "source";
    public static final String IDENTIFIER = "identifier";
    public static final String LINK = "link";
    public static final String AUTHOR = "creator";
    public static final String ITEM = "item";

    public void readFeed() throws IOException, XMLStreamException {
        InputStream is = read();
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(is);
        boolean isFeedHeader = true;

        Map<String, String> itemContent = new HashMap<String, String>();

        while(eventReader.hasNext()){
            XMLEvent event = eventReader.nextEvent();
            if(event.isStartElement()){
                String localPart = event.asStartElement().getName().getLocalPart();
                if(ITEM.equals(localPart)){
                    if(isFeedHeader) {
                        isFeedHeader = false;
                    }
                }
                else if(TITLE.equals(localPart)){
                    itemContent.put(TITLE,getData(event, eventReader));
                }
                else if(DESCRIPTION.equals(localPart)){
                    itemContent.put(DESCRIPTION,getData(event, eventReader));
                }
                else if(LINK.equals(localPart)){
                    itemContent.put(LINK,getData(event, eventReader));
                }
                else if(AUTHOR.equals(localPart) && itemContent.get(AUTHOR) == null){
                    //add only 1 author for now, for sake of simplicity
                    itemContent.put(AUTHOR,getData(event, eventReader));
                }
                else if(SOURCE.equals(localPart)){
                    itemContent.put(SOURCE,getData(event, eventReader));
                }
                else if(IDENTIFIER.equals(localPart)){
                    itemContent.put(IDENTIFIER,getData(event,eventReader));
                }
            } else if(event.isEndElement()){
                if(ITEM.equals(event.asEndElement().getName().getLocalPart())){
                    populateModel(itemContent);
                    itemContent.clear();
                }
            }
            continue;
        }
    }

    private String getData(XMLEvent event, XMLEventReader eventReader) throws XMLStreamException {

        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }

    private InputStream read() throws IOException {
        return getFeedURL().openStream();
    }

    protected abstract URL getFeedURL() throws MalformedURLException;
    protected abstract void populateModel(Map<String,String> itemContent);
}
