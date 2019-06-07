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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
// Enter bet!!
EnterBet()
// !Enter bet
    }

    fun EnterBet() {
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
                dealer.text = bet.toString()
                //set the input text in TextView
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

}
