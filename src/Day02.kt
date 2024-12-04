import kotlin.math.absoluteValue

const val DIFF_MIN = 1
const val DIFF_MAX = 3
const val LEVEL_LENGTH = 5

fun main() {
    fun part1(input: List<String>): Int {
        val levelsList = parseInput(input)
        val result = levelsList.count { levels ->
            val diffs = computeDiffs(levels)

            diffs.all { it in DIFF_MIN..DIFF_MAX }
                    || diffs.all { it < 0 && it.absoluteValue in DIFF_MIN..DIFF_MAX }
        }

        return result
    }

    fun part2BruteForce(input: List<String>): Int {
        val levelsList = parseInput(input)
        val result = levelsList.count { levels ->
            return@count (-1..< levels.size).any { index ->
                val diffs = if (index == -1) {
                    computeDiffs(levels)
                } else {
                    // Try removing one number
                    val newLevels = levels.toMutableList()
                    newLevels.removeAt(index)
                    computeDiffs(newLevels)
                }
                diffs.all { it in DIFF_MIN..DIFF_MAX }
                        || diffs.all { it < 0 && it.absoluteValue in DIFF_MIN..DIFF_MAX }
            }
        }

        return result
    }

    fun part2(input: List<String>): Int {
        val levelsList = parseInput(input)
        val result = levelsList.count { levels ->
            println("levels: $levels")
            val diffs = computeDiffs(levels)
            println("diffs: $diffs")

            // Check levels are all increasing/decreasing
            val countIncrease = diffs.count { it > 0 }
            val countDecrease = diffs.count { it < 0 }

            // If there is just one outlier, remove it
            if (setOf(countIncrease, countDecrease).any{ it == 1 }) {
                println("one outlier ($countIncrease, $countDecrease)")
                val index = if (countIncrease == 1) {
                    diffs.indexOfFirst { it > 0 }
                } else {
                    diffs.indexOfFirst { it < 0 }
                }
                println("find the index that increases/decreases, index:  $index")
                return@count setOf(index, index +1).any { idx ->
                    val newLevels = levels.toMutableList()
                    newLevels.removeAt(idx)
                    val newDiffs = computeDiffs(newLevels)
                    println("after removing outlier at idx: $idx, levels: $newLevels, diffs: $newDiffs")
                    newDiffs.all { it.absoluteValue in DIFF_MIN..DIFF_MAX }
                }
            }

            // If there are more than one outlier, even removing one is not possible
            if (!setOf(countIncrease, countDecrease).any{ it == 0 }) {
                println("more than one outlier ($countIncrease, $countDecrease), skipped")
                return@count false
            }

            // Try to find the first number against the range rule, and try to remove it
            val index = diffs.indexOfFirst { it.absoluteValue !in DIFF_MIN..DIFF_MAX }
            println("no outlier, find the index that violates range rule, index:  $index")
            if (index == -1) return@count true

            return@count setOf(index, index +1).any { idx ->
                val newLevels = levels.toMutableList()
                newLevels.removeAt(idx)
                val newDiffs = computeDiffs(newLevels)
                println("after removing outlier at idx: $idx, levels: $newLevels, diffs: $newDiffs")
                newDiffs.all { it.absoluteValue in DIFF_MIN..DIFF_MAX }
            }
        }
        println("result $result")
        return result
    }

    // Or read a large test input from the `src/Day01.txt.txt` file:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2BruteForce(testInput) == 4)
//    check(part2(testInput) == 4)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2BruteForce(input).println()
//    part2(input).println()
}

private fun parseInput(input: List<String>): List<List<Int>> =
    input.map { line -> line.split(" ").map { it.toInt() } }

private fun computeDiffs(levels: List<Int>): List<Int> =
    levels.mapIndexedNotNull { index, level -> if (index == 0) null else level - levels[index - 1] }
