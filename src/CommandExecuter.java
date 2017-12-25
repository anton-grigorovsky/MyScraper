import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Антон on 21.12.2017.
 */
public class CommandExecuter {
    private CommandParser commandParser;
    private int[] totalData;

    public CommandExecuter(String[] command) {
        commandParser = new CommandParser();
        commandParser.parseCommand(command);
        totalData = new  int[4];
    }


    public void execute() {
        for(String s: commandParser.getPaths()) {
            Validator.checkURL(s); //Check the command
            analyzePage(s); //Get needed data from one page
        }
        System.out.println();
        for(String c: commandParser.getCommands()) {  //Print total data
            switch (c) {
            case "-c":
                System.out.println("Total number of characters on pages: " + totalData[3]);
                break;
            case "-w":
                System.out.println("Total number of word matches on pages: " + totalData[2]);
                break;
            case "-v":
                System.out.println("Total time of data scraping: " + totalData[0]);
                System.out.println("Total time of data processing: " + totalData[1]);
            }
        }
    }

    //
    private void analyzePage(String path) {
        long startTime = System.currentTimeMillis();
        ContentParser contentParser = new ContentParser(path);
        String text = contentParser.getText();
        List<String> words = commandParser.getWords();
        List<String> commands = commandParser.getCommands();
        for(String c: commands) {
            switch (c) {
                case "-c":
                    int length = text.length();
                    System.out.println("Number of character in text on page " + path + ": " + length);
                    totalData[3]+=length;
                    break;
                case "-w":
                    for(String w: words) {
                        int count = 0;
                        Pattern p = Pattern.compile(w.toLowerCase());
                        Matcher m = p.matcher(text.toLowerCase());
                        while (m.find()) count++;
                        System.out.println("Number of words \"" + w  + "\" on page \"" + path  + "\" is " + count);
                        totalData[2]+=count;
                    }
                    break;
                case "-e":
                    for(String w: words) {
                        List<String> sentences = new ArrayList<>();
                        getSentences(w, text, sentences);
                        if(sentences.size()==0) {
                            System.out.println("There're no word \"" + w + "\" on page " + path + " . " );
                            continue;
                        }
                        System.out.println("The word \"" + w + "\" occurred in sentences on page " + path + ": " );
                        for(String s: sentences) {
                            System.out.println("- " + s);
                        }
                    }
                    break;
                case "-v":
                    totalData[0] += contentParser.getScrapingTime();
                    long endTime = System.currentTimeMillis();
                    totalData[1] += endTime - startTime;
                    System.out.println("Time spend on data scraping in " + path + ": " + contentParser.getScrapingTime() + "ms");
                    System.out.println("Time spend on data processing in " + path + ": " + (endTime - startTime) + "ms");
                    break;

            }
        }
    }


// Get sentences with needed word using recursion
    private void getSentences(String word, String text, List<String> sentences) {
        Pattern p = Pattern.compile(word.toLowerCase());
        Matcher m = p.matcher(text.toLowerCase());
        int index = 0;

        if(m.find()) {
            index =  m.start();
        }
        else return;

        int start = 0;
        int end = 0;
        String startChars = ".!?\n;";
        String endChars = ".!?\n;";
        for(int i = index; i>0; i--) {
            if(startChars.contains(Character.toString(text.charAt(i)))) {
                start = i+1;
                break;
            }

        }
        for(int i = index; i<text.length(); i++) {
            if(endChars.contains(Character.toString(text.charAt(i)))) {
                for (int j = i; j<text.length(); j++) {
                    if(endChars.contains(Character.toString(text.charAt(j))))
                        continue;
                    else {
                        end = j;
                        break;
                    }

                }
            }
            if(end != 0) break;

        }
        String sentence = text.substring(start, end);
        if(sentence.startsWith(")")||sentence.startsWith("(")||sentence.startsWith("^")) sentence = sentence.substring(1, sentence.length());
        sentences.add(sentence.trim());
        String subText = text.substring(end, text.length()-1);

        if(subText.toLowerCase().contains(word.toLowerCase())) {
            getSentences(word, subText, sentences);
        }

        return;



    }

}
