package genesis.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import genesis.Constantes;
import genesis.Entity;
import genesis.EntityColumn;
import genesis.EntityField;
import genesis.frontend.components.EntityComponent;
import genesis.frontend.components.ViewComponent;
import genesis.frontend.variables.FrontLangage;
import genesis.frontend.variables.FrontPage;
import genesis.frontend.variables.ImportVariable;
import genesis.frontend.variables.PageImport;
import handyman.HandyManUtils;

/**
 * frontGeneration
 */
public class FrontGeneration {
    public static int SUBSTACT_SPACE = 5;

    public static void generateView(FrontLangage langage, Entity e, String projectName, String projectFrontName,
            Scanner scanner)
            throws Throwable {

        ViewComponent view = new ViewComponent();
        EntityComponent form = generateForm(langage, e);
        form.setPath(Constantes.CURRENT_DIR + form.getPath().replace("[projectName]", projectName)
                .replace("[projectFrontName]", projectFrontName));
        view.addComponents(form);

        EntityComponent liste = generateList(langage, e);
        liste.setPath(Constantes.CURRENT_DIR + liste.getPath().replace("[projectName]", projectName)
                .replace("[projectFrontName]", projectFrontName));
        view.addComponents(liste);

        EntityComponent type = generateType(langage, e);
        type.setPath(Constantes.CURRENT_DIR + type.getPath().replace("[projectName]", projectName)
                .replace("[projectFrontName]", projectFrontName));
        view.addComponents(type);

        view.generateFile(scanner);
    }

    public static EntityComponent generateType(FrontLangage langage, Entity e) throws Throwable {
        String typeTemplate = HandyManUtils.getFileContent(Constantes.FRONT_TEMPLATE_TYPE);
        Matcher fieldMatcher = extractPartTemplate("&&field&&", "&&endField&&", typeTemplate);
        String fieldTemplate = fieldMatcher.group(1).substring(1);
        String content;
        content = typeTemplate.replace("[entityMaj]", HandyManUtils.majStart(e.getClassName()));
        FrontPage typeFile = langage.getPages().get("type");

        @SuppressWarnings("unchecked")
        HashMap<String, String> inputTypes = HandyManUtils.fromJson(HashMap.class,
                HandyManUtils.getFileContent(Constantes.FIELD_TYPES));
        String fieldContents = "";
        for (EntityField f : e.getFields()) {
            if (f.isForeign()) {
                fieldContents += fieldTemplate.replace("[field]", f.getName()).replace("[fieldType]",
                        f.getType());
                // add Import
                PageImport p = new PageImport("member", new ArrayList<String>() {
                    {
                        add(HandyManUtils.majStart(f.getType()));
                    }
                }, "./" + typeFile.getName().replace("[entityMaj]", HandyManUtils.majStart(f.getType())));

                typeFile.addImport(p);
            } else {
                fieldContents += fieldTemplate.replace("[field]", f.getName()).replace("[fieldType]",
                        inputTypes.get(f.getType()));
            }
        }
        fieldContents += fieldTemplate.replace("[field]", "label").replace("[fieldType]", "string");

        String imports = generateImport(langage, typeFile.getImports());
        content = content.replace("<import>", imports);
        content = content.replace(fieldMatcher.group(0), fieldContents);
        EntityComponent type = new EntityComponent();
        type.setContent(content);
        type.setPath(typeFile.getPath().replace("[entityMaj]", HandyManUtils.majStart(e.getClassName())));
        return type;
    }

