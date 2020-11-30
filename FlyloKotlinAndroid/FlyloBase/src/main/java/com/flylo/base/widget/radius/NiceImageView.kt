package com.flylo.base.widget.radius

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView
import com.flylo.base.R

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.base.widget.radius
 * @ClassName:      NiceImageView
 * @Author:         ANWEN
 * @CreateDate:     2020/6/20 10:05 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/20 10:05 PM
 * @UpdateRemark:
 * @Version:
 */
class NiceImageView @JvmOverloads constructor(
    context: Context,
    @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    AppCompatImageView(context, attrs, defStyleAttr) {
    private var isCircle // 是否显示为圆形，如果为圆形则设置的corner无效
            = false
    private var isCoverSrc // border、inner_border是否覆盖图片
            = false
    private var borderWidth // 边框宽度
            = 0
    private var borderColor = Color.TRANSPARENT // 边框颜色
    private var innerBorderWidth // 内层边框宽度
            = 0
    private var innerBorderColor = Color.WHITE // 内层边框充色
    private var cornerRadius // 统一设置圆角半径，优先级高于单独设置每个角的半径
            = 0
    private var cornerTopLeftRadius // 左上角圆角半径
            = 0
    private var cornerTopRightRadius // 右上角圆角半径
            = 0
    private var cornerBottomLeftRadius // 左下角圆角半径
            = 0
    private var cornerBottomRightRadius // 右下角圆角半径
            = 0
    private var maskColor // 遮罩颜色
            = 0
    private val xfermode: Xfermode
    private var width_tmp = 0
    private var height_tmp = 0
    private var radius = 0f
    private val borderRadii: FloatArray
    private val srcRadii: FloatArray
    private var srcRectF // 图片占的矩形区域
            : RectF
    private val borderRectF // 边框的矩形区域
            : RectF
    private val path = Path()
    private val paint = Paint()
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        width_tmp = w
        height_tmp = h
        initBorderRectF()
        initSrcRectF()
    }

    //替换掉NICEIAMGE类中的这个方法就行了
    override fun onDraw(canvas: Canvas) {
// 使用图形混合模式来显示指定区域的图片
        canvas.saveLayer(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            null,
            Canvas.ALL_SAVE_FLAG
        )
        val drawable = drawable
        if (drawable == null) {
            super.onDraw(canvas)
            return
        }
        paint.reset()
        path.reset()
        if (!isCoverSrc) {
            val sx =
                1.0f * (width_tmp - 2 * borderWidth - 2 * innerBorderWidth) / width_tmp
            val sy =
                1.0f * (height_tmp - 2 * borderWidth - 2 * innerBorderWidth) / height_tmp
            // 缩小画布，使图片内容不被border、padding覆盖
            canvas.scale(sx, sy, width_tmp / 2.0f, height_tmp / 2.0f)
        }
        if (isCircle) {
            path.addCircle(
                width_tmp / 2.0f,
                height_tmp / 2.0f,
                radius,
                Path.Direction.CCW
            )
        } else {
            path.addRoundRect(srcRectF, srcRadii, Path.Direction.CCW)
        }
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        canvas.drawPath(path, paint)
        paint.xfermode = xfermode
        canvas.drawBitmap(zoomDrawable(drawable), 0f, 0f, paint)
        paint.xfermode = null

        // 绘制遮罩
        if (maskColor != 0) {
            paint.color = maskColor
            canvas.drawPath(path, paint)
        }
        // 恢复画布
        canvas.restore()
        // 绘制边框
        drawBorders(canvas)
    }

    private fun drawBorders(canvas: Canvas) {
        if (isCircle) {
            if (borderWidth > 0) {
                drawCircleBorder(canvas, borderWidth, borderColor, radius - borderWidth / 2.0f)
            }
            if (innerBorderWidth > 0) {
                drawCircleBorder(
                    canvas,
                    innerBorderWidth,
                    innerBorderColor,
                    radius - borderWidth - innerBorderWidth / 2.0f
                )
            }
        } else {
            if (borderWidth > 0) {
                drawRectFBorder(canvas, borderWidth, borderColor, borderRectF, borderRadii)
            }
        }
    }

    private fun drawCircleBorder(
        canvas: Canvas,
        borderWidth: Int,
        borderColor: Int,
        radius: Float
    ) {
        initBorderPaint(borderWidth, borderColor)
        path.addCircle(
            width_tmp / 2.0f,
            height_tmp / 2.0f,
            radius,
            Path.Direction.CCW
        )
        canvas.drawPath(path, paint)
    }

    private fun drawRectFBorder(
        canvas: Canvas,
        borderWidth: Int,
        borderColor: Int,
        rectF: RectF,
        radii: FloatArray
    ) {
        initBorderPaint(borderWidth, borderColor)
        path.addRoundRect(rectF, radii, Path.Direction.CCW)
        canvas.drawPath(path, paint)
    }

    private fun initBorderPaint(borderWidth: Int, borderColor: Int) {
        path.reset()
        paint.strokeWidth = borderWidth.toFloat()
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
    }

    /**
     * 计算外边框的RectF
     */
    private fun initBorderRectF() {
        if (!isCircle) {
            borderRectF[borderWidth / 2.0f, borderWidth / 2.0f, width_tmp - borderWidth / 2.0f] =
                height_tmp - borderWidth / 2.0f
        }
    }

    /**
     * 计算图片原始区域的RectF
     */
    private fun initSrcRectF() {
        if (isCircle) {
            radius = Math.min(width_tmp, height_tmp) / 2.0f
            srcRectF[width_tmp / 2.0f - radius, height_tmp / 2.0f - radius, width_tmp / 2.0f + radius] =
                height_tmp / 2.0f + radius
        } else {
            srcRectF[0f, 0f, width_tmp.toFloat()] = height_tmp.toFloat()
            if (isCoverSrc) {
                srcRectF = borderRectF
            }
        }
    }

    /**
     * 计算RectF的圆角半径
     */
    private fun calculateRadii() {
        if (isCircle) {
            return
        }
        if (cornerRadius > 0) {
            for (i in borderRadii.indices) {
                borderRadii[i] = cornerRadius.toFloat()
                srcRadii[i] = cornerRadius - borderWidth / 2.0f
            }
        } else {
            borderRadii[1] = cornerTopLeftRadius.toFloat()
            borderRadii[0] = borderRadii[1]
            borderRadii[3] = cornerTopRightRadius.toFloat()
            borderRadii[2] = borderRadii[3]
            borderRadii[5] = cornerBottomRightRadius.toFloat()
            borderRadii[4] = borderRadii[5]
            borderRadii[7] = cornerBottomLeftRadius.toFloat()
            borderRadii[6] = borderRadii[7]
            srcRadii[1] = cornerTopLeftRadius - borderWidth / 2.0f
            srcRadii[0] = srcRadii[1]
            srcRadii[3] = cornerTopRightRadius - borderWidth / 2.0f
            srcRadii[2] = srcRadii[3]
            srcRadii[5] = cornerBottomRightRadius - borderWidth / 2.0f
            srcRadii[4] = srcRadii[5]
            srcRadii[7] = cornerBottomLeftRadius - borderWidth / 2.0f
            srcRadii[6] = srcRadii[7]
        }
    }

    private fun calculateRadiiAndRectF(reset: Boolean) {
        if (reset) {
            cornerRadius = 0
        }
        calculateRadii()
        initBorderRectF()
        invalidate()
    }

    /**
     * 目前圆角矩形情况下不支持inner_border，需要将其置0
     */
    private fun clearInnerBorderWidth() {
        if (!isCircle) {
            innerBorderWidth = 0
        }
    }

    fun isCoverSrc(isCoverSrc: Boolean) {
        this.isCoverSrc = isCoverSrc
        initSrcRectF()
        invalidate()
    }

    fun isCircle(isCircle: Boolean) {
        this.isCircle = isCircle
        clearInnerBorderWidth()
        initSrcRectF()
        invalidate()
    }

    fun setBorderWidth(borderWidth: Int) {
        this.borderWidth = dp2px(context, borderWidth)
        calculateRadiiAndRectF(false)
    }

    fun setBorderColor(@ColorInt borderColor: Int) {
        this.borderColor = borderColor
        invalidate()
    }

    fun setInnerBorderWidth(innerBorderWidth: Int) {
        this.innerBorderWidth = dp2px(context, innerBorderWidth)
        clearInnerBorderWidth()
        invalidate()
    }

    fun setInnerBorderColor(@ColorInt innerBorderColor: Int) {
        this.innerBorderColor = innerBorderColor
        invalidate()
    }

    fun setCornerRadius(cornerRadius: Int) {
        this.cornerRadius = dp2px(context, cornerRadius)
        calculateRadiiAndRectF(false)
    }

    fun setCornerTopLeftRadius(cornerTopLeftRadius: Int) {
        this.cornerTopLeftRadius = dp2px(context, cornerTopLeftRadius)
        calculateRadiiAndRectF(true)
    }

    fun setCornerTopRightRadius(cornerTopRightRadius: Int) {
        this.cornerTopRightRadius = dp2px(context, cornerTopRightRadius)
        calculateRadiiAndRectF(true)
    }

    fun setCornerBottomLeftRadius(cornerBottomLeftRadius: Int) {
        this.cornerBottomLeftRadius = dp2px(context, cornerBottomLeftRadius)
        calculateRadiiAndRectF(true)
    }

    fun setCornerBottomRightRadius(cornerBottomRightRadius: Int) {
        this.cornerBottomRightRadius = dp2px(context, cornerBottomRightRadius)
        calculateRadiiAndRectF(true)
    }

    fun setMaskColor(@ColorInt maskColor: Int) {
        this.maskColor = maskColor
        invalidate()
    }

    private fun drawableToBitamp(drawable: Drawable): Bitmap {
//        if (drawable instanceof BitmapDrawable) {
//            BitmapDrawable bd = (BitmapDrawable) drawable;
//            return bd.getBitmap();
//        }
        // 设置为颜色时，获取的drawable宽高会有问题，所有当为颜色时候获取控件的宽高
        val w =
            if (drawable.intrinsicWidth <= 0) width else drawable.intrinsicWidth
        val h =
            if (drawable.intrinsicHeight <= 0) height else drawable.intrinsicHeight
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w, h)
        drawable.draw(canvas)
        return bitmap
    }

    private fun zoomDrawable(drawable: Drawable): Bitmap {
        var width = drawable.intrinsicWidth
        var height = drawable.intrinsicHeight
        val oldbmp = drawableToBitamp(drawable)
        val matrix = Matrix()
        val scaleWidth = getWidth().toFloat() / width
        val scaleHeight = getHeight().toFloat() / height
        matrix.postScale(scaleWidth, scaleHeight)
        if (width > oldbmp.width) {
            width = oldbmp.width
        }
        if (height > oldbmp.height) {
            height = oldbmp.height
        }
        return Bitmap.createBitmap(
            oldbmp, 0, 0, width, height,
            matrix, true
        )
    }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.NiceImageView, 0, 0)
        for (i in 0 until ta.indexCount) {
            val attr = ta.getIndex(i)
            if (attr == R.styleable.NiceImageView_is_cover_src) {
                isCoverSrc = ta.getBoolean(attr, isCoverSrc)
            } else if (attr == R.styleable.NiceImageView_is_circle) {
                isCircle = ta.getBoolean(attr, isCircle)
            } else if (attr == R.styleable.NiceImageView_border_width) {
                borderWidth = ta.getDimensionPixelSize(attr, borderWidth)
            } else if (attr == R.styleable.NiceImageView_border_color) {
                borderColor = ta.getColor(attr, borderColor)
            } else if (attr == R.styleable.NiceImageView_inner_border_width) {
                innerBorderWidth = ta.getDimensionPixelSize(attr, innerBorderWidth)
            } else if (attr == R.styleable.NiceImageView_inner_border_color) {
                innerBorderColor = ta.getColor(attr, innerBorderColor)
            } else if (attr == R.styleable.NiceImageView_corner_radius) {
                cornerRadius = ta.getDimensionPixelSize(attr, cornerRadius)
            } else if (attr == R.styleable.NiceImageView_corner_top_left_radius) {
                cornerTopLeftRadius = ta.getDimensionPixelSize(attr, cornerTopLeftRadius)
            } else if (attr == R.styleable.NiceImageView_corner_top_right_radius) {
                cornerTopRightRadius = ta.getDimensionPixelSize(attr, cornerTopRightRadius)
            } else if (attr == R.styleable.NiceImageView_corner_bottom_left_radius) {
                cornerBottomLeftRadius = ta.getDimensionPixelSize(attr, cornerBottomLeftRadius)
            } else if (attr == R.styleable.NiceImageView_corner_bottom_right_radius) {
                cornerBottomRightRadius = ta.getDimensionPixelSize(attr, cornerBottomRightRadius)
            } else if (attr == R.styleable.NiceImageView_mask_color) {
                maskColor = ta.getColor(attr, maskColor)
            }
        }
        ta.recycle()
        borderRadii = FloatArray(8)
        srcRadii = FloatArray(8)
        borderRectF = RectF()
        srcRectF = RectF()
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        scaleType = ScaleType.CENTER_CROP
        calculateRadii()
        clearInnerBorderWidth()
    }

    fun dp2px(context: Context, dipValue: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}
