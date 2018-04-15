

import java.io.Serializable;


public class Chat implements Serializable
{

	private static final long serialVersionUID = 4058485121419391969L;
	public  String chatMessage = null;
	/**
	 * 发言人用户名
	 */
	public String  chatUser;
	/**
	 * 聊天内容
	 */
	/**
	 * 接受对象用户名
	 */
	public String  chatToUser;

	public boolean whisper;
}  