package genesis.frontend.variables;

import java.util.HashMap;

public class FrontLangage {
    int id;
    String name;
    String extension;
    String skeleton;
    FrontVariable variables;
    HashMap<String, FrontPage> pages;
    HashMap<String, String> folders;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public FrontVariable getVariables() {
        return variables;
    }

    public void setVariables(FrontVariable variables) {
        this.variables = variables;
    }

    public String getSkeleton() {
        return skeleton;
    }

    public void setSkeleton(String skeleton) {
        this.skeleton = skeleton;
    }

    public HashMap<String, FrontPage> getPages() {
        return pages;
    }

    public void setPages(HashMap<String, FrontPage> pages) {
        this.pages = pages;
    }

    public HashMap<String, String> getFolders() {
        return folders;
    }

    public void setFolders(HashMap<String, String> folders) {
        this.folders = folders;
    }

}
