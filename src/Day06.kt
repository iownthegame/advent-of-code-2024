enum class PointType(val value: String) {
    GUARD_UP("^"),
    GUARD_DOWN("v"),
    GUARD_LEFT("<"),
    GUARD_RIGHT(">"),
    OBSTACLE("#"),
    OBSTACLE_LOOP("O"),
    NONE("."),;

    companion object {
        fun fromValue(value: String): PointType {
            return entries.find { it.value == value } ?: throw IllegalArgumentException("Unknown Point Type: $value")
        }
    }
}

data class Point(val x: Int, val y: Int, val value: String) {
    val type: PointType = PointType.fromValue(value)
    val axis: Pair<Int, Int> = Pair(x, y)
}

class GuardMap(private val originalGrid: List<List<String>>) {
    private var grid = originalGrid.map { it.toMutableList() }.toMutableList()
    val m = grid.size
    val n = grid.first().size
    var guardPoint = findGuard()
    val paths = mutableSetOf(guardPoint.axis)
    val pathPoints = mutableSetOf(guardPoint)

    fun reset() {
        grid = originalGrid.map { it.toMutableList() }.toMutableList()
        guardPoint = findGuard()
        paths.clear()
        paths.add(guardPoint.axis)
        pathPoints.clear()
        pathPoints.add(guardPoint)
    }

    fun setObstacleLoop(x: Int, y: Int) {
        grid[x][y] = PointType.OBSTACLE_LOOP.value
    }

    private fun findGuard(): Point {
        for (i in 0 until m) {
            for (j in 0 until n) {
                if (PointType.fromValue(grid[i][j]) == PointType.GUARD_UP) {
                    return Point(i, j, grid[i][j])
                }
            }
        }
        throw RuntimeException("Guard point not found")
    }

    fun findPointInFrontGuard(): Point? {
        val direction = when(guardPoint.type) {
            PointType.GUARD_UP -> Pair(-1, 0)
            PointType.GUARD_RIGHT -> Pair(0, 1)
            PointType.GUARD_DOWN -> Pair(1, 0)
            PointType.GUARD_LEFT -> Pair(0, -1)
            else -> throw IllegalStateException("Unsupported guard type: ${guardPoint.type}")
        }
        val newX = guardPoint.x + direction.first
        val newY = guardPoint.y + direction.second
        if (newX in 0..< m && newY in 0..< n) {
            return Point(newX, newY, grid[newX][newY])
        }
        return null
    }

    fun moveGuardOneStepFront() {
        val newGuard = findPointInFrontGuard() ?: throw IllegalStateException("Guard point not found")
        val guardType = guardPoint.type
        grid[guardPoint.x][guardPoint.y] = PointType.NONE.value
        grid[newGuard.x][newGuard.y] = guardType.value
        guardPoint = Point(newGuard.x, newGuard.y, guardType.value)
        paths.add(guardPoint.axis)
        if (guardPoint in pathPoints) throw Exception("Loop Detected $guardPoint")
        pathPoints.add(guardPoint)
    }

    fun turnGuard() {
        val newGuardType = when(guardPoint.type) {
            PointType.GUARD_UP -> PointType.GUARD_RIGHT
            PointType.GUARD_RIGHT -> PointType.GUARD_DOWN
            PointType.GUARD_DOWN -> PointType.GUARD_LEFT
            PointType.GUARD_LEFT -> PointType.GUARD_UP
            else -> throw IllegalStateException("Unsupported guard type: ${guardPoint.type}")
        }
        grid[guardPoint.x][guardPoint.y] = newGuardType.value
        guardPoint = Point(guardPoint.x, guardPoint.y, newGuardType.value)
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val grid = parseInput(input)
        val guardMap = GuardMap(grid)
        traverse(guardMap)
        return guardMap.paths.size
    }

    fun part2(input: List<String>): Int {
        val grid = parseInput(input)
        val guardMap = GuardMap(grid)
        val obstacles = mutableListOf<Pair<Int, Int>>()

        for(i in 0..< guardMap.m) {
            for(j in 0..< guardMap.n) {
                if (PointType.fromValue(grid[i][j]) == PointType.NONE) {
                    // Try to put an obstacle
                    guardMap.reset()
                    guardMap.setObstacleLoop(i, j)

                    try {
                        traverse(guardMap)
                    } catch (e: Exception) {
                        // Loop detected
                        obstacles.add(i to j)
                    }
                }
            }
        }

        return obstacles.size
    }

    // Or read a large test input from the `src/Day01.txt.txt` file:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

private fun parseInput(input: List<String>): List<List<String>>
        = input.map { line -> line.map { it.toString() } }

private fun traverse(guardMap: GuardMap) {
    while (true) {
        val pointFrontGuard = guardMap.findPointInFrontGuard() ?: break
//        println("front guard: ${pointFrontGuard}")
        when(pointFrontGuard.type) {
            PointType.OBSTACLE,
            PointType.OBSTACLE_LOOP-> {
                guardMap.turnGuard()
//                println("after turn guard: ${guardMap.guardPoint}")
            }
            PointType.NONE -> {
                guardMap.moveGuardOneStepFront()
//                println("after move guard: ${guardMap.guardPoint}")
            }
            else -> throw IllegalStateException("Unsupported point type: ${pointFrontGuard.type}")
        }
    }
}
