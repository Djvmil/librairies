/*
 * Copyright 2013 Gerhard Klostermeier
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.suntelecoms.library_mifare.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.suntelecoms.library_mifare.Common;
import com.suntelecoms.library_mifare.R;
import com.suntelecoms.library_mifare.Util.Constantes;
/**
 * Main App entry point showing the main menu.
 * Some stuff about the App:
 * <ul>
 * <li>Error/Debug messages (Log.e()/Log.d()) are hard coded</li>
 * <li>This is my first App, so please by decent with me ;)</li>
 * </ul>
 * @author Gerhard Klostermeier
 */
public class WaitForReadCard extends Activity {

    private static final String LOG_TAG =
            WaitForReadCard.class.getSimpleName();

    private boolean mDonateDialogWasShown = false;
    private Intent mOldIntent = null;

    /**
     * Nodes (stats) MCT passes through during its startup.
     */
    private enum StartUpNode {
        FirstUseDialog, DonateDialog, HasNfc, HasMifareClassicSupport,
        HasNfcEnabled, HasExternalNfc, ExternalNfcServiceRunning,
        HandleNewIntent
    }

    /**
     * Check for NFC hardware, MIFARE Classic support and for external storage.
     * If the directory structure and the std. keys files is not already there
     * it will be created. Also, at the first run of this App, a warning
     * notice and a donate message will be displayed.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_read_card);

        Log.e(LOG_TAG, "onCreate: " );
  }
    /**
     * Handle new Intent as a new tag Intent and if the tag/device does not
     * support MIFARE Classic, then run {@link TagInfoTool}.
     * @see Common#treatAsNewTag(Intent, Context)
     * @see TagInfoTool
     */
    @Override
    public void onNewIntent(Intent intent2) {

        Log.e(LOG_TAG, "onNewIntent: " );
        if(Common.getPendingComponentName() != null) {
            Log.e(LOG_TAG, "getPendingComponentName(): " );
            intent2.setComponent(Common.getPendingComponentName());
            startActivity(intent2);
        } else {
            int typeCheck = Common.treatAsNewTag(intent2, this);
            Log.e(LOG_TAG, "getPendingComponentName() else: "+typeCheck );


            Intent intent = new Intent(this, ReadAllSectors.class);
            //startActivityForResult(intent, ReadTag.KEY_MAP_CREATOR);
            startActivityForResult(intent, Constantes.LECTURE);

            /*if (typeCheck == -1 || typeCheck == -2) {
                // Device or tag does not support MIFARE Classic.
                // Run the only thing that is possible: The tag info tool.
                Intent i = new Intent(this, TagInfoTool.class);
                startActivity(i);
            }*/
        }
    }


}
