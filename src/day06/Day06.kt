package day06

import DayPuzzle

fun main() {
    DayPuzzle<Int>("06")
        .withParts(Part1, Part2)
        .solve()
}

data object Part1 : DayPuzzle.PartPuzzle<Int>("Part 1", 7) {
    override fun solve(input: List<String>): Int {
        return input.first().endOfMarkerPosition(4)
    }
}

data object Part2 : DayPuzzle.PartPuzzle<Int>("Part 2", 19) {
    override fun solve(input: List<String>): Int {
        return input.first().endOfMarkerPosition(14)
    }
}

fun String.endOfMarkerPosition(distinctCharacters: Int): Int {
    return this
        .asIterable()
        .windowed(distinctCharacters)
        .mapIndexed { index, values -> index to values }
        .first { (_, values) -> values.distinct().size == values.size }
        .first + distinctCharacters
}