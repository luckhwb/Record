package zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Hua wb on 2018/7/4.
 */
public class TestZk {
    public static void cr() throws IOException, KeeperException, InterruptedException {
        final CountDownLatch connectedSignal = new CountDownLatch(1);
        String host = "127.0.0.1:2182";
        ZooKeeper zoo = new ZooKeeper(host,5000, we -> {
            // Watcher
            if (we.getState() == Watcher.Event.KeeperState.SyncConnected) {
                connectedSignal.countDown();
            }
        });
        ZkUtil zkUtil = new ZkUtil();
        ZooKeeper zk = zoo;
        String path = "/hwb2";
        byte[] bytes = "test port 2 ".getBytes();
        //zkUtil.delete(zk, path);
        zkUtil.create(zk, path, bytes);
        Stat stat = zkUtil.znode_exists(zk, path);
        System.out.println(stat);
        List<String> child = zkUtil.getChild(zk, path);
        System.out.println("child:" + child);
        String data = zkUtil.getData(zk, path);
        System.out.println("data:" + data);

    }
    public static void zkUtil() throws IOException, InterruptedException, KeeperException{
        ZkUtil zkUtil = new ZkUtil();
        ZooKeeper zk = zkUtil.connect("127.0.0.1:2181");
        String path = "/hwb";
        byte[] bytes = "test".getBytes();
        zkUtil.delete(zk, path);
        zkUtil.create(zk, path, bytes);
        Stat stat = zkUtil.znode_exists(zk, path);
        System.out.println(stat);
        List<String> child = zkUtil.getChild(zk, path);
        System.out.println("child:" + child);
        String data = zkUtil.getData(zk, path);
        System.out.println("data:" + data);
        bytes = "修改一次".getBytes();
        zkUtil.update(zk, path, bytes);
        String data1 = zkUtil.getData(zk, path);
        System.out.println("dataupdate:" + data1);
    }
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        cr();
    }
}
