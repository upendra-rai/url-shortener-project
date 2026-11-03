package com.upendra.url.service.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
public class DistributedIdGenerator {

	private final CuratorFramework client;

	private static final String COUNTER_PATH = "/id-generator/counter";

	private final AtomicLong nextId = new AtomicLong(1);

	private volatile long maxId = 0;

	@Value("${app.id-generator.block-size:1000}")
	private long blockSize;

	private InterProcessMutex lock;

	@PostConstruct
	public void initialize() throws Exception {
		lock = new InterProcessMutex(client, "/locks/id-range");
		allocateRange();
	}

	public long nextId() throws Exception {

		long id = nextId.getAndIncrement();

		if (id <= maxId) {
			return id;
		}

		synchronized (this) {
			if (nextId.get() > maxId) {
				allocateRange();
			}
		}

		return nextId();
	}

	private void allocateRange() throws Exception {
		lock.acquire();

		try {
			byte[] initialValue = "0".getBytes(StandardCharsets.UTF_8);
			try {
				client.create().creatingParentsIfNeeded().forPath(COUNTER_PATH, initialValue);
			} catch (KeeperException.NodeExistsException ignored) {
				// Counter already exists.
			}

			byte[] data = client.getData().forPath(COUNTER_PATH);
			long lastAllocatedId = Long.parseLong(new String(data, StandardCharsets.UTF_8));
			long start = lastAllocatedId + 1;
			long end = lastAllocatedId + blockSize;
			client.setData().forPath(COUNTER_PATH, String.valueOf(end).getBytes(StandardCharsets.UTF_8));
			nextId.set(start);
			maxId = end;

		} finally {
			lock.release();
		}
	}
}