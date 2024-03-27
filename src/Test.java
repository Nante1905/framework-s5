import genesis.Constantes;
import genesis.Language;
import genesis.frontend.AuthGeneration;
import handyman.HandyManUtils;

public class Test {

        public static void main(String[] args) throws Throwable {

                Language[] languages = HandyManUtils.fromJson(Language[].class,
                                HandyManUtils.getFileContent(Constantes.LANGUAGE_JSON));

                AuthGeneration authGeneration = new AuthGeneration(languages[0], "helloWorld");
                authGeneration.generateAuthService();

        }

        // public static void manampy(List<Integer> i) {
        // i.add(4);
        // }

}
