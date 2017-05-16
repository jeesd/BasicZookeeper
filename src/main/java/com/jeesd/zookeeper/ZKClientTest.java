package com.jeesd.zookeeper;

import java.util.List;

public class ZKClientTest {

	public static void main(String[] args) {  
        // 定义父子类节点路径  
       String rootPath = "/TestZookeeper";  
       String child1Path = rootPath + "/jeesd1";  
       String child2Path = rootPath + "/jeesd2";  
 
       //ZooKeeperOperate操作API  
       ZookeeperOpeate zooKeeper = new ZookeeperOpeate();  
 
       // 连接zk服务器   
       zooKeeper.connect("127.0.0.1:2181");  
 
       // 创建节点数据  
       if ( zooKeeper.createZNode(rootPath, "<父>节点数据" ) ) {  
           System.out.println( "节点[" + rootPath + "]数据内容[" + zooKeeper.readData( rootPath ) + "]" );  
       }  
       // 创建子节点, 读取 + 删除  
       if ( zooKeeper.createZNode( child1Path, "<父-子(1)>节点数据" ) ) {  
           System.out.println( "节点[" + child1Path + "]数据内容[" + zooKeeper.readData( child1Path ) + "]" );  
           zooKeeper.deteleZNode(child1Path);  
           System.out.println( "节点[" + child1Path + "]删除值后[" + zooKeeper.readData( child1Path ) + "]" );  
       }  
 
       // 创建子节点, 读取 + 修改  
       if ( zooKeeper.createZNode(child2Path, "<父-子(2)>节点数据" ) ) {  
           System.out.println( "节点[" + child2Path + "]数据内容[" + zooKeeper.readData( child2Path ) + "]" );  
           zooKeeper.updateZNodeData(child2Path, "<父-子(2)>节点数据,更新后的数据" );  
           System.out.println( "节点[" + child2Path+ "]数据内容更新后[" + zooKeeper.readData( child2Path ) + "]" );  
       }  
 
       // 获取子节点  
       List<String> childPaths = zooKeeper.getChild(rootPath);  
       if(null != childPaths){  
           System.out.println( "节点[" + rootPath + "]下的子节点数[" + childPaths.size() + "]" );  
           for(String childPath : childPaths){  
               System.out.println(" |--节点名[" +  childPath +  "]");  
           }  
       }  
       // 判断节点是否存在  
       System.out.println( "检测节点[" + rootPath + "]是否存在:" + zooKeeper.isExists(rootPath)  );  
       System.out.println( "检测节点[" + child1Path + "]是否存在:" + zooKeeper.isExists(child1Path)  );  
       System.out.println( "检测节点[" + child2Path + "]是否存在:" + zooKeeper.isExists(child2Path)  );  
 
 
       zooKeeper.close();  
   }  
}
