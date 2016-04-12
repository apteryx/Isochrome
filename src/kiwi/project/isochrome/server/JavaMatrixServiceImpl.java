package kiwi.project.isochrome.server;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import kiwi.project.isochrome.client.City;
import kiwi.project.isochrome.client.MatrixService;
import kiwi.project.isochrome.interfaces.DistanceType;
import kiwi.project.isochrome.util.DistanceFactory;

import org.python.util.PythonInterpreter; 
import org.python.core.*; 

public class JavaMatrixServiceImpl {
	private DistanceFactory factory = new DistanceFactory();
	private DistanceType distance;
	
	public JavaMatrixServiceImpl() {
		distance = factory.create();
	}
	
	private double getDistance(City origin, City destination) {
		return Math.random() * 10;
	}
	
	// draw the matrix with origins on the x axis and destinations on the y
	
	public int[][] getMatrix(ArrayList<City> origins, ArrayList<City> destinations) {
		String[] originRequest = new String[origins.size()];
		String[] destinationRequest = new String[destinations.size()];
		
		for (int i = 0; i < origins.size(); i++)
			originRequest[i] = origins.get(i).code();
		
		for (int i = 0; i < destinations.size(); i++) 
			destinationRequest[i] = destinations.get(i).code();
		
		return distance.requestMatrix(originRequest, destinationRequest);
		
	}

	
	public City getCity(String cityName) {
		ArrayList<City> cities = new ArrayList<City>();
		cities.add(new City("Austin", "Austin+TX")); cities.add(new City("New York", "New+York+NY"));
		cities.add(new City("Seattle", "Seattle+WA")); cities.add(new City("San Francisco", "San+Francisco+CA"));
		
		for (City city: cities) {
			if (city.name().contains(cityName)) return city;
		}
		
		return null;
	}

}
