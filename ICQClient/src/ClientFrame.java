import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;


@SuppressWarnings("serial")
public  class ClientFrame extends JFrame implements ActionListener {
	public Socket socket;
	public JPanel contentPane;
	public JPanel panel_right;
	public JPanel panel_left;
	public JButton Send;
	public JButton quitbutton;
	public JTextArea textArea;
	@SuppressWarnings("rawtypes")
	public JComboBox usecomboBox;
	public String strname;
	public String serverIP;
	public JRadioButton radioButton;
	public List lstUserList;
    private JScrollPane scrollPane;
    public JTextArea taUserMessage;
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
	/**
	 * Launch the application.
	 */
  
	/**
	 * Create the frame.
	 * @throws IOException 
	 */
 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ClientFrame(String name,String IP) throws IOException {
		
		this.serverIP=IP;
		 strname=name;
		setTitle("\u804A\u5929\u5BA4[用户："+strname+"]");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 616, 434);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel_left = new JPanel();
		panel_left.setOpaque(false);
		panel_left.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u6D88\u606F", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_left.setBounds(26, 27, 343, 220);
		contentPane.add(panel_left);
		panel_left.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 24, 315, 183);
		panel_left.add(scrollPane);
		
		taUserMessage = new JTextArea();
		taUserMessage.setEditable(false);
		scrollPane.setViewportView(taUserMessage);		
		panel_right = new JPanel();
		panel_right.setOpaque(false);
		panel_right.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u5728\u7EBF\u5217\u8868", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_right.setBounds(404, 27, 163, 220);
		contentPane.add(panel_right);
		panel_right.setLayout(null);
		
		lstUserList = new List();

		
		lstUserList.setBounds(14, 24, 135, 183);
	
		panel_right.add(lstUserList);
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setViewportBorder(null);
		scrollPane_1.setBounds(161, 260, 296, 67);
		contentPane.add(scrollPane_1);
		
		textArea = new JTextArea();
		scrollPane_1.setViewportView(textArea);
		
		Send = new JButton("\u53D1\u9001");
		Send.setBounds(480, 257, 93, 30);
		
		contentPane.add(Send);
		
		quitbutton = new JButton("\u4E0B\u7EBF");
		quitbutton.setBounds(480, 300, 93, 27);		
		
		contentPane.add(quitbutton);
		
		JLabel label_1 = new JLabel("\u4F60\u5BF9");
		label_1.setForeground(Color.WHITE);
		label_1.setBounds(26, 263, 35, 18);
		contentPane.add(label_1);
		
		usecomboBox = new JComboBox();
		usecomboBox.setModel(new DefaultComboBoxModel(new String[] {"\u6240\u6709\u4EBA"}));
		usecomboBox.setBounds(74, 260, 73, 24);
		contentPane.add(usecomboBox);
		radioButton = new JRadioButton("\u79C1\u804A");
		radioButton.setForeground(Color.WHITE);
		radioButton.setOpaque(false);
		radioButton.setBounds(74, 300, 72, 27);
		contentPane.add(radioButton);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(".\\image\\1.jpg"));
		label.setBounds(0, 0, 598, 357);
		contentPane.add(label);
		
		lstUserList.addActionListener(this);
		quitbutton.addActionListener(this);
		Send.addActionListener( this);
		updateUserlist();
		this.setVisible(true);
		
	}

public void updateUserlist()
{
	String User="";
	try {
		Connection c=getconn();	
		String sql="select user from usertable where status='1'";
		PreparedStatement prepare;
		prepare = c.prepareStatement(sql);	
		prepare.clearParameters();
		ResultSet r=prepare.executeQuery();
		//lstUserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	
		r.next();
	    lstUserList.removeAll();
		for(int i=0;i<r.getRow();i++)
		{	
				User=r.getString("user");			
			lstUserList.add(User);
		r.next();
		}		

		
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

public void SendMessage() throws UnknownHostException, IOException
{

	Chat chatobj = new Chat();
	chatobj.chatUser = strname;
	chatobj.chatMessage = textArea.getText();
	chatobj.chatToUser = String.valueOf(this.usecomboBox.getSelectedItem());
	chatobj.whisper = this.radioButton.isSelected() ? true : false;
	String temp_message="";
	if(!chatobj.whisper)
	{
	 temp_message = "【"+chatobj.chatUser+"】对【" +chatobj.chatToUser + "】"
			 + "说：" +chatobj.chatMessage
			+ "\n";
	}
	else
	{
		temp_message = "【"+chatobj.chatUser+"】悄悄对【" + chatobj.chatToUser
				+ "】"  + "说：" + chatobj.chatMessage
				+ "\n";		
	}
	this.taUserMessage.append(temp_message);
	
	// 向服务器发送信息
	try {
		Socket toServer = new Socket(serverIP,4000);
		ObjectOutputStream outObj = new ObjectOutputStream(toServer
				.getOutputStream());
		outObj.writeObject(chatobj);
		
		System.out.println("Serverwrite"+chatobj);
		textArea.setText(""); // 清空文本框
		outObj.close();
		toServer.close();
	}
	catch(IOException e1)
	{
		
	}
}
public void eventxiaxian()
{					
	// 发送退出信息
  try 
  {
	Exit exit=new Exit();
	exit.exitname=this.strname;
	try {
		Socket toServer = new Socket(serverIP,4000);
		ObjectOutputStream outObj = new ObjectOutputStream(toServer
				.getOutputStream());
		outObj.writeObject(exit);
		System.out.println(exit);
		outObj.close();
		toServer.close();
	}
	catch(IOException e1)
	{
		
	}
	this.dispose();
	
	} catch (Exception e) {
	}
}
@SuppressWarnings("unchecked")
public void changeUser() {

	boolean key = true;
	String selected = (String) lstUserList.getSelectedItem();
	// JOptionPane.showMessageDialog(null, selected);
	for (int i = 0; i < this.usecomboBox.getItemCount(); i++) {
		if (selected.equals(usecomboBox.getItemAt(i))) {
			key = false;
			break;
		}
	}
	if (key == true) {
		usecomboBox.insertItemAt(selected, 0);
	}
	
	usecomboBox.setSelectedItem(selected);


} // changeUser()结束
public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	Object Sourse=e.getSource();
	if(Sourse.equals(Send))
	{
	 try {
		SendMessage();
	} catch (UnknownHostException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	}
	else if(Sourse.equals(quitbutton))
	{
		eventxiaxian();
	}
	if (Sourse.equals(lstUserList)) // 双击列表框
	{
		
		this.changeUser();
	}
}
}

