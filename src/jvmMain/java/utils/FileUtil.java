package utils;

import status.AllKt;

import java.io.*;
import java.util.Objects;

public class FileUtil {
    public static void copyFileUsingFileStreams(InputStream source, File dest)
            throws IOException {
        try (source; OutputStream output = new FileOutputStream(dest)) {
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = source.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        }
    }

    public static void initFile() throws IOException {
        File cfgParent = new File(BashUtil.workDir, "cfg");
        if (!cfgParent.exists()) {
            cfgParent.mkdirs();
        }

        //adb环境
        if (Objects.equals(BashUtil.split, "\\")) {
            String adbName = "adb.exe";
            File adb1 = new File(cfgParent, adbName);
            String bundleToolsName = "bundletool-all-1.15.4.jar";
            File bundleTools = new File(cfgParent, bundleToolsName);
            if (!adb1.exists()) {
                LogUtil.Companion.d("create windows adb.exe");
            }
            new Thread(() -> {
                setFilePath(adb1, adbName, false);
                setFilePath(bundleTools, bundleToolsName, false);
                AllKt.getAdb().setValue(adb1.getAbsolutePath());
                AllKt.getBundletool().setValue(bundleTools.getAbsolutePath());
                PropertiesUtil.Companion.setValue("adb", AllKt.getAdb().getValue(), "");
                PropertiesUtil.Companion.setValue("bundletool", AllKt.getAdb().getValue(), "");
            }).start();
        } else {
            String adbName = "adb";
            File adb1 = new File(cfgParent, adbName);
            String bundleToolsName = "bundletool-all-1.15.4.jar";
            File bundleTools = new File(cfgParent, bundleToolsName);
            if (!adb1.exists()) {
                new Thread(() -> {
                    setFilePath(adb1, adbName, true);
                    setFilePath(bundleTools, bundleToolsName, true);
                    AllKt.getAdb().setValue(adb1.getAbsolutePath());
                    AllKt.getBundletool().setValue(bundleTools.getAbsolutePath());
                    PropertiesUtil.Companion.setValue("adb", AllKt.getAdb().getValue(), "");
                    PropertiesUtil.Companion.setValue("bundletool", AllKt.getAdb().getValue(), "");
                    LogUtil.Companion.d("create mac adb");
                }).start();
            }
        }
    }

    private static void setFilePath(File file, String fileName, Boolean isMac) {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(fileName);
        if (inputStream != null) {
            try {
                FileUtil.copyFileUsingFileStreams(inputStream, file);
            } catch (IOException e) {
                LogUtil.Companion.d(e.getMessage());
            }
        }
        if (isMac) {
            file.setExecutable(true);
        }
    }
}
