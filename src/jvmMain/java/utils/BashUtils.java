package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BashUtils {

    public static final String dir = System.getProperty("user.home") + "/Desktop";

    public static String execCommand(String command) throws IOException, InterruptedException {
        return execCommand(command, dir);
    }

    /**
     * 执行命令
     *
     * @param command
     * @return
     * @throws IOException
     * @throws InterruptedException
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
        System.out.printf("开始执行命令: %s \n", command);
        Process exec = processBuilder.start();
        // 获取外部程序标准输出流
        OutputHandlerRunnable runnable = new OutputHandlerRunnable(exec.getInputStream(), false);
        new Thread(runnable).start();
        // 获取外部程序标准错误流
        new Thread(new OutputHandlerRunnable(exec.getErrorStream(), true)).start();
        exec.waitFor();
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
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public String getText(){
            return stringBuilder.toString();
        }
    }
}