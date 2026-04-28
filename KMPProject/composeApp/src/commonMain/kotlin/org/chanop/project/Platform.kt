package org.chanop.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform