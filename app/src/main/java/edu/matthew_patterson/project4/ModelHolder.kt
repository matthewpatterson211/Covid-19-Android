package edu.matthew_patterson.project4

import java.lang.ref.WeakReference
import kotlin.reflect.KClass

class ModelHolder private constructor(){                        //model holder to prevent us from remaking model

    private val modelClasses = HashMap<String, WeakReference<Any?>>()

    companion object {
        val instance = ModelHolder()
    }

    fun <T: Any>get(classType: KClass<T>): T? {
        val modelClass = modelClasses[classType.java.toString()]
        return modelClass?.get() as? T
    }

    fun <T: Any>set(classInstance: T?) {
        modelClasses[classInstance?.javaClass.toString()] = WeakReference(classInstance as? Any)
    }

}