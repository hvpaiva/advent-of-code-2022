package day02

import DayPuzzle
import day02.HandShape.*

fun main() {
    DayPuzzle<Int>("02")
        .withParts(Part1, Part2)
        .solve()
}

data object Part1 : DayPuzzle.PartPuzzle<Int>("Part 1", 15) {
    override fun solve(input: List<String>): Int {
        return input
            .map { splitFromSpace(it) }
            .map { Match(HandShape.guessFromSelf(it.second), HandShape.fromString(it.first)) }
            .score()
    }
}

data object Part2 : DayPuzzle.PartPuzzle<Int>("Part 2", 12) {
    override fun solve(input: List<String>): Int {
        return input
            .map { splitFromSpace(it) }
            .map { Pair(HandShape.fromString(it.first), Outcome.fromString(it.second)) }
            .map { (opponent, outcome) -> Match(chooseHandShape(opponent, outcome), opponent) }
            .score()
    }
}

typealias Point = Int

enum class HandShape(val point: Point) {
    Rock(1), Paper(2), Scissors(3);

    companion object {
        fun fromString(value: String): HandShape {
            return when (value) {
                "A" -> Rock
                "B" -> Paper
                "C" -> Scissors
                else -> error("Unknown value: $value")
            }
        }

        fun guessFromSelf(value: String): HandShape {
            return when (value) {
                "X" -> Rock
                "Y" -> Paper
                "Z" -> Scissors
                else -> error("Unknown value: $value")
            }
        }
    }
}

data class Match(val selfHand: HandShape, val otherHand: HandShape)

fun round(match: Match): Outcome {
    return when (match.selfHand to match.otherHand) {
        Rock to Scissors -> Outcome.Win
        Rock to Paper -> Outcome.Loss
        Rock to Rock -> Outcome.Draw

        Paper to Rock -> Outcome.Win
        Paper to Scissors -> Outcome.Loss
        Paper to Paper -> Outcome.Draw

        Scissors to Paper -> Outcome.Win
        Scissors to Rock -> Outcome.Loss
        Scissors to Scissors -> Outcome.Draw
        else -> error("Unknown match: $match")
    }
}

fun chooseHandShape(opponentHand: HandShape, outcome: Outcome): HandShape {
    return when (outcome) {
        Outcome.Win -> when (opponentHand) {
            Rock -> Paper
            Paper -> Scissors
            Scissors -> Rock
        }

        Outcome.Draw -> opponentHand

        Outcome.Loss -> when (opponentHand) {
            Rock -> Scissors
            Paper -> Rock
            Scissors -> Paper
        }
    }
}

fun List<Match>.score(): Point {
    return this.sumOf { round(it).point + it.selfHand.point }
}

enum class Outcome(val point: Point) {
    Win(6), Draw(3), Loss(0);

    companion object {
        fun fromString(value: String): Outcome {
            return when (value) {
                "X" -> Loss
                "Y" -> Draw
                "Z" -> Win
                else -> error("Unknown value: $value")
            }
        }
    }
}

fun splitFromSpace(string: String): Pair<String, String> {
    val (first, second) = string.split(" ")
    return Pair(first, second)
}