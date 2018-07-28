package hfile.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by Hua wb on 2018/6/27.
 */
public class client {
    public static void main(String[] args) throws IOException {
        client();
    }
    public static void client() throws IOException {
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",0625));
        FileChannel inChannel = FileChannel.open(Paths.get("D:/1.txt"), StandardOpenOption.READ);
        ByteBuffer buf = ByteBuffer.allocate(1024);
        //读取本地文件,发送到服务端
        while (inChannel.read(buf) != -1){
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }
        sChannel.shutdownOutput();
        //接收服务端的反馈
        int len = 0;
        while ((len = sChannel.read(buf)) != -1){
            buf.flip();
            System.out.println(new String(buf.array(),0,len));
            buf.clear();
        }
        inChannel.close();
        sChannel.close();
    }
}
