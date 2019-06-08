package tr.com.onurkinay.blackjack

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.bet_dialog.view.*
import java.util.Arrays
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    val cards = arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
    var bet: Int = 1
    var wallet: Int = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enter_bet()
    }

    fun enter_bet() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.bet_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Enter bet!")
        //show dialog
        val  mAlertDialog = mBuilder.show()
        //login button click of custom layout
        mDialogView.dialogLoginBtn.setOnClickListener {
            //dismiss dialog
            if (mDialogView.dialogBetEt.text.toString() != "") {
                //get text from EditTexts of custom layout
                bet = mDialogView.dialogBetEt.text.toString().toInt()
                play_a_game()
                mAlertDialog.dismiss()
            }else{

            }

        }
        //cancel button click of custom layout
        mDialogView.dialogCancelBtn.setOnClickListener {
            exitProcess(-1)
            mAlertDialog.dismiss()
        }
    }

    fun play_a_game(){
        val dealer_cards = mutableListOf( random_card(), random_card() )
        val player_cards = mutableListOf( random_card(), random_card() )

        val pl_worth: Int by lazy {
            var worth: Int = 0
            for (i in player_cards){
                worth += give_worth_of_the_card(i)
            }
            worth
        }
        val dc_worth: Int by lazy{
            var worth: Int = 0
            for (i in dealer_cards){
                worth += give_worth_of_the_card(i)
            }
            worth
        }


        dealer.text = dealer_cards[0] + " X"
        player.text = player_cards[0] + " "+ player_cards[1]

        d_status.text = give_worth_of_the_card(dealer_cards[0]).toString()
        p_status.text = (give_worth_of_the_card(player_cards[0]) + give_worth_of_the_card(player_cards[1])).toString()

        p_status.text = "Bet: "+ bet.toString() + " -- " + p_status.text.toString()

        if(dc_worth == 21) {
            //dealer blackjack
        }else if(dc_worth > 21){
            //dealer busted
        }else {
            var dc_w: Int = 0
            var pl_w: Int = 0
            hit.setOnClickListener {
                if (dc_w == 21) {
                    //dealer blackjack
                } else if (dc_w > 21) {
                    //dealer busted
                } else {
                    if (pl_w == 21) {
                        //player blackjack
                    } else if (pl_w > 21) {
                        //player busted
                    } else {
                        dealer_cards += random_card()
                        player_cards += random_card()

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
                    }

                }
            }
            stand.setOnClickListener {
                while (dc_w < 17) {
                    if (dc_w == 21) {
                        //dealer blackjack
                    } else if (dc_w > 21) {
                        //dealer busted
                    } else {
                        dealer_cards += random_card()
                        var handDealer: String = ""
                        dc_w = 0
                        for (card in dealer_cards) {
                            handDealer += ("$card ")
                            dc_w += give_worth_of_the_card(card)
                        }
                        dealer.text = handDealer
                        d_status.text = dc_w.toString()

                    }
                }
            }
            double_b.setOnClickListener {
                if(player_cards.size < 3){
                bet *= 2
                player_cards += random_card()
                var handPlayer: String = ""
                for (card in player_cards) {
                    handPlayer += ("$card ")
                    pl_w += give_worth_of_the_card(card)
                }
                player.text = handPlayer
                p_status.text = pl_w.toString()
                p_status.text = "Bet: " + bet.toString() + " -- " + p_status.text.toString()

                while (dc_w < 17) {
                    if (dc_w == 21) {
                        //dealer blackjack
                    } else if (dc_w > 21) {
                        //dealer busted
                    } else {
                        dealer_cards += random_card()
                        var handDealer: String = ""
                        dc_w = 0
                        for (card in dealer_cards) {
                            handDealer += ("$card ")
                            dc_w += give_worth_of_the_card(card)
                        }
                        dealer.text = handDealer
                        d_status.text = dc_w.toString()

                    }
                }
            }
        }
        }
    }

    fun random_card(): String{
        return cards[(0..13).random()]
    }

    fun give_worth_of_the_card(card: String): Int{
        if(card != "A" && card != "J" && card != "Q" && card != "K"){
            return card.toInt()
        }else{
            if(card != "A"){
                return 10
            }else {
                return 1
            }
        }

    }
}


