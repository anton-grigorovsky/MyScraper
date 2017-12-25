
/**
 * Created by Антон on 23.12.2017.
 */
public class MyScraper {
    public static void main(String [] args)
    {
        String[] request = {"C:\\Users\\Антон\\Desktop\\Java\\la.txt", "Microsoft,Apple", "-v", "-w", "-c", "-e"};
        Validator.validate(request);

        CommandExecuter executer = new CommandExecuter(request);

        executer.execute();

    }
}
