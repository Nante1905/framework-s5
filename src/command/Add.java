package command;

import genesis.Project;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "add", description = "Add a new entity")
public class Add implements Runnable {
    @Option(names = { "-n", "--name" }, description = "Project name")
    String name;

    @Parameters(index = "0", description = "Object to add (ex: entity)")
    String objectToAdd;

    @Parameters(index = "1", description = "Name of the object to add (ex: User)")
    String objectName;

    @Override
    public void run() {
        try {
            if (!this.objectToAdd.equals("entity")) {
                System.out.println("Unknow object to add");
                return;
            }
            Project.generate(this.name, this.objectName);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
