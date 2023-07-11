package com.cj.customwidget.page.falling

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.apkfuns.logutils.LogUtils
import com.cj.customwidget.R
import kotlinx.android.synthetic.main.activity_falling.v_falling

/**
 * File FallingActivity.kt--
 * Date 12/25/20
 * Author lucas
 * Introduction 红包雨
 */
class FallingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_falling)
        val adapter = FallingAdapter().apply { setData(List(10000) { it }) }
        adapter.itemClickListener = object : FallingAdapter.FallingViewItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                LogUtils.i("onItemClick: position ->$position")
            }
        }
        v_falling.setAdapter(adapter)
        v_falling.startFalling()
//        Handler().postDelayed({
//            v_drawer.openDrawer(v_right)
//        },3000)
    }
}