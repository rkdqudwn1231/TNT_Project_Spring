package com.tnt.project.services;

import java.util.*;

import org.springframework.stereotype.Component;

@Component
public class BodySurveryAlgorithm {

	
	 public String evaluate(String q1, String q2, String q3, String q4, String q5) {
	        Map<String, Integer> score = new HashMap<>();
	        score.put("A", 0);
	        score.put("V", 0);
	        score.put("H", 0);
	        score.put("O", 0);
	        score.put("X", 0);

	        switch (q1) {
	            case "UPPER_DOMINANT": score.put("V", score.get("V") + 2); break;
	            case "LOWER_DOMINANT": score.put("A", score.get("A") + 2); break;
	            case "BALANCED": score.put("X", score.get("X") + 1); break;
	        }

	        switch (q2) {
	            case "WIDE_SHOULDER": score.put("V", score.get("V") + 2); break;
	            case "NARROW_SLOPE_SHOULDER": score.put("A", score.get("A") + 1); break;
	        }

	        switch (q3) {
	            case "CURVED_WAIST": score.put("X", score.get("X") + 2); break;
	            case "STRAIGHT_WAIST": score.put("H", score.get("H") + 2); break;
	            case "BELLY_CENTER": score.put("O", score.get("O") + 2); break;
	        }

	        switch (q4) {
	            case "WIDE_HIP": score.put("A", score.get("A") + 2); break;
	            case "NARROW_HIP": score.put("V", score.get("V") + 1); break;
	        }

	        switch (q5) {
	            case "O_LEG": score.put("O", score.get("O") + 2); break;
	            case "X_LEG": score.put("X", score.get("X") + 1); break;
	        }

	        return Collections.max(score.entrySet(), Map.Entry.comparingByValue()).getKey();
	    }
}
