package hfile.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by Hua wb on 2018/6/27.
 */
public class server {
    public static void main(String[] args) throws IOException {
        server();
    }
    public static void server() throws IOException {
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        FileChannel outChannel = FileChannel.open(Paths.get("D:/2.txt"), StandardOpenOption.WRITE,StandardOpenOption.CREATE);

        //绑定端口号
        ssChannel.bind(new InetSocketAddress(0625));
        //获取客户端连接的通道
        SocketChannel sChannel = ssChannel.accept();

        ByteBuffer buf = ByteBuffer.allocate(1024);
        //接收客户端的数据,并保存到本地
        while (sChannel.read(buf) != -1){
            buf.flip();
            outChannel.write(buf);
            buf.clear();
        }

        //发送反馈给客户端
        buf.put("服务端接收数据成功!".getBytes());
        buf.flip();
        sChannel.write(buf);

        sChannel.close();
        outChannel.close();
        ssChannel.close();
    }
}
