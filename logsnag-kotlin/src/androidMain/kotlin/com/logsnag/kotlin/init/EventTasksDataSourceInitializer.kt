package com.logsnag.kotlin.init

import android.content.Context
import androidx.startup.Initializer
import androidx.work.WorkManager
import com.logsnag.kotlin.api.SendEventTasksDataSource

@Suppress("unused")
internal class EventTasksDataSourceInitializer : Initializer<SendEventTasksDataSource> {
    override fun create(context: Context): SendEventTasksDataSource {
        val workManager = WorkManager.getInstance(context)
        return SendEventTasksDataSource.init(workManager)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}
