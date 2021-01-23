package com.android.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.android.tictactoe.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val boardCells = Array(3) { arrayOfNulls<ImageView>(3) }

    //creating the board instance
    var board = Board()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        loadBoard()
        binding.buttonRestart.setOnClickListener {
            //creating a new board instance it will empty every cell
            board = Board()

            //setting the result to empty
            binding.tvResult.text = ""

            //this function will map the internal board to the visual board
            mapBoardToUI()
        }

    }


    private fun mapBoardToUI() {
        //function is mapping the internal board to the ImageView array board
        for (i in board.board.indices) {
            for (j in board.board.indices) {
                when (board.board[i][j]) {
                    Board.PLAYER -> {
                        //player has circle move once moved make it disable
                        boardCells[i][j]?.setImageResource(R.drawable.circle)
                        boardCells[i][j]?.isEnabled = false
                    }
                    Board.COMPUTER -> {
                        boardCells[i][j]?.setImageResource(R.drawable.cross)
                        boardCells[i][j]?.isEnabled = false
                    }
                    else -> {
                        boardCells[i][j]?.setImageResource(0)
                        boardCells[i][j]?.isEnabled = true
                    }
                }
            }
        }
    }

    private fun loadBoard() {
        //This function is generating the tic tac toe board
        for (i in boardCells.indices) {
            for (j in boardCells.indices) {
                boardCells[i][j] = ImageView(this)
                boardCells[i][j]?.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(i)
                    columnSpec = GridLayout.spec(j)
                    width = 200
                    height = 200
                    setMargins(5, 5, 5, 5)
                }
                boardCells[i][j]?.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimary
                    )
                )
                boardCells[i][j]?.setOnClickListener(CellClickListener(i, j))
                binding.layoutBoard.addView(boardCells[i][j])
            }
        }
    }

    //click listener when user clicks specific cell to make move
    inner class CellClickListener(private val i: Int, private val j: Int) : View.OnClickListener {
        override fun onClick(v: View?) {
            if (!board.isGameOver) {
                //creating a new cell with the clicked index
                val cell = Cell(i, j)
                board.placeMove(cell, Board.PLAYER)

                if (board.availableCells.isNotEmpty()) {
                    val compCell =
                        board.availableCells[Random.nextInt(0, board.availableCells.size)]
                    board.placeMove(compCell, Board.COMPUTER)
                }
                mapBoardToUI()
            }
            when{
                board.hasPlayerWon() -> binding.tvResult.text = "Player Won"
                board.hasComputerWon() -> binding.tvResult.text = "Computer Won"
                board.isGameOver -> binding.tvResult.text = "Game Tied"
            }
        }
    }
}