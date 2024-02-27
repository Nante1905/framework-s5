import genesis.Constantes;
import genesis.frontend.FrontGeneration;
import genesis.frontend.variables.FrontLangage;
import genesis.frontend.variables.FrontPage;
import handyman.HandyManUtils;

public class Test {

    public static void main(String[] args) throws Throwable {
        String json = HandyManUtils.getFileContent(Constantes.DATA_PATH + "/front-languages.json");
        FrontLangage[] f = HandyManUtils.fromJson(FrontLangage[].class, json);
        FrontLangage react = f[0];

        FrontPage form = react.getPages().get("form");
        form.addImports(react.getOptionalImports().get("textField"));
        form.addImports(react.getOptionalImports().get("date"));
        form.addImports(react.getOptionalImports().get("select"));

        System.out.println(FrontGeneration.generateImport(react, form.getImports()));
    }
}
