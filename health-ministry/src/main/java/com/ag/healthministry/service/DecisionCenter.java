package com.ag.healthministry.service;

import org.springframework.stereotype.Service;

@Service
public class DecisionCenter {
	private Integer totalEffected = 0;

	public void evaluate(String hospitalName, String effectedCount) {
		totalEffected += Integer.valueOf(effectedCount);

		System.out.println(hospitalName + " declared " + effectedCount + " new effected case. " +
				"Total effected: " + totalEffected);

		if(totalEffected > 50) {
			resctictionLevel1();
		} else if(totalEffected > 100) {
			resctictionLevel2();
		}
	}

	private void resctictionLevel1() {
		System.out.println("complete lock-down");
	}

	private void resctictionLevel2() {
		System.out.println("all hospitals are pandemic");
	}

}
