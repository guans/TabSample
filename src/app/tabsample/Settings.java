package app.tabsample;

//��ǰ״̬��������
public class Settings {
	public static final int NORMAL = 1;
	public static final int SEARCH = 2;
	public static final int DAOHANG = 3;
	
	
	/**
	 * �����������ַ�����ʽ��ʾ����
	 * 
	 * @return �����ڲ��Ե��ַ���
	 */
	public String toString() {
		String configString = "";
		configString += "fontsize " + new Integer(NORMAL).toString();
		return configString;
	}
	
}
