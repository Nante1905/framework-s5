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
import akanjov2.entities.Produit;
import jakarta.servlet.http.HttpServletResponse;
import org.postgresql.util.PSQLException;
import eriq.flamework.dao.DatabaseInfo;

@Controller
@Singleton

public class ProduitController {
    private DAO dao = new DAO();

    
    @URLMapping("insertproduit.do")
public ModelRest insert(ServletEntity entity){
    Produit o=new Produit();
    DatabaseInfo d = DatabaseInfo.getInstance(null);
    ModelRest model=new ModelRest();
    o.setNom(entity.getData().get("nom"));o.setPrix(Double.parseDouble(entity.getData().get("prix")));o.setMarque(new akanjov2.entities.Marque(Integer.parseInt(entity.getData().get("marque"))));o.setDisponibilite(Integer.parseInt(entity.getData().get("disponibilite")));
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
@URLMapping("produit.do")
public ModelRest crudpage(ServletEntity entity){
    DatabaseInfo d = DatabaseInfo.getInstance(null);
    ModelRest model=new ModelRest();
    try(Connection connex = DAOConnexion.getConnexion(d.getDriver(), d.getSgbd(), d.getHost(), d.getPort(), d.getDatabaseName(), d.getUser(), d.getPwd(), d.isUseSSL(), d.isAllowPublicKeyRetrieval())){
        if(entity.getData().get("idproduit") != null) {
            Produit where=new Produit();
            where.setIdproduit(Integer.parseInt(entity.getData().get("idproduit")));
            Produit[] o=dao.select(connex, Produit.class, where);
            model.addItem("data", o);
        } else {
            Produit[] o=dao.select(connex, Produit.class);
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
@URLMapping("updateproduit.do")
public ModelRest update(ServletEntity entity) {
    Produit o=new Produit();
    o.setNom(entity.getData().get("nom"));o.setPrix(Double.parseDouble(entity.getData().get("prix")));o.setMarque(new akanjov2.entities.Marque(Integer.parseInt(entity.getData().get("marque"))));o.setDisponibilite(Integer.parseInt(entity.getData().get("disponibilite")));
    Produit where=new Produit();
    DatabaseInfo d = DatabaseInfo.getInstance(null);
    ModelRest model=new ModelRest();
    where.setIdproduit(Integer.parseInt(entity.getData().get("idproduit")));
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
@URLMapping("deleteproduit.do")
public ModelRest delete(ServletEntity entity){
    Produit where=new Produit();
    where.setIdproduit(Integer.parseInt(entity.getData().get("idproduit")));
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

