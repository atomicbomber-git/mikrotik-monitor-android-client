package club.democlient.mikrotikmonitor.mikrotikmonitor

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val dependencyModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("http://10.0.2.2:8000")
            .build()
    }

    single<RouterMonitorService> {
        val retrofit: Retrofit = get()
        retrofit.create(RouterMonitorService::class.java)
    }

    single<SharedPreferences> {(activity: Activity) ->
        activity.getSharedPreferences(activity.applicationContext.packageName, Context.MODE_PRIVATE)
    }
}