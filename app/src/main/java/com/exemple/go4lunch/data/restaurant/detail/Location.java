package com.exemple.go4lunch.data.restaurant.detail;

public class Location{
	private Object lng;
	private Object lat;

	public void setLng(Object lng){
		this.lng = lng;
	}

	public Object getLng(){
		return lng;
	}

	public void setLat(Object lat){
		this.lat = lat;
	}

	public Object getLat(){
		return lat;
	}

	@Override
 	public String toString(){
		return 
			"Location{" + 
			"lng = '" + lng + '\'' + 
			",lat = '" + lat + '\'' + 
			"}";
		}
}
