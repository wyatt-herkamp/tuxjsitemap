package me.kingtux.tuxjsitemap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SiteURL {
    private final static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    private Date lastMod;
    private String url;
    private ChangeFrequency changeFrequency;
    private double priority;

    public SiteURL(String fullUrl, ChangeFrequency cf, long lastMod, double priority) {
        if (lastMod != 0) {
            this.lastMod = longToDate(lastMod);
        }
        url = fullUrl;
        this.changeFrequency = cf;
        this.priority = priority;
    }

    public Element build(Document document) {
        Element element = document.createElement("url");
        Element location = document.createElement("loc");
        location.setTextContent(url);
        element.appendChild(location);
        //Add Optional Elements
        if (lastMod != null) {
            Element lastmod = document.createElement("lastmod");
            location.setTextContent(df.format(lastMod));
            element.appendChild(lastmod);
        }
        if (changeFrequency != null) {
            Element cf = document.createElement("changefreq");
            location.setTextContent(changeFrequency.name().toLowerCase());
            element.appendChild(cf);
        }
        if (priority != 0.0) {
            Element cf = document.createElement("priority");
            location.setTextContent(String.valueOf(priority));
            element.appendChild(cf);
        }
        return element;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private Date longToDate(long l) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(l);
        return calendar.getTime();
    }
}
