package genesis.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import genesis.Constantes;
import genesis.Entity;
import genesis.EntityField;
import genesis.frontend.variables.FrontLangage;
import genesis.frontend.variables.ImportVariable;
import genesis.frontend.variables.PageImport;
import handyman.HandyManUtils;

/**
 * frontGeneration
 */
public class FrontGeneration {

    public String generateForm(FrontLangage langage, Entity e) throws Throwable {
        String formTemplate = HandyManUtils.getFileContent(Constantes.FRONT_TEMPLATE_FORM);
        String inputTemplate = HandyManUtils.getFileContent(Constantes.FRONT_TEMPLATE_INPUT);
        String fkGetterTemplate = HandyManUtils.getFileContent(Constantes.FRONT_TEMPLATE_FK);
        String finalContent = "";
        String inputs = "", fkGetters = "";
        List<PageImport> imports = langage.getPages().get("form").getImports();

        for (EntityField field : e.getFields()) {
            if (field.isPrimary()) {
                System.out.println("atao hidden");
            }
            if (field.isForeign() == false) {

            }
        }

        return finalContent;
    }

    public static void rewriteEnv(FrontLangage langage, String projectName, String projectFrontName) throws Throwable {
        String envPath = projectName + "/" + projectFrontName + "/" + langage.getFiles().get("env")
                .replace("[projectName]", projectName)
                .replace("[projectFrontName]", projectFrontName);
        System.out.println(envPath);
        String content = HandyManUtils.getFileContent(envPath);
        content = content.replace("[projectNameMaj]", HandyManUtils.majStart(projectName));
        HandyManUtils.overwriteFileContent(envPath, content);
    }

    public static Matcher extractPartTemplate(String startKey, String endKey, String content) throws Exception {
        Pattern p = Pattern.compile(startKey + "(.*?)" + endKey, Pattern.DOTALL);
        Matcher m = p.matcher(content);
        while (m.find()) {
            return m;
        }
        throw new Exception("Nothing is between the startKey and endKey");
    }

    // ilay import type mbola mila jerena
    // atao manokana angamba: mampiasa ImportVariable.getImportTemplate("member")
    public static String generateImport(FrontLangage langage, List<PageImport> toImports) throws Throwable {
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