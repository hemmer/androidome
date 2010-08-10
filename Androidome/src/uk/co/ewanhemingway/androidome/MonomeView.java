package uk.co.ewanhemingway.androidome;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Iterator;

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

import com.illposed.osc.OSCPortOut;
import com.illposed.osc.OSCMessageOut;

import de.sciss.net.OSCListener;
import de.sciss.net.OSCMessage;
import de.sciss.net.OSCReceiver;

public class MonomeView extends View{

	// OSC objects
	private OSCReceiver rcv;
	private OSCPortOut oscPortOut;
	DatagramChannel dch;
	SocketAddress outgoingAddr;

	int cellSize = 56;
	String prefix = " ";
	String deviceIPAddress = " ";
	String hostIPAddress = "192.168.1.164";
	boolean sendTiltOSC = false;

	Boolean[][] gridLit;

	private long _moveDelay = 50;

	private int runMode = 0;
	public static final int PAUSE = 0;
	public static final int RUNNING = 1;
	public static final int GRID_WIDTH = 8;
	public static final int GRID_HEIGHT = 8;

	ArrayList<TouchStream> list = new ArrayList<TouchStream>();

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
		if(runMode == RUNNING) _redrawHandler.sleep(_moveDelay);
	}

	long start;

	public MonomeView(Context context, AttributeSet attrs) {
		super(context, attrs);

		initialiseMonomeGrid();
	}

	public void initialiseMonomeGrid(){
		// start animation thread
		runMode = RUNNING;
		update();

		// initialise grid to store LED status
		gridLit = new Boolean[8][8];
		// and clear it 
		resetGrid(false);

		rcv = null;
		dch = null;

		try {
			final SocketAddress incomingPort = new InetSocketAddress(8080);

			dch = DatagramChannel.open();
			dch.socket().bind(incomingPort);    // assigns a local socket address to the listener
			rcv = OSCReceiver.newUsing(dch);

			rcv.addOSCListener( new OSCListener() {
				public void messageReceived( OSCMessage message, SocketAddress sender, long time ){

					String address = message.getName().trim();
					int numArgs = message.getArgCount();

					// if the incoming messages are addressed to us,
					// i.e incoming prefix matches the stored prefix 
					if(address.substring(1, prefix.length()+1).equalsIgnoreCase(prefix)){
						address = address.substring(prefix.length()+2);

						// most likely message is led calls 
						// NOTE: must check that we have 3 arguments (x, y, on/off)
						if(address.equalsIgnoreCase("led") && numArgs == 3){

							int xPos = ((Number) message.getArg( 0 )).intValue();
							int yPos = ((Number) message.getArg( 1 )).intValue();
							int value = ((Number) message.getArg( 2 )).intValue();

							setLED(xPos, yPos, value);
						}
						// check for message to light up a column
						else if(address.equalsIgnoreCase("led_col") && numArgs == 2){

							int col = ((Number) message.getArg( 0 )).intValue();
							int value = ((Number) message.getArg( 1 )).intValue();

							setLEDCol(col, value);
						} 
						// check for message to light up a row
						else if(address.equalsIgnoreCase("led_row") && numArgs == 2){
							int row = ((Number) message.getArg( 0 )).intValue();
							int value = ((Number) message.getArg( 1 )).intValue();

							setLEDRow(row, value);
						} 
						// check for message to clear the board
						else if(address.equalsIgnoreCase("clear") && numArgs == 1){

							boolean flag = message.getArg(0).toString().equalsIgnoreCase("1");
							resetGrid(flag);
						} // check for message to turn on tilt reporting
						// should be off by default
						//						else if(address.equalsIgnoreCase("tiltmode") && numArgs == 1){
						//							setTiltMode( Integer.parseInt(args[0].toString()) == 1);
						//						}
					}

				}
			});
			rcv.startListening();

		}
		catch( IOException e2 ) {
			e2.printStackTrace();
		}

	}

	// turn reporting of tilt messages on or off
	public void setTiltMode(boolean sendTiltOSC){
		this.sendTiltOSC = sendTiltOSC;
	}

	private void setLEDCol(int col, int value) {
		int check = 1;
		for(int i = 0; i < GRID_HEIGHT; i++){
			try{
				gridLit[col][i] = ((value & check) != 0);
			}catch(ArrayIndexOutOfBoundsException e){
				l(col + " " + i);
				e.printStackTrace();

			}
			check = check << 1;  
		}
	}

	private void setLEDRow(int row, int value) {
		int check = 1;
		for(int i = 0; i < GRID_HEIGHT; i++){
			gridLit[i][row] = ((value & check) != 0);
			check = check << 1;  
		}
	}

	// resets the whole grid
	// code: true on, false off
	public void resetGrid(boolean gridOn){
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
				gridLit[i][j] = gridOn;
	}

	// to be tided up
	public void setLED(int xPos, int yPos, int state){

		// ignore input outside 8x8 grid 
		if(xPos < 0 || xPos >= GRID_WIDTH || yPos < 0 || yPos >= GRID_HEIGHT) return;
		gridLit[xPos][yPos] = (state == 1) ? true : false;
	}

	// set OSC prefix and update listener filters
	public void setPrefix(String prefix){
		this.prefix = prefix;
	}

	// create a method for the addressChanged action
	public void pingMaxWithSetupData() {
		// the variable OSCPortOut tries to get an instance of OSCPortOut at the address
		try {
			oscPortOut = new OSCPortOut(InetAddress.getByName(hostIPAddress));   
			//Log.i("Connection Info", "Connected to: " + hostIPAddress);
			// if the oscPort variable fails to be instantiated then sent the error message

		} catch (Exception e) {
			Log.e("Connection Error", "Couldn't set address" + e);
		}

		Object[] oscArgs = {deviceIPAddress};
		OSCMessageOut oscMsgIP = new OSCMessageOut("/androidome/setup", oscArgs);
		OSCMessageOut oscMsgPrefix = new OSCMessageOut("/sys/prefix /" + prefix, null);

		try {
			oscPortOut.send(oscMsgIP);
			oscPortOut.send(oscMsgPrefix);
			Log.i("OSC", "Outbound: " + oscMsgIP.getAddress() + " " + oscArgs[0]);
		} catch (IOException e) {
			Log.e("IOException", e.toString());
		}

	}

	// stop listeners/animation  when app looses focus
	public void pauseMonomeGrid(){

		// close up the listeners
		if(rcv != null){
			l("interesting");
			rcv.dispose();

		}else if(dch != null){
			try {
				dch.close();
			}catch( IOException e4 ) {
				e4.printStackTrace();
			}
		}

		oscPortOut.close();

		// pause the animation thread
		runMode = PAUSE;
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

				RectF bounds = new RectF(w * cellSize, h* cellSize, (w * cellSize) + (cellSize - 5), (h * cellSize) + (cellSize - 5));
				canvas.drawRoundRect(bounds, 4, 4, cell);
			}
		}
	}

	// check use floats maybe faster?
	@Override 
	public boolean onTouchEvent(MotionEvent ev) { 

		final int action = ev.getAction();

		switch (action & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN: {

			// find which cell the initial touch took place in
			int x = (int)ev.getX()/cellSize;
			int y = (int)ev.getY()/cellSize;

			// exclude touches outside the grid
			if(x < 0 || x >= GRID_WIDTH || y < 0 || y >= GRID_HEIGHT) return true;

			sendTouch(x, y, 1);
			final int pointerIndex = (action & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;

			TouchStream first = new TouchStream(x, y, pointerIndex);

			// Save the ID of this pointer 
			list.add(first);
			break;
		}
		case MotionEvent.ACTION_POINTER_DOWN: {

			final int pointerIndex = (action & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;

			// find which cell the initial touch took place in
			int x = (int)ev.getX(pointerIndex)/cellSize;
			int y = (int)ev.getY(pointerIndex)/cellSize;

			// exclude touches outside the grid
			if(x < 0 || x >= GRID_WIDTH || y < 0 || y >= GRID_HEIGHT) return true;

			sendTouch(x, y, 1);

			TouchStream subsequent = new TouchStream(x, y, pointerIndex);

			list.add(subsequent);
			break;
		}

		case MotionEvent.ACTION_MOVE: {
			Iterator<TouchStream> iter = list.iterator();
			while(iter.hasNext()){

				TouchStream temp = iter.next();
				// Find the index of the active pointer and fetch its position
				final int pointerIndex = ev.findPointerIndex(temp.getId());
				final int x = (int) (ev.getX(pointerIndex)/cellSize);
				final int y = (int) (ev.getY(pointerIndex)/cellSize);

				// if dragged outside the grid
				if(x < 0 || x >= GRID_WIDTH || y < 0 || y >= GRID_HEIGHT){

					// send the last stored position
					sendTouch(temp.getX(), temp.getY(), 0);
					return true;
				}

				// if we leave the previous square
				if(x != temp.getX() || y != temp.getY()){
					//send leave message
					sendTouch(temp.getX(), temp.getY(), 0);
					// change current square coordinates
					temp.setX(x);
					temp.setY(y);
					sendTouch(x, y, 1);
				}
			}
			break;

		}

		case MotionEvent.ACTION_UP: {

			// this should only be the remaining item
			if(list.size() != 0){
				TouchStream temp = list.get(0);
				sendTouch(temp.getX(), temp.getY(), 0);
				list.remove(0);
			}
			break;
		}
		case MotionEvent.ACTION_POINTER_UP: {

			// Extract the index of the pointer that left the touch sensor
			final int pointerIndex = (action & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;

			Iterator<TouchStream> iter = list.iterator();
			while(iter.hasNext()){
				TouchStream temp = iter.next();
				if(temp.getId() == pointerIndex){
					sendTouch(temp.getX(), temp.getY(), 0);
					iter.remove();
				}
			}

			break;
		}
		}

		return true; 
	} 

	// click handler for button grid
	// 1 down, 0 up
	public void sendTouch(int posX, int posY, int actionCode) {

		Object[] oscArgs = {new Integer(posX), new Integer(posY), new Integer(actionCode)};
		OSCMessageOut oscMsg = new OSCMessageOut("/" + prefix + "/press", oscArgs);

		try {
			oscPortOut.send(oscMsg);
			//Log.i("OSC", "Outbound: " + oscMsg.getAddress() + " " + oscArgs[0] + " " + oscArgs[1] + " " + oscArgs[2]);
		} catch (IOException e) {
			Log.e("IOException", e.toString());
		}
	}

	public void setDeviceIPAddress(String deviceIPAddress) {
		this.deviceIPAddress = deviceIPAddress;
	}

	public void setHostIPAddress(String hostIPAddress) {
		this.hostIPAddress = hostIPAddress;
	}

	public String getHostIPAddress() {
		return this.hostIPAddress;
	}

	public String getPrefix(){
		return this.prefix;
	}

	public void l(Object i){
		Log.i("Test", i.toString());
	}

	@Override
	public void onWindowFocusChanged (boolean hasFocus){
		cellSize = this.getWidth()/GRID_WIDTH;
	}	
}
