package majed.eddin.marvelcharacters.ui.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import majed.eddin.marvelcharacters.data.consts.Params.Companion.COMICS
import majed.eddin.marvelcharacters.data.consts.Params.Companion.EVENTS
import majed.eddin.marvelcharacters.data.consts.Params.Companion.SERIES
import majed.eddin.marvelcharacters.data.consts.Params.Companion.STORIES
import majed.eddin.marvelcharacters.data.model.service.CharactersData
import majed.eddin.marvelcharacters.data.model.service.ComicsData
import majed.eddin.marvelcharacters.data.remote.ApiResponse
import majed.eddin.marvelcharacters.data.repository.CharacterDetailsRepo

class CharacterDetailsVM(application: Application) : BaseViewModel(application) {

    private val repo: CharacterDetailsRepo = CharacterDetailsRepo()
    private val characterDetailsResponse = MutableLiveData<ApiResponse<CharactersData>>()
    private val comicsIndexResponse = MutableLiveData<ApiResponse<ComicsData>>()
    private val eventsIndexResponse = MutableLiveData<ApiResponse<ComicsData>>()
    private val seriesIndexResponse = MutableLiveData<ApiResponse<ComicsData>>()
    private val storiesIndexResponse = MutableLiveData<ApiResponse<ComicsData>>()


    val characterDetailsResult: LiveData<ApiResponse<CharactersData>>
        get() = characterDetailsResponse

    val comicsIndexResult: LiveData<ApiResponse<ComicsData>>
        get() = comicsIndexResponse

    val eventsIndexResult: LiveData<ApiResponse<ComicsData>>
        get() = eventsIndexResponse

    val seriesIndexResult: LiveData<ApiResponse<ComicsData>>
        get() = seriesIndexResponse

    val storiesIndexResult: LiveData<ApiResponse<ComicsData>>
        get() = storiesIndexResponse


    fun getCharacterDetails(characterId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCharacterDetails(characterId).collect { characterDetailsResponse.postValue(it) }
        }
    }

    fun getCharacterComicsIndex(characterId: String, currentPage: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCharacterChildDetails(characterId, COMICS, currentPage)
                .collect { comicsIndexResponse.postValue(it) }
        }
    }

    fun getCharacterEventsIndex(characterId: String, currentPage: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCharacterChildDetails(characterId, EVENTS, currentPage)
                .collect { eventsIndexResponse.postValue(it) }
        }
    }

    fun getCharacterSeriesIndex(characterId: String, currentPage: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCharacterChildDetails(characterId, SERIES, currentPage)
                .collect { seriesIndexResponse.postValue(it) }
        }
    }

    fun getCharacterStoriesIndex(characterId: String, currentPage: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCharacterChildDetails(characterId, STORIES, currentPage)
                .collect { storiesIndexResponse.postValue(it) }
        }
    }

}