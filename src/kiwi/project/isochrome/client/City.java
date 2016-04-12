package kiwi.project.isochrome.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;


public class City implements IsSerializable {
	private String name;
	private String code;
	private HashMap<String, Double> distances;
	
	public City () {
		this("", "", new HashMap<String, Double>());	
	}
	
	public City (String name, String code, HashMap<String, Double> distances) {
		this.name = name;
		this.code = code;
		this.distances = distances;
	}
	
	public City (String name, String code) {
		this (name, code, new HashMap<String, Double>());
	}
	
	public double getDistance (String destination) {
		return distances.get(destination).doubleValue();
	}
	
	public String name() {
		return name;
	}
	
	public String code() {
		return code;
	}
}