    public static EntityComponent generateForm(FrontLangage langage, Entity e) throws Throwable {
        System.out.println("Generating form for " + e.getClassName());
        String formTemplate = HandyManUtils.getFileContent(Constantes.FRONT_TEMPLATE_FORM);
        String inputTemplate = HandyManUtils.getFileContent(Constantes.FRONT_TEMPLATE_INPUT);
        String fkGetterTemplate = HandyManUtils.getFileContent(Constantes.FRONT_TEMPLATE_FK);
        String loadTemplate = HandyManUtils.getFileContent(Constantes.FRONT_TEMPLATE_LOADING);
        String finalContent = "";
        String inputs = "", fkGetters = "", fkState = "", fkInitState = "", loading = "";
        FrontPage form = langage.getPages().get("form");
        String typeFile = langage.getFolders().get("type");

        // List<PageImport> defaultImports =
        // langage.getPages().get("form").getImports();

        @SuppressWarnings("unchecked")
        HashMap<String, String> inputTypes = HandyManUtils.fromJson(HashMap.class,
                HandyManUtils.getFileContent(Constantes.INPUT_TYPES));

        int columnCount = 0;
        for (EntityField field : e.getFields()) {
            if (field.isPrimary()) {
                // String pkInput = FrontGeneration.extractPartTemplate("&&inputPk&&",
                // "&&endInputPk&&", inputTemplate)
                // .group(1);
                // pkInput = pkInput.replace("[field]", field.getName());
                // inputs += pkInput;

                formTemplate = formTemplate.replace("[entityPkField]", field.getName());
            } else if (field.isForeign()) {
                // loading
                if (loading.length() == 0) {
                    loading = loadTemplate;
                }

                String select = FrontGeneration.extractPartTemplate("&&select&&", "&&endSelect&&", inputTemplate)
                        .group(1);
                select = select.replace("[inputLabel]", HandyManUtils.formatReadable(field.getName()));
                select = select.replace("[field]", field.getName());
                select = select.replace("[type]", field.getType());

                EntityColumn c = e.getColumns()[columnCount];
                select = select.replace("[entityFkField]", HandyManUtils.toCamelCase(c.getReferencedTable()));
                select = select.replace("[entityFkFieldPk]", field.getReferencedField());

                inputs += select;
                fkGetters += FrontGeneration.generateForeignGetter(e.getColumns()[columnCount], fkGetterTemplate);

                String[] fkStateGenerated = FrontGeneration.generateFkState(field);
                fkState += fkStateGenerated[0];
                fkInitState += fkStateGenerated[1];
                // add Import
                form.addImports(langage.getOptionalImports().get("select"));
                PageImport p = new PageImport("member", new ArrayList<String>() {
                    {
                        add(HandyManUtils.majStart(field.getName()));
                    }
                }, "../../" + typeFile.replace("[entityMaj]", HandyManUtils.majStart(field.getName())));

                form.addImport(p);
            } else {
                String fieldInputType = inputTypes.get(field.getType());
                String start = "&&input" + HandyManUtils.majStart(fieldInputType) + "&&";
                String end = "&&endInput" + HandyManUtils.majStart(fieldInputType) + "&&";
                form.addImports(langage.getOptionalImports().get(fieldInputType));

                String input = FrontGeneration.extractPartTemplate(start, end, inputTemplate).group(1);
                input = input.replace("[inputLabel]", HandyManUtils.formatReadable(field.getName()));
                input = input.replace("[field]", field.getName());

                inputs += input;
            }
            columnCount++;
        }

        formTemplate = formTemplate.replace("[entityMaj]", HandyManUtils.majStart(e.getClassName()));
        formTemplate = formTemplate.replace("[entityMin]", HandyManUtils.minStart(e.getClassName()));
        formTemplate = formTemplate.replace("<input>", inputs);
        formTemplate = formTemplate.replace("<foreignKeyGetter>", fkGetters);
        formTemplate = formTemplate.replace("<foreignKeyState>", fkState);
        formTemplate = formTemplate.replace("<foreignKeyInitialState>", fkInitState);
        formTemplate = formTemplate.replace("<import>", generateImport(langage, form.getImports()));
        formTemplate = formTemplate.replace("<loading>", loading);

        finalContent = formTemplate;

        EntityComponent component = new EntityComponent();
        component.setContent(finalContent);
        component.setPath(form.getPath().replace("[entityMin]", HandyManUtils.minStart(e.getClassName())));

        return component;
    }

    public static String[] generateFkState(EntityField field) {
        String fkStateTempl = "[field] : [type][];\n";
        String fkInitStateTempl = "[field] : [],\n";

        fkStateTempl = fkStateTempl.replace("[field]", field.getName());
        fkStateTempl = fkStateTempl.replace("[type]", field.getType());

        fkInitStateTempl = fkInitStateTempl.replace("[field]", field.getName());

        return new String[] { fkStateTempl, fkInitStateTempl };

    }

    public static String generateForeignGetter(EntityColumn column, String fkGetterTemplate) {

        String template = "";
        template += fkGetterTemplate.replace("[foreignKeyEntity]",
                HandyManUtils.toCamelCase(column.getReferencedTable()));

        return template;
    }

    public static void rewriteEnv(FrontLangage langage, String projectName, String projectFrontName) throws Throwable {
        String envPath = projectName + "/" + projectFrontName + "/" + langage.getFiles().get("env")
                .replace("[projectName]", projectName)
                .replace("[projectFrontName]", projectFrontName);
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
        throw new Exception("Nothing is between " + startKey + " and " + endKey);
    }

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

