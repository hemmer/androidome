package uk.co.ewanhemingway.androidome;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AndroidomeMain extends Activity implements OnClickListener{

	public Button buttonGrid[][], clearButton;
	public boolean grid[][];

	int gridLength = 8;
	int gridHeight = 8;

	EditText hostTextBox;
	EditText prefixTextBox;

	private MonomeView _monomeView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();

		// textbox for setting monome prefix 
		prefixTextBox = (EditText) findViewById(R.id.prefix_edittext);
		prefixTextBox.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

					// update the prefix
					setPrefix();
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
		hostTextBox.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

					// update the prefix
					setHostIp();
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

		_monomeView = (MonomeView)findViewById(R.id.monome_grid);
		_monomeView.setDeviceIPAddress(intToIp(wifiInfo.getIpAddress()));
		_monomeView.setPrefix(prefixTextBox.getText().toString());
		_monomeView.setupOSC();
	}

	// sets the OSC prefix
	private void setPrefix() {
		Editable prefix = prefixTextBox.getText();
		showError("Prefix set to: " + prefix.toString());
		Log.i("Androidome", "Prefix set to: " + prefix.toString());
		_monomeView.setPrefix(prefix.toString());
	}

	// sets the host machine address
	private void setHostIp() {
		Editable host = hostTextBox.getText();
		showError("Host set to: " + host.toString());
		Log.i("Androidome", "Host set to: " + host.toString());
		_monomeView.setHostIPAddress(host.toString());
		_monomeView.setupOSC();
	}

	// create a Toast to display info/errors
	protected void showError(String anErrorMessage) {
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, anErrorMessage, duration);
		toast.show();
	}

	// adapted from http://teneo.wordpress.com/2008/12/23/java-ip-address-to-integer-and-back/  
	public static String intToIp(int i) {

		return (i & 0xFF) + "." +
		((i >> 8 )   & 0xFF) + "." +
		((i >>  16 ) & 0xFF) + "." +
		( (i >> 24 ) & 0xFF);
	}

	// handler for setup button
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.connect_button:
			_monomeView.setupOSC();
			break;
		}
	}
	
}
