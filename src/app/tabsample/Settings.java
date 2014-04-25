package app.tabsample;

//当前状态参数参数
public class Settings {
	public static final int NORMAL = 1;
	public static final int SEARCH = 2;
	public static final int DAOHANG = 3;
	
	
	/**
	 * 将配置项以字符串形式显示出来
	 * 
	 * @return 可用于测试的字符串
	 */
	public String toString() {
		String configString = "";
		configString += "fontsize " + new Integer(NORMAL).toString();
		return configString;
	}
	
}
