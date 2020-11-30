package com.flylo.frame.tool.permission

import android.Manifest

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.frame.tool.permission
 * @ClassName:      PermissionParamsUtils
 * @Author:         ANWEN
 * @CreateDate:     2020/10/24 11:37 PM
 * @UpdateUser:
 * @UpdateDate:     2020/10/24 11:37 PM
 * @UpdateRemark:
 * @Version:
 */
class PermissionParamsUtils {

    var permission_location = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    var permission_camera = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO
    )

    var permission_phone_state =
        arrayOf(Manifest.permission.READ_PHONE_STATE)

    var permission_sd_card = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )


}