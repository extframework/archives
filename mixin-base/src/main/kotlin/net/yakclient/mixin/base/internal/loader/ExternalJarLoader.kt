package net.yakclient.mixin.base.internal.loader

import java.net.URL
import java.net.URLClassLoader

class ExternalJarLoader(
    url: URL, parent:
    TargetClassLoader
) : URLClassLoader(arrayOf(url), parent)