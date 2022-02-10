package net.adriantodt.thewolf.stories.view

import kotlinx.html.*

fun HEAD.storiesHead() {
    meta(charset = "utf-8")
    meta(name = "viewport", content = "width=device-width, initial-scale=1.0")
    link(rel = "apple-touch-icon", href = "$imageCdn/apple-touch-icon.png") {
        sizes = "180x180"
    }
    link(rel = "icon", type = "image/svg+xml", href = "$imageCdn/favicon.svg")
    link(rel = "icon", type = "image/png", href = "$imageCdn/favicon-32x32.png") {
        sizes = "32x32"
    }
    link(rel = "icon", type = "image/png", href = "$imageCdn/favicon-16x16.png") {
        sizes = "16x16"
    }
    link(rel = "manifest", href = "$imageCdn/site.webmanifest")
    link(rel = "mask-icon", href = "$imageCdn/safari-pinned-tab.svg") {
        attributes["color"] = "#de122d"
    }
    meta(name = "msapplication-TileColor", content = "#de122d")
    meta(name = "theme-color", content = "#ffffff")
    style {
        unsafe {
            +"""
                html, body {
                    background-color: #21232b;
                    font-family: -apple-system, BlinkMacSystemFont, avenir next, avenir, segoe ui, helvetica neue, helvetica, Ubuntu, roboto, noto, arial, sans-serif;
                    color: #e5edf6;
                    font-weight: bold;
                    font-style: italic;
                }
                .red {color: #f20928;}
            """.trimIndent()
        }
    }
}

fun HtmlBlockTag.wolfImg() {
    img(src = "$imageCdn/favicon.svg", alt = "A very big, black wolf with blood-red accents.") {
        style = "margin-top: 0.5rem; max-width: min(max(40vh, 200px), 90vw);"
        title = "Note: it may not look like it, but he's actually a good boy."
    }
}

fun HtmlBlockTag.separator() {
    div {
        style = "max-width: min(25rem, 90vw); margin: 8px auto; border-top: 2px solid rgba(229,237,246,0.4);"
    }
}

fun HtmlBlockTag.traefikProxy() {
    span {
        style = "color: #24a0c0;"
        title = """
            Traefik Proxy.
            Excellent piece of software .
        """.trimIndent()
        +"Traefik"
    }
}

fun HtmlBlockTag.thewolfServer() {
    span("red") {
        title = """
            "SIAMÉS - The Wolf"
            (Also, the server you're accessing.)
        """.trimIndent()
        +"THEWOLF"
    }
}

const val textCenter = "text-align: center"

const val imageCdn = "https://assets.home.adriantodt.net/thewolf_red"

const val civilization = "https://www.google.com/"

const val musicDisc = "https://www.youtube.com/watch?v=lX44CAz-JhU"

const val systemFontMono = "font-family: Menlo, Consolas, Monaco, Liberation Mono, Lucida Console, monospace;"