    public static EntityComponent generateList(FrontLangage langage, Entity e) throws Throwable {
        System.out.println("generating list for " + e.getClassName());
        String listTemplate = HandyManUtils.getFileContent(Constantes.LIST_TEMPLATE);
        String tableHeadTemplate = HandyManUtils.getFileContent(Constantes.TABLEHEAD_TEMPLATE);
        String tableBodyTemplate = HandyManUtils.getFileContent(Constantes.TABLEBODY_TEMPLATE);
        String fkGetterTemplate = HandyManUtils.getFileContent(Constantes.FRONT_TEMPLATE_FK);
        String finalContent = "";
        String tablehead = "", tablebody = "", fkGetters = "";

        FrontPage listePage = langage.getPages().get("list");
        listePage.setName(listePage.getName().replace("[entity]", e.getClassName().toLowerCase()));
        String typeFile = langage.getFolders().get("type");
        int columnCount = 0;

        for (EntityField field : e.getFields()) {

            if (field.isPrimary()) {
                listTemplate = listTemplate.replace("[fieldpk]", field.getName());
                listTemplate = listTemplate.replace("[entityFkFieldPk]", field.getName());

            } else {
                String start = "%%tableHead%%";
                String end = "%%endtableHead%%";

                String fieldHeadTemplate = FrontGeneration.extractPartTemplate(start, end, tableHeadTemplate).group(1);

                String fieldHead = fieldHeadTemplate.replace("[field]", HandyManUtils.majStart(field.getName()));
                tablehead += fieldHead.substring(0, fieldHead.length() - 1);

                String startBody = "%%tableBody%%";
                String endBody = "%%endtableBody%%";

                String fieldBodyTemplate = FrontGeneration.extractPartTemplate(startBody, endBody, tableBodyTemplate)
                        .group(1);

                if (field.isForeign()) {
                    String fieldbody = fieldBodyTemplate.replace("[field]", field.getName() + "?")
                            .replace("[field2]", "label");
                    tablebody += fieldbody.substring(0, fieldbody.length() - 1);
                    fkGetters += FrontGeneration.generateForeignGetter(e.getColumns()[columnCount], fkGetterTemplate);

                    // import fk type
                    // PageImport p = new PageImport("member", new ArrayList<String>() {
                    // {
                    // add(HandyManUtils.majStart(field.getType()));
                    // }
                    // }, "../../" + typeFile.replace("[entityMaj]",
                    // HandyManUtils.majStart(field.getType())));
                    // listePage.addImport(p);
                } else {
                    String fieldBodyNoFk = fieldBodyTemplate.replace("[field]", field.getName())
                            .replace("." + "[field2]", "");
                    tablebody += fieldBodyNoFk.substring(0, fieldBodyNoFk.length() - 1);
                }

            }
            columnCount++;

        }

        // import entity type
        PageImport p = new PageImport("member", new ArrayList<String>() {
            {
                add(HandyManUtils.majStart(e.getClassName()));
            }
        }, "../../" + typeFile.replace("[entityMaj]", HandyManUtils.majStart(e.getClassName())));
        listePage.addImport(p);

        // import form component
        listePage.addImport(new PageImport("single", new ArrayList<String>() {
            {
                add(langage.getFolders().get("formComponentName").replace("[entityMaj]",
                        HandyManUtils.majStart(e.getClassName())));
            }
        }, "./" + langage.getFolders().get("formComponent").replace("[entityMin]",
                e.getClassName().toLowerCase())));

        listTemplate = listTemplate.replace("[entityReadable]",
                HandyManUtils.majStart(HandyManUtils.formatReadable(e.getClassName()).trim()));
        listTemplate = listTemplate.replace("[entityMaj]", HandyManUtils.majStart(e.getClassName()));
        listTemplate = listTemplate.replace("[entityMin]", HandyManUtils.minStart(e.getClassName()));
        listTemplate = listTemplate.replace("<tableHead>", tablehead);
        listTemplate = listTemplate.replace("<tableBody>", tablebody);
        listTemplate = listTemplate.replace("<foreignKeyGetter>", fkGetters);
        listTemplate = listTemplate.replace("<import>", generateImport(langage, listePage.getImports()));
        finalContent = listTemplate;

        EntityComponent component = new EntityComponent();
        component.setContent(finalContent);
        component.setPath(listePage.getPath().replace("[entityMin]", HandyManUtils.minStart(e.getClassName())));

        return component;
    }
}