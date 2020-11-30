package com.flylo.photo.tool

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import com.flylo.photo.compression.CompressionPredicate
import com.flylo.photo.compression.CompressionTool
import com.flylo.photo.compression.OnCompressListener
import java.io.File

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.photo.tool
 * @ClassName:      SelectPhotoTool
 * @Author:         ANWEN
 * @CreateDate:     2020/10/24 11:39 PM
 * @UpdateUser:
 * @UpdateDate:     2020/10/24 11:39 PM
 * @UpdateRemark:
 * @Version:
 */
class SelectPhotoTool {

    // 上下文
    private var act: Activity? = null

    // 拍照或剪切后图片的存放位置(参考file_provider_paths.xml中的路径)
    private var imgPath = Environment.getExternalStorageDirectory()
        .absolutePath + File.separator + System.currentTimeMillis()
        .toString() + ".jpg"

    //FileProvider的主机名：一般是包名+".fileprovider"，严格上是build.gradle中defaultConfig{}中applicationId对应的值+".fileprovider"
    private var AUTHORITIES = "packageName" + ".fileprovider"

    //是否要裁剪（默认不裁剪）
    private var isCrop = false
    private var mOutputUri: Uri? = null
    private var mInputFile: File? = null
    private var mOutputFile: File? = null

    //剪裁图片宽高比
    private var mAspectX = 1
    private var mAspectY = 1

    //剪裁图片大小
    private var mOutputX = 500
    private var mOutputY = 500
    fun Init(act: Activity, isCrop: Boolean, listener: PhotoSelectListener?) {
        this.act = act
        this.listener = listener
        this.isCrop = isCrop
        AUTHORITIES = act.packageName + ".fileprovider"
        imgPath = generateImgePath()
    }

    /**
     * 可以拍照或从图库选取照片后裁剪的比例及宽高
     *
     * @param act 上下文
     * @param aspectX  图片裁剪时的宽度比例
     * @param aspectY  图片裁剪时的高度比例
     * @param outputX  图片裁剪后的宽度
     * @param outputY  图片裁剪后的高度
     * @param listener 选取图片监听
     */
    fun Init(
        act: Activity,
        aspectX: Int,
        aspectY: Int,
        outputX: Int,
        outputY: Int,
        listener: PhotoSelectListener?
    ) {
        Init(act, true, listener)
        mAspectX = aspectX
        mAspectY = aspectY
        mOutputX = outputX
        mOutputY = outputY
    }

    /**
     * 设置FileProvider的主机名：一般是包名+".fileprovider"，严格上是build.gradle中defaultConfig{}中applicationId对应的值+".fileprovider"
     *
     *
     * 该工具默认是应用的包名+".fileprovider"，如项目build.gradle中defaultConfig{}中applicationId不是包名，则必须调用此方法对FileProvider的主机名进行设置，否则Android7.0以上使用异常
     *
     * @param authorities FileProvider的主机名
     */
    fun setAuthorities(authorities: String) {
        AUTHORITIES = authorities
    }

    /**
     * 修改图片的存储路径（默认的图片存储路径是SD卡上 Android/data/应用包名/时间戳.jpg）
     *
     * @param imgPath 图片的存储路径（包括文件名和后缀）
     */
    fun setImgPath(imgPath: String) {
        this.imgPath = imgPath
    }

    /**
     * 拍照获取
     */
    fun TakePhoto() {
        val imgFile = File(imgPath)
        if (!imgFile.parentFile.exists()) {
            imgFile.parentFile.mkdirs()
        }
        var imgUri: Uri? = null
        imgUri = if (Build.VERSION.SDK_INT < 24) {
            // 从文件中创建uri
            Uri.fromFile(imgFile)
        } else {
            //兼容android7.0 使用共享文件的形式
            val contentValues = ContentValues(1)
            contentValues.put(MediaStore.Images.Media.DATA, imgFile.absolutePath)
            act!!.application.contentResolver
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        }
        val intent = Intent()
        intent.action = MediaStore.ACTION_IMAGE_CAPTURE
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
        act!!.startActivityForResult(intent, TAKE_PHOTO)
    }

    /**
     * 从图库获取
     */
    fun SelectPhoto() {
        val intent = Intent(Intent.ACTION_PICK, null)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        act!!.startActivityForResult(intent, SELECT_PHOTO)
    }

