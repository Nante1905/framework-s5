package genesis.frontend.variables;

import java.util.List;

public class PageImport {
    String type;
    List<String> elements;
    String source;

    public PageImport(String type, List<String> elements, String source) {
        setType(type);
        setElements(elements);
        setSource(source);
    }

    public PageImport() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getElements() {
        return elements;
    }

    public void setElements(List<String> elements) {
        this.elements = elements;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
