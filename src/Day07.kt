data class Equation(val answer: Long, val numbers: List<Long>)
enum class OperationType {
    ADD,
    MULTIPLY,
    CONCATENATION,
}

fun main() {
    fun part1(input: List<String>): Long {
        val equations = parseInput(input)
        return equations.filter {
            canBeMade(
                equation = it,
                operationTypes = setOf(OperationType.ADD, OperationType.MULTIPLY)
            )
        }.sumOf { it.answer }
    }

    fun part2(input: List<String>): Long {
        val equations = parseInput(input)
        return equations.filter {
            canBeMade(
                equation = it,
                operationTypes = setOf(OperationType.ADD, OperationType.MULTIPLY, OperationType.CONCATENATION)
            )
        }.sumOf { it.answer }
    }

    // Or read a large test input from the `src/Day01.txt.txt` file:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749.toLong())
    check(part2(testInput) == 11387.toLong())

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

private fun parseInput(input: List<String>): List<Equation> {
    return input.map { line ->
        val (answerString, numbersString) = line.split(": ")
        Equation(answerString.toLong(), numbersString.split(" ").map { it.toLong() })
    }
}

private fun canBeMade(equation: Equation, operationTypes: Set<OperationType>): Boolean {
    val answer = equation.answer
    val numbers = equation.numbers
    val operationsCount = numbers.size - 1

    // Permutation "+" and "-"
    var generationFromOperationsList: List<List<OperationType>> = emptyList()
    while (true) {
        val operationsList = generateOperations(generationFromOperationsList, operationsCount, operationTypes)
        if (operationsList.isEmpty()) return false

        operationsList.forEach { operations ->
            if (operations.size < numbers.size - 1) return@forEach

            if (compute(numbers, operations) == answer) {
                return true
            }
        }
        generationFromOperationsList = operationsList
    }
}

private fun generateOperations(
    operationsList: List<List<OperationType>>,
    count: Int,
    operationTypes: Set<OperationType>
): List<List<OperationType>> {
    if (operationsList.isEmpty()) return operationTypes.map { listOf(it) }

    return operationsList.flatMap { operations ->
        if (operations.size < count) {
            operationTypes.flatMap {
                listOf(operations + it)
            }
        } else {
            emptyList()
        }
    }
}

private fun compute(numbers: List<Long>, operations: List<OperationType>): Long {
//    println("compute $numbers, $operations")
    var sum = numbers.first()
    numbers.forEachIndexed { index, number ->
        if (index == 0) { return@forEachIndexed }
        sum = when (operations[index - 1]) {
            OperationType.ADD -> sum + number
            OperationType.MULTIPLY -> sum * number
            OperationType.CONCATENATION -> "$sum$number".toLong()
        }
    }
//    println("compute $numbers, $operations = $sum")
    return sum
}