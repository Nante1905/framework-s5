package genesis.frontend.components;

import java.util.ArrayList;
import java.util.List;

import handyman.HandyManUtils;

public class ViewComponent {
    List<EntityComponent> components;

    public void addComponents(EntityComponent c) {
        if (components == null) {
            components = new ArrayList<EntityComponent>();
        }
        components.add(c);
    }

    public void generateFile() throws Throwable {
        for (EntityComponent c : getComponents()) {
            HandyManUtils.createFile(c.getPath());
            HandyManUtils.overwriteFileContent(c.getPath(), c.getContent());
        }
    }

    public List<EntityComponent> getComponents() {
        return components;
    }

    public void setComponents(List<EntityComponent> components) {
        this.components = components;
    }

}
