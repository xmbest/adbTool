package utils;

import java.io.*;

public class FileUtil {
    public static void copyFileUsingFileStreams(InputStream source, File dest)
            throws IOException {
        OutputStream output = null;
        try {
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = source.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            source.close();
            if (output != null)
                output.close();
        }
    }
}
