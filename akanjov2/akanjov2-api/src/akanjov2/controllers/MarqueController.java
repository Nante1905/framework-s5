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
import akanjov2.entities.Marque;
import jakarta.servlet.http.HttpServletResponse;
import org.postgresql.util.PSQLException;
import eriq.flamework.dao.DatabaseInfo;

@Controller
@Singleton

public class MarqueController {
    private DAO dao = new DAO();

    
    @URLMapping("insertmarque.do")
public ModelRest insert(ServletEntity entity){
    Marque o=new Marque();
    DatabaseInfo d = DatabaseInfo.getInstance(null);
    ModelRest model=new ModelRest();
    o.setNom(entity.getData().get("nom"));
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
@URLMapping("marque.do")
public ModelRest crudpage(ServletEntity entity){
    DatabaseInfo d = DatabaseInfo.getInstance(null);
    ModelRest model=new ModelRest();
    try(Connection connex = DAOConnexion.getConnexion(d.getDriver(), d.getSgbd(), d.getHost(), d.getPort(), d.getDatabaseName(), d.getUser(), d.getPwd(), d.isUseSSL(), d.isAllowPublicKeyRetrieval())){
        if(entity.getData().get("idmarque") != null) {
            Marque where=new Marque();
            where.setIdmarque(Integer.parseInt(entity.getData().get("idmarque")));
            Marque[] o=dao.select(connex, Marque.class, where);
            model.addItem("data", o);
        } else {
            Marque[] o=dao.select(connex, Marque.class);
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
@URLMapping("updatemarque.do")
public ModelRest update(ServletEntity entity) {
    Marque o=new Marque();
    o.setNom(entity.getData().get("nom"));
    Marque where=new Marque();
    DatabaseInfo d = DatabaseInfo.getInstance(null);
    ModelRest model=new ModelRest();
    where.setIdmarque(Integer.parseInt(entity.getData().get("idmarque")));
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
@URLMapping("deletemarque.do")
public ModelRest delete(ServletEntity entity){
    Marque where=new Marque();
    where.setIdmarque(Integer.parseInt(entity.getData().get("idmarque")));
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

