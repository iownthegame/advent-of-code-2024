import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        val (leftNums, rightNums) = parseInput(input)

        val result = leftNums.sorted().zip(rightNums.sorted()).sumOf {
            (leftNum, rightNum) -> (leftNum - rightNum).absoluteValue
        }
        return result
    }

    fun part2(input: List<String>): Int {
        val (leftNums, rightNums) = parseInput(input)
        val rightNumsFrequency = rightNums.groupingBy { it }.eachCount()
        val result = leftNums.sumOf { leftNum -> leftNum * (rightNumsFrequency[leftNum] ?: 0) }
        return result
    }

    // Or read a large test input from the `src/Day01.txt.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

private fun parseInput(input: List<String>): Pair<List<Int>, List<Int>> {
    val leftNums = mutableListOf<Int>()
    val rightNums = mutableListOf<Int>()

    input.forEach { line ->
        val (left, right) = line.split("   ")
        leftNums.add(left.toInt())
        rightNums.add(right.toInt())
    }
    return Pair(leftNums, rightNums)
}
