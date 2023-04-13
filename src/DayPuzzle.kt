class DayPuzzle<T>(
    private val day: String,
    private val parts: MutableList<PartPuzzle<T>> = mutableListOf()
) {
    private fun addPart(part: PartPuzzle<T>) {
        parts.add(part)
    }

    fun withParts(vararg parts: PartPuzzle<T>): DayPuzzle<T> {
        return apply { parts.forEach { addPart(it) } }
    }

    abstract class PartPuzzle<T>(val name: String, val valueTestExpected: T) {
        abstract fun solve(input: List<String>): T
    }

    fun solve(onlyTests: Boolean = false, vararg partNames: String) {
        val testInput = readInput("day$day", "Day${day}_test")
        val input = readInput("day$day", "Day$day")

        for (part in parts.filter { partNames.isEmpty() || it.name in partNames }) {
            validate("Test ${part.name}:", part.valueTestExpected, part.solve(testInput))

            if (!onlyTests) {
                println("${part.name}: ${part.solve(input)}")
            }
        }
    }
}
