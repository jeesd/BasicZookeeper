package com.jeesd.zookeeper.queue;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

public class TestSimpleDistributedQueue {

	public static void main(String[] args) {  

        ZkClient zkClient = new ZkClient("localhost:2181", 5000, 5000, new SerializableSerializer());  
        SimpleDistributedQueue<String> queue = new SimpleDistributedQueue<String>("/TestZookeeper/jeesd2", zkClient);        
          
        try {  
            queue.offer("Hello");  
            queue.offer("Word");  
            String one = queue.poll();  
            String two = queue.poll();  
            System.out.println("第一个单词"+one);
            System.out.println("第二个单词"+two);
              
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
          
    }  
}
