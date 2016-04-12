package kiwi.project.isochrome.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("matrix")
public interface MatrixService extends RemoteService {
	ArrayList<ArrayList<Integer>> getMatrix (String[] originCodes, String[] destinationCodes);
	City getCity (String cityName);
}
