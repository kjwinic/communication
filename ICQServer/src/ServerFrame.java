

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.JTextArea;



@SuppressWarnings("serial")
public class ServerFrame extends JFrame {
 
	private JPanel contentPane;
    private JPanel panel;
    public JButton start;
    public JButton end;
    private JLabel lblNewLabel;
    public  String IP;
    public String ServerName;
    public int port;
    public JTextArea textArea;
	public ServerFrame() {
     
		super("·þÎñÆ÷");
		setBounds(100, 100, 450, 350);
		contentPane = new JPanel();
		contentPane.setToolTipText("");
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setOpaque(false);
		panel.setBounds(14, 13, 404, 219);
		panel.setBorder(new TitledBorder(null, "\u670D\u52A1\u5668\u65E5\u5FD7", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel);
		panel.setLayout(null);		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 25, 376, 181);
		panel.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		start = new JButton("\u542F\u52A8\u670D\u52A1");
		start.setBounds(57, 245, 93, 30);
		contentPane.add(start);
		
			end = new JButton("\u505C\u6B62\u670D\u52A1");
			end.setBounds(253, 245, 93, 30);
			contentPane.add(end);
			
			lblNewLabel = new JLabel("");
			lblNewLabel.setIcon(new ImageIcon(".\\image\\1.jpg"));
			lblNewLabel.setBounds(0, 0, 434, 303);
			contentPane.add(lblNewLabel);
	
	}
}
