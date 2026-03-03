package com.polidea.rxandroidble2.internal.util;

import android.content.ContentResolver;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

import com.polidea.rxandroidble2.internal.RxBleLog;

import io.reactivex.annotations.Nullable;

public class LocationManagerWrapper {

    @Nullable
    private final LocationManager manager;
    private final ContentResolver contentResolver;

    public LocationManagerWrapper(Context context) {
        this.manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.contentResolver = context.getContentResolver();
    }

    public boolean isLocationProviderEnabled() {
        if (manager == null) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= 28 /* Build.VERSION_CODES.P */) {
            return manager.isLocationEnabled();
        }
        if (Build.VERSION.SDK_INT >= 19 /* Build.VERSION_CODES.KITKAT */) {
            return isLocationProviderEnabledBelowApi28();
        }
        return isLocationProviderEnabledBelowApi19();
    }

    // Android Studio does not see the deprecation of Settings.Secure.LOCATION_MODE in this method (if you the same code it to
    // `isLocationProviderEnabled` then AS will properly see it). On the other hand Gradle will complain that a deprecated code is used.
    // To silence Gradle warning a @SuppressWarnings is needed. @SuppressWarnings triggers a warning in AS as a redundant suppression.
    @SuppressWarnings({"deprecation", "RedundantSuppression"})
    @RequiresApi(19 /* Build.VERSION_CODES.KITKAT */)
    private boolean isLocationProviderEnabledBelowApi28() {
        try {
            return Settings.Secure.getInt(contentResolver, Settings.Secure.LOCATION_MODE) != Settings.Secure.LOCATION_MODE_OFF;
        } catch (Settings.SettingNotFoundException e) {
            RxBleLog.w(e, "Could not use LOCATION_MODE check. Falling back to a legacy/heuristic function.");
            return isLocationProviderEnabledBelowApi19();
        }
    }

    private boolean isLocationProviderEnabledBelowApi19() {
        return manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                || manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
