package myplayground.example.githubsearch.activities

import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.drawerlayout.widget.DrawerLayout
import myplayground.example.githubsearch.R

open class DrawerActivity : AppCompatActivity() {
    private lateinit var fullLayout: DrawerLayout
    private lateinit var frameLayout: FrameLayout

    override fun setContentView(view: View) {
        // wrap view inside frame layout
        fullLayout = layoutInflater.inflate(R.layout.activity_drawer, null) as DrawerLayout
        frameLayout = fullLayout.findViewById(R.id.drawer_frame)

        frameLayout.addView(view)

        super.setContentView(fullLayout)

        // drawer content
        val actionbar = supportActionBar!!
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setBackgroundDrawable(ContextCompat.getColor(this, R.color.primary).toDrawable())
    }
}