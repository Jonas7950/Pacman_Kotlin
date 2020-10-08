package org.pondar.pacmankotlin

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    //reference to the game class.
    private var game: Game? = null
    private var gameTimer: Timer = Timer()
    private var timeTimer: Timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        game = Game(this,pointsView,timerView)


        game?.setGameView(gameView)
        gameView.setGame(game)
        game?.newGame()
        moveRight.setOnClickListener {
            game?.movePacmanRight(10)
        }
        moveLeft.setOnClickListener {
            game?.movePacmanLeft(10)
        }
        moveUp.setOnClickListener {
            game?.movePacmanUp(10)
        }
        moveDown.setOnClickListener {
            game?.movePacmanDown(10)
        }
        pauseButton.setOnClickListener {
            game?.pause()
        }

        game?.Running = true

        //fast timer for movement
        gameTimer.schedule((object: TimerTask() {
            override fun run() {
                timermethod()
            }
        }), 0, 100)

        //slow timer for taking time
        timeTimer.schedule((object: TimerTask(){
             override fun run() {
                Timertimermethod()
            }
        }), 0, 1000)
    }

    private fun timermethod(){
        this.runOnUiThread(timerTick)
    }

    private fun Timertimermethod(){
        this.runOnUiThread(timerTimerTick)
    }

    private val timerTick = Runnable {
        if (game!!.Running){
        game?.movePacman(10)
        }
    }
    private val timerTimerTick = Runnable {
        if(game!!.Running){
            game?.Timer()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_settings) {
            Toast.makeText(this, "settings clicked", Toast.LENGTH_LONG).show()
            return true
        } else if (id == R.id.action_newGame) {
            Toast.makeText(this, "New Game clicked", Toast.LENGTH_LONG).show()
            game?.newGame()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
