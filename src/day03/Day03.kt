package day03

import DayPuzzle

fun main() {
    DayPuzzle<Int>("03")
        .withParts(Part1, Part2)
        .solve()
}

data object Part1 : DayPuzzle.PartPuzzle<Int>("Part 1", 157) {
    override fun solve(input: List<String>): Int {
        return input
            .map { divideInHalf(it) }
            .map { duplicatedStrings(it.first, it.second) }
            .sumOf { priority(it) }
    }
}

data object Part2 : DayPuzzle.PartPuzzle<Int>("Part 2", 70) {
    override fun solve(input: List<String>): Int {
        return input
            .windowed(3, 3)
            .map { discoverBadge(it) }
            .sumOf { priority(it) }
    }
}

fun priority(item: Char): Int {
    val aToZ = ('a'..'z') + ('A'..'Z')
    if (item in aToZ) {
        return aToZ.indexOf(item) + 1
    }

    return 0
}

fun priority(item: String): Int {
    return item.sumOf { priority(it) }
}

fun duplicatedStrings(first: String, second: String): String {
    val firstChars = first.toCharArray().toMutableList()
    val secondChars = second.toCharArray().toMutableList()

    val result = mutableSetOf<Char>()

    for (char in firstChars) {
        if (char in secondChars) {
            result.add(char)
        }
    }

    return result.joinToString("")
}

fun discoverBadge(rucksacks: List<String>): String {
    val biggest = rucksacks.find { rucksack -> rucksack.length == rucksacks.maxOf { it.length } } ?: ""
    val result = mutableSetOf<Char>()

    biggest
        .toCharArray()
        .forEach { char ->
            if (rucksacks.all { it.contains(char) }) {
                result.add(char)
            }
        }

    return result.joinToString("")
}

fun divideInHalf(input: String): Pair<String, String> {
    val half = input.length / 2
    return Pair(input.substring(0, half), input.substring(half))
}
