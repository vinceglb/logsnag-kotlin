package com.logsnag.kotlin.init

import android.content.Context
import androidx.startup.Initializer
import androidx.work.WorkManager
import com.logsnag.kotlin.api.SendEventTasksDataSource

@Suppress("unused")
internal class EventTasksDataSourceInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val workManager = WorkManager.getInstance(context)
        SendEventTasksDataSource.init(workManager)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}
