package com.djamil.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 4/15/20
 */
public class ConnectionDetector {
	private static final String TAG = "ConnectionDetector";

	private static ConnectionDetector instance = new ConnectionDetector();
	private static Context context;
	private boolean isConnectedToWifi = false;
	private boolean isConnectedToMobile = false;
	private static ConnectivityManager connectivityManager;
	NetworkInfo wifiInfo, mobileInfo;
	boolean connected = false;

	public static ConnectionDetector getInstance(Context ctx) {
		context = ctx;
		return instance;
	}

	/**
	 * V�rifie la pr�sence de connexion sur un r�seau
	 *            Permet d'acc�der au contexte de l'application
	 * @return boolean
	 */
	public boolean isConnectingToInternet() {

		try {

			connectivityManager = (ConnectivityManager) ConnectionDetector.context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			// Log.d("networkInfo", networkInfo.getState().toString());
			// Log.d("networkInfo.isAvailable()",
			// Boolean.toString(networkInfo.isAvailable()));
			// Log.d("networkInfo.isConnected()",
			// Boolean.toString(networkInfo.isConnected()));
			connected = networkInfo != null && networkInfo.isAvailable()
					&& networkInfo.isConnected();
			Log.d("connected", Boolean.toString(connected));
			return connected;

		} catch (Exception e) {

			System.out.println("isConnectingToInternet Exception: "
					+ e.getMessage());
			Log.v("ConnectionDetector", e.toString());

		}

		return connected;
	}

	/**
	 * V�rifie les r�seaux accessibles
	 *            Permet d'acc�der au contexte de l'application
	 * @return boolean
	 */
	public void checkNetworks() {

		try {

			connectivityManager = (ConnectivityManager) ConnectionDetector.context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo[] netInfo = connectivityManager.getAllNetworkInfo();

			for (NetworkInfo network : netInfo) {
				if (network.getTypeName().equalsIgnoreCase("WIFI")) {
					if (network.isConnected()) {
						this.isConnectedToWifi = true;
					}
				}
				if (network.getTypeName().equalsIgnoreCase("MOBILE")) {
					if (network.isConnected()) {
						this.isConnectedToMobile = true;
					}
				}
			}

		} catch (Exception e) {
			System.out.println("checkNetwork Exception: " + e.getMessage());
			Log.v("ConnectionDetector", e.toString());
		}

	}

	/**
	 * V�rifie si le r�seau Wifi est accessible
	 * 
	 * @return boolean
	 */
	public boolean checkWifiConnexion() {
		return this.isConnectedToWifi;
	}

	/**
	 * Vérifie si le réseau 3G, LTE est accessible
	 * 
	 * @return boolean
	 */
	public boolean checkConnectedToMobile() {
		return this.isConnectedToMobile;
	}

	// Run it on another Thread please
	// urltoReach : http://192.168.43.211
	// Precisely the app: http://192.168.43.211:8080/kairos

	/**
	 * Vérifie si le serveur d'application online est disponible
	 * 
	 * @return boolean
	 */
	public boolean isURLReachable(Context context, String urltoReach) {
		connectivityManager = (ConnectivityManager) ConnectionDetector.context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnected()) {
			try {
				Log.i("netInfo.isConnected()",
						String.valueOf(netInfo.isConnected()));

				urltoReach = urltoReach.contains("http") ? urltoReach
						: "https://" + urltoReach;
				// Log.d("url to reach >> ", urltoReach);
				URL url = new URL(urltoReach); // Change to "http://google.com"
												// for www test.
				HttpURLConnection urlc = (HttpURLConnection) url
						.openConnection();
				urlc.setConnectTimeout(3000); // 3 s min:155.
				urlc.connect();
				if (urlc.getResponseCode() == 200) { // 200 = "OK" code (http
														// connection is fine).
					Log.i("Connection", "Success !");
					return true;
				} else {
					Log.e("Connection", "Nok !");
					return false;
				}
			} catch (MalformedURLException e1) {
				Log.e(TAG,"ConectionDetector.MalformedURLException: "+ e1.getMessage());
				return false;
			} catch (IOException e) {
				Log.e(TAG, "ConectionDetector.IOException: "+ e.getMessage());
				return false;
			}
		} else {
			Log.d(TAG, "ConnectionDetector.isURLReachable: URL unReached");
			return false;
		}
	}

}
