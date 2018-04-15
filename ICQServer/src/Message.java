import java.io.Serializable;
import java.util.Vector;

public class Message implements Serializable {

	private static final long serialVersionUID = -3831507106408529855L;
	public String name;
	/**
	 * 用户在线对象集
	 */
	@SuppressWarnings("rawtypes")
	public Vector userOnLine;

	/**
	 * 聊天信息集
	 */
	public Vector<Chat> privatechat;
	/*私有信息集合*/
	public Vector<Chat> chat;

}