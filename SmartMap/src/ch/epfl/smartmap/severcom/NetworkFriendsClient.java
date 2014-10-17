package ch.epfl.smartmap.severcom;

import java.util.List;

/**
 * @author marion-S
 *
 */

/**
 * A {@link SmartMapFriendsClient} implementation that uses a
 * {@link NetworkProvider} to communicate with a SmartMap server.
 * 
 */
public class NetworkFriendsClient implements SmartMapFriendsClient {

	private String mServerUrl;
	private NetworkProvider mNetworkProvider;

	/**
	 * Creates a new NetworkFriendsClient instance that communicates with a
	 * SmartMap server at a given location, through a {@link NetworkProvider}
	 * object.
	 * 
	 * @param serverUrl
	 *            the base URL of the SmartMap
	 * @param networkProvider
	 *            a NetworkProvider object through which to channel the server
	 *            communication.
	 */
	public NetworkFriendsClient(String serverUrl,
			NetworkProvider networkProvider) {
		this.mServerUrl = serverUrl;
		this.mNetworkProvider = networkProvider;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.epfl.smartmap.severcom.SmartMapFriendsClient#listFriendPos()
	 */
	@Override
	public void listFriendPos() throws SmartMapClientException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.epfl.smartmap.severcom.SmartMapFriendsClient#followFriend(int)
	 */
	@Override
	public void followFriend(int id) throws SmartMapClientException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.epfl.smartmap.severcom.SmartMapFriendsClient#unfollowFriend(int)
	 */
	@Override
	public void unfollowFriend(int id) throws SmartMapClientException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.epfl.smartmap.severcom.SmartMapFriendsClient#allowFriend(int)
	 */
	@Override
	public void allowFriend(int id) throws SmartMapClientException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.epfl.smartmap.severcom.SmartMapFriendsClient#disallowFriend(int)
	 */
	@Override
	public void disallowFriend(int id) throws SmartMapClientException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.epfl.smartmap.severcom.SmartMapFriendsClient#allowFriendList(java.
	 * util.List)
	 */
	@Override
	public void allowFriendList(List<Integer> ids) throws SmartMapClientException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.epfl.smartmap.severcom.SmartMapFriendsClient#disallowFriendList(java
	 * .util.List)
	 */
	@Override
	public void disallowFriendList(List<Integer> ids) throws SmartMapClientException {
		// TODO Auto-generated method stub

	}

}
