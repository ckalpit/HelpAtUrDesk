package com.digitalwebweaver.elearning.HelpAtUrDesk.sync;

/**
 * Created by k on 3/8/2015.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * The service which allows the sync adapter framework to access the authenticator.
 */
public class HelpAtUrDeskAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private HelpAtUrDeskAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new HelpAtUrDeskAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
