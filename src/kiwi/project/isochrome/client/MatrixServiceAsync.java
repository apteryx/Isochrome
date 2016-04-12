package kiwi.project.isochrome.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MatrixServiceAsync {
	void getMatrix(String[] originCodes, String[] destinationCodes,
			AsyncCallback<ArrayList<ArrayList<Integer>>> callback);
	void getCity(String cityName, AsyncCallback<City> callback);
}
