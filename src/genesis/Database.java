package genesis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import exceptions.EntityNotFoundException;
import exceptions.MissingColumnException;

public class Database {
    private int id;
    private String nom;
    private String driver;
    private String port;
    private HashMap<String, String> types;
    private String getcolumnsQuery;
    private String gettablesQuery;
    private String singleTableQuery;
    private HashMap<String, String> columnType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public HashMap<String, String> getTypes() {
        return types;
    }

    public void setTypes(HashMap<String, String> types) {
        this.types = types;
    }

    public String getGetcolumnsQuery() {
        return getcolumnsQuery;
    }

    public void setGetcolumnsQuery(String getcolumnsQuery) {
        this.getcolumnsQuery = getcolumnsQuery;
    }

    public String getGettablesQuery() {
        return gettablesQuery;
    }

    public void setGettablesQuery(String gettablesQuery) {
        this.gettablesQuery = gettablesQuery;
    }

    public Connection getConnexion(Credentials credentials) throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        String url = "jdbc:%s://%s:%s/%s?user=%s&password=%s&useSSL=%s&allowPublicKeyRetrieval=%s";
        url = String.format(url, getNom(), credentials.getHost(), credentials.getPort(), credentials.getDatabaseName(),
                credentials.getUser(), credentials.getPwd(), credentials.isUseSSL(),
                credentials.isAllowPublicKeyRetrieval());
        Connection connex = DriverManager.getConnection(url);
        connex.setAutoCommit(false);
        return connex;
    }

    public Entity verifyAuthTable(Connection conn, Credentials credentials, String tableName, String pseudoName,
            String pwdName, Language language) throws Exception {
        try {
            Entity e = getEntities(conn, credentials, tableName).get(0);
            e.initialize(conn, credentials, this, language);
            String errorMessage = "";
            if (e.hasColumn(pseudoName) == false) {
                errorMessage += "Column " + pseudoName + " doesn't exist in " + tableName + ".\n";
            }
            if (e.hasColumn(pwdName) == false) {
                errorMessage += "Column " + pwdName + " doesn't exist in " + tableName + ".";
            }
            if (errorMessage.equals("")) {
                return e;
            }
            throw new MissingColumnException(errorMessage);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (ClassNotFoundException | SQLException e) {
            throw e;
        }
    }

    public Entity createTable(Connection conn, Credentials credentials, Entity entity) throws Exception {
        String query = " CREATE TABLE IF NOT EXISTS  " + entity.getTableName() + " ( ";
        for (EntityColumn c : entity.getColumns()) {
            query += c.getName() + " " + c.getType() + " ";
            if (c.isPrimary()) {
                query += " PRIMARY KEY";
            }
            query += ",";
        }
        query = query.substring(0, query.length() - 1);
        query += " )";
        System.out.println(query);
        Statement statement = conn.createStatement();
        statement.executeUpdate(query);
        conn.commit();
        statement.close();

        // get Entity information
        try {
            return getEntities(conn, credentials, entity.getTableName()).get(0);
        } catch (EntityNotFoundException e) {
            throw new Exception("Cannot create table " + entity.getTableName());
        }
    }

    public List<Entity> getEntities(Connection connex, Credentials credentials, String entityName)
            throws ClassNotFoundException, SQLException, EntityNotFoundException {
        boolean opened = false;
        Connection connect = connex;
        if (connect == null) {
            connect = getConnexion(credentials);
            opened = true;
        }
        String query;
        if (entityName.equals("*")) {
            query = getGettablesQuery().replace("[databaseName]", credentials.getDatabaseName());
        } else {
            query = getSingleTableQuery().replace("[tableName]", entityName);
        }
        PreparedStatement statement = connect.prepareStatement(query);
        try {
            List<Entity> liste = new ArrayList<Entity>();
            Entity entity;
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    entity = new Entity();
                    entity.setTableName(result.getString("table_name"));
                    liste.add(entity);
                }
            }
            if (liste.size() == 0) {
                throw new EntityNotFoundException(entityName + " not found in your database ");
            }
            return liste;
        } finally {
            statement.close();
            if (opened) {
                connect.close();
            }
        }
    }

    public String getSingleTableQuery() {
        return singleTableQuery;
    }

    public void setSingleTableQuery(String singleTableQuery) {
        this.singleTableQuery = singleTableQuery;
    }

    public HashMap<String, String> getColumnType() {
        return columnType;
    }

    public void setColumnType(HashMap<String, String> columnType) {
        this.columnType = columnType;
    }
}
