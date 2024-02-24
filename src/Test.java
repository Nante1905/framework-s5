import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import genesis.Constantes;
import genesis.frontend.FrontGeneration;
import genesis.frontend.variables.FrontLangage;
import genesis.frontend.variables.FrontPage;
import genesis.frontend.variables.PageImport;
import handyman.HandyManUtils;

public class Test {
    public static void main(String[] args) throws Throwable {
        String json = HandyManUtils.getFileContent(Constantes.DATA_PATH + "/front-languages.json");
        FrontLangage[] f = HandyManUtils.fromJson(FrontLangage[].class, json);
        FrontLangage react = f[0];
        String imports = "";

        for (FrontPage page : react.getPages()) {
            System.out.println("====== " + page.getName() + "=================");
            if (page.getName().contains("form")) {
                List<PageImport> i = new ArrayList<>(Arrays.asList(page.getImports()));
                PageImport tmp = new PageImport("member", List.of("Produit"), "../../shared/types/Produit");
                i.add(tmp);
                page.setImports(i.toArray(new PageImport[0]));
            }
            imports = FrontGeneration.generateImport(react, page.getImports());
            System.out.println(imports);
        }
    }
}
