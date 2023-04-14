package day02

import DayPuzzle

fun main() {
    DayPuzzle<Int>("02")
        .withParts(Part1, Part2)
        .solve()
}

data object Part1 : DayPuzzle.PartPuzzle<Int>("Part 1", 15) {
    override fun solve(input: List<String>): Int {
        return input
            .map { line ->
                val (other, self) = line.split(" ")
                Match(HandShape.guessFromSelf(self), HandShape.fromString(other))
            }
            .score()
    }
}

data object Part2 : DayPuzzle.PartPuzzle<Int>("Part 2", 12) {
    override fun solve(input: List<String>): Int {
        return input
            .map { line ->
                val (opponent, outcome) = line.split(" ")
                Pair(HandShape.fromString(opponent), Outcome.fromString(outcome))
            }
            .map { (opponent, outcome) -> Match(chooseHandShape(opponent, outcome), opponent) }
            .score()
    }
}

typealias Point = Int

enum class HandShape {
    Rock, Paper, Scissors;

    val point: Point
        get() = when (this) {
            Rock -> 1
            Paper -> 2
            Scissors -> 3
        }

    companion object {
        fun fromString(value: String): HandShape {
            return when (value) {
                "A" -> Rock
                "B" -> Paper
                "C" -> Scissors
                else -> throw IllegalArgumentException("Unknown value: $value")
            }
        }
    }
}

data class Match(val selfHand: HandShape, val otherHand: HandShape)

fun round(match: Match): Outcome {
    return when (match.selfHand) {
        HandShape.Rock -> when (match.otherHand) {
            HandShape.Rock -> Outcome.Draw
            HandShape.Paper -> Outcome.Loss
            HandShape.Scissors -> Outcome.Win
        }

        HandShape.Paper -> when (match.otherHand) {
            HandShape.Rock -> Outcome.Win
            HandShape.Paper -> Outcome.Draw
            HandShape.Scissors -> Outcome.Loss
        }

        HandShape.Scissors -> when (match.otherHand) {
            HandShape.Rock -> Outcome.Loss
            HandShape.Paper -> Outcome.Win
            HandShape.Scissors -> Outcome.Draw
        }
    }
}

fun chooseHandShape(opponentHand: HandShape, outcome: Outcome): HandShape {
    return when (outcome) {
        Outcome.Win -> when (opponentHand) {
            HandShape.Rock -> HandShape.Paper
            HandShape.Paper -> HandShape.Scissors
            HandShape.Scissors -> HandShape.Rock
        }

        Outcome.Draw -> opponentHand

        Outcome.Loss -> when (opponentHand) {
            HandShape.Rock -> HandShape.Scissors
            HandShape.Paper -> HandShape.Rock
            HandShape.Scissors -> HandShape.Paper
        }
    }
}

fun List<Match>.score(): Point {
    return this.sumOf { round(it).point + it.selfHand.point }
}

enum class Outcome {
    Win, Draw, Loss;

    val point: Point
        get() = when (this) {
            Win -> 6
            Draw -> 3
            Loss -> 0
        }

    companion object {
        fun fromString(value: String): Outcome {
            return when (value) {
                "X" -> Loss
                "Y" -> Draw
                "Z" -> Win
                else -> throw IllegalArgumentException("Unknown value: $value")
            }
        }
    }
}

fun HandShape.Companion.guessFromSelf(value: String): HandShape {
    return when (value) {
        "X" -> HandShape.Rock
        "Y" -> HandShape.Paper
        "Z" -> HandShape.Scissors
        else -> throw IllegalArgumentException("Unknown value: $value")
    }
}