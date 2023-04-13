package day01

import DayPuzzle

fun main() {
    DayPuzzle<Int>("01")
        .withParts(Part1)
        .solve()
}


object Part1 : DayPuzzle.PartPuzzle<Int>("Part 1", 5) {
    override fun solve(input: List<String>): Int {
        return input.size
    }
}