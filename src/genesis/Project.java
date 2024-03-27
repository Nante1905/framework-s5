package genesis;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;

import exceptions.EntityNotFoundException;
import exceptions.MissingColumnException;
import genesis.frontend.FrontGeneration;
import genesis.frontend.variables.FrontLangage;
import genesis.frontend.variables.FrontPage;
import genesis.frontend.variables.PageImport;
import handyman.HandyManUtils;

public class Project {
    public static void generate(String projectName, String entityAdd) throws Throwable {
        Database[] databases = HandyManUtils.fromJson(Database[].class,
                HandyManUtils.getFileContent(Constantes.DATABASE_JSON));
        Language[] languages = HandyManUtils.fromJson(Language[].class,
                HandyManUtils.getFileContent(Constantes.LANGUAGE_JSON));
        FrontLangage[] frontLangages = HandyManUtils.fromJson(FrontLangage[].class,
                HandyManUtils.getFileContent(Constantes.FRONT_LANGUAGE_JSON));
        Database database;
        Language language;
        FrontLangage frontLangage;
        // String databaseName = "akanjo", user = "postgres", pwd = "2003", host =
        // "localhost", port = "5432";
        String databaseName, user, pwd, host, port;
        boolean useSSL = false, allowPublicKeyRetrieval = true;
        String entityName = "";
        File credentialFile;
        Credentials credentials;
        String projectNameTagPath, projectNameTagContent;
        File project, apiProject, frontProject;
        String customFilePath, customFileContentOuter;
        List<Entity> entities;
        String modelFile, controllerFile, customFile;
        String customFileContent;
        String foreignContext;
        String customChanges, changesFile;
        String pageInfoContent = "";
        String projectApiName, projectFrontName;
        List<PageImport> pageInfoImports;
        PageImport importListe = new PageImport();
        int frontLangageNum = 0;
        Preference preference = new Preference();
        File preferenceFile;

        // AUTHENTIFICATION VARIABLE
        String authClassName = null;
        String authPseudoName = null;
        String authPwdName = null;
        Entity authEntity = null;
        boolean addAuth = false;

        // boolean addEntity = false;
        HashMap<String, String> pageInfoItems = new HashMap<String, String>();
        try (Scanner scanner = new Scanner(System.in)) {
            project = new File(Constantes.CURRENT_DIR + projectName);
            credentialFile = new File(Constantes.CURRENT_DIR + projectName + "/database-credentials.json");
            preferenceFile = new File(Constantes.CURRENT_DIR + projectName + "/preference.json");

            // Ajout entite fotsiny
            if (entityAdd != null && entityAdd.length() > 0) {
                System.out.println("new entity : " + entityAdd);

                entityName = entityAdd;

                if (preferenceFile.exists() == false) {
                    throw new Exception("Unable to found Preference.json in the project folder");
                }
                preference = HandyManUtils.fromJson(Preference.class,
                        HandyManUtils.getFileContent(preferenceFile.getPath()));
                database = databases[preference.getDatabase()];
                language = languages[preference.getBackLangage()];
                frontLangageNum = preference.getFrontLangage();
                frontLangage = frontLangages[frontLangageNum];

                if (credentialFile.exists() == false) {
                    throw new Exception(
                            "Unable to found database-credentials.json in the project folder");
                }
                credentials = HandyManUtils.fromJson(Credentials.class,
                        HandyManUtils.getFileContent(credentialFile.getPath()));
            } else {
                System.out.println("Choose a database engine:");
                for (int i = 0; i < databases.length; i++) {
                    System.out.println((i + 1) + ") " + databases[i].getNom());
                }
                System.out.print("> ");
                int databaseId = scanner.nextInt() - 1;
                database = databases[databaseId];
                System.out.println("Choose a framework:");
                for (int i = 0; i < languages.length; i++) {
                    System.out.println((i + 1) + ") " + languages[i].getNom());
                }
                System.out.print("> ");
                int langageId = scanner.nextInt() - 1;
                language = languages[langageId];
                System.out.println("Enter your database credentials:");
                System.out.print("Database name: ");
                databaseName = scanner.next();
                System.out.print("Username: ");
                user = scanner.next();
                System.out.print("Password: ");
                pwd = scanner.next();
                System.out.print("Database host: ");
                host = scanner.next();
                System.out.print("Database port: ");
                port = scanner.next();
                System.out.print("Use SSL ?(Y/n): ");
                useSSL = scanner.next().equalsIgnoreCase("Y");
                System.out.print("Allow public key retrieval ?(Y/n): ");
                allowPublicKeyRetrieval = scanner.next().equalsIgnoreCase("Y");

                credentials = new Credentials(databaseName, user, pwd, host, port, useSSL,
                        allowPublicKeyRetrieval);
                credentials.setSgbd(database.getNom());
                credentials.setDriver(database.getDriver());

                System.out.println("Test database connection");
                try (Connection connect = database.getConnexion(credentials)) {
                    System.out.println("Connection successfully established.");
                } catch (Exception e) {
                    System.out.println("Cannot establish connection with the database.");
                    System.out.println(e.getMessage());
                    return;
                }

                // AUTHENTIFICATION
                if (entityAdd.equals("") == true) {
                    System.out.println("Do you want to add authentification ? (y/n):");
                    addAuth = scanner.next().toLowerCase().equals("y");
                    if (addAuth == true) {
                        System.out.print(
                                "Enter the table's name for the authentification (it will be created if it doesn't exist yet)\n> ");
                        authClassName = scanner.next();
                        System.out.print("Enter the name of column to use as username: ");
                        authPseudoName = scanner.next();
                        System.out.print("Enter the name of column to use as password: ");
                        authPwdName = scanner.next();
                        Connection connect = database.getConnexion(credentials);
                        try {
                            authEntity = database.verifyAuthTable(connect, credentials, authClassName, authPseudoName,
                                    authPwdName,
                                    language);

                        } catch (MissingColumnException e) {
                            System.out.println(e.getMessage());
                            return;
                        } catch (EntityNotFoundException e) {
                            System.out.println("Creation of " + authClassName);
                            Entity toCreate = AuthGenerationTmp.setAuthInformation(authClassName, authPseudoName,
                                    authPwdName, database);
                            authEntity = database.createTable(connect, credentials, toCreate);
                            authEntity.initialize(connect, credentials, database, language);
                        } catch (Exception e) {
                            throw e;
                        }
                    }
                }

                System.out.print("Which entity to import ?(* to select all): ");
                entityName = scanner.next();
                System.out.println("Which langage do you want to use for your frontEnd application ?");
                for (int i = 0; i < frontLangages.length; i++) {
                    System.out.println(i + 1 + ") " + frontLangages[i].getName());
                }
                System.out.print("> ");
                frontLangageNum = scanner.nextInt() - 1;
                frontLangage = frontLangages[frontLangageNum];

                project = new File(Constantes.CURRENT_DIR + projectName);
                project.mkdir();
                // saving credentials
                credentialFile.createNewFile();
                HandyManUtils.overwriteFileContent(credentialFile.getPath(),
                        HandyManUtils.toJson(credentials));

                // SET PREFERENCE : TODO alaina ftsn rehefa manampy entité
                preference.setDatabase(databaseId);
                preference.setBackLangage(langageId);
                preference.setFrontLangage(frontLangageNum);
                preferenceFile.createNewFile();
                HandyManUtils.overwriteFileContent(preferenceFile.getPath(),
                        HandyManUtils.toJson(preference));
            }

            entityName = entityName.toLowerCase();
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
                // AUTHENTIFICATION: add entity to entities-List if it's generation of new
                // Project
                if (addAuth && entityName.equals("*") == false
                        && entityName.equalsIgnoreCase(authClassName.toLowerCase()) == false) {
                    System.out.println(authEntity.getTableName());
                    entities.add(authEntity);
                }

                String modelContent = "";
                String controllerContent = "";
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
                    String menuItemContent = "";
                    for (int i = 0; i < entities.size(); i++) {
                        System.out.println("Generating entity : " +
                                entities.get(i).getTableName());
                        entities.get(i).initialize(connect, credentials, database, language);
                        modelContent = language.generateModel(entities.get(i), projectName, sc,
                                labelChoice, entities);
                        controllerContent = language.generateController(entities.get(i),
                                database,
                                credentials,
                                projectName);
                        // views[i] = language.generateView(entities.get(i), projectName);
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
                        // HandyManUtils.majStart(entities.get(i).getClassName()));
                        // viewFile = viewFile.replace("[classNameMin]",
                        // HandyManUtils.minStart(entities.get(i).getClassName()));
                        modelFile = modelFile.replace("[projectNameMin]",
                                HandyManUtils.minStart(projectName));
                        controllerFile = controllerFile.replace("[projectNameMin]",
                                HandyManUtils.minStart(projectName));
                        modelFile += "/" + HandyManUtils
                                .majStart(entities.get(i).getClassName())
                                + "."
                                + language.getModel().getModelExtension();
                        controllerFile += "/"
                                + HandyManUtils.majStart(entities.get(i).getClassName())
                                + language.getController().getControllerNameSuffix()
                                + "."
                                + language.getController().getControllerExtension();
                        // viewFile += "/" + language.getView().getViewName() + "."
                        // + language.getView().getViewExtension();
                        // viewFile = viewFile.replace("[classNameMin]",
                        // HandyManUtils.minStart(entities.get(i).getClassName()));
                        for (CustomFile f : language.getModel().getModelAdditionnalFiles()) {
                            foreignContext = "";
                            for (EntityField ef : entities.get(i).getFields()) {
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
                                            entities.get(i).getClassName()));
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
                                            .majStart(entities.get(i)
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
                        // HandyManUtils.createFile(viewFile);

                        // to avoid overwrite
                        if (new File(modelFile).exists()) {
                            if (entityAdd != null) {
                                System.out.println(modelFile
                                        + " already exists and will not be overwritten");
                            }
                        } else {
                            HandyManUtils.createFile(modelFile);
                            HandyManUtils.overwriteFileContent(modelFile, modelContent);
                        }

                        if (new File(controllerFile).exists()) {
                            if (entityAdd != null) {
                                System.out.println(controllerFile
                                        + " already exists and will not be overwritten ");

                            }
                        } else {
                            HandyManUtils.createFile(controllerFile);
                            HandyManUtils.overwriteFileContent(controllerFile,
                                    controllerContent);
                        }

                        FrontGeneration.generateView(HandyManUtils.fromJson(
                                FrontLangage[].class,
                                HandyManUtils.getFileContent(
                                        Constantes.FRONT_LANGUAGE_JSON))[preference
                                                .getFrontLangage()],
                                entities.get(i), projectName,
                                projectFrontName, scanner);

                        // PageInfo Content
                        importListe = new PageImport("single",
                                List.of(HandyManUtils
                                        .majStart(entities.get(i)
                                                .getClassName())
                                        + "Liste"),
                                "../../" + frontLangage.getFolders().get("liste")
                                        .replace("[entityMin]", HandyManUtils
                                                .minStart(entities
                                                        .get(i)
                                                        .getClassName())));
                        pageInfoImports.add(importListe);
                        if (i > 0) {
                            pageInfoContent += "\n";
                        }
                        menuItemContent = menuItemTemplate
                                .replace("[entityMin]",
                                        HandyManUtils.minStart(entities.get(i)
                                                .getClassName()))
                                .replace("[entityMaj]", HandyManUtils
                                        .majStart(entities.get(i)
                                                .getClassName()));
                        // avoid repetition pageInfo
                        pageInfoItems.put(entities.get(i).getClassName().toLowerCase(),
                                menuItemContent);
                    }

                    // generate PageInfo.tsx
                    pageInfo.setPath(projectName, projectFrontName, null,
                            frontLangage.getExtension());
                    File pageInfoFile = new File(pageInfo.getPath());
                    // System.out.println(pageInfoContent);
                    if (pageInfoFile.exists()) {
                        pageInfoContent = "";
                        String lastContent = HandyManUtils
                                .getFileContent(pageInfoFile.getPath());

                        // verify if pageInfo already content the Entity
                        for (Map.Entry<String, String> item : pageInfoItems.entrySet()) {
                            if (pageInfoContent.contains("/" + item.getKey()) == false
                                    && lastContent.contains(
                                            "/" + item.getKey()) == false) {
                                pageInfoContent += item.getValue() + "\n";
                            }
                        }
                        // IMPORT
                        List<PageImport> toImports = new ArrayList<PageImport>();
                        for (PageImport p : pageInfoImports) {
                            if (lastContent.contains(p.getSource()) == false) {
                                toImports.add(p);
                            }
                        }
                        String importContent = FrontGeneration.generateImport(
                                frontLangages[preference.getFrontLangage()],
                                toImports);

                        lastContent = importContent + lastContent;

                        lastContent = lastContent.replace("];", pageInfoContent + "\n];");
                        HandyManUtils.overwriteFileContent(pageInfo.getPath(), lastContent);

                    } else if (pageInfoFile.exists() == false) {

                        for (Map.Entry<String, String> item : pageInfoItems.entrySet()) {
                            pageInfoContent += item.getValue() + "\n";
                        }
                        pageInfoTemplate = pageInfoTemplate.replace(templateMatcher.group(),
                                pageInfoContent);
                        pageInfoTemplate = pageInfoTemplate
                                .replace("[exportKey]",
                                        frontLangage.getVariables()
                                                .getExportKey())
                                .replace("[endLine]", frontLangage.getVariables()
                                        .getEndLine());
                        pageInfo.setImports(pageInfoImports);

                        // IMPORT
                        String pageInfoImportContent = FrontGeneration.generateImport(
                                frontLangages[preference.getFrontLangage()],
                                pageInfo.getImports());
                        pageInfoTemplate = pageInfoTemplate.replace("<import>",
                                pageInfoImportContent);
                        HandyManUtils.createFile(pageInfo.getPath());
                        HandyManUtils.overwriteFileContent(pageInfo.getPath(),
                                pageInfoTemplate);
                    }

                    // Landing page

                    FrontPage landing = frontLangage.getPages().get("landingPage");
                    landing.setPath(projectName, projectFrontName, null, "tsx");
                    String landingPageContent = HandyManUtils.getFileContent(landing.getPath());
                    landingPageContent = landingPageContent.replace("[projectName]",
                            HandyManUtils.majStart(projectName));
                    HandyManUtils.overwriteFileContent(landing.getPath(), landingPageContent);

                    // Menu

                    FrontPage menu = frontLangage.getPages().get("menu");
                    menu.setPath(projectName, projectFrontName, null, "tsx");
                    String menuContent = HandyManUtils.getFileContent(menu.getPath());
                    menuContent = menuContent.replace("[projectNameMaj]",
                            HandyManUtils.majStart(projectName));
                    HandyManUtils.overwriteFileContent(menu.getPath(), menuContent);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // ENV
                FrontGeneration.rewriteEnv(frontLangage, projectName, projectFrontName);

                // navLinkPath = language.getNavbarLinks().getPath().replace("[projectNameMaj]",
                // HandyManUtils.majStart(projectName))
                // .replace("[projectApiName]", projectApiName);
                // navLinkPath = navLinkPath.replace("[projectNameMin]",
                // HandyManUtils.minStart(projectName))
                // .replace("[projectApiName]", projectApiName);

                // HandyManUtils.overwriteFileContent(navLinkPath,
                // HandyManUtils.getFileContent(navLinkPath).replace("[navbarLinks]",
                // navLink));
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
