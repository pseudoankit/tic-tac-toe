package com.android.tictactoe

class Board {
    companion object {
        const val PLAYER = "O"
        const val COMPUTER = "X"
    }

    val board = Array(3) { arrayOfNulls<String>(3) }

    val availableCells: List<Cell>
        //method to return available cells so computer make move
        get() {
            val cells = mutableListOf<Cell>()
            for (i in board.indices) {
                for (j in board.indices) {
                    if (board[i][j].isNullOrEmpty()) cells.add(Cell(i, j))
                }
            }
            return cells
        }

    fun placeMove(cell: Cell, player: String) {
        board[cell.i][cell.j] = player
    }

    val isGameOver: Boolean
        get() = hasComputerWon() || hasPlayerWon() || availableCells.isEmpty()

    /*
    *winning is decided if row or column are equal or
    *diagonal values are equal
     */
    fun hasComputerWon(): Boolean {
        if (board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == COMPUTER ||
            board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == COMPUTER
        ) {
            return true
        }
        for (i in board.indices) {
            if (board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == COMPUTER ||
                board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == COMPUTER
            ) {
                return true
            }
        }
        return false
    }

    fun hasPlayerWon(): Boolean {
        if (board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == PLAYER ||
            board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == PLAYER
        ) {
            return true
        }
        for (i in board.indices) {
            if (board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == PLAYER ||
                board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == PLAYER
            ) {
                return true
            }
        }
        return false
    }

    var computersMove: Cell? = null
    fun miniMax(depth: Int, player: String): Int {
        if (hasComputerWon()) return +1
        if (hasPlayerWon()) return -1
        if (availableCells.isEmpty()) return 0

        var min = Integer.MAX_VALUE
        var max = Integer.MIN_VALUE
        for (i in availableCells.indices) {
            val cell = availableCells[i]
            if (player == COMPUTER) {
                placeMove(cell, COMPUTER)
                val currentScore = miniMax(depth + 1, PLAYER)
                max = currentScore.coerceAtLeast(max)

                if (currentScore >= 0) {
                    if (depth == 0) computersMove = cell
                }
                if (currentScore == 1) {
                    board[cell.i][cell.j] = ""
                    break
                }
                if (i == availableCells.size - 1 && max < 0) {
                    if (depth == 0) computersMove = cell
                }
            } else if (player == PLAYER) {
                placeMove(cell, PLAYER)
                val currentScore = miniMax(depth + 1, COMPUTER)
                min = currentScore.coerceAtMost(min)

                if (min == -1) {
                    board[cell.i][cell.j] = ""
                    break
                }
            }
            board[cell.i][cell.j] = ""
        }
        return if (player == COMPUTER) max else min
    }
}