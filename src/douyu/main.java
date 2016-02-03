package douyu;

import java.io.*;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

///import domain.Message;

public class main {
	static Socket socket = null;  
	public static void main(String[] args) throws IOException, InterruptedException 
	{
		System.out.println((System.currentTimeMillis() / 10000));
		dyserver dy = new dyserver();
		dy.Init();
		Thread.sleep(3000);
		dy.send();
		Thread.sleep(2000);
		dy.send2();

		
		Runnable live= new Runnable()
        {
            
            public void run() 
            {
            	while(true)
            	{
						try {
							try {
								Thread.sleep(50000);
								dy.sendKeepLive();
								System.out.println("************************************"+"弹幕速率："+dy.speed+"平均弹幕速率："+dy.totalSpeed+"************************************************");
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				
            	}
            }
        };
		
		Runnable work= new Runnable()
        {
            
            public void run() {

						try {
							dy.receive();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				
 
            }
        };
        Thread t=new Thread(work)   ;
        t.start();
        
        Thread t2=new Thread(live)   ;
        t2.start();
	}
	
}
