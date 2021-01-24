package com.android.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.android.tictactoe.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val boardCells = Array(3) { arrayOfNulls<ImageView>(3) }
    private var isEasy = true
    private var isSinglePlayer = true
    private var player: String? = null

    //creating the board instance
    var board = Board()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        loadBoard()
        binding.buttonRestart.setOnClickListener { buttonRestart() }
        binding.buttonLevel.setOnClickListener { buttonLevel() }
        binding.buttonSMPlayer.setOnClickListener { buttonSMPlayer() }

    }

    private fun buttonSMPlayer() {
        popup(binding.buttonSMPlayer, R.menu.menul_s_m_player).setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.single_player -> {
                    binding.buttonLevel.visibility = View.VISIBLE
                    binding.buttonSMPlayer.text = resources.getString(R.string.text_single_player)
                    isSinglePlayer = true
                    buttonRestart()
                }
                R.id.multi_player -> {
                    binding.buttonLevel.visibility = View.GONE
                    binding.buttonSMPlayer.text = resources.getString(R.string.text_multi_player)
                    isSinglePlayer = false
                    buttonRestart()
                }
            }
            true
        }
    }

    private fun buttonLevel() {
        popup(binding.buttonLevel, R.menu.menu_level).setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.level_easy -> {
                    binding.buttonLevel.text = resources.getString(R.string.text_easy)
                    isEasy = true
                    buttonRestart()
                }
                R.id.level_hard -> {
                    binding.buttonLevel.text = resources.getString(R.string.text_hard)
                    isEasy = false
                    buttonRestart()
                }
            }
            true
        }
    }

    private fun buttonRestart() {
        player = null
        //creating a new board instance it will empty every cell
        board = Board()

        //setting the result to empty
        binding.tvResult.text = ""

        //this function will map the internal board to the visual board
        mapBoardToUI()
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

            //click allowed only when game not over
            if (!board.isGameOver) {
                //creating a new cell with the clicked index
                if (player == null) player = Board.PLAYER
                val cell = Cell(i, j)
                board.placeMove(cell, player!!)

                if (player == Board.PLAYER) player = Board.COMPUTER
                else if (player == Board.COMPUTER) player = Board.PLAYER

                if (isSinglePlayer) {
                    if (isEasy) {
                        if (board.availableCells.isNotEmpty()) {
                            val compCell =
                                board.availableCells[Random.nextInt(0, board.availableCells.size)]
                            board.placeMove(compCell, player!!)
                        }
                    } else {
                        board.miniMax(0, player!!)
                        board.computersMove?.let {
                            board.placeMove(it, player!!)
                        }
                    }
                    if (player == Board.PLAYER) player = Board.COMPUTER
                    else if (player == Board.COMPUTER) player = Board.PLAYER
                }
                mapBoardToUI()
            }
            when {
                board.hasPlayerWon() -> binding.tvResult.text = "Player Won"
                board.hasComputerWon() && isSinglePlayer -> binding.tvResult.text = "Computer Won"
                board.hasComputerWon() && !isSinglePlayer -> binding.tvResult.text = "Player2 Won"
                board.isGameOver -> binding.tvResult.text = "Game Tied"
            }
        }
    }

    private fun popup(view: View, layout: Int): PopupMenu {
        val popupMenu = PopupMenu(this, view)
        val menu = popupMenu.menu
        popupMenu.menuInflater.inflate(layout, menu)
        popupMenu.show()
        return popupMenu
    }
}