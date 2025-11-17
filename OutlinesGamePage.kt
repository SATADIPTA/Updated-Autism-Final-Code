package com.example.firebase.pages

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.AttributeSet
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.firebase.R
import java.util.*

@Composable
fun OutlinesGamePage(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(Unit) {
        val ttsInstance = TextToSpeech(context) {}
        tts = ttsInstance
        onDispose {
            ttsInstance.stop()
            ttsInstance.shutdown()
        }
    }

    AndroidView(
        factory = { ctx ->
            OutlinesGameView(ctx)
        },
        modifier = modifier.fillMaxSize()
    )
}

class OutlinesGameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs), TextToSpeech.OnInitListener {

    private lateinit var questionText: TextView
    private lateinit var feedbackText: TextView
    private lateinit var placeholderContainer: LinearLayout
    private lateinit var draggableContainer: FrameLayout
    private lateinit var shapeMap: Map<String, ImageView>
    private lateinit var placeholderShapes: List<Pair<ImageView, String>>
    private lateinit var shapeOrder: List<String>
    private var currentIndex = 0
    private lateinit var tts: TextToSpeech
    private lateinit var micIcon: ImageView

    init {
        initializeView()
    }

    @SuppressLint("InflateParams")
    private fun initializeView() {
        LayoutInflater.from(context).inflate(R.layout.activity_outlines_game, this, true)

        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(0xFFFFE0B2.toInt(), 0xFFFBE9E7.toInt())
        )
        background = gradient

        tts = TextToSpeech(context, this)
        questionText = findViewById(R.id.question_text)
        placeholderContainer = findViewById(R.id.placeholder_container)
        draggableContainer = findViewById(R.id.draggable_container)
        micIcon = findViewById(R.id.voice_icon)

        micIcon.setOnClickListener {
            if (currentIndex < shapeOrder.size) {
                val shapeName = shapeOrder[currentIndex]
                speakOut("Drag the $shapeName to its matching outline")
            }
        }

        val circle = findViewById<ImageView>(R.id.draggable_circle)
        val square = findViewById<ImageView>(R.id.draggable_square)
        val rectangle = findViewById<ImageView>(R.id.draggable_rectangle)
        val octagon = findViewById<ImageView>(R.id.draggable_octagon)

        shapeMap = mapOf(
            "circle" to circle,
            "square" to square,
            "rectangle" to rectangle,
            "octagon" to octagon
        )

        placeholderShapes = listOf(
            Pair(findViewById(R.id.placeholder_circle), "circle"),
            Pair(findViewById(R.id.placeholder_square), "square"),
            Pair(findViewById(R.id.placeholder_rectangle), "rectangle"),
            Pair(findViewById(R.id.placeholder_octagon), "octagon")
        )

        shapeOrder = shapeMap.keys.shuffled()

        feedbackText = TextView(context).apply {
            textSize = 20f
            setTypeface(null, Typeface.BOLD)
            setPadding(16, 16, 16, 32)
            visibility = View.GONE
            textAlignment = View.TEXT_ALIGNMENT_CENTER
        }

        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        addView(feedbackText, layoutParams)
        feedbackText.translationY = resources.displayMetrics.heightPixels * 0.9f
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
            showNextShape()
        }
    }

    private fun showNextShape() {
        questionText.apply {
            textSize = 22f
            setTextColor(Color.BLACK)
            setTypeface(null, Typeface.BOLD)
        }

        draggableContainer.removeAllViews()

        if (currentIndex < shapeOrder.size) {
            val shuffledPlaceholders = placeholderShapes.shuffled()
            placeholderContainer.removeAllViews()
            shuffledPlaceholders.forEach { (view, tag) ->
                view.setImageDrawable(context.getDrawable(getPlaceholderDrawable(tag)))
                placeholderContainer.addView(view)
                setDropListener(view, tag)
            }

            val shapeName = shapeOrder[currentIndex]
            val shapeView = shapeMap[shapeName]!!

            shapeView.visibility = View.VISIBLE
            draggableContainer.addView(shapeView)
            setDragListener(shapeView)

            questionText.text = "Drag the $shapeName to its matching outline"
            speakOut("Drag the $shapeName to its matching outline")
        } else {
            val finalMessage = "Well done! All shapes matched."
            questionText.text = "\uD83D\uDE0D  \uD83C\uDFC6 $finalMessage"
            speakOut(finalMessage)
        }
    }

    private fun getPlaceholderDrawable(tag: String): Int {
        return when (tag) {
            "circle" -> R.drawable.placeholder_shape_circle
            "square" -> R.drawable.placeholder_shape_square
            "rectangle" -> R.drawable.placeholder_shape_rectangle
            "octagon" -> R.drawable.placeholder_shape_octagon
            else -> R.drawable.placeholder_shape_circle
        }
    }

    private fun speakOut(text: String, onDone: (() -> Unit)? = null) {
        val utteranceId = UUID.randomUUID().toString()
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}
            override fun onError(utteranceId: String?) {}

            override fun onDone(utteranceId: String?) {
                onDone?.let {
                    post { it() }
                }
            }
        })
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    private fun setDragListener(view: ImageView) {
        view.setOnLongClickListener {
            val clipData = ClipData.newPlainText("", "")
            val shadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(clipData, shadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true
        }
    }

    private fun setDropListener(targetView: ImageView, expectedTag: String) {
        targetView.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    val draggedView = event.localState as View
                    if ((draggedView as ImageView).tag == expectedTag) {
                        targetView.setImageDrawable(draggedView.drawable)
                        draggedView.visibility = View.INVISIBLE
                        feedbackText.apply {
                            text = "\uD83C\uDF89 Hurray! It's Correct!"
                            setTextColor(Color.parseColor("#2E7D32"))
                            visibility = View.VISIBLE
                        }
                        speakOut("Hurray! It's correct!") {
                            post {
                                feedbackText.visibility = View.GONE
                                currentIndex++
                                showNextShape()
                            }
                        }
                    } else {
                        draggedView.visibility = View.VISIBLE
                        feedbackText.apply {
                            text = "\u274C Oops! Wrong. Try Again!"
                            setTextColor(Color.parseColor("#C62828"))
                            visibility = View.VISIBLE
                        }
                        speakOut("Oops! Wrong. Try Again!") {
                            postDelayed({
                                feedbackText.visibility = View.GONE
                            }, 2000)
                        }
                    }
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    val draggedView = event.localState as View
                    if (!event.result) draggedView.visibility = View.VISIBLE
                    true
                }

                else -> true
            }
        }
    }

    override fun onDetachedFromWindow() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDetachedFromWindow()
    }
}
