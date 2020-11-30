package com.flylo.frame.tool.permission

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.SparseArray
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.frame.tool
 * @ClassName:      PermissionTool
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 4:45 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 4:45 PM
 * @UpdateRemark:
 * @Version:
 */
/**
 * Android运行时权限申请
 *
 *
 * 需要申请的权限列表，<a></a>"href=https://developer.android.google.cn/guide/topics/security/permissions.html?hl=zh-cn#normal-dangerous">Google Doc
 *
 *
 * -CALENDAR<br></br>
 * [android.Manifest.permission.READ_CALENDAR]<br></br>
 * [android.Manifest.permission.WRITE_CALENDAR]<br></br>
 *
 *
 * -CAMERA<br></br>
 * [android.Manifest.permission.CAMERA]<br></br>
 *
 *
 * -CONTACTS<br></br>
 * [android.Manifest.permission.READ_CONTACTS]<br></br>
 * [android.Manifest.permission.WRITE_CONTACTS]<br></br>
 * [android.Manifest.permission.GET_ACCOUNTS]<br></br>
 *
 *
 * -LOCATION<br></br>
 * [android.Manifest.permission.ACCESS_FINE_LOCATION]<br></br>
 * [android.Manifest.permission.ACCESS_COARSE_LOCATION]<br></br>
 *
 *
 * -MICROPHONE<br></br>
 * [android.Manifest.permission.RECORD_AUDIO]<br></br>
 *
 *
 * -PHONE<br></br>
 * [android.Manifest.permission.READ_PHONE_STATE]<br></br>
 * [android.Manifest.permission.CALL_PHONE]<br></br>
 * [android.Manifest.permission.READ_CALL_LOG]<br></br>
 * [android.Manifest.permission.WRITE_CALL_LOG]<br></br>
 * [android.Manifest.permission.ADD_VOICEMAIL]<br></br>
 * [android.Manifest.permission.USE_SIP]<br></br>
 * [android.Manifest.permission.PROCESS_OUTGOING_CALLS]<br></br>
 *
 *
 * -SENSORS<br></br>
 * [android.Manifest.permission.BODY_SENSORS]<br></br>
 *
 *
 * -SMS<br></br>
 * [android.Manifest.permission.SEND_SMS]<br></br>
 * [android.Manifest.permission.RECEIVE_SMS]<br></br>
 * [android.Manifest.permission.READ_SMS]<br></br>
 * [android.Manifest.permission.RECEIVE_WAP_PUSH]<br></br>
 * [android.Manifest.permission.RECEIVE_MMS]<br></br>
 *
 *
 * -STORAGE<br></br>
 * [android.Manifest.permission.READ_EXTERNAL_STORAGE]<br></br>
 * [android.Manifest.permission.WRITE_EXTERNAL_STORAGE]<br></br>
 * @author axw_an
 */
class PermissionTool {
    private val sRequestCode =
        AtomicInteger(0)
    private val sResultArray =
        SparseArray<Result?>()
    private var sManifestPermissionSet: HashSet<String>? = null

    interface Result {
        fun onGranted()
        fun onDenied()
    }

    private var mObject: Any
    private var mPermissions: Array<String>? = null
    private var mResult: Result? = null

    private constructor(`object`: Any) {
        mObject = `object`
    }

    constructor(@NonNull activity: Activity) {
        mObject = activity
    }

    constructor(@NonNull fragment: Fragment) {
        mObject = fragment
    }

    fun permissions(@NonNull permissions: Array<String>?): PermissionTool {
        mPermissions = permissions
        return this
    }

    fun result(@Nullable result: Result?): PermissionTool {
        mResult = result
        return this
    }

    fun request() {
        val activity = getActivity(mObject)
            ?: throw IllegalArgumentException(mObject.javaClass.name + " is not supported")
        initManifestPermission(activity)
        for (permission in mPermissions!!) {
            if (!sManifestPermissionSet!!.contains(permission)) {
                if (mResult != null) {
                    mResult!!.onDenied()
                }
                return
            }
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (mResult != null) {
                mResult!!.onGranted()
            }
            return
        }
        val deniedPermissionList =
            getDeniedPermissions(activity, mPermissions!!)
        if (deniedPermissionList.isEmpty()) {
            if (mResult != null) {
                mResult!!.onGranted()
            }
            return
        }
        val requestCode = genRequestCode()
        val deniedPermissions =
            deniedPermissionList.toTypedArray()
        requestPermissions(mObject, deniedPermissions, requestCode)
        sResultArray.put(requestCode, mResult)
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String?>?,
        @NonNull grantResults: IntArray
    ) {
        val result = sResultArray[requestCode] ?: return
        sResultArray.remove(requestCode)
        for (grantResult in grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                result.onDenied()
                return
            }
        }
        result.onGranted()
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun requestPermissions(
        `object`: Any,
        permissions: Array<String>,
        requestCode: Int
    ) {
        if (`object` is Activity) {
            `object`.requestPermissions(permissions, requestCode)
        } else if (`object` is Fragment) {
            (`object` as Fragment).requestPermissions(permissions, requestCode)
        }
    }

    fun getDeniedPermissions(
        context: Context?,
        permissions: Array<String>
    ): List<String> {
        val deniedPermissionList: MutableList<String> =
            ArrayList()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    permission
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                deniedPermissionList.add(permission)
            }
        }
        return deniedPermissionList
    }

    @Synchronized
    private fun initManifestPermission(context: Context) {
        if (sManifestPermissionSet == null) {
            sManifestPermissionSet = HashSet()
            try {
                val packageInfo = context.packageManager
                    .getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)
                val permissions = packageInfo.requestedPermissions
                Collections.addAll(sManifestPermissionSet, *permissions)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    private fun getActivity(`object`: Any?): Activity? {
        if (`object` != null) {
            if (`object` is Activity) {
                return `object`
            } else if (`object` is Fragment) {
                return (`object` as Fragment).getActivity()
            }
        }
        return null
    }

    private fun genRequestCode(): Int {
        return sRequestCode.incrementAndGet()
    }
}