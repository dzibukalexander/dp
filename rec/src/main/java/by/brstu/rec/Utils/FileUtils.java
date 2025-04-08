package by.brstu.rec.Utils;

import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    public static MultipartFile createMultipartFile(byte[] data, String filename, String contentType) {
        return new MultipartFile() {
            @Override
            public String getName() {
                return "file";
            }

            @Override
            public String getOriginalFilename() {
                return filename;
            }

            @Override
            public String getContentType() {
                return contentType;
            }

            @Override
            public boolean isEmpty() {
                return data == null || data.length == 0;
            }

            @Override
            public long getSize() {
                return data.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return data;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(data);
            }

            @Override
            public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
                new java.io.FileOutputStream(dest).write(data);
            }
        };
    }

    public static byte[] byteArrToPrimitive(Byte[] byteObjects) {
        byte[] bytes = new byte[byteObjects.length];
        int i=0;
        for(byte b: byteObjects)
            bytes[i++] = b;
        return bytes;
    }
}