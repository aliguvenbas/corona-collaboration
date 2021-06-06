package com.ag.healthministry.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	private Integer totalEffected = 0;

	@PostMapping(value = "/daily-case", consumes = "application/json")
	public void dailyCoronaCounts(@RequestBody String pubsubMessage) {
		try {
			JsonObject convertedObject = new Gson().fromJson(pubsubMessage, JsonObject.class);

			String message = convertedObject.get("message").getAsJsonObject().get("data").getAsString();

			String hospitalName = convertedObject.get("message").getAsJsonObject()
					.get("attributes").getAsJsonObject()
					.get("hospital-name").getAsString();

			String effectedCount = new String(Base64.getDecoder().decode(message), StandardCharsets.UTF_8);
			totalEffected += Integer.valueOf(effectedCount);

			System.out.println(hospitalName + " declared " + effectedCount + " new effected case. " +
					"Total effected: " + totalEffected);

			if(totalEffected > 50) {
				resctictionLevel1();
			} else if(totalEffected > 100) {
				resctictionLevel2();
			}
		} catch(Exception ex) {
			System.out.println("Exception ... ");
		}

	}

	private void resctictionLevel1() {
		System.out.println("complete lock-down");
	}

	private void resctictionLevel2() {
		System.out.println("all hospitals are pandemic");
	}

}
