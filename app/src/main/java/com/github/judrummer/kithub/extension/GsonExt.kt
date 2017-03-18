package com.github.judrummer.kithub.extension

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.Reader
import java.lang.reflect.Type


fun DGson(): Gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create()

inline fun <reified T> T.toJson(): String = DGson().toJson(this)
inline fun <reified T> String.parseJson() = DGson().fromJson<T>(this, typeToken<T>())
inline fun <reified T> Reader.parseJson() = DGson().fromJson<T>(this, typeToken<T>())
inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, com.github.judrummer.kithub.extension.typeToken<T>())
inline fun <reified T> Gson.fromJson(json: Reader) = this.fromJson<T>(json, com.github.judrummer.kithub.extension.typeToken<T>())
inline fun <reified T> typeToken(): Type = object : TypeToken<T>() {}.type

