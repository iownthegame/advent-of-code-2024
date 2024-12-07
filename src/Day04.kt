import kotlin.math.absoluteValue

val DIRECTIONS = listOf(
    Pair(-1, 0),
    Pair(1, 0),
    Pair(0, 1),
    Pair(0, -1),
    Pair(1, -1),
    Pair(-1, -1),
    Pair(-1, 1),
    Pair(1, 1),
)
val DIAGONALS = listOf(
    Pair(-1, -1),
    Pair(-1, 1),
)

val LETTERS = arrayOf("X", "M", "A", "S")

fun main() {
    fun part1(input: List<String>): Int {
        val grid = parseInput(input)
        val m = grid.size
        val n = grid.first().size

        var result = 0
        for (i in 0 until m) {
            for (j in 0 until n) {
                if (grid[i][j] == LETTERS[0]) {
                    result += findXmas(grid, i, j, m, n)
                }
            }
        }
        return result
    }

    fun part2(input: List<String>): Int {
        val grid = parseInput(input)
        val m = grid.size
        val n = grid.first().size

        var result = 0
        for (i in 0 until m) {
            for (j in 0 until n) {
                // Get the center of the X-MAS
                if (grid[i][j] == "A") {
                    result += if (hasMasWithinX(grid, i, j, m, n)) 1 else 0
                }
            }
        }
        return result
    }

    // Or read a large test input from the `src/Day01.txt.txt` file:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

private fun parseInput(input: List<String>): List<List<String>>
        = input.map { line -> line.map { it.toString() }
}


private fun findXmas(grid: List<List<String>>, i: Int, j: Int, m: Int, n: Int): Int {
    return DIRECTIONS.count { (dx, dy) ->
        LETTERS.forEachIndexed { idx, letter ->
            val x = i + dx * idx
            val y = j + dy * idx
            if (x in 0..<m && y in 0..<n && grid[x][y] == letter) {
                // continue
                Unit
            }
            else {
                return@count false
            }
        }
        true
    }
}

private fun hasMasWithinX(grid: List<List<String>>, i: Int, j: Int, m: Int, n: Int): Boolean {
    return DIAGONALS.all {  (dx, dy) ->
        val x = i + dx
        val y = j + dy
        val x2 = i - dx
        val y2 = j - dy
        if (x in 0..<m && y in 0..<n && x2 in 0..<m && y2 in 0..<n) {
            setOf(grid[x][y], grid[x2][y2]) == setOf("M", "S")
        } else {
            return@all false
        }
    }
}