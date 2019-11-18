package moe.kogane.viewbinding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import moe.kogane.viewbinding.binding.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var mViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewModel = MainViewModel(this)
        mViewModel.register(findViewById(android.R.id.content))
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.unregister()
    }
}
