package com.mgp.estimator.service;

public interface HazelService {
	
	/**
	 * Add the data to cache.
	 * 
	 * @param cacheName 
	 * @param key - key
	 * @param value - value to store 
	 * @return
	 */
	public String writeData(String cacheName, String key, String value);

	
	/**
	 * Read the data from Cache 
	 * 
	 * @param cacheName 
	 * @param key
	 * @return
	 */
	public String readData(String cacheName, String key);
	
	/**
	 * Update existing key Data in Cache
	 * 
	 * @param cacheName
	 * @param key - Data will be updated for this key.
	 * @param value
	 * @return
	 */
	public String updateData(String cacheName, String key, String value);

	/**
	 * clear the entire data for a key from cache
	 * 
	 * @param cacheName
	 * @param key
	 */
	public void clearData(String cacheName, String key);

	/**
	 * 
	 * Clear full cache. 
	 * 
	 * @param cacheName
	 */
	public void clearCompleteData(String cacheName);
}
