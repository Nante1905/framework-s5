package genesis.frontend.variables;

import java.util.HashMap;

public class FrontVariable {
    String endLine;
    HashMap<String, ImportVariable> imports;

    public String getEndLine() {
        return endLine;
    }

    public void setEndLine(String endLine) {
        this.endLine = endLine;
    }

    public HashMap<String, ImportVariable> getImports() {
        return imports;
    }

    public void setImports(HashMap<String, ImportVariable> imports) {
        this.imports = imports;
    }

}
