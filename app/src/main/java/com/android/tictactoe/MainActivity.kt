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

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val boardCells = Array(3) { arrayOfNulls<ImageView>(3) }
    var board = Board()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        loadBoard()
        binding.buttonRestart.setOnClickListener{
            board = Board()
            binding.tvResult.text = ""
            mapBoardToUI()
        }

    }

    private fun mapBoardToUI(){
        for (i in board.board.indices){
            for (j in board.board.indices){
                when (board.board[i][j]){
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
                    setMargins(5,5,5,5)
                }
                boardCells[i][j]?.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                boardCells[i][j]?.setOnClickListener(CellClickListener(i,j))
                binding.layoutBoard.addView(boardCells[i][j])
            }
        }
    }

    //click listener when user clicks specific cell to make move
    inner class CellClickListener(private val i: Int,private val j: Int)
        :View.OnClickListener{
        override fun onClick(v: View?) {
            val cell = Cell(i,j)
            board.placeMove(cell,Board.PLAYER)
            mapBoardToUI()
        }

    }
}