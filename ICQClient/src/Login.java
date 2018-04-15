
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ImageIcon;


@SuppressWarnings("serial")
public class Login extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField userName;
	private JPasswordField Password;
	private JButton exitButton;
	private JButton zhuceButton;
	private JButton loginButton;
	public String serverIP;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
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
	public Login() {
		setTitle("\u767B\u5F55\u754C\u9762");
		serverIP="127.0.0.1";
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 356, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		loginButton = new JButton("\u767B\u5F55");
		loginButton.setBounds(40, 181, 70, 30);
		contentPane.add(loginButton);
		
		zhuceButton = new JButton("\u6CE8\u518C");
		zhuceButton.setBounds(130, 181, 70, 30);
		contentPane.add(zhuceButton);
		
		exitButton = new JButton("\u9000\u51FA");
		exitButton.setBounds(220, 181, 70, 30);
		contentPane.add(exitButton);
		loginButton.addActionListener(this);
		exitButton.addActionListener(this);
		zhuceButton.addActionListener(this);
		JLabel label = new JLabel("\u7528\u6237\u540D:");
		label.setBounds(64, 58, 72, 18);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("\u5BC6\u7801:");
		label_1.setBounds(64, 105, 72, 18);
		contentPane.add(label_1);
		
		userName = new JTextField();
		userName.setBounds(150, 55, 116, 24);
		contentPane.add(userName);
		userName.setColumns(10);
		
		Password = new JPasswordField();
		Password.setBounds(150, 102, 116, 24);
		contentPane.add(Password);
		Password.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(".\\image\\2.jpg"));
		lblNewLabel.setBounds(0, 0, 338, 253);
		contentPane.add(lblNewLabel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object sourse=e.getSource();
		if(sourse.equals(exitButton))
		{
			System.exit(0);
		}
		if(sourse.equals(zhuceButton))
		{
			this.dispose();
			try {
				Zhuce zhuce=new Zhuce(serverIP);
				zhuce.setVisible(true);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(sourse.equals(loginButton))
		{
			try {
				login();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
  
	public void login() throws IOException
    {
    	String csName=userName.getText();
 		String csPassword=String.valueOf(Password.getPassword());
 	 int usrflag=1,psdflag=1;
 	 if(csName.isEmpty())
 	 {
 		 usrflag=0;
 		JOptionPane.showMessageDialog(null, "用户名不能为空","系统提示",JOptionPane.ERROR_MESSAGE);
 	 }
 	 if(csPassword.isEmpty())
 	 {
 		 psdflag=0;
 		JOptionPane.showMessageDialog(null, "密码不能为空","系统提示",JOptionPane.ERROR_MESSAGE);
 	 }
 	 if(usrflag==1&&psdflag==1)
 	 {
			//连接到服务器
 		 	Customer data = new Customer();
 		 	data.custName=csName;
 		 	data.custPassword=csPassword;
		   	Socket toServer;
		   	toServer = new Socket(serverIP,4000);
		   	ObjectOutputStream streamToServer=new ObjectOutputStream (toServer.getOutputStream());					
		   	
			//写客户详细资料到服务器socket
		   	streamToServer.writeObject((Customer)data);   
		   	System.out.println(data);
        	//读来自服务器socket的登录状态
        	BufferedReader fromServer=new BufferedReader(new InputStreamReader(toServer.getInputStream()));
        
        	String status=fromServer.readLine();
        	
        	if (status.equals("登录成功"))
        	{
        		System.out.println("登录成功");
        		JOptionPane.showMessageDialog(null, "登录成功","系统提示",JOptionPane.INFORMATION_MESSAGE);		
				
        		 new ClientThread(csName,serverIP);
        	    this.dispose();
        	    //关闭流对象
		     streamToServer.close();
             fromServer.close();
             toServer.close();
        	}
			else if(status.equals("用户名错误"))
			{
				JOptionPane.showMessageDialog(null, "用户名错误","系统提示",JOptionPane.ERROR_MESSAGE); 
				streamToServer.close();
				fromServer.close();
				toServer.close();
			}
			else if(status.equals("密码错误"))
			{
				JOptionPane.showMessageDialog(null, "密码错误","系统提示",JOptionPane.ERROR_MESSAGE);
				streamToServer.close();
				fromServer.close();
				toServer.close();
			}
		
		} 
 	 
		}	
    }

