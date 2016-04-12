package kiwi.project.isochrome.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

import com.google.gwt.query.client.GQuery;
import static com.google.gwt.query.client.GQuery.*;

public class DistanceMatrix implements EntryPoint {
	private FlexTable matrixFlexTable = new FlexTable();
	private Button addDestinationButton = new Button("Add Destination");
	private TextBox addDestinationBox = new TextBox();
	private Button addOriginButton = new Button("Add Origin");
	private TextBox addOriginBox = new TextBox();
	
	private CityButtonHandler cityHandler = new CityButtonHandler();

	
	private ArrayList<City> origins = (ArrayList<City>) Arrays.asList( new City[] {
		new City("Seattle", "Seattle+WA"), new City("San Francisco", "San+Francisco+CA")
	} );
	private ArrayList<City> destinations = new ArrayList<City>();

	private final MatrixServiceAsync matrixService = GWT.create(MatrixService.class);
		
	// handler for origin & destination buttons (used to delete row & col)
	class CityButtonHandler implements MouseOverHandler, MouseOutHandler, ClickHandler {
		public void onMouseOver(MouseOverEvent event) {
		    Widget sender = (Widget) event.getSource();
		    sender.addStyleName("delete");
		}
		public void onMouseOut(MouseOutEvent event) {
		    Widget sender = (Widget) event.getSource();
		    sender.removeStyleName("delete");
		}
		public void onClick(ClickEvent event) {
		    Widget sender = (Widget) event.getSource();
		    sender.getElement().getParentElement().setId("deleting");
		    			    
		    int row = getRowToDelete();
		    int col = getColToDelete();
		    
		    if (row > 0) {
		    	deleteRow(row);
		    } else if (col > 0) {
		    	deleteCol(col);
		    }
		}
	}
	
	@Override
	public void onModuleLoad() {		
		// TODO: automatic defaults
		origins.add(new City("Seattle", "Seattle+WA")); origins.add(new City("San Francisco", "San+Francisco+CA"));
		destinations.add(origins.get(0)); destinations.add(new City("New York", "New+York+NY"));
		
		for (int o=1; o<=origins.size(); o++)
			matrixFlexTable.setWidget(0, o, buildCityButton(origins.get(o-1), "originButton"));
	
		for (int d=1; d<=destinations.size(); d++)
			matrixFlexTable.setWidget(d, 0, buildCityButton(destinations.get(d-1), "destinationButton"));
		
		matrixFlexTable.setWidget(3, 0, addDestinationButton);
		matrixFlexTable.setWidget(0, 3, addOriginButton);
				
		matrixFlexTable.setStyleName("distanceMatrix");
		matrixFlexTable.getRowFormatter().addStyleName(0, "origins");
		matrixFlexTable.getColumnFormatter().setStyleName(0, "destinations");
				
		RootPanel.get().add(matrixFlexTable);
		
		addOriginButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				matrixFlexTable.setWidget(0, origins.size()+1, addOriginBox);
				addOriginBox.setFocus(true);
			}
		});
		
		addOriginBox.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					addOrigin();
				}
			}
		});
		
		addDestinationButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				matrixFlexTable.setWidget(destinations.size()+1, 0, addDestinationBox);
				addDestinationBox.setFocus(true);
			}
		});
		
		addDestinationBox.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					addDestination();
				}
			}
		});
		
		
		loadMatrix();
	}
	
	private int getColToDelete() {
		GQuery mark = $("#deleting");				// cell marked for col deletion
		Element e = mark.elements()[0];				// DOM element for that cell
		return mark.parent().children().index(e);	// find index of cell in row
	}
	
	private int getRowToDelete() {
		GQuery mark = $("#deleting").parent();		// row marked for row deletion
		Element e = mark.elements()[0];				// DOM element for that row
		return mark.parent().children().index(e);	// find index of row in table
	}
	
	private void deleteRow(int r) {
		matrixFlexTable.removeRow(r);
	}
	
	private void deleteCol(int c) {
		for (int r = 0; r < matrixFlexTable.getRowCount(); r++) {
			matrixFlexTable.removeCell(r, c);
		}
	}
	
	private Button buildCityButton(City city, String style) {
		Button button = new Button(city.name());
		button.addStyleName(style);
		
		button.addMouseOverHandler(cityHandler);
		button.addMouseOutHandler(cityHandler);
		button.addClickHandler(cityHandler);
		
		return button;
	}	
	
	private void loadMatrix() {
		String[] originCodes = new String [origins.size()];
		String[] destinationCodes = new String [destinations.size()];
		
		for (int i = 0; i < origins.size(); i++)
			originCodes[i] = origins.get(i).code();
		
		for (int i = 0; i < destinations.size(); i++) {
			destinationCodes[i] = destinations.get(i).code();
		}
		
		matrixService.getMatrix(originCodes, destinationCodes, new AsyncCallback<ArrayList<ArrayList<Integer>>>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO: replace with actual error dialog
				Window.alert(caught.getMessage());
			}

			@Override
			public void onSuccess(ArrayList<ArrayList<Integer>> result) {
				for (int row = 0; row < result.size(); row++) {
					for (int col = 0; col < result.get(0).size(); col++) {
						int dist = result.get(row).get(col);
						
						if (dist == 0) 	matrixFlexTable.setText(row + 1, col + 1, "-");
						else 			matrixFlexTable.setText(row + 1, col + 1, String.valueOf(dist));
					}
				}
				
			}
			
		});
	}

	private void addOrigin() {
		matrixService.getCity(addOriginBox.getText(), new AsyncCallback<City>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}

			@Override
			public void onSuccess(City origin) {
				addOriginBox.setText("");
				origins.add(origin);
				
				matrixFlexTable.setWidget(0, origins.size(), buildCityButton(origin, "originButton"));
				matrixFlexTable.setWidget(0, origins.size() + 1, addOriginButton);
				
				loadMatrix();
			}
		});
	}
	
	private void addDestination() {
		matrixService.getCity(addDestinationBox.getText(), new AsyncCallback<City>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			
			@Override
			public void onSuccess(City destination) {
				addDestinationBox.setText("");
				
				destinations.add(destination);
				
				matrixFlexTable.setWidget(destinations.size(), 0, buildCityButton(destination, "destinationButton"));
				matrixFlexTable.setWidget(destinations.size() + 1, 0, addDestinationButton);
				
				loadMatrix();
			}
		});
	}
}
