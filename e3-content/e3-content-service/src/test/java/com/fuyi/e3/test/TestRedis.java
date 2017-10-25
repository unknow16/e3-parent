package com.fuyi.e3.test;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fuyi.e3.common.jedis.JedisClient;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/applicationContext-*.xml"})
public class TestRedis {
	
	@Resource
	private JedisClient jedisClientPool;
	
	//@Resource
	//private JedisClient jedisClientCluster;

	
	@Test
	public void testJedisSingle() {
		Jedis jedis = new Jedis("192.168.0.109", 6379);
		jedis.auth("root");
		String s = jedis.hget("client_user_tokenId", "a9fcb504603141e0bcd6a541b6e9d0d4");
		System.out.println(s);
		jedis.close();
	}
	
	/**
	 * 使用连接池连接单机redis
	 */
	@Test
	public void testJedisPool() {
		JedisPool jedisPool = new JedisPool("192.168.0.109", 6379);
		Jedis jedis = jedisPool.getResource();
		jedis.auth("root");
		String s = jedis.hget("client_user_tokenId", "a9fcb504603141e0bcd6a541b6e9d0d4");
		System.out.println(s);
		jedis.close();
		jedisPool.close();
	}
	
	/**
	 * 使用连接池连接集群redis
	 */
	@Test
	public void testJedisCluster() {
		
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		nodes.add(new HostAndPort("192.168.0.109", 6379));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		//直接使用jedisCluster操作redis，在系统中单例存在
		String s = jedisCluster.hget("client_user_tokenId", "a9fcb504603141e0bcd6a541b6e9d0d4");
		System.out.println(s);
		jedisCluster.close();
	}
	
	@Test
	public void testSpringJedisPool() {
		String s = jedisClientPool.hget("client_user_tokenId", "a9fcb504603141e0bcd6a541b6e9d0d4");
		System.out.println(s);
	}
	
	@Test
	public void testSpringJedisCluster() {
		//String s = jedisClientCluster.hget("client_user_tokenId", "a9fcb504603141e0bcd6a541b6e9d0d4");
		//System.out.println(s);
		
	}
}
