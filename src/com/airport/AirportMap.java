package com.airport;

import java.util.List;

import com.airport.build.AirportMarkerBuild;
import com.airport.build.AirportRoutesMakerBuild;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

/**
 * An applet that shows airports (and routes) on a world map.
 * 
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 *         MOOC team
 * @author Hans Melo - modified class
 */

public class AirportMap extends PApplet {

	UnfoldingMap map;
	private List<Marker> airportList;
	List<Marker> routeList;
	private CommonMarker lastSelected;
	private Boolean lastClicked = true;

	public void setup() {
		// setting up PAppler
		size(800, 600, OPENGL);

		// setting up map and default events
		map = new UnfoldingMap(this, 50, 50, 750, 550);
		MapUtils.createDefaultEventDispatcher(this, map);

		List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
		AirportMarkerBuild airportMarkerBuild = new AirportMarkerBuild(features);
		airportList = airportMarkerBuild.makeAirportsMarkers();
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
		AirportRoutesMakerBuild airportRoutesMakerBuild = new AirportRoutesMakerBuild(routes, features);
		routeList = airportRoutesMakerBuild.make();

		map.zoomToLevel(2);
		map.addMarkers(routeList);
		map.addMarkers(airportList);
	}

	public void draw() {
		background(0);
		map.draw();
	}

	/**
	 * Event handler that gets called automatically when the mouse moves.
	 */
	@Override
	public void mouseMoved() {
		clearLastSelected();
		selectAirportIfHover(airportList);
	}

	private void clearLastSelected() {
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		}
	}

	private void selectAirportIfHover(List<Marker> airports) {
		if (lastSelected != null) {
			return;
		}

		for (Marker m : airports) {
			CommonMarker marker = (CommonMarker) m;
			if (marker.isInside(map, mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}

	/**
	 * The event handler for mouse clicks It will display an earthquake and its
	 * threat circle of cities Or if a city is clicked, it will display all the
	 * earthquakes where the city is in the threat circle
	 */
	@Override
	public void mouseClicked() {
		if (lastClicked) {
			showRouteAirport();
			lastClicked = false;
		} else {
			hiddenRoutesAirport();
			lastClicked = true;
		}
	}

	private void showRouteAirport() {
		for (Marker m : airportList) {
			CommonMarker airport = (CommonMarker) m;
			if (airport.isInside(map, mouseX, mouseY)) {
				for (Marker route : routeList) {
					if (airport.getId().equals(route.getProperty("source"))) {
						route.setHidden(false);
					}
				}
				break;
			}
		}
	}

	private void hiddenRoutesAirport() {
		routeList.forEach(route -> route.setHidden(true));
	}
}
