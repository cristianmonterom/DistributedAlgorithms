/**
 * 
 */
package common;

/**
 * @author Cristian
 *
 */
public final class CommonFunctions {

	public static String convertBoolean(boolean _param) {
		return _param ? "yes" : "no";
	}
	public static boolean convertString(String _param) {
		return _param.equals("yes");
	}
	
	public static boolean isPortValid(int _portNumber) {
		return (_portNumber > 0 && _portNumber < 65535);
	}
}
