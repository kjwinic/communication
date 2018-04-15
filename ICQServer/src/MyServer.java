
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
		// ��ȡ����������������IP��ַ
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
				
				// ���������ܿͻ�������
			try{
				Socket client = serverSocket.accept();     
			//	frame.textArea.append("���ӵ�"+client+"\n");
				new ServerThread(frame,client,userOnline,v,pc); // ֧�ֶ��߳�
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
					frame.textArea.append("�����������ɹ�"+serverSocket+"\n");
					System.out.println("seccess");
					this.start(); // �����߳�
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					frame.textArea.append("����������ʧ��"+e1.getMessage()+"\n");
				}

			
	}
	else if(sourse.equals(frame.end))
	{
			     try {
					serverSocket.close();
					
					frame.textArea.append("�رշ������ɹ�\n");
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
	 * ����������
	 */

}
