package com.mgp.estimator.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.mgp.estimator.controller.CacheController;

@Service("hazelServiceImpl")
public class HazelServiceImpl implements HazelService {

	private final Logger logger = LoggerFactory.getLogger(CacheController.class);

	private final HazelcastInstance hazelcastInstance;

	@Autowired
	HazelServiceImpl(HazelcastInstance hazelcastInstance) {
		this.hazelcastInstance = hazelcastInstance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mgp.estimator.service.HazelService#writeData(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String writeData(String cacheName, String key, String value) {
		Map<String, String> hazelMap = hazelcastInstance.getMap(cacheName);
		logger.debug("data updating in cache {}", cacheName);
		hazelMap.put(key, value);
		return "Data stored";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mgp.estimator.service.HazelService#readData(java.lang.String)
	 */
	@Override
	public String readData(String cacheName, String key) {
		Map<String, String> hazelMap = hazelcastInstance.getMap(cacheName);
		logger.debug("data retreieved from cache {}", cacheName);
		return hazelMap.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mgp.estimator.service.HazelService#readData(java.lang.String)
	 */
	@Override
	public String updateData(String cacheName, String key, String value) {
		Map<String, String> hazelMap = hazelcastInstance.getMap(cacheName);
		logger.debug("data retreieved from cache {}", cacheName);
		return hazelMap.put(key, value);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mgp.estimator.service.HazelService#clearData(java.lang.String)
	 */
	@Override
	public void clearData(String cacheName, String key) {
		Map<String, String> hazelMap = hazelcastInstance.getMap(cacheName);
		hazelMap.remove(key);
		logger.debug("data cleared for key {} from cache {}", key, cacheName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mgp.estimator.service.HazelService#clearCompleteData()
	 */
	@Override
	public void clearCompleteData(String cacheName) {
		Map<String, String> hazelMap = hazelcastInstance.getMap(cacheName);
		hazelMap.clear();
		logger.debug("Full cache {} cleared", cacheName);
	}

}
