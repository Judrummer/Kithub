package com.github.judrummer.kithub.extension

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.judrummer.kithub.extension.mapResult
import com.github.kittinunf.fuel.core.Deserializable
import com.github.kittinunf.fuel.core.deserializers.ByteArrayDeserializer
import com.github.kittinunf.fuel.core.deserializers.StringDeserializer
import com.github.kittinunf.fuel.core.response
import com.github.kittinunf.result.Result
import io.reactivex.Observable
import java.io.Reader
import java.nio.charset.Charset

fun Request.jsonBody(body: String, charset: Charset = Charsets.UTF_8) = header("Content-Type" to "application/json").body(body, charset)

fun Request.jsonBody(body: ByteArray) = header("Content-Type" to "application/json").body(body)

inline fun <reified T : Any> Request.rx_gson() = rx_object(object : ResponseDeserializable<T> {
    override fun deserialize(reader: Reader): T {
        return reader.parseJson<T>()
    }
})

inline fun <reified T : Any> Request.rx_gsonResult() = rx_gson<T>().mapResult()

fun Request.rx_bytes() = rx_result(ByteArrayDeserializer())

fun <T : Any> Request.rx_object(deserializable: Deserializable<T>) = rx_result(deserializable)

fun Request.rx_string(charset: Charset = Charsets.UTF_8) = rx_result(StringDeserializer(charset))

private fun <T : Any> Request.rx_result(deserializable: Deserializable<T>): Observable<T> =
        Observable.create<T> { subscriber ->
            response(deserializable) { request, response, result ->
                when (result) {
                    is Result.Success -> {
                        subscriber.onNext(result.value)
                        subscriber.onComplete()
                    }

                    is Result.Failure -> {
                        subscriber.onError(result.error)
                    }
                }
            }
        }.doOnDispose { request.cancel() }


