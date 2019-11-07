package com.halcyonmobile.multiplatformplayground.shared.observer

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import java.lang.IllegalArgumentException

actual class Observable<T> {

    actual var value: T? = null

    private val liveData = MutableLiveData<T>()

    actual fun observe(observer: Observer<T>) {
        (observer as? LiveDataObserver)?.let {
            liveData.observe(it.lifecycleOwner, it.androidObserver)
        }
            ?: throw IllegalArgumentException("Observer should be a ${LiveDataObserver::class.java.canonicalName}")
    }

    actual fun removeObserver(observer: Observer<T>) {
        (observer as? LiveDataObserver?)?.let {
            liveData.removeObserver(it.androidObserver)
        }
            ?: throw IllegalArgumentException("Observer should be a ${LiveDataObserver::class.java.canonicalName}")
    }
}

class LiveDataObserver<T>(
    val lifecycleOwner: LifecycleOwner,
    val androidObserver: androidx.lifecycle.Observer<T>
) : Observer<T> {

    override fun onChanged(value: T?) {
        androidObserver.onChanged(value)
    }
}