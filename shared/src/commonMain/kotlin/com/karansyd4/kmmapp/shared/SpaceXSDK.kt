package com.karansyd4.kmmapp.shared

import com.karansyd4.kmmapp.shared.cache.Database
import com.karansyd4.kmmapp.shared.cache.DatabaseDriverFactory
import com.karansyd4.kmmapp.shared.entity.RocketLaunch
import com.karansyd4.kmmapp.shared.network.SpaceXApi

class SpaceXSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = SpaceXApi()

    @Throws(Exception::class) suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            api.getAllLaunches().also {
                database.clearDatabase()
                database.createLaunches(it)
            }
        }
    }

}