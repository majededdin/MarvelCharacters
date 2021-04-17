package majed.eddin.marvelcharacters.ui.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import majed.eddin.marvelcharacters.data.model.service.Data
import majed.eddin.marvelcharacters.data.remote.ApiResponse
import majed.eddin.marvelcharacters.data.repository.CharactersIndexRepo

class CharactersIndexVM(application: Application) : BaseViewModel(application) {

    private val repo: CharactersIndexRepo = CharactersIndexRepo(application)
    private var searchJob: Job? = null

    private val charactersIndexResponse = MutableLiveData<ApiResponse<Data>>()

    val charactersIndexResult: LiveData<ApiResponse<Data>>
        get() = charactersIndexResponse


    fun getCharactersIndex(name: String?, currentPage: Int) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            repo.getCharactersIndex(name, currentPage)
                .collect { charactersIndexResponse.postValue(it) }
        }
    }
}