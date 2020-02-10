package com.xuecheng.fastdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class testFastdfs {

    @Test
    public void testupload() {
        try {
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
            System.out.println("charset=" + ClientGlobal.g_charset);
            //创建客户端
            TrackerClient tc = new TrackerClient();
            //连接
            TrackerServer ts = tc.getConnection();
            if(ts==null){
                return ;
            }
            //获取store server
            StorageServer ss = tc.getStoreStorage(ts);
            //创建一个store存储客户端
            StorageClient1 sc1 = new StorageClient1(ts, ss);
            //本地文件路径
            String item = "E:\\img_0940.jpg";
            String file1 = sc1.upload_file1(item, "jpg", null);
            System.out.println(file1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testdowloadFile() {
        try {
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            TrackerClient tc = new TrackerClient();
            TrackerServer ts = tc.getConnection();
            StorageServer storageServer = null;
            StorageClient1 storageClient1 = new StorageClient1(ts, storageServer);
            byte[] res = storageClient1.download_file1("group1/M00/00/00/rBCpbF4-amaAaqwnAAEa2cZcgDY099.png");
            File file = new File("C:\\Users\\6yang\\Desktop\\1.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(res);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testqueryFile() throws IOException, MyException {
        ClientGlobal.initByProperties("config/fastdfs-client.properties");

        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        StorageServer storageServer = null;
        StorageClient storageClient = new StorageClient(trackerServer,
                storageServer);
        FileInfo fileInfo = storageClient.query_file_info("group1", "M00/00/00/rBCpbF4-amaAaqwnAAEa2cZcgDY099.png");
        System.out.println(fileInfo);

    }
}
