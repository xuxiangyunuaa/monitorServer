package org.nit.monitorserver;


import static org.nit.monitorserver.constant.GlobalConsts.YZ_HOME;

/**
 * @author eda
 * @date 2018/5/21
 */

public class Config {

    private static String confRoot = ((System.getenv(YZ_HOME) == null) ? "./conf/" :
            (System.getenv(YZ_HOME) + "/conf/"));

    private static String confFile = confRoot + "config.ini";
    private static String localConfFile = confRoot + "configLocalDebug.ini";
    private static String remoteConfFile = confRoot + "configProductNode.ini";

    private static String databaseConfFile = confRoot + "database.conf";
    private static String localDatabaseConfFile = confRoot + "databaseLocalDebug.conf";
    private static String remoteDatabaseConfFile = confRoot + "databaseRemoteDebug.conf";

    private static String databaseMongoConfFile = confRoot + "databaseMongo.conf";
    private static String localDatabaseMongoConfFile = confRoot + "databaseMonoLocalDebug.conf";
    private static String remoteDatabaseMongoConfFile = confRoot + "databaseMongoRemoteDebug.conf";

    private static String hbaseConfFile = confRoot + "hbase.conf";


    public static void setLocalConfFile() {
        Config.confFile = localConfFile;
    }

    public static void setRemoteConfFile() {
        Config.confFile = remoteConfFile;
    }

    public static String getConfFile() {
        return Config.confFile;
    }

    public static void setDatabaseConfFile(final String databaseConfFile) {//mysql
        Config.databaseConfFile = databaseConfFile;
    }

    public static void setLocalDatabaseConfFile() {//mysql
        Config.databaseConfFile = localDatabaseConfFile;
    }

    public static void setDatabaseMongoConfFile(final String databaseMongoConfFile) {//mongodb
        Config.databaseMongoConfFile = databaseMongoConfFile;
    }

    public static void setLocalDatabaseMongoConfFile() {//mongodb
        Config.databaseMongoConfFile = localDatabaseMongoConfFile;
    }

    public static void setRemoteDatabaseConfFile() {
        Config.databaseConfFile = remoteDatabaseConfFile;
    }

    public static String getDatabaseConfFile() {//mysql
        return Config.databaseConfFile;
    }

    public static String getDatabaseMongoConfFile() {//mongodb
        return Config.databaseMongoConfFile;
    }
//返回玖翼的数据库连接，目前没用
    public static String getRemoteDatabaseConfFile() {
        return Config.databaseConfFile;
    }

    public static String getHbaseConfFile() {
        return Config.hbaseConfFile;
    }

}
