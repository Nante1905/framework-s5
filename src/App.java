import java.io.File;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

import genesis.Constantes;
import genesis.Credentials;
import genesis.CustomChanges;
import genesis.CustomFile;
import genesis.Database;
import genesis.Entity;
import genesis.EntityField;
import genesis.Language;
import genesis.frontend.FrontGeneration;
import genesis.frontend.variables.FrontLangage;
import genesis.frontend.variables.FrontPage;
import genesis.frontend.variables.PageImport;
import handyman.HandyManUtils;

public class App {
        public static void main(String[] args) throws Throwable {
                Database[] databases = HandyManUtils.fromJson(Database[].class,
                                HandyManUtils.getFileContent(Constantes.DATABASE_JSON));
                Language[] languages = HandyManUtils.fromJson(Language[].class,
                                HandyManUtils.getFileContent(Constantes.LANGUAGE_JSON));
                FrontLangage[] frontLangages = HandyManUtils.fromJson(FrontLangage[].class,
                                HandyManUtils.getFileContent(Constantes.FRONT_LANGUAGE_JSON));
                Database database;
                Language language;
                FrontLangage frontLangage;
                String databaseName = "poketra", user = "postgres", pwd = "2003", host = "localhost", port = "5432";
                boolean useSSL = false, allowPublicKeyRetrieval = true;
                String projectName = "poketrav2", entityName = "*";
                Credentials credentials;
                String projectNameTagPath, projectNameTagContent;
                File project, credentialFile, apiProject, frontProject;
                String customFilePath, customFileContentOuter;
                Entity[] entities;
                String[] models, controllers;
                String modelFile, controllerFile, customFile;
                String customFileContent;
                String foreignContext;
                String customChanges, changesFile;
                String navLink, navLinkPath;
                String pageInfoContent = "";
                String projectApiName, projectFrontName;
                List<PageImport> pageInfoImports;
                database = databases[0];
                language = languages[0];
                try (Scanner scanner = new Scanner(System.in)) {
                        // System.out.println("Choose a database engine:");
                        // for (int i = 0; i < databases.length; i++) {
                        // System.out.println((i + 1) + ") " + databases[i].getNom());
                        // }
                        // System.out.print("> ");
                        // database = databases[scanner.nextInt() - 1];
                        // System.out.println("Choose a framework:");
                        // for (int i = 0; i < languages.length; i++) {
                        // System.out.println((i + 1) + ") " + languages[i].getNom());
                        // }
                        // System.out.print("> ");
                        // language = languages[scanner.nextInt() - 1];
                        // System.out.println("Enter your database credentials:");
                        // System.out.print("Database name: ");
                        // databaseName = scanner.next();
                        // System.out.print("Username: ");
                        // user = scanner.next();
                        // System.out.print("Password: ");
                        // pwd = scanner.next();
                        // System.out.print("Database host: ");
                        // host = scanner.next();
                        // System.out.print("Database port: ");
                        // host = scanner.next();
                        // System.out.print("Use SSL ?(Y/n): ");
                        // useSSL = scanner.next().equalsIgnoreCase("Y");
                        // System.out.print("Allow public key retrieval ?(Y/n): ");
                        // allowPublicKeyRetrieval = scanner.next().equalsIgnoreCase("Y");
                        // System.out.println();
                        credentials = new Credentials(databaseName, user, pwd, host, port, useSSL,
                                        allowPublicKeyRetrieval);
                        credentials.setSgbd(database.getNom());
                        credentials.setDriver(database.getDriver());
                        // try (Connection connect = database.getConnexion(credentials)) {
                        // System.out.println("Test database connection");
                        // } catch (Exception e) {
                        // System.out.println("Cannot establish connection with the database.");
                        // }
                        // System.out.print("Enter your project name: ");
                        // projectName = scanner.next();
                        // System.out.print("Which entities to import ?(* to select all): ");
                        // entityName = scanner.next();
                        System.out.println("Which langage do you want to use for your frontEnd application ?");
                        for (int i = 0; i < frontLangages.length; i++) {
                                System.out.println(i + 1 + ") " + frontLangages[i].getName());
                        }
                        System.out.print("> ");
                        frontLangage = frontLangages[scanner.nextInt() - 1];
                        project = new File(projectName);
                        project.mkdir();
                        projectApiName = projectName + "-api";
                        projectFrontName = projectName + "-front";

                        // API
                        apiProject = new File(project.getPath() + "/" + projectApiName);
                        apiProject.mkdir();

                        // FRONT
                        frontProject = new File(project.getPath() + "/" + projectFrontName);
                        frontProject.mkdir();

                        for (CustomFile c : language.getAdditionnalFiles()) {
                                customFilePath = c.getName();
                                customFilePath = customFilePath.replace("[projectNameMaj]",
                                                HandyManUtils.majStart(projectName));
                                HandyManUtils.createFile(customFilePath);
                                customFileContentOuter = HandyManUtils
                                                .getFileContent(Constantes.DATA_PATH + "/" + c.getContent())
                                                .replace("[projectNameMaj]", HandyManUtils.majStart(projectName));
                                HandyManUtils.overwriteFileContent(customFilePath, customFileContentOuter);
                        }

                        // Extract API skeleton
                        HandyManUtils.extractDir(
                                        Constantes.DATA_PATH + "/" + language.getSkeleton() + "."
                                                        + Constantes.SKELETON_EXTENSION,
                                        apiProject.getPath());

                        // EXTRACT front skeleton
                        HandyManUtils.extractDir(
                                        Constantes.DATA_PATH + "/" + frontLangage.getSkeleton() + "."
                                                        + Constantes.SKELETON_EXTENSION,
                                        frontProject.getPath());

                        credentialFile = new File(apiProject.getPath(), Constantes.CREDENTIAL_FILE);
                        credentialFile.createNewFile();
                        HandyManUtils.overwriteFileContent(credentialFile.getPath(), HandyManUtils.toJson(credentials));
                        for (String replace : language.getProjectNameTags()) {
                                projectNameTagPath = replace
                                                .replace("[projectNameMaj]", HandyManUtils.majStart(projectName))
                                                .replace("[projectNameMin]", HandyManUtils.minStart(projectName))
                                                .replace("[projectApiName]", projectApiName);

                                if (replace.contains("xml")) {
                                        System.out.println(projectNameTagPath);
                                }
                                projectNameTagContent = HandyManUtils.getFileContent(projectNameTagPath).replace(
                                                "[projectNameMaj]",
                                                HandyManUtils.majStart(projectName));
                                projectNameTagContent = projectNameTagContent.replace("[databaseHost]",
                                                credentials.getHost());
                                projectNameTagContent = projectNameTagContent.replace("[databasePort]",
                                                credentials.getPort());
                                projectNameTagContent = projectNameTagContent.replace("[databaseName]",
                                                credentials.getDatabaseName());
                                projectNameTagContent = projectNameTagContent.replace("[user]", credentials.getUser());
                                projectNameTagContent = projectNameTagContent.replace("[pwd]", credentials.getPwd());
                                projectNameTagContent = projectNameTagContent.replace("[projectNameMin]",
                                                HandyManUtils.minStart(projectName));
                                HandyManUtils.overwriteFileContent(projectNameTagPath, projectNameTagContent);
                        }
                        try (Connection connect = database.getConnexion(credentials)) {
                                entities = database.getEntities(connect, credentials, entityName);
                                for (int i = 0; i < entities.length; i++) {
                                        entities[i].initialize(connect, credentials, database, language);
                                }
                                models = new String[entities.length];
                                controllers = new String[entities.length];
                                // views = new String[entities.length];
                                navLink = "";
                                try (Scanner sc = new Scanner(System.in)) {
                                        System.out.println("How do you want to set label for the Foreign Keys ?");
                                        System.out.println(
                                                        "1) Set per entity: getLabel() method will be created and you will choose its return value during code generation");
                                        System.out.println(
                                                        "2) Use the first String field in entity: getLabel() method will be created and will return the value of the first field that is a String.");
                                        System.out.println(
                                                        "3) Set later: getLabel() method will be created in each entity and must be implemented or removed.");
                                        System.out.print("> ");
                                        int labelChoice = sc.nextInt();
                                        while (labelChoice != Constantes.LABEL_FIRST_STRING
                                                        && labelChoice != Constantes.LABEL_PER_ENTITY
                                                        && labelChoice != Constantes.SET_LABEL_LATER) {
                                                System.out.println("Please choose number from the options.");
                                                System.out.print("> ");
                                                labelChoice = sc.nextInt();
                                        }

                                        // FRONT: generate PageINfo for menu and routing
                                        FrontPage pageInfo = frontLangage.getPages().get("pageInfo");
                                        pageInfoImports = pageInfo.getImports();
                                        // mbola mila amboarina ny import sy variable
                                        String pageInfoTemplate = HandyManUtils.getFileContent(
                                                        Constantes.FRONT_TEMPLATE_DIR + "/" + pageInfo.getTemplate());
                                        Matcher templateMatcher = FrontGeneration.extractPartTemplate("&&pageInfo&&",
                                                        "&&endPageInfo&&", pageInfoTemplate);
                                        // représente une page
                                        String menuItemTemplate = templateMatcher.group(1);
                                        menuItemTemplate = menuItemTemplate.substring(1, menuItemTemplate.length() - 1);

                                        for (int i = 0; i < models.length; i++) {
                                                models[i] = language.generateModel(entities[i], projectName, sc,
                                                                labelChoice);
                                                controllers[i] = language.generateController(entities[i], database,
                                                                credentials,
                                                                projectName);
                                                // views[i] = language.generateView(entities[i], projectName);
                                                modelFile = language.getModel().getModelSavePath().replace(
                                                                "[projectNameMaj]",
                                                                HandyManUtils.majStart(projectName))
                                                                .replace("[projectApiName]",
                                                                                projectApiName);
                                                controllerFile = language.getController().getControllerSavepath()
                                                                .replace(
                                                                                "[projectNameMaj]",
                                                                                HandyManUtils.majStart(
                                                                                                projectName))
                                                                .replace("[projectApiName]", projectApiName);
                                                // viewFile = language.getView().getViewSavePath().replace(
                                                // "[projectNameMaj]",
                                                // HandyManUtils.majStart(projectName));
                                                // viewFile = viewFile.replace("[projectNameMin]",
                                                // HandyManUtils.minStart(projectName));
                                                // viewFile = viewFile.replace("[classNameMaj]",
                                                // HandyManUtils.majStart(entities[i].getClassName()));
                                                // viewFile = viewFile.replace("[classNameMin]",
                                                // HandyManUtils.minStart(entities[i].getClassName()));
                                                modelFile = modelFile.replace("[projectNameMin]",
                                                                HandyManUtils.minStart(projectName));
                                                controllerFile = controllerFile.replace("[projectNameMin]",
                                                                HandyManUtils.minStart(projectName));
                                                modelFile += "/" + HandyManUtils.majStart(entities[i].getClassName())
                                                                + "."
                                                                + language.getModel().getModelExtension();
                                                controllerFile += "/"
                                                                + HandyManUtils.majStart(entities[i].getClassName())
                                                                + language.getController().getControllerNameSuffix()
                                                                + "."
                                                                + language.getController().getControllerExtension();
                                                // viewFile += "/" + language.getView().getViewName() + "."
                                                // + language.getView().getViewExtension();
                                                // viewFile = viewFile.replace("[classNameMin]",
                                                // HandyManUtils.minStart(entities[i].getClassName()));
                                                HandyManUtils.createFile(modelFile);
                                                for (CustomFile f : language.getModel().getModelAdditionnalFiles()) {
                                                        foreignContext = "";
                                                        for (EntityField ef : entities[i].getFields()) {
                                                                if (ef.isForeign()) {
                                                                        foreignContext += language.getModel()
                                                                                        .getModelForeignContextAttr();
                                                                        foreignContext = foreignContext.replace(
                                                                                        "[classNameMaj]",
                                                                                        HandyManUtils.majStart(
                                                                                                        ef.getType()));
                                                                }
                                                        }
                                                        customFile = f.getName().replace("[classNameMaj]",
                                                                        HandyManUtils.majStart(
                                                                                        entities[i].getClassName()));
                                                        customFile = language.getModel().getModelSavePath().replace(
                                                                        "[projectNameMaj]",
                                                                        HandyManUtils.majStart(projectName)) + "/"
                                                                        + customFile;
                                                        customFile = customFile.replace("[projectNameMin]",
                                                                        HandyManUtils.minStart(projectName));
                                                        customFileContent = HandyManUtils
                                                                        .getFileContent(Constantes.DATA_PATH + "/"
                                                                                        + f.getContent())
                                                                        .replace("[classNameMaj]", HandyManUtils
                                                                                        .majStart(entities[i]
                                                                                                        .getClassName()));
                                                        customFileContent = customFileContent.replace(
                                                                        "[projectNameMin]",
                                                                        HandyManUtils.minStart(projectName));
                                                        customFileContent = customFileContent.replace(
                                                                        "[projectNameMaj]",
                                                                        HandyManUtils.majStart(projectName));
                                                        customFileContent = customFileContent.replace("[databaseHost]",
                                                                        credentials.getHost());
                                                        customFileContent = customFileContent.replace("[databaseName]",
                                                                        credentials.getDatabaseName());
                                                        customFileContent = customFileContent.replace("[user]",
                                                                        credentials.getUser());
                                                        customFileContent = customFileContent.replace("[pwd]",
                                                                        credentials.getPwd());
                                                        customFileContent = customFileContent
                                                                        .replace("[modelForeignContextAttr]",
                                                                                        foreignContext);
                                                        HandyManUtils.createFile(customFile);
                                                        HandyManUtils.overwriteFileContent(customFile,
                                                                        customFileContent);
                                                }
                                                HandyManUtils.createFile(controllerFile);
                                                // HandyManUtils.createFile(viewFile);
                                                HandyManUtils.overwriteFileContent(modelFile, models[i]);
                                                HandyManUtils.overwriteFileContent(controllerFile, controllers[i]);
                                                // HandyManUtils.overwriteFileContent(viewFile, views[i]);
                                                // navLink += language.getNavbarLinks().getLink();
                                                // navLink = navLink.replace("[projectNameMaj]",
                                                // HandyManUtils.majStart(projectName));
                                                // navLink = navLink.replace("[projectNameMin]",
                                                // HandyManUtils.minStart(projectName));
                                                // navLink = navLink.replace("[classNameMin]",
                                                // HandyManUtils.minStart(entities[i].getClassName()));
                                                // navLink = navLink.replace("[classNameMaj]",
                                                // HandyManUtils.majStart(entities[i].getClassName()));
                                                // navLink = navLink.replace("[classNameformattedMin]",
                                                // HandyManUtils.minStart(HandyManUtils
                                                // .formatReadable(entities[i]
                                                // .getClassName())));
                                                // navLink = navLink.replace("[classNameformattedMaj]",
                                                // HandyManUtils.majStart(HandyManUtils
                                                // .formatReadable(entities[i]
                                                // .getClassName())));

                                                // PageInfo Content
                                                // TODO: ahoana ty path tyh tyh XD
                                                pageInfoImports.add(new PageImport("single",
                                                                List.of(HandyManUtils
                                                                                .majStart(entities[i].getClassName())
                                                                                + "Liste"),
                                                                "../../" + frontLangage.getFolders().get("liste")
                                                                                .replace("[entityMin]", HandyManUtils
                                                                                                .minStart(entities[i]
                                                                                                                .getClassName()))));
                                                if (i > 0) {
                                                        pageInfoContent += ",\n";
                                                }
                                                pageInfoContent += menuItemTemplate
                                                                .replace("[entityMin]",
                                                                                HandyManUtils.minStart(entities[i]
                                                                                                .getClassName()))
                                                                .replace("[entityMaj]", HandyManUtils
                                                                                .majStart(entities[i].getClassName()));
                                        }

                                        // generate PageInfo.tsx
                                        pageInfoTemplate = pageInfoTemplate.replace(templateMatcher.group(),
                                                        pageInfoContent);
                                        pageInfoTemplate = pageInfoTemplate
                                                        .replace("[exportKey]",
                                                                        frontLangage.getVariables().getExportKey())
                                                        .replace("[endLine]", frontLangage.getVariables().getEndLine());
                                        pageInfo.setImports(pageInfoImports);

                                        // IMPORT
                                        String pageInfoImportContent = FrontGeneration.generateImport(frontLangage,
                                                        pageInfo.getImports());
                                        pageInfoTemplate = pageInfoTemplate.replace("<import>", pageInfoImportContent);
                                        pageInfo.setPath(projectName, projectFrontName, null,
                                                        frontLangage.getExtension());
                                        System.out.println(pageInfo.getPath());
                                        HandyManUtils.createFile(pageInfo.getPath());
                                        HandyManUtils.overwriteFileContent(pageInfo.getPath(), pageInfoTemplate);

                                } catch (Exception e) {
                                        e.printStackTrace();
                                }

                                // ENV
                                FrontGeneration.rewriteEnv(frontLangage, projectName, projectFrontName);

                                navLinkPath = language.getNavbarLinks().getPath().replace("[projectNameMaj]",
                                                HandyManUtils.majStart(projectName))
                                                .replace("[projectApiName]", projectApiName);
                                navLinkPath = navLinkPath.replace("[projectNameMin]",
                                                HandyManUtils.minStart(projectName))
                                                .replace("[projectApiName]", projectApiName);
                                System.out.println(navLinkPath);
                                HandyManUtils.overwriteFileContent(navLinkPath,
                                                HandyManUtils.getFileContent(navLinkPath).replace("[navbarLinks]",
                                                                navLink));
                                for (CustomChanges c : language.getCustomChanges()) {
                                        customChanges = "";
                                        for (Entity e : entities) {
                                                customChanges += c.getChanges();
                                                customChanges = customChanges.replace("[classNameMaj]",
                                                                HandyManUtils.majStart(e.getClassName()));
                                                customChanges = customChanges.replace("[classNameMin]",
                                                                HandyManUtils.minStart(e.getClassName()));
                                                customChanges = customChanges.replace("[databaseHost]",
                                                                credentials.getHost());
                                                customChanges = customChanges.replace("[user]", credentials.getUser());
                                                customChanges = customChanges.replace("[databaseName]",
                                                                credentials.getDatabaseName());
                                                customChanges = customChanges.replace("[pwd]", credentials.getPwd());
                                        }
                                        if (c.isWithEndComma() == false) {
                                                customChanges = customChanges.substring(0, customChanges.length() - 1);
                                        }
                                        changesFile = c.getPath().replace("[projectNameMaj]",
                                                        HandyManUtils.majStart(projectName));
                                        HandyManUtils.overwriteFileContent(changesFile,
                                                        HandyManUtils.getFileContent(changesFile)
                                                                        .replace("[customChanges]", customChanges));
                                }
                        }
                }
        }
}
