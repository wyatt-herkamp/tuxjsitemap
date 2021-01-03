package me.kingtux.tuxjsitemap;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class SiteMap {
    private String indexFile;
    //This can be null
    private Map<String, String> subFiles;

    public SiteMap(String toString) {
        this.indexFile = toString;
    }

    public SiteMap(String toString, Map<String, String> pagesString) {
        indexFile = toString;
        subFiles = pagesString;
    }

    public String getSubPage(String sub) {
        return subFiles.get(sub);
    }

    public Map<String, String> getSubFiles() {
        return subFiles;
    }

    public String getIndexPage() {
        return indexFile;
    }

    public void writeToFolder(File folder) throws IOException {
        if (!folder.exists()) folder.mkdir();
        FileUtils.writeStringToFile(new File(folder, "sitemap.xml"), indexFile, "UTF-8");
        if (subFiles != null) {
            for (Map.Entry<String, String> entry : subFiles.entrySet()) {
                String s = entry.getKey();
                String s2 = entry.getValue();
                FileUtils.writeStringToFile(new File(folder, String.format("sitemap-%1$s.xml", s)), s2, "UTF-8");
            }
        }
    }
}
