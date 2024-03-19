package genesis.frontend.components;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import handyman.HandyManUtils;

public class ViewComponent {
    List<EntityComponent> components;

    public void addComponents(EntityComponent c) {
        if (components == null) {
            components = new ArrayList<EntityComponent>();
        }
        components.add(c);
    }

    public void generateFile(Scanner scanner) throws Throwable {
        for (EntityComponent c : getComponents()) {
            if (new File(c.getPath()).exists()) {
                System.out.println(c.getPath() + " already exists and will not be overwritten.");
                // if (scanner.next().trim() == "y") {
                // HandyManUtils.overwriteFileContent(c.getPath(), c.getContent());
                // }
            } else {
                HandyManUtils.createFile(c.getPath());
                HandyManUtils.overwriteFileContent(c.getPath(), c.getContent());
            }
        }
    }

    public List<EntityComponent> getComponents() {
        return components;
    }

    public void setComponents(List<EntityComponent> components) {
        this.components = components;
    }

}
