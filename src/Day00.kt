fun main() {
    fun part1(input: List<String>): Int {
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // Or read a large test input from the `src/Day01.txt.txt` file:
    val testInput = readInput("Day00_test")
    check(part1(testInput) == 0)
    check(part2(testInput) == 0)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day00")
    part1(input).println()
    part2(input).println()
}