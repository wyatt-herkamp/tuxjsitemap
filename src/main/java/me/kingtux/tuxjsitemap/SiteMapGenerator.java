package me.kingtux.tuxjsitemap;

import com.google.common.collect.Lists;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("All")
public class SiteMapGenerator {
    private final String urlPattern = "%1$s/%2$s";
    private String url;
    private String subPattern = "%1$s/sitemap-%2$s.xml";
    private List<SiteURL> siteURLList = new ArrayList<>();

    public SiteMapGenerator(String url) {
        this.url = url;
    }


    public SiteMapGenerator addURL(String path) {
        return addURL(path, null);
    }

    public SiteMapGenerator addURL(String path, ChangeFrequency cf) {
        return addURL(path, cf, 0);
    }

    public SiteMapGenerator addURL(String path, ChangeFrequency cf, long lastMod) {
        return addURL(path, cf, lastMod, 0.0);
    }

    public SiteMapGenerator addURL(String path, ChangeFrequency cf, long lastMod, double priority) {
        String fullUrl = String.format(urlPattern, url, path);
        siteURLList.add(new SiteURL(fullUrl, cf, lastMod, priority));
        return this;
    }

    public SiteMap build() {
        SiteMap siteMap;
        if (siteURLList.size() <= 50000) {
            Document document = TJSMUtils.docBuilder.newDocument();
            Element element = document.createElement("urlset");
            element.setAttribute("xmlns", "http://www.sitemaps.org/schemas/sitemap/0.9");
            document.appendChild(element);
            for (SiteURL s : siteURLList) {
                element.appendChild(s.build(document));
            }
            siteMap = new SiteMap(TJSMUtils.toString(document));
        } else {
            List<List<SiteURL>> lists = Lists.partition(siteURLList, 50000);
            Map<Integer, Document> pages = new HashMap<>();
            int i = 0;
            for (List<SiteURL> urls : lists) {
                i++;
                Document document = TJSMUtils.docBuilder.newDocument();
                Element element = document.createElement("urlset");
                element.setAttribute("xmlns", "http://www.sitemaps.org/schemas/sitemap/0.9");
                for (SiteURL s : urls) {
                    element.appendChild(s.build(document));
                }
                document.appendChild(element);
                pages.put(i, document);
            }
            Document document = TJSMUtils.docBuilder.newDocument();

            Element sitemapindex = document.createElement("sitemapindex");
            document.appendChild(sitemapindex);
            sitemapindex.setAttribute("xmlns", "http://www.sitemaps.org/schemas/sitemap/0.9");

            for (int j = 1; j <= i; j++) {
                Element item = document.createElement("sitemap");
                sitemapindex.appendChild(item);

                Element loc = document.createElement("loc");
                loc.setTextContent(String.format(subPattern, url, j));
                item.appendChild(loc);
            }
            Map<String, String> pagesString = new HashMap<>();
            pages.forEach((integer, document1) -> pagesString.put(String.valueOf(integer), TJSMUtils.toString(document1)));
            siteMap = new SiteMap(TJSMUtils.toString(document), pagesString);
        }
        return siteMap;
    }

}
