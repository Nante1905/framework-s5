package genesis.frontend.variables;

import java.util.List;

import handyman.HandyManUtils;

public class FrontPage {
    String name;
    String path;
    String template;
    List<PageImport> imports;

    public void addImports(List<PageImport> a) {
        name = name + "h";
        for (PageImport p : a) {
            addImport(p);
        }
    }

    public void addImport(PageImport a) {
        for (PageImport p : getImports()) {
            if (p.getSource().equals(a.getSource())) {
                p.addElement(a.getElements());
                return;
            }
        }
        imports.add(a);
    }

    public void setPath(String projectName, String projectFrontName, String entityName, String extension) {
        String p = getPath().replace(("[projectName]"), projectName)
                .replace("[projectFrontName]", projectFrontName);
        if (entityName != null) {
            p = p.replace("[entityMin]", HandyManUtils.minStart(entityName));
        }
        if (extension != null) {
            p += "." + extension;
        }
        setPath(p);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PageImport> getImports() {
        return imports;
    }

    public void setImports(List<PageImport> imports) {
        this.imports = imports;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

}
