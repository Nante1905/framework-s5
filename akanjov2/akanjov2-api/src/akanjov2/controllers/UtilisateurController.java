package akanjov2.controllers;
import eriq.flamework.annotations.Controller;
import eriq.flamework.annotations.Singleton;
import eriq.flamework.annotations.URLMapping;
import eriq.flamework.model.ModelRest;
import eriq.flamework.model.ModelView;
import eriq.flamework.servlet.ServletEntity;
import veda.godao.DAO;
import veda.godao.utils.DAOConnexion;
import java.sql.Connection;
import akanjov2.entities.Utilisateur;
import jakarta.servlet.http.HttpServletResponse;
import org.postgresql.util.PSQLException;
import eriq.flamework.dao.DatabaseInfo;

@Controller
@Singleton

public class UtilisateurController {
    private DAO dao = new DAO();

    
    @URLMapping("insertutilisateur.do")
public ModelRest insert(ServletEntity entity){
    Utilisateur o=new Utilisateur();
    DatabaseInfo d = DatabaseInfo.getInstance(null);
    ModelRest model=new ModelRest();
    o.setNom(entity.getData().get("nom"));o.setJour(java.time.LocalDate.parse(entity.getData().get("jour")));
    try(Connection connex = DAOConnexion.getConnexion(d.getDriver(), d.getSgbd(), d.getHost(), d.getPort(), d.getDatabaseName(), d.getUser(), d.getPwd(), d.isUseSSL(), d.isAllowPublicKeyRetrieval())){
        dao.insertWithoutPrimaryKey(connex, o);
        connex.commit();
        model.addItem("data", "Inseré");
        return model;
    } catch (Exception e) {
        e.printStackTrace();
        if (e instanceof PSQLException) {
            System.out.println("ERREUR SQL ========");
            model.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            model.addItem("err", "Erreur du serveur : probleme de contrainte de donnee");
        } else {
            model.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            model.addItem("err", "Erreur interne du serveur");
        }
        return model;
    }
}
@URLMapping("utilisateur.do")
public ModelRest crudpage(ServletEntity entity){
    DatabaseInfo d = DatabaseInfo.getInstance(null);
    ModelRest model=new ModelRest();
    try(Connection connex = DAOConnexion.getConnexion(d.getDriver(), d.getSgbd(), d.getHost(), d.getPort(), d.getDatabaseName(), d.getUser(), d.getPwd(), d.isUseSSL(), d.isAllowPublicKeyRetrieval())){
        if(entity.getData().get("id") != null) {
            Utilisateur where=new Utilisateur();
            where.setId(Integer.parseInt(entity.getData().get("id")));
            Utilisateur[] o=dao.select(connex, Utilisateur.class, where);
            model.addItem("data", o);
        } else {
            Utilisateur[] o=dao.select(connex, Utilisateur.class);
            model.addItem("data", o);
        }
        return model;
    } catch (Exception e) {
        e.printStackTrace();
        if (e instanceof PSQLException) {
            System.out.println("ERREUR SQL ========");
            model.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            model.addItem("err", "Erreur du serveur : probleme de récupération de donnee");
        } else {
            model.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            model.addItem("err", "Erreur interne du serveur");
        }
        return model;
    }
}
@URLMapping("updateutilisateur.do")
public ModelRest update(ServletEntity entity) {
    Utilisateur o=new Utilisateur();
    o.setNom(entity.getData().get("nom"));o.setJour(java.time.LocalDate.parse(entity.getData().get("jour")));
    Utilisateur where=new Utilisateur();
    DatabaseInfo d = DatabaseInfo.getInstance(null);
    ModelRest model=new ModelRest();
    where.setId(Integer.parseInt(entity.getData().get("id")));
    try(Connection connex = DAOConnexion.getConnexion(d.getDriver(), d.getSgbd(), d.getHost(), d.getPort(), d.getDatabaseName(), d.getUser(), d.getPwd(), d.isUseSSL(), d.isAllowPublicKeyRetrieval())){
        dao.update(connex, o, where);
        connex.commit();
        model.addItem("data", "Modifié");
        return model;
    } catch (Exception e) {
        e.printStackTrace();
        if (e instanceof PSQLException) {
            System.out.println("ERREUR SQL ========");
            model.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            model.addItem("err", "Erreur du serveur : probleme de contrainte de donnee");
        } else {
            model.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            model.addItem("err", "Erreur interne du serveur");
        }
        return model;
    }
}
@URLMapping("deleteutilisateur.do")
public ModelRest delete(ServletEntity entity){
    Utilisateur where=new Utilisateur();
    where.setId(Integer.parseInt(entity.getData().get("id")));
    DatabaseInfo d = DatabaseInfo.getInstance(null);
    ModelRest model=new ModelRest();
    try(Connection connex = DAOConnexion.getConnexion(d.getDriver(), d.getSgbd(), d.getHost(), d.getPort(), d.getDatabaseName(), d.getUser(), d.getPwd(), d.isUseSSL(), d.isAllowPublicKeyRetrieval())){
        dao.delete(connex, where);
        connex.commit();
        model.addItem("data", "Supprimé");
        return model;
    } catch (Exception e) {
        e.printStackTrace();
        if (e instanceof PSQLException) {
            System.out.println("ERREUR SQL ========");
            model.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            model.addItem("err", "Erreur du serveur : probleme de contrainte de donnee");
        } else {
            model.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            model.addItem("err", "Erreur interne du serveur");
        }
        return model;
    }
}

}

