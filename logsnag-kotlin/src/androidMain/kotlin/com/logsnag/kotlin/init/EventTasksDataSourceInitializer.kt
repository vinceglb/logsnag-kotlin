package com.logsnag.kotlin.init

import android.content.Context
import androidx.startup.Initializer
import androidx.work.WorkManager
import androidx.work.WorkManagerInitializer
import com.logsnag.kotlin.api.SendEventTasksDataSource

public class EventTasksDataSourceInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val workManager = WorkManager.getInstance(context)
        SendEventTasksDataSource.init(workManager)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(WorkManagerInitializer::class.java)
    }
}
