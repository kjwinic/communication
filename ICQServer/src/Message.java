import java.io.Serializable;
import java.util.Vector;

public class Message implements Serializable {

	private static final long serialVersionUID = -3831507106408529855L;
	public String name;
	/**
	 * �û����߶���
	 */
	@SuppressWarnings("rawtypes")
	public Vector userOnLine;

	/**
	 * ������Ϣ��
	 */
	public Vector<Chat> privatechat;
	/*˽����Ϣ����*/
	public Vector<Chat> chat;

}