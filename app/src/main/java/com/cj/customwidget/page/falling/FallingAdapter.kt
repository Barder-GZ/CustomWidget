package com.cj.customwidget.page.falling

import android.graphics.Path
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageView
import com.cj.customwidget.R
import com.cj.customwidget.widget.FallingView
import java.util.*
import kotlin.collections.ArrayList

class FallingAdapter : FallingView.IFallingAdapter<Int>(R.layout.item_redpack) {
    private val random = Random()
    private val animDuration = 6000L//物件动画时长
    private val count = 10//一屏显示物件的个数

    private val animInterval = ArrayList<Interval>()
    var itemClickListener: FallingViewItemClickListener? = null

    fun setData(data: List<Int>) {
        datas = data
    }

    private fun createPath(parent: ViewGroup, position: Int, view: View): Path =
        Path().apply {
            view.measure(0, 0)
            val width = parent.width - view.measuredWidth
            val height = parent.height
            val swing = width / 3f//x轴摆动范围
            //限制动画区间使物件分布均匀
            if (animInterval.isEmpty()) {
                animInterval.add(Interval(view.measuredWidth / 2f, swing))
                animInterval.add(Interval(swing, swing * 2))
                animInterval.add(Interval(swing * 2, parent.width - view.measuredWidth / 2f))
            }
//            "animInterval:${animInterval.size}".p()
            val interval = if (animInterval.size == 1) {
                animInterval[0]
            } else {
                animInterval[random.nextInt(animInterval.size)]
            }
            animInterval.remove(interval)
            val startPointX = random.nextInt(width).toFloat()
            moveTo(startPointX, -view.measuredHeight.toFloat())

            //控制点
            var point1X = random.nextInt(interval.getLength().toInt()) + interval.start
            val point1Y = random.nextInt(height / 2).toFloat()

            var point2X = random.nextInt(interval.getLength().toInt()) +interval.start
            val point2Y = random.nextInt(height / 2).toFloat() + height / 2

            var point3X = random.nextInt(interval.getLength().toInt()) + interval.start

            cubicTo(point1X, point1Y, point2X, point2Y, point3X, height.toFloat())
        }


    override fun convert(parent: ViewGroup, holder: FallingView.Holder) {
        if (holder.position%20==0){
            (holder.view as ImageView).setImageResource(R.mipmap.ic_readpack2)
        }else{
            (holder.view as ImageView).setImageResource(R.mipmap.ic_readpack)
        }
        holder.config.startTime = holder.position * (animDuration / count)
        holder.view.setOnClickListener {//点中红包回调
            itemClickListener?.onItemClick(holder.position, it)
        }
    }

    override fun convertAnim(parent: ViewGroup, holder: FallingView.Holder): Animation {
        val path = createPath(parent, holder.position, holder.view)
        holder.config.path = path
        //旋转方向
        val rotation:Float
        if (random.nextInt(2)==0){
            rotation = 30f*random.nextFloat()
        }else{
            rotation = -30f*random.nextFloat()
        }
        val redPackAnim = RedPackAnim(path, rotation, holder.view)
        //动画时长-下落速度
        redPackAnim.duration = (animDuration*(0.6+random.nextInt(4)*0.1)).toLong()
        return redPackAnim
    }

    //区间
    class Interval(val start: Float, val end: Float) {
        fun getLength() = end - start
    }

    interface FallingViewItemClickListener{
        fun onItemClick(position: Int, view: View)
    }
}