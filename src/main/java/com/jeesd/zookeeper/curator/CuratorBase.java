package com.jeesd.zookeeper.curator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;

public class CuratorBase {
	
	/**
     * zookeeper地址
     */
	public static final String CONNECT_ADDR = "localhost:2181";
	/**
	 * session超时时间
	 */
	public static final int SESSION_OUTTIME = 5000;
	
	public static void main(String[] args) {
		//1 重试策略：初试时间为1s 重试10次
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10); 
        //2 通过工厂创建连接
        CuratorFramework cf = null;
        try {
        	cf = CuratorFrameworkFactory.builder()
        			.connectString(CONNECT_ADDR)
                    .sessionTimeoutMs(SESSION_OUTTIME)
                    .retryPolicy(retryPolicy)
                    .namespace("TestZookeeper")
                    .build();
        	//3 开启连接
            cf.start();
            //create创建节点方法可选的链式项：creatingParentsIfNeeded（是否同时创建父节点）、withMode（创建的节点类型）、forPath（创建的节点路径）、withACL（安全项）
            //delete删除节点方法可选的链式项：deletingChildrenIfNeeded（是否同时删除子节点）、guaranteed（安全删除）、withVersion（版本检查）、forPath（删除的节点路径）
            //inBackground绑定异步回调方法。比如在创建节点时绑定一个回调方法，该回调方法可以输出服务器的状态码以及服务器的事件类型等信息，还可以加入一个线程池进行优化操作。
            //4 建立节点 指定节点类型（不加withMode默认为持久类型节点）、路径、数据内容
            //cf.create().withMode(CreateMode.PERSISTENT).forPath("/jeesd2/c1", "c1内容".getBytes());
            //5 删除节点
            cf.delete().forPath("/jeesd2/c1");
           
            /*创建节点*/
            cf.create().withMode(CreateMode.PERSISTENT).forPath("/jeesd2/c1", "c1内容".getBytes());
            //cf.create().withMode(CreateMode.PERSISTENT).forPath("/jeesd2/c2", "c2内容".getBytes());
            /*读取节点*/
            String ret1 = new String(cf.getData().forPath("/jeesd2/c2"));
            System.out.println(ret1);
            /*修改节点*/
            cf.setData().forPath("/jeesd2/c2", "修改c2内容".getBytes());
            String ret2 = new String(cf.getData().forPath("/jeesd2/c2"));
            System.out.println(ret2);
            
            // 绑定回调函数
            ExecutorService pool = Executors.newCachedThreadPool();
            cf.create().withMode(CreateMode.PERSISTENT)
            	.inBackground(new BackgroundCallback() {
					
					public void processResult(CuratorFramework cf, CuratorEvent ce) throws Exception {
						System.out.println("code:" + ce.getResultCode());
                        System.out.println("type:" + ce.getType());
                        System.out.println("线程为:" + Thread.currentThread().getName());
						
					}
				},pool)
            	 .forPath("/jeesd2/c3", "c3内容".getBytes());

            Thread.sleep(Integer.MAX_VALUE);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭连接");
            CloseableUtils.closeQuietly(cf);
        }
        
        
        /*try {  
            client.start();  
            // 开启事务  
            CuratorTransaction transaction = client.inTransaction();  
  
            Collection<CuratorTransactionResult> results = transaction.create()  
                    .forPath("/a/path", "some data".getBytes()).and().setData()  
                    .forPath("/another/path", "other data".getBytes()).and().delete().forPath("/yet/another/path")  
                    .and().commit();  
  
            for (CuratorTransactionResult result : results) {  
                System.out.println(result.getForPath() + " - " + result.getType());  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            // 释放客户端连接  
            CloseableUtils.closeQuietly(client);  
        }  */
	}

}
