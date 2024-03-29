
package com.exemple.go4lunch.data.restaurant.nerbysearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

// This class has been generated with https://www.jsonschema2pojo.org/
public class Northeast implements Serializable
{

    @SerializedName("lat")
    @Expose
    private float lat;
    @SerializedName("lng")
    @Expose
    private float lng;
    private final static long serialVersionUID = -368828609472904381L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Northeast() {
    }

    /**
     * 
     * @param lng
     * @param lat
     */
    public Northeast(float lat, float lng) {
        super();
        this.lat = lat;
        this.lng = lng;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public Northeast withLat(float lat) {
        this.lat = lat;
        return this;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public Northeast withLng(float lng) {
        this.lng = lng;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Northeast.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("lat");
        sb.append('=');
        sb.append(this.lat);
        sb.append(',');
        sb.append("lng");
        sb.append('=');
        sb.append(this.lng);
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+ Float.floatToIntBits(this.lng));
        result = ((result* 31)+ Float.floatToIntBits(this.lat));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Northeast) == false) {
            return false;
        }
        Northeast rhs = ((Northeast) other);
        return ((Float.floatToIntBits(this.lng) == Float.floatToIntBits(rhs.lng))&&(Float.floatToIntBits(this.lat) == Float.floatToIntBits(rhs.lat)));
    }

}
