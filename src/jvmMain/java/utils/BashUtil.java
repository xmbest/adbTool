package utils;

import status.AllKt;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BashUtil {

    // back.inline.test.conn.cfg.txz~dat
    public static byte[] test = {9,0,0,0,8,-49,-127,-96,-123,12,16,-69,3,102,22,-28,120};
    //back.inline.dev.conn.cfg.txz~dat
    public static byte[] dev = {9,0,0,0,8,-51,-127,-96,-123,12,16,-69,3,27,17,-63,58};

    public static String split = "\\";

    public static boolean runing = false;

    public static String desktop_dir = System.getProperty("user.home") + split + "Desktop";
    public static  String workDir = System.getProperty("user.home") + split + "ADBTool";

    public static void init() throws IOException {
        if (!SysUtilKt.getOsType().equals("windows")){
            split = "/";
            desktop_dir = System.getProperty("user.home") + split + "Desktop";
            workDir = System.getProperty("user.home") + split + "ADBTool";
            AllKt.getDesktop().setValue(desktop_dir);
        }
    }


    public static String execCommand(String command) throws IOException, InterruptedException {
        return execCommand(command, workDir);
    }


    /**
     * 执行命令
     *
     * @param command
     */
    public static String execCommand(String command, String dir) throws IOException, InterruptedException {
        String[] commands = command.split(" ");
        List<String> commandList = new ArrayList<>(commands.length);
        for (String s : commands) {
            if (s.isBlank()) {
                continue;
            }
            commandList.add(s);
        }
        commands = new String[commandList.size()];
        commands = commandList.toArray(commands);
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        if (!dir.isBlank()) {
            processBuilder.directory(new File(dir));
        }
        if (!command.equals("adb devices"))
            LogUtil.Companion.d("exec: "+command);
        runing = true;
        Process exec = processBuilder.start();
        // 获取外部程序标准输出流
        OutputHandlerRunnable runnable = new OutputHandlerRunnable(exec.getInputStream(), false);
        new Thread(runnable).start();
        // 获取外部程序标准错误流
        new Thread(new OutputHandlerRunnable(exec.getErrorStream(), true)).start();
        exec.waitFor();
        runing = false;
//        System.out.println(runnable.getText());
        LogUtil.Companion.flushRes(runnable.getText());
        return runnable.getText();
    }

    private static class OutputHandlerRunnable implements Runnable {
        private InputStream in;

        private boolean error;

        private StringBuilder stringBuilder = new StringBuilder();


        public OutputHandlerRunnable(InputStream in, boolean error) {
            this.in = in;
            this.error = error;
        }

        @Override
        public void run() {
            try (BufferedReader bufr = new BufferedReader(new InputStreamReader(this.in))) {
                String line = null;
                while ((line = bufr.readLine()) != null) {
                    if (!error) {
                        stringBuilder.append(line).append("\n");
                    } else {
                        LogUtil.Companion.e(line);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public String getText() {
            return stringBuilder.toString();
        }
    }
}