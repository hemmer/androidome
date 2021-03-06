package uk.co.ewanhemingway.androidome;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AndroidomeMain extends Activity implements OnClickListener{

	public static final String PREFS_NAME = "AndroidomePrefsFile";

	public Button buttonGrid[][], clearButton;
	public boolean grid[][];

	int gridLength = 8;
	int gridHeight = 8;

	EditText hostTextBox;
	EditText prefixTextBox;

	private MonomeView _monomeView;

	private static SensorManager mySensorManager;
	private boolean sensorrunning;

	// implement SensorEventListener to track phone changes
	private SensorEventListener mySensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			sendTilt((int) event.values[1], (int) event.values[2]);
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) { }
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// retrieve preferences
		SharedPreferences mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		String lastHostIP = mPrefs.getString("lastHostIP", "192.168.1.1");
		String lastPrefix = mPrefs.getString("lastPrefix", "test");

		// textbox for setting monome prefix 
		prefixTextBox = (EditText) findViewById(R.id.prefix_edittext);
		prefixTextBox.setText(lastPrefix);
		prefixTextBox.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

					// update the prefix
					prefixChanged();
					// and hide the keyboard for now
					InputMethodManager inputMM = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMM.hideSoftInputFromWindow(prefixTextBox.getWindowToken(), 0);
					return true;
				}
				return false;
			}


		});

		// textbox for setting ip address of host
		hostTextBox = (EditText) findViewById(R.id.host_edittext);
		hostTextBox.setText(lastHostIP);
		hostTextBox.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

					// update the prefix
					hostIpChange();
					// and hide the keyboard for now
					InputMethodManager inputMM = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMM.hideSoftInputFromWindow(prefixTextBox.getWindowToken(), 0);
					return true;
				}
				return false;
			}

		});

		// add button listeners
		Button connectButton = (Button) findViewById(R.id.connect_button);
		connectButton.setOnClickListener(this);

		Button helpButton = (Button) findViewById(R.id.help_button);
		helpButton.setOnClickListener(this);

		// set up sensor listeners
		mySensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> mySensors = mySensorManager.getSensorList(Sensor.TYPE_ORIENTATION);

		if(mySensors.size() > 0){
			mySensorManager.registerListener(mySensorEventListener, mySensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
			sensorrunning = true;
		}else sensorrunning = false;

		_monomeView = (MonomeView)findViewById(R.id.monome_grid);
	}

	// set up monome grid for action
	private void prepareMonomeGrid(){

		// start UI threads, clear grid
		_monomeView.initialiseMonomeGrid();

		// initialise OSC listener
		_monomeView.initialiseIncomingOSC();
		
		// set the default dummy address
		// this shouldn't ever be used
		_monomeView.setDeviceIPAddress("127.0.0.1");

		// find IP address of phone
		WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);

		// make sure wi-fi is enabled
		if(wifiManager.isWifiEnabled()){
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();

			// and that we are connected
			if(wifiInfo.getNetworkId() == -1){
				showToast("Please make sure you are connected to the same network as the host.");
			}else{
				_monomeView.setDeviceIPAddress(intToIp(wifiInfo.getIpAddress()));
			}
		}else{
			showToast("Please enable Wifi to continue.");
		}

		// read host ip from textbox and inform _monomeView
		_monomeView.setHostIPAddress(hostTextBox.getText().toString());

		// update prefix
		_monomeView.setPrefix(prefixTextBox.getText().toString());

		// let max know of any changes
		_monomeView.pingMaxWithSetupData();
	}

	// sets the OSC prefix
	private void prefixChanged() {

		// get monome prefix from EditText
		String prefix = prefixTextBox.getText().toString();
		// tell user/log
		showToast("Prefix set to: " + prefix.toString());

		// update the monome grid object
		_monomeView.setPrefix(prefix.toString());
	}

	// sets the host machine address
	private void hostIpChange() {

		// get host machine address from EditText
		String host = hostTextBox.getText().toString();
		// tell user/log
		showToast("Host set to: " + host);

		// update the monome grid object, 
		// and check with Max
		_monomeView.setHostIPAddress(host);
		_monomeView.pingMaxWithSetupData();
	}

	// create a Toast to display info/errors etc
	protected void showToast(String anErrorMessage) {
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast.makeText(context, anErrorMessage, duration).show();
	}

	// adapted from http://teneo.wordpress.com/2008/12/23/java-ip-address-to-integer-and-back/  
	public static String intToIp(int i) {

		return (i & 0xFF) + "." +
		((i >> 8 )   & 0xFF) + "." +
		((i >>  16 ) & 0xFF) + "." +
		( (i >> 24 ) & 0xFF);
	}

	// click handlers for setup/help buttons
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.connect_button:
			_monomeView.pingMaxWithSetupData();
			break;

		case R.id.help_button:
			Intent i = new Intent(this, HelpActivity.class);
			startActivity(i);
			break;
		}
	}

	// deal with cases when application looses focus
	@Override
	public void onPause(){
		super.onPause();

		if(sensorrunning) mySensorManager.unregisterListener(mySensorEventListener);

		// stop threads/listeners
		_monomeView.pauseMonomeGrid();
	}

	// restart it all
	@Override
	public void onResume(){
		super.onResume();
		prepareMonomeGrid();
	}
	
	// write prefs when app is stopped 
	@Override
	protected void onStop(){
		super.onStop();

		// We need an Editor object to make preference changes.
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("lastHostIP", _monomeView.getHostIPAddress());
		editor.putString("lastPrefix", _monomeView.getPrefix());

		// make sure we commit the edits!
		editor.commit();
	}

	// pass the tilt messages to the monome class,
	// and convert to the specified format.
	private void sendTilt(int xTilt, int yTilt){
		xTilt = (xTilt + 90)*255/180;
		yTilt = (yTilt + 90)*255/180;
		_monomeView.sendTiltOSC(xTilt, yTilt);
	}
}
