package tr.com.onurkinay.blackjack

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.bet_dialog.view.*
import java.util.Arrays
import kotlin.random.Random
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    val cards = arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
    var bet: Int = 1
    var wallet: Int = 1000

    var dealer_cards: MutableList<String> = mutableListOf("")
    var player_cards: MutableList<String> = mutableListOf("")

    var dc_w: Int = 0
    var pl_w: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hit.setOnClickListener {
            hit()
        }
        stand.setOnClickListener {
            stand()
        }
        double_b.setOnClickListener {
            double_b()
        }
        enter_bet()
    }

    fun enter_bet() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.bet_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Enter bet!")
        //show dialog
        val mAlertDialog = mBuilder.show()
        mDialogView.walletStatus.text = "You have $" + wallet
        //login button click of custom layout
        mDialogView.dialogLoginBtn.setOnClickListener {
            //dismiss dialog
            if (mDialogView.dialogBetEt.text.toString() != "" && wallet >= mDialogView.dialogBetEt.text.toString().toInt()) {
                //get text from EditTexts of custom layout
                bet = mDialogView.dialogBetEt.text.toString().toInt()
                wallet -= bet
                mAlertDialog.dismiss()
                start_a_game()
            } else {

            }

        }
        //cancel button click of custom layout
        mDialogView.dialogCancelBtn.setOnClickListener {
            exitProcess(-1)
            mAlertDialog.dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    fun start_a_game() {
        if (wallet < bet * 2) double_b.isEnabled = false
        dealer_cards = mutableListOf(random_card(), random_card())
        player_cards = mutableListOf(random_card(), random_card())


        dc_w = 0
        pl_w = 0

        val pl_worth: Int by lazy {
            var worth: Int = 0
            for (i in player_cards) {
                worth += give_worth_of_the_card(i)
            }
            worth
        }
        val dc_worth: Int by lazy {
            var worth: Int = 0
            for (i in dealer_cards) {
                worth += give_worth_of_the_card(i)
            }
            worth
        }

        dealer.text = dealer_cards[0] + " X"
        player.text = player_cards[0] + " " + player_cards[1]

        d_status.text = give_worth_of_the_card(dealer_cards[0]).toString()
        p_status.text = (give_worth_of_the_card(player_cards[0]) + give_worth_of_the_card(player_cards[1])).toString()

        p_status.text = "Bet: " + bet.toString() + " -- " + p_status.text.toString()

        if (dc_worth == 21) {//dealer blackjack
            game_over(dc_worth, pl_worth, 0)
        } else if (dc_worth > 21) {
            //dealer busted
            game_over(dc_worth, pl_worth, 1)
        } else {
        }
    }

    fun hit() {
        double_b.isEnabled = false
        dealer_cards.add(random_card())
        player_cards.add(random_card())

        var handDealer: String = ""
        var handPlayer: String = ""

        dc_w = 0
        pl_w = 0

        for (card in dealer_cards) {
            handDealer += ("$card ")
            dc_w += give_worth_of_the_card(card)
        }

        for (card in player_cards) {
            handPlayer += ("$card ")
            pl_w += give_worth_of_the_card(card)
        }

        dealer.text = handDealer
        player.text = handPlayer

        d_status.text = dc_w.toString()
        p_status.text = pl_w.toString()

        p_status.text = "Bet: " + bet.toString() + " -- " + p_status.text.toString()

        if (dc_w == 21) {//dealer blackjack
            game_over(dc_w, pl_w, 0)
        } else if (dc_w > 21) {//dealer busted
            game_over(dc_w, pl_w, 1)
        } else if (dc_w >= 17) {
            game_over(dc_w, pl_w, 3)
        } else if (pl_w >= 21) {
            game_over(dc_w, pl_w, 3)
        }
        /// HIT
    }

    fun stand() {
        stand.isEnabled = false
        double_b.isEnabled = false
        while (dc_w < 17) {

            dealer_cards.add(random_card())
            var handDealer: String = ""
            dc_w = 0
            for (card in dealer_cards) {
                handDealer += ("$card ")
                dc_w += give_worth_of_the_card(card)
            }
            dealer.text = handDealer
            d_status.text = dc_w.toString()

            if (dc_w == 21) {//dealer blackjack
                game_over(dc_w, pl_w, 0)
            } else if (dc_w > 21) {//dealer busted
                game_over(dc_w, pl_w, 1)
            } else if (dc_w >= 17) {
                game_over(dc_w, pl_w, 3)
            } else if (pl_w >= 21) {
                game_over(dc_w, pl_w, 3)
            }
        }

    }

    fun double_b() {
        if (player_cards.size < 3) {

            stand.isEnabled = false
            double_b.isEnabled = false
            bet *= 2
            player_cards.add(random_card())
            var handPlayer: String = ""
            for (card in player_cards) {
                handPlayer += ("$card ")
                pl_w += give_worth_of_the_card(card)
            }
            player.text = handPlayer
            p_status.text = pl_w.toString()
            p_status.text = "Bet: " + bet.toString() + " -- " + p_status.text.toString()

            while (dc_w < 17) {

                dealer_cards.add(random_card())
                var handDealer: String = ""
                dc_w = 0
                for (card in dealer_cards) {
                    handDealer += ("$card ")
                    dc_w += give_worth_of_the_card(card)
                }
                dealer.text = handDealer
                d_status.text = dc_w.toString()

                if (dc_w == 21) {//dealer blackjack
                    game_over(dc_w, pl_w, 0)
                } else if (dc_w > 21) {//dealer busted
                    game_over(dc_w, pl_w, 1)
                } else if (dc_w >= 17) {
                    game_over(dc_w, pl_w, 3)
                } else if (pl_w >= 21) {
                    game_over(dc_w, pl_w, 3)
                }
            }


        }
    }

    fun random_card(): String {
        return cards[Random.nextInt(0, 13)]
    }

    fun give_worth_of_the_card(card: String): Int {
        if (card != "A" && card != "J" && card != "Q" && card != "K") {
            return card.toInt()
        } else {
            if (card != "A") {
                return 10
            } else {
                return 1
            }
        }

    }

    fun game_over(dc: Int, pc: Int, result: Int) {
        var status: String = ""
        if (result == 3) {
            if (pc == 21) {
                status = "Player blackjack. You won!"
                wallet += bet * 2
            } else if (pc > 21) {
                status = "Player lost!"
            } else if (dc < pc) {
                status = "Player won!"
                wallet += bet * 2
            } else if (dc == pc) {
                status = "Push. No one won"
                wallet += bet
            } else status = "Dealer won!"
        } else if (result == 4) {
            status = "Player busted! You lost"
        } else if (result == 0) {
            status = "Dealer Blackjack. You lost!"
        } else if (result == 1) {
            status = "Dealer Busted. You Won"
            wallet += bet * 2
        }

        status += " You have $" + wallet.toString()
        val builder = AlertDialog.Builder(this)
        builder.setTitle(status)
        builder.setMessage(if (wallet > 0) "New Game?" else "Thanks for playing. You are poor now!")
        //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            Toast.makeText(
                applicationContext,
                android.R.string.yes, Toast.LENGTH_SHORT
            ).show()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            exitProcess(-1)
        }
        if (wallet > 0) {
            builder.setNeutralButton("New Game") { dialog, which ->
                stand.isEnabled = true
                double_b.isEnabled = true
                enter_bet()

            }
        }
        builder.show()
    }
}


