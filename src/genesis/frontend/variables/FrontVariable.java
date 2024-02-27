package genesis.frontend.variables;

import java.util.HashMap;

public class FrontVariable {
    String endLine;
    String exportKey;
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

    public String getExportKey() {
        return exportKey;
    }

    public void setExportKey(String exportKey) {
        this.exportKey = exportKey;
    }

}
