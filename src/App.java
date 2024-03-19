import java.nio.file.FileSystems;

import command.Add;
import command.Generate;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "genesis", description = "Genesis CLI", subcommands = {
        Generate.class,
        Add.class
})
public class App implements Runnable {
    public static void main(String[] args) throws Throwable {
        CommandLine.run(new App(), args);
    }

    @Override
    public void run() {
        System.out.println("Welcome to Genesis App !");
    }

    @Command(name = "pwd")
    public void pwd() {
        System.out.println("Current directory: " + System.getProperty("user.dir"));
        System.out.println("Chemin d'accès actuel : " + System.getenv("PWD"));
        System.out
                .println("Chemin d'accès actuel : " + FileSystems.getDefault().getPath("").toAbsolutePath().toString());
    }
}
