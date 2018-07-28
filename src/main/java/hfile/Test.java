package hfile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by Hua wb on 2018/6/27.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        copy2();
    }
    public static void copy(){
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            fis = new FileInputStream("D:\\1.txt");
            fos = new FileOutputStream("D:\\2.txt");
            inChannel = fis.getChannel();
            outChannel = fos.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while (inChannel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                outChannel.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 使用直接缓冲区完成文件的复制(内存映射文件)
     */
    public static void copy2() throws IOException {
        FileChannel inChannel = FileChannel.open(Paths.get("D:/1.txt"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("D:/2.txt"),StandardOpenOption.WRITE,StandardOpenOption.READ,StandardOpenOption.CREATE);
        //内存映射文件
        MappedByteBuffer inMappedBuf = inChannel.map(FileChannel.MapMode.READ_ONLY,0,inChannel.size());
        MappedByteBuffer outMappedBuf = outChannel.map(FileChannel.MapMode.READ_WRITE,0,inChannel.size());
        //直接对缓冲区进行读写操作
        byte[] dst = new byte[inMappedBuf.limit()];
        inMappedBuf.get(dst);
        outMappedBuf.put(dst);

        inChannel.close();
        outChannel.close();
    }

}
