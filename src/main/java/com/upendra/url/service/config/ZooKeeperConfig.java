package com.upendra.url.service.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZooKeeperConfig {

	@Bean(initMethod = "start", destroyMethod = "close")
	public CuratorFramework curatorFramework(
			@Value("${zookeeper.connect-string:127.0.0.1:2181}") String connectString,
			@Value("${zookeeper.session-timeout:60000}") int sessionTimeout,
			@Value("${zookeeper.connection-timeout:15000}") int connectionTimeout) {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		return CuratorFrameworkFactory.builder().connectString(connectString).sessionTimeoutMs(sessionTimeout)
				.connectionTimeoutMs(connectionTimeout).retryPolicy(retryPolicy).namespace("url-shortener").build();
	}
}