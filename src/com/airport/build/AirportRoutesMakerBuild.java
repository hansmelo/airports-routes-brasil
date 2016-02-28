package com.airport.build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;

public class AirportRoutesMakerBuild {
	List<ShapeFeature> routes;
	List<PointFeature> features;

	public AirportRoutesMakerBuild(List<ShapeFeature> routes, List<PointFeature> features) {
		this.routes = routes;
		this.features = features;
	}

	public ArrayList<Marker> make() {
		AirportMarkerBuild airportMarkerBuild = new AirportMarkerBuild(features);
		HashMap<Integer, Location> airports = airportMarkerBuild.makeAirportsMap();
		ArrayList<Marker> routeList = new ArrayList<Marker>();

		for (ShapeFeature route : routes) {
			// get source and destination airportIds
			int source = Integer.parseInt((String) route.getProperty("source"));
			int dest = Integer.parseInt((String) route.getProperty("destination"));
			// get locations for airports on route
			if (airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
			}
			SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
			sl.setHidden(true);
			routeList.add(sl);
		}

		return routeList;
	}

}
