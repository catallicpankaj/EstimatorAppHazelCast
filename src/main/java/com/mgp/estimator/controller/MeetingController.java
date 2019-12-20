package com.mgp.estimator.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mgp.estimator.service.HazelServiceImpl;

@RestController
public class MeetingController {

	private static final String INITIATOR_CACHE_NAME = "initiatorCache";
	private final Logger logger = LoggerFactory.getLogger(MeetingController.class);

	@Autowired
	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	@Qualifier("hazelServiceImpl")
	HazelServiceImpl hazelServiceImpl;

	@PostMapping(value = "/create-session", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public JsonNode createSessionId(@RequestParam String initiatorName) throws IOException {
		UUID sessionUuid = UUID.randomUUID();
		logger.info("uuid generated for initiator {} is {}", initiatorName, sessionUuid);
		ArrayNode arrayNode = mapper.createArrayNode();
		ObjectNode objectNode = mapper.createObjectNode();
		UUID userUuid = UUID.randomUUID();
		Map<String, String> userMap = new HashMap<>();
		userMap.put("userName", initiatorName);
		userMap.put("isInitiator", "true");
		userMap.put("votedAs", "");
		userMap.put("showVotes", "");
		objectNode.set(userUuid.toString(), mapper.readTree(mapper.writeValueAsString(userMap)));
		arrayNode.add(objectNode);
		hazelServiceImpl.writeData(INITIATOR_CACHE_NAME, sessionUuid.toString(), mapper.writeValueAsString(arrayNode));
		ObjectNode outputNode = mapper.createObjectNode();
		Map<String, String> nodeData = new HashMap<>();
		nodeData.put("initiatedBy", initiatorName);
		nodeData.put("generatedUniqueKey", sessionUuid.toString());
		outputNode.set("sessionInitiatorData", mapper.readTree(mapper.writeValueAsString(nodeData)));
		return outputNode;
	}

	@PutMapping(value = "/update-session", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void updateSessionData(@RequestParam String sessionKey, @RequestParam String newUserName)
			throws IOException {
		logger.info("Updating data from session Key {} ", sessionKey);
		ObjectNode objectNode = mapper.createObjectNode();
		UUID userUuid = UUID.randomUUID();
		Map<String, String> userMap = new HashMap<>();
		userMap.put("userName", newUserName);
		userMap.put("votedAs", "");
		userMap.put("showVotes", "");
		objectNode.set(userUuid.toString(), mapper.readTree(mapper.writeValueAsString(userMap)));
		ArrayNode existingCacheData = (ArrayNode) mapper
				.readTree(hazelServiceImpl.readData(INITIATOR_CACHE_NAME, sessionKey));
		logger.info("cacheData is {}", existingCacheData);
		existingCacheData.add(objectNode);
		hazelServiceImpl.updateData(INITIATOR_CACHE_NAME, sessionKey, mapper.writeValueAsString(existingCacheData));
	}

	@SuppressWarnings("unchecked")
	@PutMapping(value = "update-vote", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void updateVoteForUsers(@RequestParam String sessionKey, @RequestParam String uuid,
			@RequestParam String votedValue) throws IOException {
		logger.info("Updating vote in session Key {} for uuid {} ", sessionKey, uuid);
		String cacheDataForSessionKey = hazelServiceImpl.readData(INITIATOR_CACHE_NAME, sessionKey);
		if (cacheDataForSessionKey != null) {
			ArrayNode existingCacheData = (ArrayNode) mapper.readTree(cacheDataForSessionKey);
			logger.info("cacheData is {}", existingCacheData);
			for (int i = 0; i < existingCacheData.size(); i++) {
				if (uuid.equals(existingCacheData.get(i).fieldNames().next())) {
					logger.info("Match found updating the data for {}", existingCacheData.get(i).fieldNames().next());
					Map<String, String> mapToBeUpdated = mapper.convertValue(existingCacheData.get(i).at("/" + uuid),
							Map.class);
					mapToBeUpdated.put("votedAs", votedValue);
					((ObjectNode) existingCacheData.get(i)).set(uuid,
							mapper.readTree(mapper.writeValueAsString(mapToBeUpdated)));
				}
			}
			hazelServiceImpl.updateData(INITIATOR_CACHE_NAME, sessionKey, mapper.writeValueAsString(existingCacheData));
		}
	}

	
	@PutMapping(value = "update-ticket", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void updateTicketForUsers(@RequestParam String sessionKey, @RequestParam String uuid,
			@RequestParam String ticketNumber) throws IOException {
		logger.info("Updating vote in session Key {} for uuid {} ", sessionKey, uuid);
		String cacheDataForSessionKey = hazelServiceImpl.readData(INITIATOR_CACHE_NAME, sessionKey);
		if (cacheDataForSessionKey != null) {
			Map<String,String> ticketNumberObject = new HashMap<>();
			ticketNumberObject.put("ticketNumber", ticketNumber);
			ArrayNode existingCacheData = (ArrayNode) mapper.readTree(cacheDataForSessionKey);
			existingCacheData.add(mapper.readTree(mapper.writeValueAsString(ticketNumberObject)));
			hazelServiceImpl.updateData(INITIATOR_CACHE_NAME, sessionKey, mapper.writeValueAsString(existingCacheData));
		}
	}
	
	@SuppressWarnings("unchecked")
	@PutMapping(value = "clear-votes", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void clearVoteForUsers(@RequestParam String sessionKey) throws IOException {
		logger.info("Updating vote in session Key {} ", sessionKey);
		String cacheDataForSessionKey = hazelServiceImpl.readData(INITIATOR_CACHE_NAME, sessionKey);
		if (cacheDataForSessionKey != null) {
			ArrayNode existingCacheData = (ArrayNode) mapper.readTree(cacheDataForSessionKey);
			logger.info("cacheData is {}", existingCacheData);
			for (int i = 0; i < existingCacheData.size(); i++) {
					logger.info("Clearing votes for uuid {}", existingCacheData.get(i).fieldNames().next());
					Map<String, String> mapToBeUpdated = mapper.convertValue(existingCacheData.get(i).at("/" + existingCacheData.get(i).fieldNames().next()),
							Map.class);
					mapToBeUpdated.put("votedAs", "");
					mapToBeUpdated.put("showVotes", "");
					((ObjectNode) existingCacheData.get(i)).set(existingCacheData.get(i).fieldNames().next(),
							mapper.readTree(mapper.writeValueAsString(mapToBeUpdated)));
				}
			hazelServiceImpl.updateData(INITIATOR_CACHE_NAME, sessionKey, mapper.writeValueAsString(existingCacheData));
		}
	}
	
	@SuppressWarnings("unchecked")
	@PutMapping(value = "show-vote", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void showVoteToUsers(@RequestParam String sessionKey) throws IOException {
		logger.info("Updating show vote in session Key {} for all users ", sessionKey);
		String cacheDataForSessionKey = hazelServiceImpl.readData(INITIATOR_CACHE_NAME, sessionKey);
		if (cacheDataForSessionKey != null) {
			ArrayNode existingCacheData = (ArrayNode) mapper.readTree(cacheDataForSessionKey);
			logger.info("cacheData is {}", existingCacheData);
			for (int i = 0; i < existingCacheData.size(); i++) {
					logger.info("Match found updating the data for {}", existingCacheData.get(i).fieldNames().next());
					Map<String, String> mapToBeUpdated = mapper.convertValue(existingCacheData.get(i).at("/" + existingCacheData.get(i).fieldNames().next()),
							Map.class);
					mapToBeUpdated.put("showVotes", "true");
					((ObjectNode) existingCacheData.get(i)).set(existingCacheData.get(i).fieldNames().next(),
							mapper.readTree(mapper.writeValueAsString(mapToBeUpdated)));
			}
			hazelServiceImpl.updateData(INITIATOR_CACHE_NAME, sessionKey, mapper.writeValueAsString(existingCacheData));
		}
	}
}
