package com.jeesd.zookeeper.queue;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.I0Itec.zkclient.ExceptionUtil;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

/**
 * 分布式队列
 * @author song
 *
 */
public class SimpleDistributedQueue<T> {
	
	private final ZkClient zkClient;
	
	private final String basePath;
	 
	private  static final String  QUEUE_NAME ="queue-";
	
	public SimpleDistributedQueue(String path, ZkClient client) {
		this.basePath = path;
		this.zkClient = client;
	}
	
	//获取队列的大小  
    public int size() {  
        /** 
         * 通过获取根节点下的子节点列表 
         */  
        return zkClient.getChildren(basePath).size();  
    }  
      
    //判断队列是否为空  
    public boolean isEmpty() {  
        return zkClient.getChildren(basePath).size() == 0;  
    }  
      
    /** 
     * 向队列提供数据 
     * @param element 
     * @return 
     * @throws Exception 
     */  
    public boolean offer(T element) throws Exception{  
          
        //构建数据节点的完整路径  
        String nodeFullPath = basePath.concat( "/" ).concat(QUEUE_NAME);  
        try {  
            //创建持久的节点，写入数据  
            zkClient.createPersistentSequential(nodeFullPath , element);  
        }catch (ZkNoNodeException e) {  
            zkClient.createPersistent(basePath);  
            offer(element);  
        } catch (Exception e) {  
            throw ExceptionUtil.convertToRuntimeException(e);  
        }  
        return true;  
    } 
    
    //从队列取数据  
    @SuppressWarnings("unchecked")  
    public T poll() throws Exception {           
        try {  
            List<String> list = zkClient.getChildren(basePath);  
            if (list.size() == 0) {  
                return null;  
            }  
            //将队列安装由小到大的顺序排序  
            Collections.sort(list, new Comparator<String>() {  
                public int compare(String lhs, String rhs) {  
                    return getNodeNumber(lhs, QUEUE_NAME).compareTo(getNodeNumber(rhs, QUEUE_NAME));  
                }  
            });  
              
            /** 
             * 将队列中的元素做循环，然后构建完整的路径，在通过这个路径去读取数据 
             */  
            for (String nodeName : list) {    
                String nodeFullPath = basePath.concat("/").concat(nodeName);      
                try {  
                    T node = (T) zkClient.readData(nodeFullPath);  
                    zkClient.delete(nodeFullPath);  
                    return node;  
                } catch (ZkNoNodeException e) {  
                    // ignore  
                }  
            }               
            return null;     
        } catch (Exception e) {  
            throw ExceptionUtil.convertToRuntimeException(e);  
        }  
  
    }  
    
    private String getNodeNumber(String str, String nodeName) {  
        int index = str.lastIndexOf(nodeName);  
        if (index >= 0) {  
            index += QUEUE_NAME.length();  
            return index <= str.length() ? str.substring(index) : "";  
        }  
        return str;  
  
    }  

}
