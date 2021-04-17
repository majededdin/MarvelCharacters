package majed.eddin.marvelcharacters.ui.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import majed.eddin.marvelcharacters.R

class LoadingFragment : DialogFragment() {

    override fun onResume() {
        super.onResume()
        if (dialog != null)
            dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_loading, null)

}