package com.jdannhausenbrun.demoproject.common

import android.content.Context
import coil.ImageLoader
import coil.decode.SvgDecoder

object CoilSVGLoader {
    private var imageLoader: ImageLoader? = null

    @Synchronized
    fun getInstance(context: Context): ImageLoader {
        if (imageLoader != null) {
            return imageLoader!!
        }

        imageLoader = ImageLoader.Builder(context)
            .componentRegistry {
                add(SvgDecoder(context))
            }
            .build()

        return imageLoader!!
    }
}