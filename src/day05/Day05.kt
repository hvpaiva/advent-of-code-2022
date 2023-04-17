package day05

import DayPuzzle

fun main() {
    DayPuzzle<String>("05")
        .withParts(Part1, Part2)
        .solve()
}

data object Part1 : DayPuzzle.PartPuzzle<String>("Part 1", "CMZ") {
    override fun solve(input: List<String>) = solve(CrateMover.CM9000, input)
}

data object Part2 : DayPuzzle.PartPuzzle<String>("Part 2", "MCD") {
    override fun solve(input: List<String>) = solve(CrateMover.CM9001, input)
}

fun solve(mover: CrateMover, input: List<String>): String {
    val (moves, cargoMap) = toMovementsAndCargoMap(input)

    val cargoStore = createCargoStore(cargoMap, mover)
    val movements = moves.map { movement(it) }
    with(cargoStore) {
        movements
            .forEach { (quantity, origin, destination) ->
                // Bonus DSL style call, it could be replaced just by:
                // move(quantity, origin, destination) in CargoStore
                move(quantity) from origin to destination
            }
    }

    return cargoStore.lasts()
}

fun toMovementsAndCargoMap(input: List<String>): Pair<List<String>, List<String>> {
    return input
        .filter { it.isNotEmpty() }
        .partition { "move" in it }
}

enum class CrateMover {
    CM9000, CM9001
}

data class CargoStore(val stacks: List<ArrayDeque<Crate>>, val mover: CrateMover) {
    private fun takeLasts(): List<Crate> {
        return stacks.map { it.last() }
    }

    fun lasts(): String {
        return takeLasts().joinToString("") { it.name }
    }

    fun move(quantity: Int, from: Int, to: Int) {
        return when (mover) {
            CrateMover.CM9000 -> moveAtOnce(quantity, from, to)
            CrateMover.CM9001 -> moveMultiple(quantity, from, to)
        }
    }

    private fun moveAtOnce(quantity: Int, from: Int, to: Int) {
        val fromStack = stacks[from - 1]
        val toStack = stacks[to - 1]

        repeat(quantity) {
            val crate = fromStack.removeLast()

            toStack.addLast(crate)
        }
    }

    private fun moveMultiple(quantity: Int, from: Int, to: Int) {
        val fromStack = stacks[from - 1]
        val toStack = stacks[to - 1]

        val crates = fromStack.takeLast(quantity)
        repeat(quantity) { fromStack.removeLast() }

        toStack.addAll(crates)
    }

    override fun toString(): String {
        val maxRows = stacks.maxOf { it.size }
        val sb = StringBuilder()

        for (row in maxRows - 1 downTo 0) {
            for (stack in stacks.indices) {
                val crate = if (row < stacks[stack].size) {
                    stacks[stack][row].toString()
                } else {
                    "   "
                }
                sb.append(crate.padEnd(4))
            }
            sb.append("\n")
        }

        for (stackNumber in 1..stacks.size) {
            sb.append(stackNumber.toString().padStart(2).padEnd(4))
        }

        return sb.toString()
    }
}

@JvmInline
value class Crate(val name: String) {
    override fun toString(): String = "[$name]"
}

data class Movement(val quantity: Int, var from: Int, var to: Int)

fun movement(move: String): Movement {
    val (quantity, from, to) = Regex("""move (\d+) from (\d+) to (\d+)""")
        .find(move)!!
        .destructured

    return Movement(quantity.toInt(), from.toInt(), to.toInt())
}

fun createCargoStore(lines: List<String>, mover: CrateMover): CargoStore {
    val stackMap = mutableMapOf<Int, ArrayDeque<Crate>>()

    val crateRegex = Regex("""\[(\w)]""")

    for (line in lines) {
        val crates = crateRegex.findAll(line)

        for (match in crates) {
            val crateName = match.groupValues[1]
            val stackNumber = match.range.first / 3
            val crate = Crate(crateName)

            stackMap.computeIfAbsent(stackNumber) { ArrayDeque() }.addFirst(crate)
        }
    }

    return CargoStore(stackMap.toSortedMap().values.toList(), mover)
}

/**
 * Bonus.
 *
 * DSL style call for `move` function in [CargoStore]. Only a syntactic sugar for been able to write
 * similar to the puzzle input.
 *
 * It can be used as:
 * `move(2) from 2 to 1`
 *
 * While the puzzle input is:
 * `move 2 from 2 to 1`
 *
 * The `move` function is defined in [CargoStore] class.
 */
context(CargoStore)
@Suppress("ClassName", "kotlin:S101")
class move(private val n: Int) {
    private var origin: Int? = null
    infix fun from(origin: Int): move {
        this.origin = origin
        return this
    }

    infix fun to(destination: Int): move {
        move(n, origin!!, destination)
        return this
    }
}