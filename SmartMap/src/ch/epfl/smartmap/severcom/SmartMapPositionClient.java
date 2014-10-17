package ch.epfl.smartmap.severcom;

/**
 * A client object to a SmartMap server that abstracts the underlying
 * communication protocol and data formats concerning the position's update.
 *
 */

/**
 * @author marion-S
 * 
 */
public interface SmartMapPositionClient {

	/**
	 * Sends the latitude and longitude to the server
	 * @throws SmartMapClientException
	 */
	void updatePos() throws SmartMapClientException;

}
