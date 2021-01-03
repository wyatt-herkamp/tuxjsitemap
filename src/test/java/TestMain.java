import me.kingtux.tuxjsitemap.SiteMap;
import me.kingtux.tuxjsitemap.SiteMapGenerator;

import java.io.File;
import java.io.IOException;

public class TestMain {
    public static void main(String[] args) {
        SiteMapGenerator mapGenerator = new SiteMapGenerator("https://kingtux.me");
        mapGenerator.addURL("").addURL("home").addURL("about");
        for (int i = 0; i < 85232; i++) {
            mapGenerator.addURL("post/"+i);
        }
        SiteMap siteMap = mapGenerator.build();
        try {
            siteMap.writeToFolder(new File("tests"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
