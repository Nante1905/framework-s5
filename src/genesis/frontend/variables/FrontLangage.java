package genesis.frontend.variables;

import java.util.HashMap;
import java.util.List;

public class FrontLangage {
    int id;
    String name;
    String extension;
    String skeleton;
    FrontVariable variables;
    HashMap<String, FrontPage> pages;
    HashMap<String, String> folders;
    HashMap<String, String> files;
    HashMap<String, List<PageImport>> optionalImports;

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

    public HashMap<String, String> getFiles() {
        return files;
    }

    public void setFiles(HashMap<String, String> files) {
        this.files = files;
    }

    public HashMap<String, List<PageImport>> getOptionalImports() {
        return optionalImports;
    }

    public void setOptionalImports(HashMap<String, List<PageImport>> optionalImports) {
        this.optionalImports = optionalImports;
    }

}
