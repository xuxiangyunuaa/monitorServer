package org.nit.monitorserver.util;

import org.ini4j.Wini;
import org.nit.monitorserver.Config;

import java.io.File;
import java.io.IOException;

import static org.nit.monitorserver.constant.GlobalConsts.*;

/**
 * @author sensordb
 * @date 2018/5/21
 */

public class IniUtil {

    private static IniUtil instance;//单例模式
    private static String initFilePath = Config.getConfFile();
//    private static String httpServer="http";
    private static String udpServer="udp";

    private Wini ini;

    public static final int TEST_MODE = 0;
    public static final int LOCAL_DEBUG_MODE = 1;
    public static final int PRODUCT_MODE = 2;

    private IniUtil() {
        try {
            ini = new Wini(new File(initFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private IniUtil(String initFilePath) {
        try {
            ini = new Wini(new File(initFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setInstance(IniUtil iniUtil) {
        IniUtil.instance = iniUtil;
    }


    private static void changeIniUtil(String initFilePath) {
        IniUtil iniUtil = new IniUtil(initFilePath);
        IniUtil.setInstance(iniUtil);
    }

    private static void changeToTestServer() {
        IniUtil.changeIniUtil("./conf/configTestNode.ini");
    }

    private static void changeToProductServer() {
        IniUtil.changeIniUtil("./conf/configProductNode.ini");
    }


    private static void changeToLocalDebugServer() {
        IniUtil.changeIniUtil("./conf/configLocalDebug.ini");
    }

    public static void changeMode(int mode) {
        if (mode == IniUtil.TEST_MODE) {
            IniUtil.changeToTestServer();
        } else if (mode == IniUtil.LOCAL_DEBUG_MODE) {
            IniUtil.changeToLocalDebugServer();
        } else if (mode == IniUtil.PRODUCT_MODE) {
            IniUtil.changeToProductServer();
        }
    }

    public static IniUtil getInstance() {
        if (instance == null) {
            instance = new IniUtil();
        }
        return IniUtil.instance;
    }


    public int getHTTPServerPort() {
        try {
            return Integer.parseInt(ini.get(HTTP_STR, PORT_STR));

        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 80;
        }
    }

    public String getServerHostName() {
        return ini.get(SERVER_STR, HOST_STR);
    }

    public String getRedisHost() {
        return ini.get(REDIS_STR, HOST_STR);

    }

    public String getRedisPort() {
        return ini.get(REDIS_STR, PORT_STR);
    }

    //获取udp服务器
    public String getUdpServerHost(){
        String udpServerHost=ini.get(udpServer,HOST_STR);
        return udpServerHost;
    }

    public int getUdpServerPort(){
        try {
            return Integer.parseInt(ini.get(udpServer,PORT_STR));

        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 80;
        }
    }
    //获取指令集反馈信息监听端口
    public int getCommandPort(){
        try {
            return Integer.parseInt(ini.get(PORT_STR,COMMANDPORT_STR));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 80;
        }
    }

    public int getCfgcmdPort(){
        try {
            return Integer.parseInt(ini.get(PORT_STR,CFGCMDPORT_STR));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 80;
        }
    }
    public int getDacmdPort(){
        try {
            return Integer.parseInt(ini.get(PORT_STR,DACMDPORT_STR));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 80;
        }
    }

}
