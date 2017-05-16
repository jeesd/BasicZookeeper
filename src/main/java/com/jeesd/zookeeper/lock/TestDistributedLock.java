package com.jeesd.zookeeper.lock;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;

public class TestDistributedLock {

	public static void main(String[] args) {  
        
        final ZkClient zkClient1 = new ZkClient("localhost:2181", 5000, 5000, new BytesPushThroughSerializer());  
        final SimpleDistributedLock jeesd1 = new SimpleDistributedLock(zkClient1, "/TestZookeeper/jeesd2",null);  
          
        final ZkClient zkClient2 = new ZkClient("localhost:2181", 5000, 5000, new BytesPushThroughSerializer());  
        final SimpleDistributedLock jeesd2 = new SimpleDistributedLock(zkClient2, "/TestZookeeper/jeesd2",null);  
          
        try {  
        	jeesd1.acquire();  
            System.out.println("Client1 locked");  
            Thread client2Thd = new Thread(new Runnable() {  
                  
                public void run() {  
                    try {  
                    	jeesd2.acquire();  
                        System.out.println("Client2 locked");  
                        jeesd2.release();  
                        System.out.println("Client2 released lock");  
                          
                    } catch (Exception e) {  
                        e.printStackTrace();  
                    }                 
                }  
            });  
            client2Thd.start();  
            Thread.sleep(5000);  
            jeesd1.release();             
            System.out.println("Client1 released lock");  
              
            client2Thd.join();  
              
        } catch (Exception e) {  
  
            e.printStackTrace();  
        }  
          
    }  
}
