
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
	

	 static Connection getconn()//获得数据库链接
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
			boolean isFound; // 判断是否找到用户
			int intMessageCounter = 0;
			
				
			while(this.frame.isVisible())
			{
				try{					
					
					Socket	toServer = new Socket(serverIP, 4000);
					Message messobj = new Message();
						messobj.name=this.name;	
					ObjectOutputStream streamtoserver = new ObjectOutputStream(toServer.getOutputStream());
					streamtoserver.writeObject((Message) messobj);
					// 收来自服务器的信息
					ObjectInputStream streamfromserver = new ObjectInputStream(toServer.getInputStream());	
					messobj = (Message) streamfromserver.readObject();
					streamtoserver.close();
					streamfromserver.close();
					toServer.close();
					
					
					System.out.println(messobj);				
					// 将信息发往服务器			
					frame.updateUserlist();
					String temp_message="";	
				
						for (int i = intMessageCounter; i < messobj.chat.size(); i++)
						{
								Chat temp = (Chat) messobj.chat.elementAt(i);	
								if(temp.chatUser.equals(messobj.name)==false)
								temp_message = "【"+temp.chatUser+"】对【" + temp.chatToUser + "】"
											 + "说：" + temp.chatMessage
											+ "\n";
								
								intMessageCounter++;
								System.out.println(temp_message);
								frame.taUserMessage.append(temp_message);	
						}																		
									
					for(int j=intprivateCount;j<messobj.privatechat.size();j++)
					{
						Chat temp1 = (Chat) messobj.privatechat.elementAt(j);
						
						temp_message = "【"+temp1.chatUser+"】悄悄对【" + temp1.chatToUser
								+ "】"  + "说：" + temp1.chatMessage
								+ "\n";		
						
						intprivateCount++;
						frame.taUserMessage.append(temp_message);	
					}
					
					frame.updateUserlist();
					// 显示用户进入聊天室的信息
					if (messobj.userOnLine.size() > intUserTotal) {
						String tempstr = ((Customer) messobj.userOnLine
								.elementAt(messobj.userOnLine.size() - 1)).custName;
						if (!tempstr.equals(this.name)) {
							frame.taUserMessage.append("【" + tempstr + "】来了" + "\n");
						}
					}
					// 显示用户离开聊天室的信息
					// 显示用户离开聊天室的信息
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
							if (!isFound) // 没有发现该用户
							{
								String tempstr = ((Customer) user_exit.elementAt(b)).custName;

								if (!tempstr.equals(this.name)) {
									frame.taUserMessage.append("【" + tempstr + "】走了"
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