    private fun Crop(inputFile: File, outputFile: File) {
        val parentFile = outputFile.parentFile
        if (!parentFile.exists()) {
            parentFile.mkdirs()
        }
        val intent = Intent("com.android.camera.action.CROP")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(getImageContentUri(act, inputFile), "image/*")
        } else {
            intent.setDataAndType(Uri.fromFile(inputFile), "image/*")
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        intent.putExtra("crop", "true")
        //设置剪裁图片宽高比
        intent.putExtra("aspectX", mAspectX)
        intent.putExtra("aspectY", mAspectY)
        //设置剪裁图片大小
//        intent.putExtra("outputX", mOutputX);
//        intent.putExtra("outputY", mOutputY);
        // 是否返回uri
        intent.putExtra("return-data", false)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile))
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("scale", true)
        intent.putExtra("scaleUpIfNeeded", true)
        act!!.startActivityForResult(intent, ZOOM_PHOTO)
    }

    /**
     * 安卓7.0裁剪根据文件路径获取uri
     */
    private fun getImageContentUri(
        context: Context?,
        imageFile: File
    ): Uri? {
        val filePath = imageFile.absolutePath
        val cursor = context!!.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID),
            MediaStore.Images.Media.DATA + "=? ",
            arrayOf(filePath),
            null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(
                cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID)
            )
            val baseUri =
                Uri.parse("content://media/external/images/media")
            Uri.withAppendedPath(baseUri, "" + id)
        } else {
            if (imageFile.exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, filePath)
                context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                )
            } else {
                null
            }
        }
    }

    /**
     * 产生图片的路径，带文件夹和文件名，文件名为当前毫秒数
     */
    private fun generateImgePath(): String {
        return getExternalStoragePath() + File.separator + System.currentTimeMillis()
            .toString() + ".jpg"
    }

    /**
     * 获取SD下的应用目录
     */
    fun getExternalStoragePath(): String {
        val sb = StringBuilder()
        sb.append(Environment.getExternalStorageDirectory().absolutePath)
        sb.append(File.separator)
        val ROOT_DIR = "Android/data/" + act!!.packageName
        sb.append(ROOT_DIR)
        sb.append(File.separator)
        val path = sb.toString()
        val file = File(path)
        if (!file.exists()) {
            file.mkdirs()
        }
        return path
    }

    fun ActivityForResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                TAKE_PHOTO -> {
                    mInputFile = File(imgPath)
                    if (isCrop) { //裁剪
                        mOutputFile = File(generateImgePath())
                        mOutputUri = Uri.fromFile(mOutputFile)
                        Crop(mInputFile!!, mOutputFile!!)
                    } else { //不裁剪
                        mOutputUri = Uri.fromFile(mInputFile)
                        if (listener != null) {
                            listener!!.onFinish(mInputFile, mOutputUri)
                            Compression(mInputFile)
                        }
                    }
                }
                SELECT_PHOTO -> if (data != null) {
                    val sourceUri = data.data
                    val proj =
                        arrayOf(MediaStore.Images.Media.DATA)
                    val cursor =
                        act!!.managedQuery(sourceUri, proj, null, null, null)
                    val columnIndex =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    cursor.moveToFirst()
                    val imgPath = cursor.getString(columnIndex)
                    mInputFile = File(imgPath)
                    if (isCrop) { //裁剪
                        mOutputFile = File(generateImgePath())
                        mOutputUri = Uri.fromFile(mOutputFile)
                        Crop(mInputFile!!, mOutputFile!!)
                    } else { //不裁剪
                        mOutputUri = Uri.fromFile(mInputFile)
                        if (listener != null) {
                            listener!!.onFinish(mInputFile, mOutputUri)
                            Compression(mInputFile)
                        }
                    }
                }
                ZOOM_PHOTO -> if (data != null) {
                    if (mOutputUri != null) {
                        //删除拍照的临时照片
                        val tmpFile = File(imgPath)
                        if (tmpFile.exists()) tmpFile.delete()
                        if (listener != null) {
                            listener!!.onFinish(mOutputFile, mOutputUri)
                        }
                        Compression(mOutputFile)
                    }
                }
            }
        }
    }

    /**
     * 返回图片监听
     */
    private var listener: PhotoSelectListener? = null

    interface PhotoSelectListener {
        fun onFinish(outputFile: File?, outputUri: Uri?)
        fun onCompressionFinish(outputFile: File?)
    }

    fun Compression(file: File?) {
        CompressionTool.with(act)
            .load(file)
            .ignoreBy(100)
            .setTargetDir(getExternalStoragePath())
            .filter(object : CompressionPredicate {
                override fun apply(path: String): Boolean {
                    return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"))
                }
            })
            .setCompressListener(object : OnCompressListener {
                override fun onStart() {
                    println("error")
                    // TODO 压缩开始前调用，可以在方法内启动 loading UI
                }

                override fun onSuccess(file: File?) {
                    // TODO 压缩成功后调用，返回压缩后的图片文件
                    if (listener != null) {
                        listener!!.onCompressionFinish(file)
                    }
                }

                override fun onError(e: Throwable?) {
                    // TODO 当压缩过程出现问题时调用
                    println("error")
                }
            }).launch()
    }

    companion object {
        const val TAKE_PHOTO = 0x1001 // 拍照
        const val SELECT_PHOTO = 0x1002 // 选择图片
        const val ZOOM_PHOTO = 0x1003 // 剪切图片
    }
}