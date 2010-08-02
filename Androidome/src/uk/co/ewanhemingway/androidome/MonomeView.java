package uk.co.ewanhemingway.androidome;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;


public class MonomeView extends View{

	private OSCPortOut oscPortOut;
	private OSCPortIn oscPortIn;

	OSCListener listener;

	int cellSize = 55;
	String prefix = " ";
	String deviceIPAddress = " ";
	String hostIPAddress = "192.168.1.164";
	boolean sendTiltOSC = false;

	Boolean[][] gridLit;

	private long _moveDelay = 20;

	private RefreshHandler _redrawHandler = new RefreshHandler();

	class RefreshHandler extends Handler {

		@Override
		public void handleMessage(Message message) {
			MonomeView.this.update();
			MonomeView.this.invalidate();
		}

		public void sleep(long delayMillis) {
			removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};

	private void update() {
		_redrawHandler.sleep(_moveDelay);
	}

	long start;
	
	public MonomeView(Context context, AttributeSet attrs) {
		super(context, attrs);

		start = System.currentTimeMillis();
		
		// start animation thread
		update();

		// initialise grid to store
		// LED status
		gridLit = new Boolean[8][8];
		// and clear it 
		resetGrid(false);
		
		// create listener/port objects
		try {
			oscPortIn = new OSCPortIn(8080);
		} catch (SocketException e) {
			Log.e("SocketException", e.toString());
		}

		listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				String address = message.getAddress().trim();
				Object[] args = message.getArguments();
				Log.i("Test2", address);

				// if the incoming messages are addressed to us,
				// i.e incoming prefix matches the stored prefix 
				if(address.substring(1, prefix.length()+1).equalsIgnoreCase(prefix)){
					address = address.substring(prefix.length()+2);
					Log.i("Test", address);

					// most likely message is led calls 
					// NOTE: must check that we have 3 arguments (x, y, on/off)
					if(address.equalsIgnoreCase("led") && args.length == 3){
						setLED(Integer.parseInt(args[0].toString()), Integer.parseInt(args[1].toString()), args[2].toString());
					}
					// check for message to clear the board
					else if(address.equalsIgnoreCase("clear") && args.length == 1){
						resetGrid(Integer.parseInt(args[0].toString()) == 1);
					} // check for message to turn on tilt reporting
					  // should be off by default
					else if(address.equalsIgnoreCase("tiltmode") && args.length == 1){
						setTiltMode( Integer.parseInt(args[0].toString()) == 1);
					}
				}

			}
		};


	}
	
	// turn reporting of tilt messages on or off
	public void setTiltMode(boolean sendTiltOSC){
		this.sendTiltOSC = sendTiltOSC;
	}

	// resets the whole grid
	// code: true on, false off
	public void resetGrid(boolean gridOn){
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
				gridLit[i][j] = gridOn;
	}

	
	// to be tided up
	public void setLED(int xPos, int yPos, String state){
		Log.i("OSC", "Inbound: /" + prefix + "/led " + xPos + " " + yPos + " " + state + ", time: " + (System.currentTimeMillis()-start) );

		// ignore input outside 8x8 grid 
		if(xPos < 0 || xPos > 7 || yPos < 0 || yPos > 7) return;
		gridLit[xPos][yPos] = (state.equalsIgnoreCase("1")) ? true : false;
	}

	// set OSC prefix and update listener filters
	public void setPrefix(String prefix){
		this.prefix = prefix;
		Log.i("OSC", prefix);
		//try{
		oscPortIn.addListener("/" + prefix + "/led", listener);
		oscPortIn.addListener("/" + prefix + "/clear", listener);
		oscPortIn.addListener("/" + prefix + "/tiltmode", listener);
		oscPortIn.startListening();
//		}catch(NullPointerException e){
//			Log.e("error", e.toString());
//		}
	}

	// create a method for the addressChanged action
	public void setupOSC() {
		// the variable OSCPortOut tries to get an instance of OSCPortOut at the address
		try {
			oscPortOut = new OSCPortOut(InetAddress.getByName(hostIPAddress));   
			Log.i("Connection Info", "Connected to: " + hostIPAddress);
			// if the oscPort variable fails to be instantiated then sent the error message

		} catch (Exception e) {
			Log.e("Connection Error", "Couldn't set address" + e);
		}

		Object[] oscArgs = {deviceIPAddress};
		OSCMessage oscMsgIP = new OSCMessage("/androidome/setup", oscArgs);
		OSCMessage oscMsgPrefix = new OSCMessage("/sys/prefix /" + prefix, null);

		try {
			oscPortOut.send(oscMsgIP);
			oscPortOut.send(oscMsgPrefix);
			Log.i("OSC", "Outbound: " + oscMsgIP.getAddress() + " " + oscArgs[0]);
		} catch (IOException e) {
			Log.e("IOException", e.toString());
		}

	}

	protected void onDraw(Canvas canvas) {
		Paint background = new Paint();
		background.setColor(getResources().getColor(R.color.background));

		Paint cell = new Paint();
		cell.setColor(getResources().getColor(R.color.button));

		// draw background
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);

		// draw cells
		for (int h = 0; h < 8; h++) {
			for (int w = 0; w < 8; w++) {

				if(gridLit[w][h]){
					cell.setColor(getResources().getColor(R.color.lit));
					cell.setStyle(Style.FILL);
				}else {
					cell.setColor(getResources().getColor(R.color.button));
					cell.setStyle(Style.STROKE);
				}

				RectF bounds = new RectF(w * cellSize, h* cellSize, (w * cellSize) + (cellSize -2), (h * cellSize) + (cellSize -2));
				canvas.drawRoundRect(bounds, 4, 4, cell);
			}
		}
	}

	/* BUG 
	 * To fix:
	 * Press down and drag and release on different square
	 * Don't receive up if released on different square
	 * 
	 */
	@Override 
	public boolean onTouchEvent(MotionEvent event) { 

		int xPos = (int)event.getX()/cellSize;
		int yPos = (int)event.getY()/cellSize;

		if(xPos < 0 || xPos > 7 || yPos < 0 || yPos > 7) return true;

		if(event.getAction() == MotionEvent.ACTION_DOWN){
			sendTouch(xPos, yPos, 1);
			Log.i("KeyPress", xPos + " " + yPos + " DOWN");
		}

		if(event.getAction() == MotionEvent.ACTION_UP){
			sendTouch(xPos, yPos, 0);
			Log.i("KeyPress", xPos + " " + yPos + " UP");
		}

		return true; 
	} 

	// handler for button grid 
	public void sendTouch(int posX, int posY, int actionCode) {

		Object[] oscArgs = {new Integer(posX), new Integer(posY), new Integer(actionCode)};
		OSCMessage oscMsg = new OSCMessage("/" + prefix + "/press", oscArgs);

		try {
			oscPortOut.send(oscMsg);
			Log.i("OSC", "Outbound: " + oscMsg.getAddress() + " " + oscArgs[0] + " " + oscArgs[1] + " " + oscArgs[2]);
		} catch (IOException e) {
			Log.e("IOException", e.toString());
		}
	}

	public void setDeviceIPAddress(String deviceIPAddress) {
		this.deviceIPAddress = deviceIPAddress;
		Log.i("IP", deviceIPAddress);
	}

	public void setHostIPAddress(String hostIPAddress) {
		this.hostIPAddress = hostIPAddress;
		Log.i("IP", hostIPAddress);
	}

}
