package com.airport.build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.airport.AirportMarker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;

public class AirportMarkerBuild {

	List<PointFeature> features;

	public AirportMarkerBuild(List<PointFeature> features) {
		this.features = features;
	}

	public List<Marker> makeAirportsMarkers() {
		List<Marker> airportList = new ArrayList<Marker>();
		for (PointFeature feature : features) {
			if (isBrazil(feature)) {
				AirportMarker m = new AirportMarker(feature);
				m.setRadius(5);
				m.setId(feature.getId());
				airportList.add(m);
			}
		}
		return airportList;
	}

	public HashMap<Integer, Location> makeAirportsMap() {
		HashMap<Integer, Location> airports = new HashMap<Integer, Location>();
		for (PointFeature feature : features) {
			if (isBrazil(feature)) {
				airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
			}
		}
		return airports;
	}

	private static boolean isBrazil(PointFeature feature) {
		return feature.getProperty("country").equals("\"Brazil\"");
	}
}
