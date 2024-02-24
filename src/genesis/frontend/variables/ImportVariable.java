package genesis.frontend.variables;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import genesis.Constantes;
import handyman.HandyManUtils;

public class ImportVariable {
    String importKey;
    String importStart;
    String importEnd;
    String importFrom;

    public static String getImportTemplate(String type) throws Throwable {
        String template = HandyManUtils.getFileContent(Constantes.FRONT_TEMPLATE_DIR + "/import-template.templ");
        String startTag = "&&" + type + "&&";
        String endTag = "&&end" + HandyManUtils.majStart(type) + "&&";
        Pattern pattern = Pattern.compile(startTag + "(.*?)" + endTag, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(template);

        while (matcher.find()) {
            return matcher.group(1).substring(1);
        }
        throw new Exception("Unknown type. Allowed import types are: member, singlz, global");
    }

    public String getImportKey() {
        return importKey;
    }

    public void setImportKey(String importKey) {
        this.importKey = importKey;
    }

    public String getImportStart() {
        return importStart;
    }

    public void setImportStart(String importStart) {
        this.importStart = importStart;
    }

    public String getImportEnd() {
        return importEnd;
    }

    public void setImportEnd(String importEnd) {
        this.importEnd = importEnd;
    }

    public String getImportFrom() {
        return importFrom;
    }

    public void setImportFrom(String importFrom) {
        this.importFrom = importFrom;
    }

}
