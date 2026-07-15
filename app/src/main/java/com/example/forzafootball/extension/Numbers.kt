package com.example.forzafootball.extension

/** Formats large numbers compactly, e.g. 2_700_000 -> "2.7M". */
fun formatCompact(value: Long): String = when {
    value >= 1_000_000 -> {
        val millions = value / 100_000 / 10.0
        if (millions % 1.0 == 0.0) "${millions.toInt()}M" else "${millions}M"
    }

    value >= 1_000 -> {
        val thousands = value / 100 / 10.0
        if (thousands % 1.0 == 0.0) "${thousands.toInt()}K" else "${thousands}K"
    }

    else -> value.toString()
}
