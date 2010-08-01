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

	int cellSize = 38;
	String prefix = " ";
	String deviceIPAddress = " ";
	String hostIPAddress = "192.168.1.164";

	Boolean[][] gridLit;
	
	private long _moveDelay = 10;

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
	
	public MonomeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		update();

		// create listener/port objects
		try {
			oscPortIn = new OSCPortIn(8080);
		} catch (SocketException e) {
			Log.e("SocketException", e.toString());
		}

		listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				String add = message.getAddress();
				Object[] args = message.getArguments(); 

				if(add.trim().substring(add.length()-4).equalsIgnoreCase("/led") && args.length == 3){
					try{
						setLED(Integer.parseInt(args[0].toString()), Integer.parseInt(args[1].toString()), args[2].toString());
					}catch (NullPointerException e){
						Log.e("NPE", e.toString());
					}
				}

			}
		};

		gridLit = new Boolean[8][8];

		resetGrid();
	}
	
	public void resetGrid(){
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
				gridLit[i][j] = false;
			
		
	}

	// to be tided up
	public void setLED(int xPos, int yPos, String state){
		Log.i("OSC", "Inbound: /" + prefix + "/led " + xPos + " " + yPos + " " + state);
		
		if(xPos < 0 || xPos > 7 || yPos < 0 || yPos > 7) return;
		
		gridLit[xPos][yPos] = (state.equalsIgnoreCase("1")) ? true : false;
		//MonomeView.this.postInvalidate();
	}

	// set OSC prefix and update listener filter
	public void setPrefix(String prefix){
		this.prefix = prefix;
		oscPortIn.addListener("/" + prefix + "/led", listener);
		oscPortIn.startListening();
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
		OSCMessage oscMsgPrefix = new OSCMessage("/system/prefix /" + prefix, null);

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
	 * Dont recieve up if released on different square
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
