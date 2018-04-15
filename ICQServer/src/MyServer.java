
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;

public  class MyServer extends Thread implements ActionListener{
private	ServerFrame frame;
 ServerSocket serverSocket;
	@SuppressWarnings("rawtypes")
	private static Vector userOnline = new Vector(1, 1);
	@SuppressWarnings({ "rawtypes" })
	private static Vector v = new Vector(1, 1);
	@SuppressWarnings("rawtypes")
	private static Vector pc = new Vector(1, 1);
public MyServer()
{
	frame=new ServerFrame();
	try {
		// 获取服务器的主机名和IP地址
		//serverSocket = new ServerSocket(8000);
		InetAddress address = InetAddress.getLocalHost();
		frame.ServerName=address.getHostName();
		frame.IP=address.getHostAddress();
		frame.port=4000;
	} catch (IOException e) {
		
	}
	
	frame.start.addActionListener(this);
	frame.end.addActionListener(this);
	frame.setVisible(true);
	
}
public static void main(String[] args)
{
  new MyServer();
}

	public void run() {
		try {
			while (true) {
				
				// 监听并接受客户的请求
			try{
				Socket client = serverSocket.accept();     
			//	frame.textArea.append("链接到"+client+"\n");
				new ServerThread(frame,client,userOnline,v,pc); // 支持多线程
			}
				catch(SocketException e2)
				{
					
				}
          
				// System.out.println("userLength:"+userLength);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch(NullPointerException e)
		{
			
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	Object sourse=e.getSource();
	if(sourse.equals(frame.start))
	{
				try {
					serverSocket = new ServerSocket(4000);
					frame.textArea.append("启动服务器成功"+serverSocket+"\n");
					System.out.println("seccess");
					this.start(); // 启动线程
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					frame.textArea.append("启动服务器失败"+e1.getMessage()+"\n");
				}

			
	}
	else if(sourse.equals(frame.end))
	{
			     try {
					serverSocket.close();
					
					frame.textArea.append("关闭服务器成功\n");
					System.out.println("seccessclose");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
			    catch(NullPointerException e1)
			    {
			    	System.out.println("NULLPointException");

					frame.textArea.append(e1.getMessage());
			    }
	}
	}

	/**
	 * 启动服务器
	 */

}
