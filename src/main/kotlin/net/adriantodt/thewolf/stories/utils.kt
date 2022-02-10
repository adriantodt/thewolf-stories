package net.adriantodt.thewolf.stories

fun String.xmlEscape(): String {
    var escaped = this
    if (escaped.contains('&')) {
        escaped = escaped.replace("&", "&amp;")
    }
    if (escaped.contains('<')) {
        escaped = escaped.replace("<", "&lt;")
    }
    if (escaped.contains('>')) {
        escaped = escaped.replace(">", "&gt;")
    }
    return escaped
}

fun String.jsonEscape(): String {
    var escaped = this
    if (escaped.contains('\\')) {
        escaped = escaped.replace("\\", "\\\\")
    }
    if (escaped.contains('/')) {
        escaped = escaped.replace("/", "\\/")
    }
    if (escaped.contains('"')) {
        escaped = escaped.replace("\"", "\\\"")
    }
    if (escaped.contains('\n')) {
        escaped = escaped.replace("\n", "\\n")
    }
    if (escaped.contains('\r')) {
        escaped = escaped.replace("\r", "\\r")
    }
    if (escaped.contains('\t')) {
        escaped = escaped.replace("\t", "\\t")
    }
    if (escaped.contains('\u000c')) {
        escaped = escaped.replace("\u000c", "\\f")
    }
    if (escaped.contains('\b')) {
        escaped = escaped.replace("\b", "\\b")
    }
    return escaped.encodeToByteArray().joinToString("") {
        val c = it.toInt().toChar()
        if (c.isISOControl()) {
            "\\u${it.toString(16).padStart(4, '0')}"
        } else c.toString()
    }
}
