package com.mindnerves.meidcaldiary;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.jackson.JacksonFactory;

import Model.PlacesList;

@SuppressWarnings("deprecation")
public class GooglePlaces {

	/** Global instance of the HTTP transport. */
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	// Google API Key
	private static final String API_KEY = "AIzaSyCRLa4LQZWNQBcjCYcIVYA45i9i8zfClqc"; // place your API key here

	// Google Places serach url's
	private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
	private static final String PLACES_TEXT_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
	private static final String PLACES_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?";

	private double _latitude;
	private double _longitude;
	private double _radius;
    private String types;
    private PlacesList list;
	

	public PlacesList search(double latitude, double longitude, double radius, final String types)
			throws Exception {
        this._latitude = latitude;
        this._longitude = longitude;
        this._radius = radius;
        this.types = types;

        try {

            HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
            HttpRequest request = httpRequestFactory
                    .buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));
            request.getUrl().put("key", API_KEY);
            request.getUrl().put("location", _latitude + "," + _longitude);
            request.getUrl().put("radius", 2000); // in meters
            request.getUrl().put("sensor", "false");
            request.getUrl().put("types", "Police");
            System.out.println("Request:::::::" + request.getUrl());
            list = request.execute().parseAs(PlacesList.class);
            System.out.println("List:::::" + list.resultSize());
            // Check log cat for places response status


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return list;



	}

//	public PlaceDetails getPlaceDetails(String reference) throws Exception {
//		try {
//
//			HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
//			HttpRequest request = httpRequestFactory
//					.buildGetRequest(new GenericUrl(PLACES_DETAILS_URL));
//			request.getUrl().put("key", API_KEY);
//			request.getUrl().put("reference", reference);
//			request.getUrl().put("sensor", "false");
//
//			PlaceDetails place = request.execute().parseAs(PlaceDetails.class);
//
//			return place;
//
//		} catch (HttpResponseException e) {
//
//			throw e;
//		}
//	}

	/**
	 * Creating http request Factory
	 * */
	public static HttpRequestFactory createRequestFactory(
			final HttpTransport transport) {
		return transport.createRequestFactory(new HttpRequestInitializer() {
			public void initialize(HttpRequest request) {
				GoogleHeaders headers = new GoogleHeaders();
				headers.setApplicationName("AndroidHive-Places-Test");
				request.setHeaders(headers);
				JsonHttpParser parser = new JsonHttpParser(new JacksonFactory());
				request.addParser(parser);
			}
		});
	}

}
