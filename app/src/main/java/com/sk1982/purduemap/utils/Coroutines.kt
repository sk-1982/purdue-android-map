package com.sk1982.purduemap.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Coroutines {
    inline fun launch(crossinline work: suspend (() -> Unit)) {
        CoroutineScope(Dispatchers.Main).launch {
            work()
        }
    }
}
