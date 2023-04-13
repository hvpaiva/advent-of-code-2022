import java.io.File

/**
 * Reads lines from the given input txt file.
 */
fun readInput(folder: String = "", name: String) = File("src", "$folder/$name.txt")
    .readLines()


/**
 * Validates the given value against the expected value.
 */
fun <T> validate(name: String, expected: T?, value: T?) {
    if (expected == value)
        println("$name $value OK")
    else
        println("$name Failed. Expecting $expected but was $value")
}
