<?php

/**
* /!\ This file must stay private. Making it public enables anyone to impersonate SmartMap FB app
*/
namespace SmartMap\Control;

use Silex\Application;

use Symfony\Component\HttpFoundation\Session\Session;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\JsonResponse;

use SmartMap\DBInterface\User;
use SmartMap\DBInterface\UserRepository;

use Facebook\FacebookSession;
use Facebook\FacebookRedirectLoginHelper;
use Facebook\FacebookRequest;
use Facebook\FacebookResponse;
use Facebook\FacebookSDKException;
use Facebook\FacebookRequestException;
use Facebook\FacebookAuthorizationException;
use Facebook\GraphObject;
use Facebook\GraphUser;

/**
 * This class handles client-server authentication.
 *
 * @author SpicyCH
 *        
 *        
 */
class AuthenticationController
{
    private static $APP_ID = '305881779616905';
    // This is the APP_SECRET, which is highly confidential as it could enable someone to impersonate the app
    private static $APP_SECRET = 'b851a1eb3edcaf637f92fbb2af2b3b47';
    private $mAppSecret;
    private $mAppId;
    private $mRepo;
    
    function __construct(UserRepository $repo, $appId, $appSecret) {
        $this->mRepo = $repo;
        $this->mAppId = $appId;
        $this->mAppSecret = $appSecret;
    }
    
    /**
     * Authenticates the caller to the SmartMap server.
     * 
     * @param Request $request
     *            This must include the $_POST parameters: name, facebookId, facebookToken.
     * @throws ControlException When something goes wrong during the authentication procedure.
     * @return \Symfony\Component\HttpFoundation\JsonResponse A json with the field "status": "OK" or "error" and
     *  a field "message" giving details about the status.
     */
    public function authenticate(Request $request) {
        
        FacebookSession::setDefaultApplication ( self::$APP_ID, self::$APP_SECRET );
        
        $name = $request->request->get ( 'name' );
        $facebookToken = $request->request->get ( 'facebookToken' );
        $facebookId = $request->request->get ( 'facebookId' );
        
        // Check if function called with proper parameters
        if ($name == null or $facebookToken == null or $facebookId == null) {
            throw new ControlException ( 'Missing POST parameter.' );
        }
        
        // Check if token is valid and matches name + ID
        $session = new FacebookSession ( $facebookToken );
        
        // Validate session
        try {
            $session->validate ();
        } catch ( FacebookRequestException $ex ) {
            // Session not valid, Graph API returned an exception with the reason.
            throw new ControlException ( 'Invalid Facebook session' );
        } catch ( \Exception $ex ) {
            // Graph API returned info, but it may mismatch the current app or have expired.
            throw new ControlException ( 'Mismatch or expired facebook data.' );
        }
        
        // At this point the session is valid. We check the session is associated with the user name and id.
        try {
            $user_profile = (new FacebookRequest ( $session, 'GET', '/me' ))->execute ()->getGraphObject ( 
                            GraphUser::className () );
            if ($name != $user_profile->getName () or $facebookId != $user_profile->getId ()) {
                throw new ControlException ( "Id and name do not match the fb access token" );
            }
        } catch ( FacebookRequestException $e ) {
            throw new ControlException ( $e->getMessage() );
        }
        
        // OK, at this point user is successfully authenticated!
        
        // Configure the session
        $session = $request->getSession ();
        if ($session == null) {
            throw new ControlException ( 'Session is null. Did you send session cookie ?' );
        }
        // Set session parameter or create new user
        try {
            $userId = $this->mRepo->getUserIdFromFb ( $facebookId );
            if (! $userId) {
                // Since the DB increments the id by one for each new user, the first param is not relevant
                $user = new User ( 1 , $facebookId, $name, 'VISIBLE', 0.0, 0.0 );
                $user = $this->mRepo->createUser ( $user );
                $session->set ( 'userId', $user->getId () );
            } else {
                $session->set ( 'userId', $userId );
            }
            
            return new JsonResponse ( array (
                            'status' => "OK",
                            'message' => "Sucessfully authenticated" 
            ) );
            
        } catch ( \PDOException $ex ) {
            throw new ControlException ( 'An error occured while dealing with the database' );
        }
    }
    
    /*
     * Development method used to simplify tests
     */
    public function fakeAuth(Request $request) {
        $id = $request->request->get ( 'user_id' );
        if ($id === null) {
            throw new \Exception ( 'Field user_id is not set !' );
        }
        $session = $request->getSession ();
        if ($session == null)
        {
            throw new \Exception('Session is not set.');
        }
        $session->set('userId', $id);
        return new JsonResponse (array(
                        'status' => 'Ok',
                        'message' => 'Fakeauthentified as id ' . $session->get('userId')
        ));
    }
}