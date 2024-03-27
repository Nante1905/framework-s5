package genesis;

public class AuthGenerationTmp {
    public static Entity setAuthInformation(String authClassName, String pseudoName, String pwdName, Database db) {
        Entity e = new Entity(authClassName);
        EntityColumn pk = new EntityColumn();
        pk.setName("id");
        pk.setPrimary(true);
        pk.setType(db.getColumnType().get("incrementType"));
        EntityColumn pseudo = new EntityColumn();
        pseudo.setName(pseudoName);
        pseudo.setType(db.getColumnType().get("pseudoType"));
        EntityColumn pwd = new EntityColumn();
        pwd.setName(pwdName);
        pwd.setType(db.getColumnType().get("pwdType"));
        e.setColumns(pk, pseudo, pwd);
        return e;
    }
}
