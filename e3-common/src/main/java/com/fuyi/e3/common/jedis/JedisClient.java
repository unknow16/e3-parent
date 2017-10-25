package com.fuyi.e3.common.jedis;

/**
 * jedis 客户端接口
 * 提供两个实现类，分别对应单机和集群
 *
 */
public interface JedisClient {

	String set(String key, String value);
	String get(String key);
	Boolean exists(String key);
	Long expire(String key, int seconds);
	Long ttl(String key);
	Long incr(String key);
	Long hset(String key, String field, String value);
	String hget(String key, String field);
	Long hdel(String key, String... field);
}
