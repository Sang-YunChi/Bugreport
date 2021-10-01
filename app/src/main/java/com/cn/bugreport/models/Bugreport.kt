package com.cn.bugreport.models
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Bugreport(val reportId: String, val reporterId: String, val stackTraceUrl: String, val title: String, val content:String, val timestamp: Long):
    Parcelable {
    constructor(): this("", "", "", "", "", -1)
}