package zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Hua wb on 2018/7/4.
 */
public class ZkUtil {
    private ZooKeeper zoo;
    final CountDownLatch connectedSignal = new CountDownLatch(1);

    public ZooKeeper connect(String host) throws IOException,InterruptedException {
        zoo = new ZooKeeper(host,5000, we -> {
            // Watcher
            if (we.getState() == Watcher.Event.KeeperState.SyncConnected) {
                connectedSignal.countDown();
            }
        });
        connectedSignal.await();
        return zoo;
    }
    public void create(ZooKeeper zk, String path, byte[] data) throws
            KeeperException,InterruptedException {
        zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
    }
    public void delete(ZooKeeper zk, String path) throws KeeperException,InterruptedException {
        zk.delete(path,zk.exists(path,true).getVersion());
    }
    public Stat znode_exists(ZooKeeper zk, String path) throws
            KeeperException,InterruptedException {
        return zk.exists(path, true);
    }

    public List<String> getChild(ZooKeeper zk, String path) throws KeeperException, InterruptedException {
        Stat stat = znode_exists(zk,path);
        if(stat!= null) {
            //“getChildren" method- get all the children of znode.It has two
            //args, path and watch
            return zk.getChildren(path, false);
        } else {
            return null;
        }
    }
    public String getData(ZooKeeper zk, String path) throws KeeperException, InterruptedException,
            UnsupportedEncodingException {
        Stat stat = znode_exists(zk,path);
        byte[] b = new byte[]{};
        if(stat != null) {
           b = zk.getData(path, we -> {
               if (we.getType() == Watcher.Event.EventType.None) {
                   switch(we.getState()) {
                       case Expired:
                           new CountDownLatch(1).countDown();
                           break;
                   }

               } else {
                   String path1 = "/MyFirstZnode";

                   try {
                       byte[] bn = zk.getData(path1,
                               false, null);
                       String data = new String(bn,
                               "UTF-8");
                       System.out.println(data);
                       new CountDownLatch(1).countDown();
                   } catch(Exception ex) {
                       System.out.println(ex.getMessage());
                   }
               }
           }, null);
    }
    return new String(b, "UTF-8");
    }

    public void update(ZooKeeper zk, String path, byte[] data) throws
            KeeperException,InterruptedException {
        zk.setData(path, data, zk.exists(path,true).getVersion());
    }


    public void close() throws InterruptedException {
        zoo.close();
    }
}
