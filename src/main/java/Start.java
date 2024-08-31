import org.hinoob.pharadox.PharadoxBot;

public class Start {

    public static void main(String[] args) {
        PharadoxBot bot = new PharadoxBot();
        PharadoxBot.setInstance(bot);
        bot.start();
    }
}
