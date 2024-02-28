// import com.fasterxml.jackson.databind.ObjectMapper;

// import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.sql.Connection;

import genesis.Constantes;
import genesis.Credentials;
import genesis.Database;
import genesis.Entity;
import genesis.Language;
import genesis.frontend.FrontGeneration;
import genesis.frontend.variables.FrontLangage;
import handyman.HandyManUtils;

public class Test {

    public static void main(String[] args) throws Throwable {

        Database[] databases = HandyManUtils.fromJson(Database[].class,
                HandyManUtils.getFileContent(Constantes.DATABASE_JSON));
        Language[] languages = HandyManUtils.fromJson(Language[].class,
                HandyManUtils.getFileContent(Constantes.LANGUAGE_JSON));
        String databaseName = "akanjo", user = "postgres", pwd = "2003", host = "localhost";
        boolean useSSL = false, allowPublicKeyRetrieval = true;
        Credentials credentials = new Credentials(databaseName, user, pwd, host,
                "5432", useSSL,
                allowPublicKeyRetrieval);
        Database database = databases[0];
        Language language = languages[0];
        Connection connect = database.getConnexion(credentials);

        Entity[] entities = database.getEntities(connect, credentials, "*");
        for (Entity entity : entities) {
            entity.initialize(connect, credentials, database, language);
        }

        System.out.println("Generating entity : " + entities[0].getTableName());
        String generated = FrontGeneration.generateForm(HandyManUtils.fromJson(FrontLangage[].class,
                HandyManUtils.getFileContent(Constantes.FRONT_LANGUAGE_JSON))[0],
                entities[0]);

        File file = new File("test.tsx");
        file.createNewFile();

        HandyManUtils.overwriteFileContent("test.tsx", generated);

    }

}
