package com.github.judrummer.kithub

import android.app.Application
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.interceptors.cUrlLoggingRequestInterceptor
import com.taskworld.kxandroid.logD

/**
 * Created by judrummer on 19/3/2560.
 */


class KithubApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            FuelManager.instance.apply {
                addRequestInterceptor(cUrlLoggingRequestInterceptor())
                addRequestInterceptor { next: (Request) -> Request ->
                    { req: Request ->
                        logD("Request $req", "FUELDEBUG")
                        next(req)
                    }
                }
                addResponseInterceptor { next: (Request, Response) -> Response ->
                    { req: Request, res: Response ->
                        logD("Response $res", "FUELDEBUG")
                        next(req, res)
                    }
                }
            }
        }
    }

}