

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
public class ServerThread extends Thread {
private Socket netClient;
private Vector<Customer> userOnline;
private Vector<Chat> userChat;
private Vector<Chat>privateChat;
private ObjectInputStream fromClient;
private PrintStream toClient;
private Object obj;
private ServerFrame sFrame;
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
	public ServerThread(){}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ServerThread(ServerFrame frame, Socket client, Vector u, Vector c,Vector pc)throws IOException
	{
		netClient = client;
		userOnline = u;
		userChat = c;
		privateChat=pc;
		sFrame = frame;
		try {
			// ����˫��ͨ��
			// �����ͻ�����
			fromClient = new ObjectInputStream(netClient.getInputStream());

			// ������д���ͻ�
			toClient = new PrintStream(netClient.getOutputStream());
		} catch (IOException e) {
			try {
				netClient.close();
			} catch (IOException e1) {
				System.out.println("���ܽ�����" + e1);
				return;
			}
		}
		this.start();
	}
	public void run() {
		try {// obj��Object��Ķ���
			obj = (Object) fromClient.readObject();
			System.out.println(obj);
			System.out.println("privateChat");
			
			if (obj.getClass().getName().equals("Customer")) {
				try {
					serverLogin();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (obj.getClass().getName().equals("Register_Customer")) {
				serverRegiste();
			}
			if (obj.getClass().getName().equals("Chat")) {
				serverChat();
			}
			if(obj.getClass().getName().equals("Message"))
			{
				this.serverMessage();
			}
			if(obj.getClass().getName().equals("Exit"))
			{
				serverExit();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			System.out.println("������������" + e1);
		} finally {
			try {
				netClient.close();
			} catch (IOException e) {
				System.out.println(e);
			}
		}
		 if(this.sFrame.textArea.getLineCount()>50)
         {
      	   sFrame.textArea.setText("");
         }
	}
	public void serverLogin() throws SQLException
	{
		Customer clientMessage = (Customer) obj;
	this.userChat.clear();
				Connection c=getconn();
				String sql="SELECT USER,PASSWORD FROM usertable WHERE USER=?";
				PreparedStatement prepare;
				prepare = c.prepareStatement(sql);	
				prepare.clearParameters();
				prepare.setString(1,clientMessage.custName);
				ResultSet r=prepare.executeQuery();
				if(r.next())
				{
					String pass=r.getString("password").trim();
					if(clientMessage.custPassword.regionMatches(0, pass, 0, pass.length()))
					{
						this.sFrame.textArea.append("��¼�ɹ�����ok\n");
						this.userOnline.addElement(clientMessage);
						System.out.println("��¼�ɹ�");
						toClient.println("��¼�ɹ�");
						String setip ="update usertable set ip=? where user=?";
						PreparedStatement prest =c.prepareStatement(setip);
						prest.clearParameters();
						prest.setString(1,this.netClient.getInetAddress().getHostAddress());
						prest.setString(2,clientMessage.custName);
						@SuppressWarnings("unused")
						int set=prest.executeUpdate();
						String status="update usertable set status=1 where user=?";
						PreparedStatement prest2=c.prepareStatement(status);
						prest2.clearParameters();prest2.setString(1,clientMessage.custName);
						@SuppressWarnings("unused")
						int set2=prest2.executeUpdate();
						try
						{
						c.commit();
						}
						catch(SQLException ex)
						{
							
						}
						
						
					}
					else
					{
						toClient.println("�������");
						
					}
					
				}
				else
				{
						
					toClient.println("�û�������");
					this.sFrame.textArea.append("�û�������\n");
				}
		
					
								
	}
public void serverRegiste()
{
	Register_Customer clientMessage2 = (Register_Customer) obj;
	try {
		Connection c2=getconn();
		String sql2="insert into usertable(user,password,status,ip)"+" values(?,?,0,0)";
		PreparedStatement prepare2;
		prepare2 = c2.prepareStatement(sql2);
		prepare2.clearParameters();
		prepare2.setString(1,clientMessage2.custName );
		prepare2.setString(2, clientMessage2.custPassword);
		prepare2.execute();
		this.sFrame.textArea.append("ע��ɹ�\n");
		toClient.println("ע��ɹ�");
		System.out.println("ע��ɹ�");
		
		try
		{
		c2.commit();
		}
		catch(SQLException ex)
		{
			
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}

public void  serverChat()
{
	Chat cObj = new Chat();
	cObj = (Chat) obj;

	  this.sFrame.textArea.append(cObj.chatUser+"��"+cObj.chatToUser+"������Ϣ\n"+cObj.chatMessage+"˽��="+cObj.whisper+"\n");
	// ��������Ϣ�����л�������ӵ�����������Ϣ��ʸ����
	  
	  if(cObj.whisper){
		  privateChat.addElement((Chat)cObj);
	  }
	  else
	  {
		  userChat.addElement((Chat) cObj);
	  }
}
public void serverMessage() {
	try {
		Message mess = (Message)obj;
		Message tomess=new Message();
		tomess.userOnLine = userOnline;
		tomess.name=mess.name;
		Vector<Chat> privateChat2=new Vector<Chat>();		
		tomess.chat = userChat;				
		String txt="";
		for(int i=0;i<privateChat.size();i++)
		{
			Chat chat=privateChat.elementAt(i);
		
			if(chat.chatToUser.equals(mess.name))
			{
				 txt+=chat.chatMessage;
				privateChat2.addElement((Chat)chat);
			}		
		}
		System.out.println("privateChat:"+txt);
			//tomess.privatechat=privateChat2;
		tomess.privatechat=privateChat2;
		//privateChat2.removeAllElements();
		ObjectOutputStream outputstream = new ObjectOutputStream(netClient
				.getOutputStream());
		outputstream.writeObject((Message) tomess);
		System.out.println("messfromServer");
		
	
		// server list
		// sFrame.list.setListData(new String[] { "3", "4", "5" });
		netClient.close();
		outputstream.close();
	} catch (IOException e) {
	}

}

@SuppressWarnings("unused")
public void serverExit() {
	Exit exit = new Exit();
	exit = (Exit) obj;
	Connection c=getconn();
	String sql="update usertable set status=0 where user=?";
	PreparedStatement prepare;
	try {
		prepare = c.prepareStatement(sql);
		prepare.clearParameters();
		prepare.setString(1,exit.exitname);
		int r=prepare.executeUpdate();
		this.userChat.clear();
		this.privateChat.clear();
		removeUser(exit);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
	
	// chenmin
}

/**
 * �����û���ɾ���˳��û�
 * 
 * @param exit �˳��û�������
 */
private void removeUser(Exit exit) {
	// TODO �Զ����ɷ������
	Vector<Customer> vec = new Vector<Customer>();
	Customer _cus = null;
	for (int j = 0; j < userOnline.size(); j++) {
		_cus = (Customer) userOnline.get(j);
		if (!exit.exitname.equals(_cus.custName)) {
			vec.add(_cus);
		}
		// System.out.println("list:"+_cus.custName);
	}
	userOnline.removeAllElements();
	for (int j = 0; j < vec.size(); j++) {
		_cus = (Customer) vec.get(j);
		userOnline.add(_cus);
	}
}
}