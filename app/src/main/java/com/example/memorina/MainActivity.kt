package com.example.memorina

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var firstOpenCard: ImageView? = null
    private var isChecking = false
    private var matchedPairs = 0

    private val deck = listOf(
        R.drawable.card_clubs_2, R.drawable.card_clubs_3, R.drawable.card_clubs_4,
        R.drawable.card_clubs_5, R.drawable.card_clubs_6, R.drawable.card_clubs_7,
        R.drawable.card_clubs_8, R.drawable.card_clubs_9, R.drawable.card_clubs_10,
        R.drawable.card_clubs_j, R.drawable.card_clubs_q, R.drawable.card_clubs_k,
        R.drawable.card_clubs_a,
        R.drawable.card_diamonds_2, R.drawable.card_diamonds_3, R.drawable.card_diamonds_4,
        R.drawable.card_diamonds_5, R.drawable.card_diamonds_6, R.drawable.card_diamonds_7,
        R.drawable.card_diamonds_8, R.drawable.card_diamonds_9, R.drawable.card_diamonds_10,
        R.drawable.card_diamonds_j, R.drawable.card_diamonds_q, R.drawable.card_diamonds_k,
        R.drawable.card_diamonds_a,
        R.drawable.card_hearts_2, R.drawable.card_hearts_3, R.drawable.card_hearts_4,
        R.drawable.card_hearts_5, R.drawable.card_hearts_6, R.drawable.card_hearts_7,
        R.drawable.card_hearts_8, R.drawable.card_hearts_9, R.drawable.card_hearts_10,
        R.drawable.card_hearts_j, R.drawable.card_hearts_q, R.drawable.card_hearts_k,
        R.drawable.card_hearts_a,
        R.drawable.card_spades_2, R.drawable.card_spades_3, R.drawable.card_spades_4,
        R.drawable.card_spades_5, R.drawable.card_spades_6, R.drawable.card_spades_7,
        R.drawable.card_spades_8, R.drawable.card_spades_9, R.drawable.card_spades_10,
        R.drawable.card_spades_j, R.drawable.card_spades_q, R.drawable.card_spades_k,
        R.drawable.card_spades_a,
        R.drawable.card_joker,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(applicationContext).apply {
            orientation = LinearLayout.VERTICAL
        }

        val cardParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f).apply {
            setMargins(8, 8, 8, 8)
        }
        val rowParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f)

        val selectedFaces = deck.shuffled().take(8)
        val pairs = (selectedFaces + selectedFaces).shuffled()

        val catViews = ArrayList<ImageView>(16)
        for (faceResId in pairs) {
            catViews.add(
                ImageView(applicationContext).apply {
                    setImageResource(R.drawable.card_back)
                    layoutParams = cardParams
                    tag = faceResId
                    setOnClickListener(colorListener)
                }
            )
        }

        val rows = Array(4) {
            LinearLayout(applicationContext).apply {
                layoutParams = rowParams
            }
        }

        for ((count, view) in catViews.withIndex()) {
            rows[count / 4].addView(view)
        }
        for (row in rows) {
            layout.addView(row)
        }

        setContentView(layout)
    }

    private val colorListener = View.OnClickListener { v ->
        if (isChecking) return@OnClickListener
        val card = v as ImageView
        card.setImageResource(card.tag as Int)
        card.isClickable = false

        val first = firstOpenCard
        if (first == null) {
            firstOpenCard = card
        } else {
            firstOpenCard = null
            if (first.tag == card.tag) {
                matchedPairs++
                if (matchedPairs == 8) {
                    Toast.makeText(this, "Вы выиграли!", Toast.LENGTH_LONG).show()
                }
            } else {
                isChecking = true
                lifecycleScope.launch {
                    delay(1000)
                    first.setImageResource(R.drawable.card_back)
                    card.setImageResource(R.drawable.card_back)
                    first.isClickable = true
                    card.isClickable = true
                    isChecking = false
                }
            }
        }
    }
}
