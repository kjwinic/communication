

import java.io.Serializable;


public class Chat implements Serializable
{

	private static final long serialVersionUID = 4058485121419391969L;
	public  String chatMessage = null;
	/**
	 * �������û���
	 */
	public String  chatUser;
	/**
	 * ��������
	 */
	/**
	 * ���ܶ����û���
	 */
	public String  chatToUser;

	public boolean whisper;
}  