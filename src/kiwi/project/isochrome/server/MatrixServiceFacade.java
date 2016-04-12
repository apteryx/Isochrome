package kiwi.project.isochrome.server;

import java.util.ArrayList;

import org.plyjy.factory.JythonObjectFactory;
import org.python.core.PyArray;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import kiwi.project.isochrome.client.City;
import kiwi.project.isochrome.client.MatrixService;

public class MatrixServiceFacade extends RemoteServiceServlet implements MatrixService {
	private JythonObjectFactory factory = null;
	String pyServletName = null;


	@Override
	public City getCity(String cityName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<ArrayList<Integer>> getMatrix(String[] originCodes, String[] destinationCodes) {
		factory = factory.getInstance();
		pyServletName = getInitParameter("PyServletName");
		MatrixService jythonServlet = (MatrixService) JythonObjectFactory.createObject(MatrixService.class, pyServletName);
		
		originCodes = new String[] {"Seattle+WA", "San+Francisco+CA"};
		destinationCodes = new String[] {"Seattle+WA", "New+York+NY"};
		return jythonServlet.getMatrix(originCodes, destinationCodes);
	}

}
