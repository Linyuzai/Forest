package eason.linyuzai.forest

import android.app.Activity
import android.app.Application
import eason.linyuzai.download.ELoad

inline fun Application.eload(): ELoad = (this as ForestApp).eload()

inline fun Activity.eload(): ELoad = this.application.eload()