import java.util.ArrayList;
import java.util.List;

import handyman.HandyManUtils;

public class Test {

        public static void main(String[] args) throws Throwable {

                // Database[] databases = HandyManUtils.fromJson(Database[].class,
                // HandyManUtils.getFileContent(Constantes.DATABASE_JSON));
                // Language[] languages = HandyManUtils.fromJson(Language[].class,
                // HandyManUtils.getFileContent(Constantes.LANGUAGE_JSON));
                // String databaseName = "akanjo", user = "postgres", pwd = "2003", host =
                // "localhost";
                // boolean useSSL = false, allowPublicKeyRetrieval = true;
                // Credentials credentials = new Credentials(databaseName, user, pwd, host,
                // "5432", useSSL,
                // allowPublicKeyRetrieval);
                // Database database = databases[0];
                // Language language = languages[0];
                // Connection connect = database.getConnexion(credentials);

                // List<Entity> entities = database.getEntities(connect, credentials, "marque");
                // for (Entity entity : entities) {
                // entity.initialize(connect, credentials, database, language);
                // System.out.println("Generating entity : " + entity.getTableName());
                // FrontGeneration.generateView(HandyManUtils.fromJson(FrontLangage[].class,
                // HandyManUtils.getFileContent(Constantes.FRONT_LANGUAGE_JSON))[0], entity,
                // "akanjov3", "akanjov3-front");
                // }

                // System.out.println("Generating entity : " + entities[2].getTableName());
                // FrontGeneration.generateView(HandyManUtils.fromJson(FrontLangage[].class,
                // HandyManUtils.getFileContent(Constantes.FRONT_LANGUAGE_JSON))[0],
                // entities[2],
                // "akanjov3", "akanjov3-front");

                List<Integer> i = new ArrayList<Integer>();
                System.out.println(i);
                manampy(i);
                System.out.println(i);
                HandyManUtils.createFile("App.txt");

        }

        public static void manampy(List<Integer> i) {
                i.add(4);
        }

}
