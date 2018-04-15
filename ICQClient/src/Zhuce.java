
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@SuppressWarnings("serial")
public class Zhuce extends JFrame {

	private JPanel contentPane;
	private JTextField username;
	private JPasswordField passwd;
	private JPasswordField repasswd;
	private JButton cancelButton;
	private JButton zhuceButton;
	private JLabel yonghutishi;
	private JLabel retishiLabel;
	String serverIP;
	public static void main(String[] arg) throws IOException
	{
		Zhuce zhuce=new Zhuce("127.0.0.1");
		zhuce.setVisible(true);
	}
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
	
	public Zhuce(String ip) throws IOException {
		serverIP=ip;
		setTitle("\u6CE8\u518C\u754C\u9762");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 435, 326);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("\u7528\u6237\u540D");
		label.setBounds(76, 64, 45, 18);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("\u5BC6\u7801");
		label_1.setBounds(76, 106, 35, 18);
		contentPane.add(label_1);
		
		JLabel label_2 = new JLabel("\u786E\u8BA4\u5BC6\u7801");
		label_2.setBounds(76, 149, 60, 18);
		contentPane.add(label_2);
		
		username = new JTextField();
		username.setBounds(150, 61, 117, 24);
		contentPane.add(username);
		username.setColumns(10);
		
		passwd = new JPasswordField();
		passwd.setBounds(150, 103, 117, 24);
		passwd.setEchoChar('*');
		contentPane.add(passwd);
		passwd.setColumns(10);
		
		repasswd = new JPasswordField();
		repasswd.setEchoChar('*');
		repasswd.setBounds(150, 146, 117, 24);
		contentPane.add(repasswd);
		repasswd.setColumns(10);
		
		zhuceButton = new JButton("\u6CE8\u518C");
		zhuceButton.setBounds(66, 199, 82, 27);
		contentPane.add(zhuceButton);
		
		 cancelButton = new JButton("\u53D6\u6D88");
		cancelButton.setBounds(256, 199, 82, 27);
		contentPane.add(cancelButton);
		
		yonghutishi = new JLabel("");
		yonghutishi.setForeground(Color.RED);
		yonghutishi.setBounds(280, 64, 123, 18);
		contentPane.add(yonghutishi);
		
		retishiLabel = new JLabel("");
		retishiLabel.setForeground(Color.RED);
		retishiLabel.setBounds(278, 149, 125, 18);
		contentPane.add(retishiLabel);
		
		JLabel beijing = new JLabel("");
		beijing.setIcon(new ImageIcon(".\\image\\3.jpg"));
		beijing.setBounds(0, 0, 417, 279);
		contentPane.add(beijing);
		zhuceButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				zhuceAction();
			}
			
		});
		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				Login login;
				login = new Login();	
				login.setVisible(true);
			
			}
			
		});
		username.addKeyListener(new KeyListener()
		{

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
				{
					jugeUser();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		repasswd.addKeyListener(new KeyListener()
		{

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
				{
					if(String.valueOf(passwd.getPassword()).regionMatches(0, String.valueOf(repasswd.getPassword()), 0, repasswd.getPassword().length))
					{
						retishiLabel.setText("");
					}
					else
					{
						retishiLabel.setText("两次密码不一致");
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	} 
	@SuppressWarnings({ "static-access", "resource" })
	public void zhuceAction() 
	{
		int zhuceflag=0;
		Register_Customer data = new Register_Customer();
	    data.custName=this.username.getText(); 
	    data.custPassword=String.valueOf(this.passwd.getPassword());
		try {
			 Socket toServer;
	  		    toServer = new Socket(serverIP,4000);
			    ObjectOutputStream streamToServer=new ObjectOutputStream (toServer.getOutputStream());					
			    
				//写客户详细资料到服务器socket
			    streamToServer.writeObject((Register_Customer)data);
	            //读来自服务器socket的登陆状态
	            BufferedReader fromServer=new BufferedReader(new InputStreamReader(toServer.getInputStream()));
	            String status=fromServer.readLine();
	            //显示成功消息
	            JOptionPane op=new JOptionPane();
	            op.showMessageDialog(null,status);
	
	            //关闭流对象
			    streamToServer.close();
	            fromServer.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(zhuceflag==1)
		{
			JOptionPane.showMessageDialog(null,"注册成功","系统提示！",JOptionPane.INFORMATION_MESSAGE);
			this.dispose();
			Login login=new Login();
			login.setVisible(true);
		}
	}
	public void jugeUser() {
		// TODO Auto-generated method stub	
		try {
			String name=username.getText();
			Connection c=getconn();	
			String sql="select user from usertable where user=?";
			PreparedStatement prepare;
			prepare = c.prepareStatement(sql);	
			prepare.clearParameters();
			prepare.setString(1, name);
			ResultSet r=prepare.executeQuery();
			if(r.next())
			{
				System.out.println(r.getString("user"));
				yonghutishi.setText("该用户名已被注册");
			}
			else
			{
				yonghutishi.setText("");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
