package genesis.frontend;

import java.util.Arrays;
import java.util.stream.Collectors;

import genesis.Constantes;
import genesis.Entity;
import genesis.Language;
import genesis.frontend.components.AuthComponent;
import handyman.HandyManUtils;

// TODO : Replace auth class maj
public class AuthGeneration {
    String projectName;
    Language language;
    Entity authEntity;
    String pseudoField = "username";
    String mdpField = "password";

    public AuthGeneration(Language language, String projectName) {
        this.setProjectName(projectName);
        this.setLanguage(language);
    }

    public String generateAuthController() throws Throwable {
        System.out.println("Generating Auth Controller");

        String authControllerTemplate = HandyManUtils
                .getFileContent(Constantes.AUTH_TEMPLATE_DIR + "/AuthController.templ");
        AuthComponent controller = this.language.getAuthController();

        // replace imports
        Arrays.asList(controller.getImports()).stream().map(i -> {
            return i.replace("[projectNameMin]", this.projectName.toLowerCase());
        }).collect(Collectors.toList()).toArray(controller.getImports());

        // replace package name
        controller.setPackageName(
                controller.getPackageName().replace("[projectNameMin]", this.projectName.toLowerCase()));

        // replace save path
        controller.setSavePath(controller.getSavePath().replace("[projectNameMin]", this.projectName.toLowerCase()));
        controller.setSavePath(
                controller.getSavePath().replace("[projectNameMaj]", HandyManUtils.majStart(this.getProjectName())));

        // init template
        authControllerTemplate = authControllerTemplate.replace("[namespace]",
                this.getLanguage().getSyntax().get("namespace"));
        authControllerTemplate = authControllerTemplate.replace("[package]", controller.getPackageName());
        authControllerTemplate = authControllerTemplate.replace("[imports]",
                Arrays.asList(controller.getImports()).stream().collect(Collectors.joining("\n")));

        authControllerTemplate = authControllerTemplate.replace("[annotations]",
                Arrays.asList(controller.getAnnotations()).stream().collect(Collectors.joining("\n")));
        authControllerTemplate = authControllerTemplate.replace("[authClassMaj]",
                "Utilisateur");

        return authControllerTemplate;
    }

    public String generateAuthFilter() throws Throwable {
        System.out.println("Generating auth filter");

        String authFilterTemplate = HandyManUtils
                .getFileContent(Constantes.AUTH_TEMPLATE_DIR + "/AuthFilter.templ");

        AuthComponent filter = this.getLanguage().getAuthFilter();

        // replace imports
        Arrays.asList(filter.getImports()).stream().map(i -> {
            return i.replace("[projectNameMin]", this.projectName.toLowerCase());
        }).collect(Collectors.toList()).toArray(filter.getImports());

        // replace package name
        filter.setPackageName(filter.getPackageName().replace("[projectNameMin]", this.projectName.toLowerCase()));
        // replace save path
        filter.setSavePath(filter.getSavePath().replace("[projectNameMin]", this.projectName.toLowerCase()));
        filter.setSavePath(
                filter.getSavePath().replace("[projectNameMaj]", HandyManUtils.majStart(this.getProjectName())));

        // init template
        authFilterTemplate = authFilterTemplate.replace("[namespace]",
                this.getLanguage().getSyntax().get("namespace"));
        authFilterTemplate = authFilterTemplate.replace("[package]", filter.getPackageName());
        authFilterTemplate = authFilterTemplate.replace("[imports]",
                Arrays.asList(filter.getImports()).stream().collect(Collectors.joining("\n")));

        authFilterTemplate = authFilterTemplate.replace("[authClassMaj]",
                "Utilisateur");

        return authFilterTemplate;

    }

