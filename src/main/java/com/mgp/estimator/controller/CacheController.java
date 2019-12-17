package com.mgp.estimator.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hazelcast.core.HazelcastInstance;

@RestController
public class CacheController {

	private final Logger logger = LoggerFactory.getLogger(CacheController.class);

	private final HazelcastInstance hazelcastInstance;

	@Autowired
	CacheController(HazelcastInstance hazelcastInstance) {
		this.hazelcastInstance = hazelcastInstance;
	}

	@PostMapping(value = "/write", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String writeData(@RequestParam String key, @RequestParam String value, @RequestParam String cacheName) {
		Map<String, String> hazelMap = hazelcastInstance.getMap(cacheName);
		hazelMap.put(key, value);
		logger.debug("data inserted");
		return "Data stored";
	}

	@GetMapping(value = "/read", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String readData(@RequestParam String key, @RequestParam String cacheName) {
		Map<String, String> hazelMap = hazelcastInstance.getMap(cacheName);
		logger.debug("data retrieved");
		return hazelMap.get(key);
	}
	
	@DeleteMapping(value = "/delete")
	public void clearData(@RequestParam String key, @RequestParam String cacheName) {
		Map<String, String> hazelMap = hazelcastInstance.getMap(cacheName);
		hazelMap.remove(key);
		logger.debug("data cleared for key {}",key);
	}
	
	@DeleteMapping(value = "/fulldelete")
	public void clearCompleteData(@RequestParam String cacheName) {
		Map<String, String> hazelMap = hazelcastInstance.getMap(cacheName);
		hazelMap.clear();
		logger.debug("Full cache cleared");
	}
	
}
