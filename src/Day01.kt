import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        val leftNums = mutableListOf<Int>()
        val rightNums = mutableListOf<Int>()

        input.forEach { line ->
            val (left, right) = line.split("   ")
            leftNums.add(left.toInt())
            rightNums.add(right.toInt())
        }
        leftNums.sort()
        rightNums.sort()

        val result = leftNums.mapIndexed { index, leftNum ->
            (leftNum - rightNums[index]).absoluteValue
        }.sum()

        return result
    }

    fun part2(input: List<String>): Int {
        val leftNums = mutableListOf<Int>()
        val rightNums = mutableListOf<Int>()

        input.forEach { line ->
            val (left, right) = line.split("   ")
            leftNums.add(left.toInt())
            rightNums.add(right.toInt())
        }
        val rightNumsMap = rightNums.groupingBy { it }.eachCount()
        val result = leftNums.map {
            leftNum -> leftNum * (rightNumsMap[leftNum] ?: 0)
        }.sum()

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
