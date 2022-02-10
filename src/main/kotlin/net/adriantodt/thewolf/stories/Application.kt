package net.adriantodt.thewolf.stories

import io.micronaut.runtime.Micronaut.*

fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("net.adriantodt.thewolf.stories")
		.start()
}

