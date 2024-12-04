import kotlin.math.absoluteValue

// Capture grouping pattern (?<theName>PATTERN)“
val regex = """mul\((?<a>\d+),(?<b>\d+)\)""".toRegex()
val regex2 = """mul\((?<a>\d+),(?<b>\d+)\)|don't\(\)|do\(\)""".toRegex()

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val matchResults = regex.findAll(line)
            matchResults.sumOf {
                val (a, b) = it.destructured
                a.toInt() * b.toInt()
            }
        }
    }

    fun part2(input: List<String>): Int {
        var isEnabled = true
        return input.sumOf { line ->
            var lineSum = 0
            var matchResult = regex2.find(line)
            while (matchResult != null) {
                val value = matchResult.value
                if (value.startsWith("mul")) {
                    if (isEnabled) {
                        val (a, b) = matchResult.destructured
                        lineSum += a.toInt() * b.toInt()
                    }
                } else if (value.startsWith("don")) {
                    isEnabled = false
                } else if (value.startsWith("do")) {
                    isEnabled = true
                }
                matchResult = matchResult.next()
            }
            lineSum
        }
    }

    // Or read a large test input from the `src/Day01.txt.txt` file:
    check(part1(listOf("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")) == 161)
    check(part2(listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")) == 48)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
