package genesis;

public class Credentials {
    private String sgbd, driver, databaseName, user, pwd, host, port;
    private boolean useSSL, allowPublicKeyRetrieval;

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isUseSSL() {
        return useSSL;
    }

    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

    public boolean isAllowPublicKeyRetrieval() {
        return allowPublicKeyRetrieval;
    }

    public void setAllowPublicKeyRetrieval(boolean allowPublicKeyRetrieval) {
        this.allowPublicKeyRetrieval = allowPublicKeyRetrieval;
    }

    public Credentials(String databaseName, String user, String pwd, String host, String port, boolean useSSL,
            boolean allowPublicKeyRetrieval) {
        setDatabaseName(databaseName);
        setUser(user);
        setPwd(pwd);
        setHost(host);
        setPort(port);
        setUseSSL(useSSL);
        setAllowPublicKeyRetrieval(allowPublicKeyRetrieval);
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSgbd() {
        return sgbd;
    }

    public void setSgbd(String sgbd) {
        this.sgbd = sgbd;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

}
