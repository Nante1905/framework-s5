package genesis.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import genesis.Constantes;
import genesis.Entity;
import genesis.EntityField;
import genesis.frontend.variables.FrontLangage;
import genesis.frontend.variables.FrontPage;
import genesis.frontend.variables.ImportVariable;
import genesis.frontend.variables.PageImport;
import handyman.HandyManUtils;

/**
 * frontGeneration
 */
public class FrontGeneration {

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

    public static String generateList(FrontLangage langage, Entity e) throws Throwable {
        String listTemplate = HandyManUtils.getFileContent(Constantes.LIST_TEMPLATE);
        String tableHeadTemplate = HandyManUtils.getFileContent(Constantes.TABLEHEAD_TEMPLATE);
        String tableBodyTemplate=HandyManUtils.getFileContent(Constantes.TABLEBODY_TEMPLATE);
        String fkGetterTemplate = HandyManUtils.getFileContent(Constantes.FRONT_TEMPLATE_FK);
        String finalContent = "";
        String tablehead="",tablebody="",fkGetters = "";
        FrontPage form = langage.getPages().get("form");
        //String typeFile = langage.getFolders().get("type");
        // List<PageImport> imports = langage.getPages().get("form").getImports();
        
        int columnCount = 0;
        for (EntityField field : e.getFields()) {
            if (field.isPrimary()) {
                listTemplate = listTemplate.replace("[fieldpk]", field.getName());

            }  else {
                String start = "%%tableHead%%";
                String end = "%%endtableHead%%";
                
                String fieldHeadTemplate = FrontGeneration.extractPartTemplate(start, end, tableHeadTemplate).group(1);
            
                String fieldHead = fieldHeadTemplate.replace("[field]", field.getName());
                tablehead+=fieldHead;
                
                String startBody = "%%tableBody%%";
                String endBody = "%%endtableBody%%";
        
                String fieldBodyTemplate = FrontGeneration.extractPartTemplate(startBody, endBody, tableBodyTemplate).group(1);
                
                    if (field.isForeign()) {
                        String fieldbody = fieldBodyTemplate.replace("[field]", field.getName())
                                                             .replace("[field2]", "nom");
                        tablebody+=fieldbody;
                        fkGetters += FrontGeneration.generateForeignGetter(e.getColumns()[columnCount], fkGetterTemplate);
                    } else {
                        String fieldBodyNoFk = fieldBodyTemplate.replace("[field]", field.getName())
                                                                 .replace("." + "[field2]", ""); 
                        tablebody+=fieldBodyNoFk;
                    }    
                       
            }
            columnCount++;
            
            
        }
        tablehead+=("<TableCell colSpan={2} className=\"text-center\">Actions</TableCell>");
        listTemplate = listTemplate.replace("[entityMaj]", HandyManUtils.majStart(e.getClassName()));
        listTemplate = listTemplate.replace("[entityMin]", HandyManUtils.minStart(e.getClassName()));
        listTemplate = listTemplate.replace("<tableHead>", tablehead);
        listTemplate = listTemplate.replace("<tableBody>", tablebody);
        listTemplate = listTemplate.replace("<foreignKeyGetter>", fkGetters);
        listTemplate = listTemplate.replace("<import>", generateImport(langage, form.getImports()));
        // inputs = FrontGeneration.generateInputs(e, inputTemplate);
        finalContent = listTemplate;
        return finalContent;
    }
}