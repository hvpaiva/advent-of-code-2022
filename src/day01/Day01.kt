package day01

import DayPuzzle

fun main() {
    DayPuzzle<Int>("01")
        .withParts(Part1, Part2)
        .solve()
}

data object Part1 : DayPuzzle.PartPuzzle<Int>("Part 1", 24000) {
    override fun solve(input: List<String>): Int {
        return groupBetweenEmpty(input).topNElves(1)
    }
}

data object Part2 : DayPuzzle.PartPuzzle<Int>("Part 2", 45000) {
    override fun solve(input: List<String>): Int {
        return groupBetweenEmpty(input).topNElves(3)
    }
}

fun groupBetweenEmpty(input: List<String>): List<List<Int>> {
    val outputList = mutableListOf<MutableList<Int>>()
    var tempList = mutableListOf<Int>()

    for (value in input) {
        if (value.isNotEmpty()) {
            tempList.add(value.toInt())
        } else {
            outputList.add(tempList)
            tempList = mutableListOf()
        }
    }
    outputList.add(tempList)

    return outputList
}

fun List<List<Int>>.topNElves(n: Int): Int {
    return this
        .map { it.sum() }
        .sortedDescending()
        .take(n)
        .sum()
}