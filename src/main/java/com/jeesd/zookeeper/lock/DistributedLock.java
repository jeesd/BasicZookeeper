package com.jeesd.zookeeper.lock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁接口
 * @author song
 *
 */
public interface DistributedLock {
	
	/**
	 * 获取锁，如果没有得到就等待
	 * @throws Exception
	 */
	void acquire() throws Exception;
	
	/**
	 * 获取锁，直到超时
	 * @param time
	 * @param unit
	 * @return
	 * @throws Exception
	 */
	boolean acquire(long time, TimeUnit unit) throws Exception;
	
	/**
	 * 释放锁
	 * @throws Exception
	 */
	void release() throws Exception;

}
