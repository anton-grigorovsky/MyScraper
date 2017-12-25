import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Антон on 21.12.2017.
 */
public class CommandParser {
    private List<String> paths; //Our URLs
    private List<String> words; //Words
    private List<String> commands; //Commands


    //Parsing request and sort commands

    public void parseCommand(String[] command) {
        paths = new ArrayList<>();
        if(!command[0].startsWith("http")) {
            try (BufferedReader reader = new BufferedReader(new FileReader(command[0]))){
                while(reader.ready()) {
                    paths.add(removeUTF8BOM(reader.readLine()).trim());
                }
            }
            catch (Exception e) {e.printStackTrace();}
        }

        else paths.add(command[0]);
        words = Arrays.asList(command[1].split(","));
        commands = new ArrayList<>();
        for(int i = 2; i<command.length; i++) {
            commands.add(command[i]);
        }

    }

    //Remove UTF8BOM and other invisible characters from start of URLs

    private static String removeUTF8BOM(String s) {
        if (s.startsWith("\uFEFF")) {
            s = s.substring(1);
        }
        s = s.substring(s.indexOf("http"), s.length());
        return s;
    }


    public List<String> getPaths() {
        return paths;
    }

    public List<String> getWords() {
        return words;
    }

    public List<String> getCommands() {
        return commands;
    }
}
