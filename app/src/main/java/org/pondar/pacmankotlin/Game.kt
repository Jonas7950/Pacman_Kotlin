package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import java.util.*
import kotlin.math.sqrt


/**
 *
 * This class should contain all your game logic
 */

class Game(private var context: Context,view: TextView, view2: TextView) {

        private var pointsView: TextView = view
        private var points : Int = 0
        private var timerView: TextView = view2

        var levelMultiplier: Int = 0

        var time : Int = 120

        var Running = false
        //bitmap of the pacman
        var pacBitmap: Bitmap
        var coinBitMap: Bitmap
        var enemyBitMap: Bitmap
        var pacx: Int = 0
        var pacy: Int = 0
        var direction: Int = 1;


        //did we initialize the coins?
        var NPCsInitialized = false

        //the list of goldcoins - initially empty
        var coins = ArrayList<GoldCoin>()

        var enemies = arrayListOf<Enemy>()

        //a reference to the gameview
        private var gameView: GameView? = null
        private var h: Int = 0
        private var w: Int = 0 //height and width of screen


    init {
        pacBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pacman)
        coinBitMap = BitmapFactory.decodeResource(context.resources, R.drawable.goldcoin)
        enemyBitMap = BitmapFactory.decodeResource(context.resources, R.drawable.enemy)
    }

    fun setGameView(view: GameView) {
        this.gameView = view
    }

    fun pause(){
        if (time > 0){
            Running = !Running
        }
    }

    //TODO initialize goldcoins also here
    fun initializeNPCs()
    {
        coins.clear()
        //DO Stuff to initialize the array list with coins.
        for (x in 0 until 10){
            var randomX = Random().nextInt(w)
            var randomY = Random().nextInt(h)
            coins.add( GoldCoin(randomX.toFloat(),randomY.toFloat()))
        }
        enemies.clear()
        for (x in 0 until levelMultiplier){
            var randomX = Random().nextInt(w)
            var randomY = Random().nextInt(h)
            enemies.add(Enemy(randomX.toFloat(),randomY.toFloat()))
        }

        NPCsInitialized = true
    }


    fun newGame() {
        

        pacx = 50
        pacy = 400 //just some starting coordinates - you can change this.
        //reset the points
        NPCsInitialized = false
        points = 0
        pointsView.text = "${context.resources.getString(R.string.points)} $points"
        gameView?.invalidate() //redraw screen
        direction = 0
        time = 360 - (20 * levelMultiplier)
        timerView.text = "time left: $time"
        Running = true

    }

    fun setSize(h: Int, w: Int) {
        this.h = h
        this.w = w
    }

    fun movePacman(pixels: Int){
        if (direction == 1 && pacx + pixels + pacBitmap.width < w){
            //right
            pacx = pacx + pixels
        }
        else if (direction == 4 && pacy + pixels + pacBitmap.height < h){
            //down
            pacy = pacy + pixels
        }
        else if (direction == 3 && pacx - pixels + pacBitmap.width > 0){
            //left
            pacx = pacx - pixels
        }
        else if (direction == 2 && pacy - pixels + pacBitmap.height > 0){
            //up
            pacy = pacy - pixels
        }

        for (enemy in enemies){
            if (enemy.direction == 1 && enemy.posX + pixels + enemyBitMap.width < w){
                //right
                enemy.posX = enemy.posX + (pixels/2 + levelMultiplier*2)
            }
            else if (enemy.direction == 2 && enemy.posY + pixels + enemyBitMap.height < h){
                //up
                enemy.posY = enemy.posY + (pixels/2 + levelMultiplier*2)
            }
            else if (enemy.direction == 3 && enemy.posX - pixels + enemyBitMap.width < w){
                //right
                enemy.posX = enemy.posX - (pixels/2 + levelMultiplier*2)
            }
            else if (enemy.direction == 4 && enemy.posY - pixels + enemyBitMap.height < h) {
                //down
                enemy.posY = enemy.posY - (pixels/2 + levelMultiplier*2)
            }
        }



        doCollisionCheck()
        gameView!!.invalidate()
    }

    fun movePacmanRight(pixels: Int) {
        //still within our boundaries?
        direction = 1
    }
    fun movePacmanLeft(pixels: Int) {
        //still within our boundaries?
        direction = 3
    }
    fun movePacmanUp(pixels: Int) {
        //still within our boundaries?
        direction = 2
    }
    fun movePacmanDown(pixels: Int) {
        //still within our boundaries?
        direction = 4
    }

    fun distance(x1:Float,y1:Float,x2:Float,y2:Float) : Float {
        // calculate distance and return it
        return sqrt(((x2-x1) * (x2-x1)) + ((y2-y1) * (y2-y1)))
    }

    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    fun doCollisionCheck() {
        for (coin in coins){
            if (!coin.taken){
                if (distance(coin.posX, coin.posY, pacx.toFloat(), pacy.toFloat()) < 30){
                    coin.taken = true
                    points += 1
                    pointsView.text = "points: $points"
                    Log.d("yeet", "you got $points")
                    wincondition()
                }
            }
        }
        for (enemy in enemies){
            if (distance(enemy.posX, enemy.posY, pacx.toFloat(), pacy.toFloat()) < 30){
                time = 0
                Running = false
                Toast.makeText(this.context,"You got hit, Press new game to try again", Toast.LENGTH_SHORT).show()
                levelMultiplier = 0
            }
        }
    }

    fun wincondition(){
        for (coin in coins){
            if (!coin.taken)
                return
        }
        Running = false
        Toast.makeText(this.context,"You got all the coins, congrats!", Toast.LENGTH_SHORT).show()
        levelMultiplier += 1
    }

    fun Timer(){
        if (time > 0){
            time = time - 1
            timerView.text = "time left: $time"
        }
        else {
            Toast.makeText(this.context,"Times up, You Lose", Toast.LENGTH_SHORT).show()
            Running = false
            levelMultiplier = 0
        }

        for (enemy in enemies){
            var randomDirection = Random().nextInt(4)
            enemy.direction = randomDirection + 1
            Log.d("tag", "${enemy.direction}")
        }
    }

}