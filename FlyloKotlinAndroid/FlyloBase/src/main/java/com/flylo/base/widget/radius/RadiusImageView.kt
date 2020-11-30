package com.flylo.base.widget.radius

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import com.flylo.base.R

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.base.widget.radius
 * @ClassName:      RadiusImageView
 * @Author:         ANWEN
 * @CreateDate:     2020/6/14 8:06 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/14 8:06 PM
 * @UpdateRemark:
 * @Version:
 */
class RadiusImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.RadiusImageViewStyle
) :
    AppCompatImageView(context, attrs, defStyleAttr) {
    private var mIsSelected = false
    private var mIsOval = false
    private var mIsCircle = false
    private var mBorderWidth: Int
    private var mBorderColor: Int
    private var mSelectedBorderWidth: Int
    private var mSelectedBorderColor: Int
    private var mSelectedMaskColor: Int
    private var mIsTouchSelectModeEnabled = true
    private var mCornerRadius = 0
    private var mBitmapPaint: Paint? = null
    private val mBorderPaint: Paint
    private var mColorFilter: ColorFilter? = null
    private var mSelectedColorFilter: ColorFilter? = null
    private var mBitmapShader: BitmapShader? = null
    private var mNeedResetShader = false
    private val mRectF = RectF()
    private val mDrawRectF = RectF()
    private var mBitmap: Bitmap? = null
    private val mMatrix: Matrix
    private var mWidth = 0
    private var mHeight = 0
    private var mLastCalculateScaleType: ScaleType? = null

    override fun setAdjustViewBounds(adjustViewBounds: Boolean) {
        require(!adjustViewBounds) { "不支持adjustViewBounds" }
    }

    fun setBorderWidth(borderWidth: Int) {
        if (mBorderWidth != borderWidth) {
            mBorderWidth = borderWidth
            invalidate()
        }
    }

    fun setBorderColor(@ColorInt borderColor: Int) {
        if (mBorderColor != borderColor) {
            mBorderColor = borderColor
            invalidate()
        }
    }

    fun setCornerRadius(cornerRadius: Int) {
        if (mCornerRadius != cornerRadius) {
            mCornerRadius = cornerRadius
            if (!mIsCircle && !mIsOval) {
                invalidate()
            }
        }
    }

    fun setSelectedBorderColor(@ColorInt selectedBorderColor: Int) {
        if (mSelectedBorderColor != selectedBorderColor) {
            mSelectedBorderColor = selectedBorderColor
            if (mIsSelected) {
                invalidate()
            }
        }
    }

    fun setSelectedBorderWidth(selectedBorderWidth: Int) {
        if (mSelectedBorderWidth != selectedBorderWidth) {
            mSelectedBorderWidth = selectedBorderWidth
            if (mIsSelected) {
                invalidate()
            }
        }
    }

    fun setSelectedMaskColor(@ColorInt selectedMaskColor: Int) {
        if (mSelectedMaskColor != selectedMaskColor) {
            mSelectedMaskColor = selectedMaskColor
            mSelectedColorFilter = if (mSelectedMaskColor != Color.TRANSPARENT) {
                PorterDuffColorFilter(mSelectedMaskColor, PorterDuff.Mode.DARKEN)
            } else {
                null
            }
            if (mIsSelected) {
                invalidate()
            }
        }
        mSelectedMaskColor = selectedMaskColor
    }

    fun setCircle(isCircle: Boolean) {
        if (mIsCircle != isCircle) {
            mIsCircle = isCircle
            requestLayout()
            invalidate()
        }
    }

    fun setOval(isOval: Boolean) {
        var forceUpdate = false
        if (isOval) {
            if (mIsCircle) {
                // 必须先取消圆形
                mIsCircle = false
                forceUpdate = true
            }
        }
        if (mIsOval != isOval || forceUpdate) {
            mIsOval = isOval
            requestLayout()
            invalidate()
        }
    }

    fun getBorderColor(): Int {
        return mBorderColor
    }

    fun getBorderWidth(): Int {
        return mBorderWidth
    }

    fun getCornerRadius(): Int {
        return mCornerRadius
    }

    fun getSelectedBorderColor(): Int {
        return mSelectedBorderColor
    }

    fun getSelectedBorderWidth(): Int {
        return mSelectedBorderWidth
    }

    fun getSelectedMaskColor(): Int {
        return mSelectedMaskColor
    }

    fun isCircle(): Boolean {
        return mIsCircle
    }

    fun isOval(): Boolean {
        return !mIsCircle && mIsOval
    }

    override fun isSelected(): Boolean {
        return mIsSelected
    }

    override fun setSelected(isSelected: Boolean) {
        if (mIsSelected != isSelected) {
            mIsSelected = isSelected
            invalidate()
        }
    }

    fun setTouchSelectModeEnabled(touchSelectModeEnabled: Boolean) {
        mIsTouchSelectModeEnabled = touchSelectModeEnabled
    }

    fun isTouchSelectModeEnabled(): Boolean {
        return mIsTouchSelectModeEnabled
    }

    fun setSelectedColorFilter(cf: ColorFilter) {
        if (mSelectedColorFilter === cf) {
            return
        }
        mSelectedColorFilter = cf
        if (mIsSelected) {
            invalidate()
        }
    }

    override fun setColorFilter(cf: ColorFilter) {
        if (mColorFilter === cf) {
            return
        }
        mColorFilter = cf
        if (!mIsSelected) {
            invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(widthSize, heightSize)
            return
        }
        if (mIsCircle) {
            if (widthMode == MeasureSpec.EXACTLY) {
                setMeasuredDimension(widthSize, widthSize)
            } else if (heightMode == MeasureSpec.EXACTLY) {
                setMeasuredDimension(heightSize, heightSize)
            } else {
                if (mBitmap == null) {
                    setMeasuredDimension(0, 0)
                } else {
                    val w = Math.min(mBitmap!!.width, widthSize)
                    val h = Math.min(mBitmap!!.height, heightSize)
                    val size = Math.min(w, h)
                    setMeasuredDimension(size, size)
                }
            }
            return
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        setupBitmap()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        setupBitmap()
    }

    private fun getBitmap(): Bitmap? {
        val drawable = drawable ?: return null
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap ?: return null
            val bmWidth = bitmap.width.toFloat()
            val bmHeight = bitmap.height.toFloat()
            if (bmWidth == 0f || bmHeight == 0f) {
                return null
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                // ensure minWidth and minHeight
                val minScaleX = minimumWidth / bmWidth
                val minScaleY = minimumHeight / bmHeight
                if (minScaleX > 1 || minScaleY > 1) {
                    val scale = Math.max(minScaleX, minScaleY)
                    val matrix = Matrix()
                    matrix.postScale(scale, scale)
                    return Bitmap.createBitmap(
                        bitmap, 0, 0, bmWidth.toInt(), bmHeight.toInt(), matrix, false
                    )
                }
            }
            return bitmap
        }
        return try {
            val bitmap: Bitmap
            bitmap = if (drawable is ColorDrawable) {
                Bitmap.createBitmap(
                    COLOR_DRAWABLE_DIMEN,
                    COLOR_DRAWABLE_DIMEN,
                    BITMAP_CONFIG
                )
            } else {
                Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    BITMAP_CONFIG
                )
            }
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun setupBitmap() {
        val bm = getBitmap()
        if (bm == mBitmap) {
            return
        }
        mBitmap = bm
        if (mBitmap == null) {
            mBitmapShader = null
            invalidate()
            return
        }
        mNeedResetShader = true
        mBitmapShader = BitmapShader(mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        if (mBitmapPaint == null) {
            mBitmapPaint = Paint()
            mBitmapPaint!!.isAntiAlias = true
        }
        mBitmapPaint!!.shader = mBitmapShader
        requestLayout()
        invalidate()
    }

    private fun updateBitmapShader() {
        mMatrix.reset()
        mNeedResetShader = false
        if (mBitmapShader == null || mBitmap == null) {
            return
        }
        updateMatrix(mMatrix, mBitmap!!, mRectF)
        mBitmapShader!!.setLocalMatrix(mMatrix)
        mBitmapPaint!!.shader = mBitmapShader
    }

    private fun updateMatrix(
        matrix: Matrix,
        bitmap: Bitmap,
        drawRect: RectF
    ) {
        val bmWidth = bitmap.width.toFloat()
        val bmHeight = bitmap.height.toFloat()
        val scaleType = scaleType
        if (scaleType == ScaleType.MATRIX) {
            updateScaleTypeMatrix(matrix, bitmap, drawRect)
        } else if (scaleType == ScaleType.CENTER) {
            val left = (mWidth - bmWidth) / 2
            val top = (mHeight - bmHeight) / 2
            matrix.postTranslate(left, top)
            drawRect[Math.max(0f, left), Math.max(0f, top), Math.min(
                left + bmWidth,
                mWidth.toFloat()
            )] =
                Math.min(top + bmHeight, mHeight.toFloat())
        } else if (scaleType == ScaleType.CENTER_CROP) {
            val scaleX = mWidth / bmWidth
            val scaleY = mHeight / bmHeight
            val scale = Math.max(scaleX, scaleY)
            matrix.setScale(scale, scale)
            matrix.postTranslate(-(scale * bmWidth - mWidth) / 2, -(scale * bmHeight - mHeight) / 2)
            drawRect[0f, 0f, mWidth.toFloat()] = mHeight.toFloat()
        } else if (scaleType == ScaleType.CENTER_INSIDE) {
            val scaleX = mWidth / bmWidth
            val scaleY = mHeight / bmHeight
            if (scaleX >= 1 && scaleY >= 1) {
                val left = (mWidth - bmWidth) / 2
                val top = (mHeight - bmHeight) / 2
                matrix.postTranslate(left, top)
                drawRect[left, top, left + bmWidth] = top + bmHeight
            } else {
                val scale = Math.min(scaleX, scaleY)
                matrix.setScale(scale, scale)
                val bw = bmWidth * scale
                val bh = bmHeight * scale
                val left = (mWidth - bw) / 2
                val top = (mHeight - bh) / 2
                matrix.postTranslate(left, top)
                drawRect[left, top, left + bw] = top + bh
            }
        } else if (scaleType == ScaleType.FIT_XY) {
            val scaleX = mWidth / bmWidth
            val scaleY = mHeight / bmHeight
            matrix.setScale(scaleX, scaleY)
            drawRect[0f, 0f, mWidth.toFloat()] = mHeight.toFloat()
        } else {
            val scaleX = mWidth / bmWidth
            val scaleY = mHeight / bmHeight
            val scale = Math.min(scaleX, scaleY)
            matrix.setScale(scale, scale)
            val bw = bmWidth * scale
            val bh = bmHeight * scale
            if (scaleType == ScaleType.FIT_START) {
                drawRect[0f, 0f, bw] = bh
            } else if (scaleType == ScaleType.FIT_CENTER) {
                val left = (mWidth - bw) / 2
                val top = (mHeight - bh) / 2
                matrix.postTranslate(left, top)
                drawRect[left, top, left + bw] = top + bh
            } else {
                matrix.postTranslate(mWidth - bw, mHeight - bh)
                drawRect[mWidth - bw, mHeight - bh, mWidth.toFloat()] = mHeight.toFloat()
            }
        }
    }

    protected fun updateScaleTypeMatrix(
        matrix: Matrix,
        bitmap: Bitmap,
        drawRect: RectF
    ) {
        matrix.set(imageMatrix)
        drawRect[0f, 0f, mWidth.toFloat()] = mHeight.toFloat()
    }

    private fun drawBitmap(canvas: Canvas, borderWidth: Int) {
        val halfBorderWidth = borderWidth * 1.0f / 2
        mBitmapPaint!!.colorFilter = if (mIsSelected) mSelectedColorFilter else mColorFilter
        if (mIsCircle) {
            canvas.drawCircle(
                mRectF.centerX(),
                mRectF.centerY(),
                Math.min(mRectF.width() / 2, mRectF.height() / 2) - halfBorderWidth,
                mBitmapPaint!!
            )
        } else {
            mDrawRectF.left = mRectF.left + halfBorderWidth
            mDrawRectF.top = mRectF.top + halfBorderWidth
            mDrawRectF.right = mRectF.right - halfBorderWidth
            mDrawRectF.bottom = mRectF.bottom - halfBorderWidth
            if (mIsOval) {
                canvas.drawOval(mDrawRectF, mBitmapPaint!!)
            } else {
                canvas.drawRoundRect(
                    mDrawRectF,
                    mCornerRadius.toFloat(),
                    mCornerRadius.toFloat(),
                    mBitmapPaint!!
                )
            }
        }
    }

    private fun drawBorder(canvas: Canvas, borderWidth: Int) {
        if (borderWidth <= 0) {
            return
        }
        val halfBorderWidth = borderWidth * 1.0f / 2
        mBorderPaint.color = if (mIsSelected) mSelectedBorderColor else mBorderColor
        mBorderPaint.strokeWidth = borderWidth.toFloat()
        if (mIsCircle) {
            canvas.drawCircle(
                mRectF.centerX(),
                mRectF.centerY(),
                Math.min(mRectF.width(), mRectF.height()) / 2 - halfBorderWidth,
                mBorderPaint
            )
        } else {
            mDrawRectF.left = mRectF.left + halfBorderWidth
            mDrawRectF.top = mRectF.top + halfBorderWidth
            mDrawRectF.right = mRectF.right - halfBorderWidth
            mDrawRectF.bottom = mRectF.bottom - halfBorderWidth
            if (mIsOval) {
                canvas.drawOval(mDrawRectF, mBorderPaint)
            } else {
                canvas.drawRoundRect(
                    mDrawRectF,
                    mCornerRadius.toFloat(),
                    mCornerRadius.toFloat(),
                    mBorderPaint
                )
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        val width = width
        val height = height
        if (width <= 0 || height <= 0) {
            return
        }
        val borderWidth = if (mIsSelected) mSelectedBorderWidth else mBorderWidth
        if (mBitmap == null || mBitmapShader == null) {
            drawBorder(canvas, borderWidth)
            return
        }
        if (mWidth != width || mHeight != height || mLastCalculateScaleType != scaleType || mNeedResetShader
        ) {
            mWidth = width
            mHeight = height
            mLastCalculateScaleType = scaleType
            updateBitmapShader()
        }
        drawBitmap(canvas, borderWidth)
        drawBorder(canvas, borderWidth)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!this.isClickable) {
            this.isSelected = false
            return super.onTouchEvent(event)
        }
        if (!mIsTouchSelectModeEnabled) {
            return super.onTouchEvent(event)
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> this.isSelected = true
            MotionEvent.ACTION_UP, MotionEvent.ACTION_SCROLL, MotionEvent.ACTION_OUTSIDE, MotionEvent.ACTION_CANCEL -> this.isSelected =
                false
        }
        return super.onTouchEvent(event)
    }

    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.GRAY
        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
        private const val COLOR_DRAWABLE_DIMEN = 2
    }

    init {
        mBorderPaint = Paint()
        mBorderPaint.isAntiAlias = true
        mBorderPaint.style = Paint.Style.STROKE
        mMatrix = Matrix()
        scaleType = ScaleType.CENTER_CROP
        val array = context.obtainStyledAttributes(
            attrs, R.styleable.RadiusImageViewStyle, defStyleAttr, 0
        )
        mBorderWidth =
            array.getDimensionPixelSize(R.styleable.RadiusImageViewStyle_borderwidth, 0)
        mBorderColor = array.getColor(
            R.styleable.RadiusImageViewStyle_bordercolor,
            DEFAULT_BORDER_COLOR
        )
        mSelectedBorderWidth = array.getDimensionPixelSize(
            R.styleable.RadiusImageViewStyle_selected_border_width, mBorderWidth
        )
        mSelectedBorderColor = array.getColor(
            R.styleable.RadiusImageViewStyle_selected_border_color, mBorderColor
        )
        mSelectedMaskColor = array.getColor(
            R.styleable.RadiusImageViewStyle_selected_mask_color,
            Color.TRANSPARENT
        )
        if (mSelectedMaskColor != Color.TRANSPARENT) {
            mSelectedColorFilter = PorterDuffColorFilter(mSelectedMaskColor, PorterDuff.Mode.DARKEN)
        }
        mIsTouchSelectModeEnabled = array.getBoolean(
            R.styleable.RadiusImageViewStyle_is_touch_select_mode_enabled, true
        )
        mIsCircle = array.getBoolean(R.styleable.RadiusImageViewStyle_iscircle, false)
        if (!mIsCircle) {
            mIsOval = array.getBoolean(R.styleable.RadiusImageViewStyle_is_oval, false)
        }
        if (!mIsOval) {
            mCornerRadius = array.getDimensionPixelSize(
                R.styleable.RadiusImageViewStyle_cornerradius, 0
            )
        }
        array.recycle()
    }
}