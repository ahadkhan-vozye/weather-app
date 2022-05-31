package com.app.weatherapp.utils

import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit

fun View.setSafeOnClickListener(onClick: (View) -> Unit) {
    RxView.clicks(this).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe {
        onClick(this)
    }
}