import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Антон on 22.12.2017.
 */
public class Validator {
    //Check the request
    public static void validate(String[] command) {
        List<String> commands = Arrays.asList("-v", "-w", "-c", "-e");
        String message = "Wrong request! Try again. Pattern: \"java -jar scraper.jar https://en.wikipedia.org/wiki/Google Microsoft,Apple -v -w -c -e\"";
        if(command.length<2||command.length>6) throw new IncorrectCommandException(message);
        else if(command.length==2) {
            if(!command[1].equals("-c")||!command[1].equals("-v"))
                throw new IncorrectCommandException(message);
        }
        else {
            for (int i = 2; i < command.length; i++) {
                if(!commands.contains(command[i])) throw new IncorrectCommandException(message);
            }
        }
    }

    //Check URL
    public static void checkURL (String url) {
        Pattern pattern = Pattern.compile("^https?:\\/\\/(([a-z0-9_-]+)\\.)?(([a-z0-9_-]+)\\.)([a-z]{2,6})(\\/.*)?$");
        Matcher matcher = pattern.matcher(url);
        if(!matcher.find()) throw new IncorrectCommandException("Wrong URL address " + url+ "! Check it. Example of right URL: http://test.ru");
    }
}
