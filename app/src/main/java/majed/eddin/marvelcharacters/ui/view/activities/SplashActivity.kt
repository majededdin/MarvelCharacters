package majed.eddin.marvelcharacters.ui.view.activities

import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import majed.eddin.marvelcharacters.databinding.ActivitySplashBinding
import majed.eddin.marvelcharacters.ui.base.BaseActivity
import majed.eddin.marvelcharacters.ui.viewModel.CharacterDetailsVM
import majed.eddin.marvelcharacters.utils.extentionUtils.intentWithSlideInOutFinishAll
import majed.eddin.marvelcharacters.utils.extentionUtils.transparentStatusBar

class SplashActivity : BaseActivity<CharacterDetailsVM>() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var job: Job


    override fun onStop() {
        super.onStop()
        job.cancel()
    }


    override fun onResume() {
        super.onResume()
        updateView()
    }


    override fun getViewModel(): Class<CharacterDetailsVM> = CharacterDetailsVM::class.java


    override fun viewModelInstance(viewModel: CharacterDetailsVM?) {
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparentStatusBar()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


    override fun updateView() {
        job = CoroutineScope(IO).launch {
            delay(2000)
            intentWithSlideInOutFinishAll(CharactersIndexActivity::class.java)
        }

    }


    override fun viewInit() {
    }

}