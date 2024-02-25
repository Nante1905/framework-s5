package genesis.frontend.variables;

public class FrontLangage {
    int id;
    String name;
    String extension;
    String skeleton;
    FrontVariable variables;
    FrontPage[] pages;

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

    public FrontPage[] getPages() {
        return pages;
    }

    public void setPages(FrontPage[] pages) {
        this.pages = pages;
    }

    public String getSkeleton() {
        return skeleton;
    }

    public void setSkeleton(String skeleton) {
        this.skeleton = skeleton;
    }

}
