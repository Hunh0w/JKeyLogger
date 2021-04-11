package fr.kezua.kl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketUtils {
	
	//Class Created by Hunh0w
	
	public static String getReceived(Socket sock) {
		String rep = "";
		try {
			InputStream in = sock.getInputStream();	
			BufferedInputStream br = new BufferedInputStream(in);
			int stream;
			byte[] b = new byte[4096];
			stream = br.read(b);
			if(stream <= 0) return null;
			rep = new String(b, 0, stream);
			if(rep.toCharArray().length < 1) return null;
			return rep;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Object getReceivedObject(Socket sock) {
		try {
			ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
			Object obj = in.readObject();
			return obj;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void sendToSocket(Socket sock, String send) {
		if(sock == null) return;
		try {
			PrintWriter wr = new PrintWriter(sock.getOutputStream(), true);
			wr.write(send);
			wr.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendToSocket(Socket sock, Object send) {
		if(sock == null) return;
		try {
			ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
			out.writeObject(send);
		} catch (IOException e) {}
	}
	
	
	public static void close(Socket sock) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					sock.close();
				} catch (IOException e) {}
			}
		}).start();
		
	}
}
