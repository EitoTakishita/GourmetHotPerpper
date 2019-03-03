package com.gourmet.hotpepper.opening

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView

class WaveAnimationLayout : LinearLayout {

    var color: Int = 0 // テキストカラー
    var textSizePx = DEFAULT_TEXT_SIZE
        private set //テキストサイズ
    private var speed = DEFAULT_SPEED //次のアニメーションが実行するスピード
    private var text: String? = null
    private var startOffset: Int = 0 //最初のアニメーション開始までのoffset
    private var linearLayouts: ArrayList<LinearLayout>? = null

    //アニメーションの終了を通知するリスナー
    interface EndAnimationListener {
        fun onEnd()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        parseAttribute(attrs)
        init()
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        parseAttribute(attrs)
        init()
    }

    fun setTextSizePx(textSizePx: Int) {
        this.textSizePx = textSizePx.toFloat()
    }

    fun getText(): String? {
        return text
    }

    fun setText(text: String) {
        this.text = text
        init()
    }

    /**
     * layoutのattrsパラメーターが設定されていれば取得し、設定する
     *
     * @param attrs
     */
    fun parseAttribute(attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, com.gourmet.hotpepper.R.styleable.WaveAnimationLayout)
        if (ta != null) {
            text = ta!!.getString(com.gourmet.hotpepper.R.styleable.WaveAnimationLayout_waveText)
            color = ta!!.getColor(com.gourmet.hotpepper.R.styleable.WaveAnimationLayout_waveTextColor, DEFAULT_COLOR)
            textSizePx = ta!!.getDimension(com.gourmet.hotpepper.R.styleable.WaveAnimationLayout_waveTextSize, DEFAULT_TEXT_SIZE)
        }
    }

    fun init() {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        linearLayouts = ArrayList()
        addTextViews()
    }

    fun createLinearLayout(): LinearLayout {
        val linearLayout = LinearLayout(context, null)
        linearLayout.layoutParams =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        linearLayouts!!.add(linearLayout)
        addView(linearLayout)
        return linearLayout
    }

    fun setStartOffset(offset: Int) {
        startOffset = offset
    }

    fun setSpeed(speed: Int) {
        this.speed = speed
    }

    /**
     * 設定されているtextの一文字ずつ取り出し、TextViewにして
     * LinearLayoutに追加していく
     */
    fun addTextViews() {
        var linearLayout = createLinearLayout()
        for (i in 0 until text!!.length) {
            //改行が見つかれば、新しいLinearLayout(horizontal)を追加する
            if (text!![i] == '\n') {
                linearLayout = createLinearLayout()
                continue
            }
            val textView = TextView(context)
            textView.setTextColor(color)
            textView.textSize = textSizePx
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx)
            textView.text = text!![i] + ""
            val tf = Typeface.createFromAsset(context.assets, "fonts/HonyaJi-Re.ttf")
            textView.typeface = tf
            textView.visibility = View.INVISIBLE
            linearLayout.addView(textView)
        }
    }

    /**
     * 内包している全てのViewに対してAnimationを実行していく
     *
     * @param endAnimationListener
     */
    fun startWaveAnimation(endAnimationListener: EndAnimationListener?) {

        var offset = startOffset
        for (i in 0 until linearLayouts!!.size) {
            for (j in 0 until linearLayouts!![i].childCount) {
                val animation = AnimationUtils.loadAnimation(context, com.gourmet.hotpepper.R.anim.popup)
                animation.startOffset = offset.toLong()
                //最後のアニメーションに対してListenerを設定する
                if (j == linearLayouts!![i].childCount - 1 && i == childCount - 1) {
                    animation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation) {}

                        override fun onAnimationEnd(animation: Animation) {
                            endAnimationListener?.onEnd()
                        }

                        override fun onAnimationRepeat(animation: Animation) {}
                    })
                }
                linearLayouts!![i].getChildAt(j).visibility = View.VISIBLE
                linearLayouts!![i].getChildAt(j).startAnimation(animation)
                offset += speed
            }
        }
    }

    companion object {

        private val DEFAULT_COLOR = -0x1
        private val DEFAULT_TEXT_SIZE = 25f
        private val DEFAULT_SPEED = 50
    }
}