    public String generateAuthJwtService() throws Throwable {
        System.out.println("Generating jwt service");

        String jwtServiceTemplate = HandyManUtils
                .getFileContent(Constantes.AUTH_TEMPLATE_DIR + "/JWTService.templ");

        AuthComponent jwtService = this.getLanguage().getJwtService();

        // replace imports
        Arrays.asList(jwtService.getImports()).stream().map(i -> {
            return i.replace("[projectNameMin]", this.projectName.toLowerCase());
        }).collect(Collectors.toList()).toArray(jwtService.getImports());

        // replace package name
        jwtService.setPackageName(
                jwtService.getPackageName().replace("[projectNameMin]", this.projectName.toLowerCase()));
        // replace save path
        jwtService.setSavePath(jwtService.getSavePath().replace("[projectNameMin]", this.projectName.toLowerCase()));
        jwtService.setSavePath(
                jwtService.getSavePath().replace("[projectNameMaj]", HandyManUtils.majStart(this.getProjectName())));

        // init template
        jwtServiceTemplate = jwtServiceTemplate.replace("[namespace]",
                this.getLanguage().getSyntax().get("namespace"));
        jwtServiceTemplate = jwtServiceTemplate.replace("[package]", jwtService.getPackageName());
        jwtServiceTemplate = jwtServiceTemplate.replace("[imports]",
                Arrays.asList(jwtService.getImports()).stream().collect(Collectors.joining("\n")));

        jwtServiceTemplate = jwtServiceTemplate.replace("[authClassMaj]",
                "Utilisateur");

        return jwtServiceTemplate;
    }

    public String generateAuthService() throws Throwable {
        System.out.println("Generating auth service");

        String authServiceTemplate = HandyManUtils
                .getFileContent(Constantes.AUTH_TEMPLATE_DIR + "/AuthService.templ");

        AuthComponent authService = this.language.getAuthService();

        // replace imports
        Arrays.asList(authService.getImports()).stream().map(i -> {
            return i.replace("[projectNameMin]", this.projectName.toLowerCase());
        }).collect(Collectors.toList()).toArray(authService.getImports());

        // replace package name
        authService.setPackageName(
                authService.getPackageName().replace("[projectNameMin]", this.projectName.toLowerCase()));

        // replace save path
        authService.setSavePath(authService.getSavePath().replace("[projectNameMin]", this.projectName.toLowerCase()));
        authService.setSavePath(
                authService.getSavePath().replace("[projectNameMaj]", HandyManUtils.majStart(this.getProjectName())));

        // init template
        authServiceTemplate = authServiceTemplate.replace("[namespace]",
                this.getLanguage().getSyntax().get("namespace"));
        authServiceTemplate = authServiceTemplate.replace("[package]", authService.getPackageName());
        authServiceTemplate = authServiceTemplate.replace("[imports]",
                Arrays.asList(authService.getImports()).stream().collect(Collectors.joining("\n")));

        authServiceTemplate = authServiceTemplate.replace("[authClassMaj]",
                "Utilisateur");
        authServiceTemplate = authServiceTemplate.replace("[pseudoFieldMin]",
                this.getPseudoField().toLowerCase());
        authServiceTemplate = authServiceTemplate.replace("[mdpFieldMin]",
                this.getMdpField().toLowerCase());
        authServiceTemplate = authServiceTemplate.replace("[pseudoFieldMaj]",
                HandyManUtils.majStart(this.getPseudoField()));
        authServiceTemplate = authServiceTemplate.replace("[mdpFieldMaj]",
                HandyManUtils.majStart(this.getMdpField()));

        return authServiceTemplate;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Entity getAuthEntity() {
        return authEntity;
    }

    public void setAuthEntity(Entity authEntity) {
        this.authEntity = authEntity;
    }

    public String getPseudoField() {
        return pseudoField;
    }

    public void setPseudoField(String pseudoField) {
        this.pseudoField = pseudoField;
    }

    public String getMdpField() {
        return mdpField;
    }

    public void setMdpField(String mdpField) {
        this.mdpField = mdpField;
    }
}
