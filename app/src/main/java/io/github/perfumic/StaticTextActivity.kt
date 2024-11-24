package io.github.perfumic

import android.graphics.Typeface
import android.os.Bundle
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.text.style.UnderlineSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import io.github.perfumic.autolink.*
import io.github.perfumic.databinding.ActivityStaticTextBinding

class StaticTextActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStaticTextBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaticTextBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val custom = MODE_CUSTOM("\\sAndroid\\b", "\\smobile\\b")
        binding.autoLinkTextView.addAutoLinkMode(
                MODE_HASHTAG,
                MODE_EMAIL,
                MODE_URL,
                MODE_PHONE,
                custom,
                MODE_MENTION)

        binding.autoLinkTextView.addUrlTransformations(
                "https://en.wikipedia.org/wiki/Wear_OS" to "Wear OS",
                "https://en.wikipedia.org/wiki/Fire_OS" to "FIRE")

        binding.autoLinkTextView.attachUrlProcessor {
            when {
                it.contains("google") -> "Google"
                it.contains("github") -> "Github"
                else -> it
            }
        }

        binding.autoLinkTextView.addSpan(MODE_URL, StyleSpan(Typeface.ITALIC), UnderlineSpan())
        binding.autoLinkTextView.addSpan(MODE_HASHTAG, UnderlineSpan(), TypefaceSpan("monospace"))
        binding.autoLinkTextView.addSpan(custom, StyleSpan(Typeface.BOLD))

        binding.autoLinkTextView.hashTagModeColor = ContextCompat.getColor(this, R.color.color5)
        binding.autoLinkTextView.phoneModeColor = ContextCompat.getColor(this, R.color.color3)
        binding.autoLinkTextView.customModeColor = ContextCompat.getColor(this, R.color.color1)
        binding.autoLinkTextView.mentionModeColor = ContextCompat.getColor(this, R.color.color6)
        binding.autoLinkTextView.emailModeColor = ContextCompat.getColor(this, R.color.colorPrimary)

        binding.autoLinkTextView.text = getString(R.string.android_text)

        binding.autoLinkTextView.onAutoLinkClick {
            val message = if (it.originalText == it.transformedText) it.originalText
            else "Original text - ${it.originalText} \n\nTransformed text - ${it.transformedText}"
            val url = if (it.mode is MODE_URL) it.originalText else null
            showDialog(it.mode.modeName, message, url)
        }
    }
}
