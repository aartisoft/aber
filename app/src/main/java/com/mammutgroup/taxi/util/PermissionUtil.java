package com.mammutgroup.taxi.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * @author mushtu
 * @since 6/1/16.
 */
public class PermissionUtil {

    public static boolean checkPermission(Context context,String permissionName) {
        if(context == null) {
            throw new RuntimeException("context is null.");
        }
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context,permissionName);
    }
}
