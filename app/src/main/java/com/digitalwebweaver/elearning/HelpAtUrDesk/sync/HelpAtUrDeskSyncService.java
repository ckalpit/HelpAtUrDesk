package com.digitalwebweaver.elearning.HelpAtUrDesk.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by k on 3/8/2015.
 */
public class HelpAtUrDeskSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static HelpAtUrDeskSyncAdapter sHelpAtUrDeskSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("HelpAtUrDeskSyncService", "onCreate - HelpAtUrDeskSyncService");
        synchronized (sSyncAdapterLock) {
            if (sHelpAtUrDeskSyncAdapter == null) {
                sHelpAtUrDeskSyncAdapter = new HelpAtUrDeskSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sHelpAtUrDeskSyncAdapter.getSyncAdapterBinder();
    }
}