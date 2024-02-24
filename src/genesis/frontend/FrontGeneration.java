package genesis.frontend;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import genesis.Constantes;
import genesis.frontend.variables.FrontLangage;
import genesis.frontend.variables.ImportVariable;
import genesis.frontend.variables.PageImport;
import handyman.HandyManUtils;

/**
 * frontGeneration
 */
public class FrontGeneration {

    // ilay import type mbola mila jerena
    // atao manokana angamba: mampiasa ImportVariable.getImportTemplate("member")
    public static String generateImport(FrontLangage langage, PageImport[] toImports) throws Throwable {
        String content = "";
        String template = HandyManUtils.getFileContent(Constantes.FRONT_TEMPLATE_DIR + "/import-template.templ");
        Pattern pattern;
        Matcher matcher;
        String startTag = "", endTag = "";

        HashMap<String, ImportVariable> importVariables = langage.getVariables().getImports();
        String importFormat = "";

        for (PageImport i : toImports) {
            startTag = "&&" + i.getType() + "&&";
            endTag = "&&end" + HandyManUtils.majStart(i.getType()) + "&&";
            pattern = Pattern.compile(startTag + "(.*?)" + endTag, Pattern.DOTALL);
            matcher = pattern.matcher(template);

            while (matcher.find()) {
                importFormat = matcher.group(1).substring(1);
                // replace langage variable
                importFormat = importFormat.replace("[importKey]", importVariables.get(i.getType()).getImportKey());
                importFormat = importFormat.replace("[importStart]", importVariables.get(i.getType()).getImportStart());
                importFormat = importFormat.replace("[importEnd]", importVariables.get(i.getType()).getImportEnd());
                importFormat = importFormat.replace("[importFrom]", importVariables.get(i.getType()).getImportFrom());
                importFormat = importFormat.replace("[endLine]", langage.getVariables().getEndLine());

                // replace page variable
                importFormat = importFormat.replace("%importSource%", i.getSource());
                if (i.getElements() != null) {
                    importFormat = importFormat.replace("%toImport%",
                            i.getElements().toString().substring(1, i.getElements().toString().length() - 1));
                }
                content += importFormat;
            }
        }
        return content;
    }

}