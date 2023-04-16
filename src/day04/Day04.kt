package day04

import DayPuzzle

fun main() {
    DayPuzzle<Int>("04")
        .withParts(Part1, Part2)
        .solve()
}

data object Part1 : DayPuzzle.PartPuzzle<Int>("Part 1", 2) {
    override fun solve(input: List<String>): Int {
        return input
            .map(::pairs)
            .map { assignmentFrom(it.first) to assignmentFrom(it.second) }
            .count { it.isFullyOverlapping() }


    }
}

data object Part2 : DayPuzzle.PartPuzzle<Int>("Part 2", 4) {
    override fun solve(input: List<String>): Int {
        return input
            .map(::pairs)
            .map { assignmentFrom(it.first) to assignmentFrom(it.second) }
            .count { it.isOverlapping() }


    }
}

data class Assignment(val sectors: IntRange)

fun assignmentFrom(range: String): Assignment {
    return range
        .split("-")
        .map { it.toInt() }
        .let { Assignment(it[0]..it[1]) }
}

fun pairs(assignmentPairs: String): Pair<String, String> {
    return assignmentPairs
        .split(",")
        .let { it[0] to it[1] }
}

fun Pair<Assignment, Assignment>.isFullyOverlapping(): Boolean {
    return this.first.sectors.containsFully(this.second.sectors) ||
            this.second.sectors.containsFully(this.first.sectors)
}

fun Pair<Assignment, Assignment>.isOverlapping(): Boolean {
    return this.first.sectors.contains(this.second.sectors) ||
            this.second.sectors.contains(this.first.sectors)
}

fun IntRange.containsFully(other: IntRange): Boolean {
    return this.first in other && this.last in other
}

fun IntRange.contains(other: IntRange): Boolean {
    return this.first in other || this.last in other
}