package command;

import genesis.Project;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "generate", description = "Generate a new project")
public class Generate implements Runnable {

    @Option(names = { "-n", "--name" }, description = "Project name")
    String name;

    @Override
    public void run() {
        try {
            Project.generate(name, "");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
