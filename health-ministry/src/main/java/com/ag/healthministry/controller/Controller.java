package com.ag.healthministry.controller;

import com.ag.healthministry.service.DecisionCenter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	private DecisionCenter decisionCenter;

	@Autowired
	public Controller(DecisionCenter decisionCenter) {
		this.decisionCenter = decisionCenter;
	}

	@PostMapping(value = "/daily-case", consumes = "application/json")
	public void dailyCoronaCounts(@RequestBody String pubsubMessage) {
		try {
			JsonObject convertedObject = new Gson().fromJson(pubsubMessage, JsonObject.class);

			String message = convertedObject.get("message").getAsJsonObject().get("data").getAsString();

			String hospitalName = convertedObject.get("message").getAsJsonObject()
					.get("attributes").getAsJsonObject()
					.get("hospital-name").getAsString();

			String effectedCount = new String(Base64.getDecoder().decode(message), StandardCharsets.UTF_8);

			decisionCenter.evaluate(hospitalName, effectedCount);
		} catch(Exception ex) {
			System.out.println("Exception ... ");
		}
	}
}
