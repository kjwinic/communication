
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

public  class ClientThread extends Thread  {
	private ClientFrame frame;
	
	private String name;
	public String serverIP;
	

	 static Connection getconn()//������ݿ�����
	 {
		 String url="jdbc:mysql://localhost:3306/icq?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		 String user="root";
		 String password="poiu321ert789";
		  try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  Connection conn = null;
		try {
			conn = DriverManager.getConnection(url,user,password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  return conn;
	 }
		public ClientThread(){}
		public ClientThread(String na,String ip)throws IOException
		{ 
			 name=na;
			 serverIP=ip;
			 frame=new ClientFrame(name,serverIP);
			
			// toServer=new Socket(serverIP,4000);
			this.start();
		}
		@SuppressWarnings("rawtypes")
		public void run()
		{
			Vector user_exit = new Vector();
			int intUserTotal = 0;	
			int intprivateCount = 0;
			boolean isFound; // �ж��Ƿ��ҵ��û�
			int intMessageCounter = 0;
			
				
			while(this.frame.isVisible())
			{
				try{					
					
					Socket	toServer = new Socket(serverIP, 4000);
					Message messobj = new Message();
						messobj.name=this.name;	
					ObjectOutputStream streamtoserver = new ObjectOutputStream(toServer.getOutputStream());
					streamtoserver.writeObject((Message) messobj);
					// �����Է���������Ϣ
					ObjectInputStream streamfromserver = new ObjectInputStream(toServer.getInputStream());	
					messobj = (Message) streamfromserver.readObject();
					streamtoserver.close();
					streamfromserver.close();
					toServer.close();
					
					
					System.out.println(messobj);				
					// ����Ϣ����������			
					frame.updateUserlist();
					String temp_message="";	
				
						for (int i = intMessageCounter; i < messobj.chat.size(); i++)
						{
								Chat temp = (Chat) messobj.chat.elementAt(i);	
								if(temp.chatUser.equals(messobj.name)==false)
								temp_message = "��"+temp.chatUser+"���ԡ�" + temp.chatToUser + "��"
											 + "˵��" + temp.chatMessage
											+ "\n";
								
								intMessageCounter++;
								System.out.println(temp_message);
								frame.taUserMessage.append(temp_message);	
						}																		
									
					for(int j=intprivateCount;j<messobj.privatechat.size();j++)
					{
						Chat temp1 = (Chat) messobj.privatechat.elementAt(j);
						
						temp_message = "��"+temp1.chatUser+"�����Ķԡ�" + temp1.chatToUser
								+ "��"  + "˵��" + temp1.chatMessage
								+ "\n";		
						
						intprivateCount++;
						frame.taUserMessage.append(temp_message);	
					}
					
					frame.updateUserlist();
					// ��ʾ�û����������ҵ���Ϣ
					if (messobj.userOnLine.size() > intUserTotal) {
						String tempstr = ((Customer) messobj.userOnLine
								.elementAt(messobj.userOnLine.size() - 1)).custName;
						if (!tempstr.equals(this.name)) {
							frame.taUserMessage.append("��" + tempstr + "������" + "\n");
						}
					}
					// ��ʾ�û��뿪�����ҵ���Ϣ
					// ��ʾ�û��뿪�����ҵ���Ϣ
					if (messobj.userOnLine.size() < intUserTotal) {
						for (int b = 0; b < user_exit.size(); b++) {
							isFound = false;
							for (int c = 0; c < messobj.userOnLine.size(); c++) {
								String tempstr = ((Customer) user_exit.elementAt(b)).custName;

								if (tempstr.equals(((Customer) messobj.userOnLine
										.elementAt(c)).custName)) {
									isFound = true;
									break;
								}
							}
							if (!isFound) // û�з��ָ��û�
							{
								String tempstr = ((Customer) user_exit.elementAt(b)).custName;

								if (!tempstr.equals(this.name)) {
									frame.taUserMessage.append("��" + tempstr + "������"
											+ "\n");
								}
							}
						}
					}
					user_exit = messobj.userOnLine;
					intUserTotal = messobj.userOnLine.size();					
					Thread.sleep(3000);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
	
}
