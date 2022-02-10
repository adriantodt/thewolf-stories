package net.adriantodt.thewolf.stories

/**
 * MIME-Type Parser
 *
 *
 * This class provides basic functions for handling mime-types. It can handle
 * matching mime-types against a list of media-ranges. See section 14.1 of the
 * HTTP specification [RFC 2616] for a complete explanation.
 *
 *
 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.1
 *
 *
 * A port to Java of Joe Gregorio's MIME-Type Parser:
 *
 *
 * http://code.google.com/p/mimeparse/
 *
 *
 * Ported by Tom Zellman <tzellman></tzellman>@gmail.com>.
 */
object MIMEParse {
    /**
     * Returns the quality 'q' of a mime-type when compared against the
     * mediaRanges in ranges. For example:
     *
     * @param mimeType
     */
    fun quality(mimeType: String, ranges: String): Float {
        return FitnessAndQuality.parse(
            mimeType,
            ranges.split(',').map { ParseResults.fromMediaRange(it) }
        ).quality
    }

    /**
     * Takes a list of supported mime-types and finds the best match for all the
     * media-ranges listed in header. The value of header must be a string that
     * conforms to the format of the HTTP Accept: header. The value of
     * 'supported' is a list of mime-types.
     *
     *
     * MimeParse.bestMatch(Arrays.asList(new String[]{"application/xbel+xml", "text/xml"}),
     * "text/ *;q=0.5,*; q=0.1") 'text/xml'
     *
     * @param supported
     * @param header
     * @return
     */
    fun bestMatch(supported: Collection<String>, header: String): String {
        val parseResults = header.split(',').map { ParseResults.fromMediaRange(it) }
        val weightedMatches = supported.map { FitnessAndQuality.parse(it, parseResults) }.sorted()
        val lastOne = weightedMatches[weightedMatches.size - 1]
        return if (lastOne.quality.compareTo(0f) != 0) lastOne.mimeType else ""
    }

    /**
     * Parse results container
     */
    private class ParseResults(
        val type: String,
        val subType: String,
        // !a dictionary of all the parameters for the media range
        val params: MutableMap<String, String>
    ) {
        override fun toString() = buildString {
            append("('$type', '$subType', {")
            for (k in params.keys) {
                append("'").append(k).append("':'").append(params[k]).append("',")
            }
            append("})")
        }

        companion object {
            /**
             * Carves up a mime-type and returns a ParseResults object
             *
             *
             * For example, the media range 'application/xhtml;q=0.5' would get parsed into:
             *
             *
             * ('application', 'xhtml', {'q', '0.5'})
             */
            fun fromMimeType(mimeType: String): ParseResults {
                val parts = mimeType.split(";")
                val params = mutableMapOf<String, String>()

                for (i in 1 until parts.size) {
                    val p = parts[i]
                    val subParts = p.split('=')
                    if (subParts.size == 2) params[subParts[0].trim { it <= ' ' }] = subParts[1].trim { it <= ' ' }
                }

                // Java URLConnection class sends an Accept header that includes a
                // single "*" - Turn it into a legal wildcard.
                val (first, second) = parts[0].trim { it <= ' ' }.let { if (it == "*") "*/*" else it }.split("/")
                return ParseResults(first.trim { it <= ' ' }, second.trim { it <= ' ' }, params)
            }

            /**
             * Carves up a media range and returns a ParseResults.
             *
             *
             * For example, the media range 'application/ *;q=0.5' would get parsed into:
             *
             *
             * ('application', '*', {'q', '0.5'})
             *
             *
             * In addition this function also guarantees that there is a value for 'q'
             * in the params dictionary, filling it in with a proper default if
             * necessary.
             *
             * @param range
             */
            fun fromMediaRange(range: String): ParseResults {
                val results = fromMimeType(range)
                val q = results.params["q"]
                val f = q?.toFloatOrNull() ?: 1f
                if (q.isNullOrBlank() || f < 0 || f > 1) results.params["q"] = "1"
                return results
            }
        }
    }

    /**
     * Structure for holding a fitness/quality combo
     */
    private class FitnessAndQuality(
        val mimeType: String, val fitness: Int, val quality: Float
    ) : Comparable<FitnessAndQuality> {

        override fun compareTo(other: FitnessAndQuality): Int {
            return when {
                fitness == other.fitness -> when {
                    quality == other.quality -> 0
                    quality < other.quality -> -1
                    else -> 1
                }
                fitness < other.fitness -> -1
                else -> 1
            }
        }

        companion object {
            /**
             * Find the best match for a given mimeType against a list of media_ranges
             * that have already been parsed by MimeParse.parseMediaRange(). Returns a
             * tuple of the fitness value and the value of the 'q' quality parameter of
             * the best match, or (-1, 0) if no match was found. Just as for
             * quality_parsed(), 'parsed_ranges' must be a list of parsed media ranges.
             *
             * @param mimeType
             * @param parsedRanges
             */
            fun parse(mimeType: String, parsedRanges: List<ParseResults>): FitnessAndQuality {
                var bestFitness = -1
                var bestFitQ = 0f
                val target = ParseResults.fromMediaRange(mimeType)
                for (range in parsedRanges) {
                    if ((target.type == range.type || range.type == "*" || target.type == "*") && (target.subType == range.subType || range.subType == "*" || target.subType == "*")) {
                        for (k in target.params.keys) {
                            val fitness = if (range.type == target.type) 100 else 0 +
                                if (range.subType == target.subType) 10 else 0 +
                                    if (k != "q" && range.params.containsKey(k) && target.params[k] == range.params[k]) 1 else 0
                            if (fitness > bestFitness) {
                                bestFitness = fitness
                                bestFitQ = range.params["q"]?.toFloatOrNull() ?: 0f
                            }
                        }
                    }
                }
                return FitnessAndQuality(mimeType, bestFitness, bestFitQ)
            }
        }
    }
}
