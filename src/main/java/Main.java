import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.HJDBuilder;

public class Main {
    public static void main(String[] args){
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        String token = "PihoCLytOG9oS3henjkNQVY0cVNwL9iy";
        builder.setToken(token);
    }
}
