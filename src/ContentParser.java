import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Антон on 21.12.2017.
 */
public class ContentParser {
    private BufferedReader reader;
    private URL url;
    private long scrapingTime;

    //Constructor
    public ContentParser(String path) {
        try {
            System.out.println("------------------------------");
            System.out.println(path);
            System.out.println("------------------------------");
            this.url = new URL(path);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Parsing text from site, remove tags
    public String getText() {
        long startTime = System.currentTimeMillis();
        StringBuffer sb = new StringBuffer();
        boolean isBody = false;
        boolean noScript = true;
        try {
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.contains("<body")) isBody = true;
                if(line.contains("<!--")) isBody = false;
                if(line.toLowerCase().contains("<script")) noScript = false;
                if(line.toLowerCase().contains("</script>")) noScript = true;
                if(line.toLowerCase().contains("<style")) noScript = false;
                if(line.toLowerCase().contains("</style>")) noScript = true;
                if(line.contains("<body")||line.contains("-->")) isBody = true;
                if(line.contains("<div class=\"printfooter\">")) line+=reader.readLine();
                if(isBody&&noScript) {
                    line = line.replaceAll("<script.*<\\/script>", "").
                            replaceAll("<div class=\"printfooter\">.*</div>", "").
                            replaceAll("</p>", "\n").
                            replaceAll("<li>","\n").
                            replaceAll("\\<.*?>","").
                            replaceAll("&nbsp;"," ").
                            replaceAll("-->","").
                            replaceAll("\\&\\#[0-9]*\\;", " ").
                            replaceAll("&quot;", "\"").
                            replaceAll("&laquo;", "\"").
                            replaceAll("&amp;", " ").trim();

                    if(!line.equals(""))
                            sb.append(line).append("\n");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();}
        String text = sb.toString();
        scrapingTime = System.currentTimeMillis() - startTime;
        return text;
    }

    public long getScrapingTime() {
        return scrapingTime;
    }
}
