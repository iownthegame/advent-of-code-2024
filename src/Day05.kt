fun main() {
    fun part1(input: List<String>): Int {
        val (pageOrderRules, updates) = parseInput(input)
//        println("rules = $pageOrderRules: ")
//        println("updates = $updates")
        val orderedUpdates = updates.filter { update -> arePagesOrdered(update, pageOrderRules) }
//        println("orderedUpdates :$orderedUpdates")
        return orderedUpdates.sumOf { getMiddleNumber(it) }
    }

    fun part2(input: List<String>): Int {
        val (pageOrderRules, updates) = parseInput(input)
        val unorderedUpdates = updates.filter { update -> !arePagesOrdered(update, pageOrderRules) }
        val orderedUpdates = mutableListOf<List<Int>>()

        unorderedUpdates.forEach { update ->
            val n = update.size
            val attemptUpdate = update.toMutableList()
            do {
                var isSwapped = false
                for (i in 0 until n-1) {
                    if (isSwapped) { break }

                    val page = attemptUpdate[i]
                    for (j in i+1 until n) {
                        val anotherPage = attemptUpdate[j]
                        val anotherPageRules = pageOrderRules[anotherPage] ?: emptySet()
                        if (page in anotherPageRules) {
                            // page shouldn't be after anotherPage, swap them
                            attemptUpdate[i] = anotherPage
                            attemptUpdate[j] = page
                            isSwapped = true
                            break
                        }
                    }
                }
            } while (!arePagesOrdered(attemptUpdate, pageOrderRules))
            orderedUpdates.add(attemptUpdate)
        }

//        println("unorderedUpdates :$unorderedUpdates")
//        println("orderedUpdates :$orderedUpdates")
        return orderedUpdates.sumOf { getMiddleNumber(it) }
    }

    // Or read a large test input from the `src/Day01.txt.txt` file:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

private fun parseInput(input: List<String>): Pair<Map<Int, Set<Int>>, List<List<Int>>> {
    val pageOrderRules = mutableMapOf<Int, MutableSet<Int>>()
    val updates = mutableListOf<List<Int>>()
    input.forEach { line ->
        if (line.contains("|")) {
            val (key, value) = line.split("|").map(String::toInt)
            if (key !in pageOrderRules.keys) {
                pageOrderRules[key] = mutableSetOf(value)
            } else {
                pageOrderRules[key]!!.add(value)
            }
        } else if (line.isEmpty()) {
            return@forEach
        } else {
            updates.add(line.split(",").map(String::toInt))
        }
    }
    return Pair(pageOrderRules, updates)
}

private fun getMiddleNumber(list: List<Int>): Int {
    return list[(list.size - 1) / 2]
}

private fun arePagesOrdered(update: List<Int>, pageOrderRules: Map<Int, Set<Int>>): Boolean {
    val n = update.size
    for (i in 0 until n-1) {
        val page = update[i]
        for (j in i+1 until n) {
            val anotherPage = update[j]
            val anotherPageRules = pageOrderRules[anotherPage] ?: emptySet()
            if (page in anotherPageRules) {
                // page shouldn't be after anotherPage
                return false
            }
        }
    }
    return true
}