package majed.eddin.marvelcharacters.ui.view.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import majed.eddin.marvelcharacters.R
import majed.eddin.marvelcharacters.data.consts.Params.Companion.CHARACTER
import majed.eddin.marvelcharacters.data.model.service.CharactersData
import majed.eddin.marvelcharacters.data.model.service.Comics
import majed.eddin.marvelcharacters.data.model.service.ComicsData
import majed.eddin.marvelcharacters.data.model.service.MarvelCharacter
import majed.eddin.marvelcharacters.data.remote.ApiResponse
import majed.eddin.marvelcharacters.data.remote.ApiStatus
import majed.eddin.marvelcharacters.databinding.ActivityCharacterDetailsBinding
import majed.eddin.marvelcharacters.ui.base.BaseCollapseActivity
import majed.eddin.marvelcharacters.ui.view.adapters.ComicsAdapter
import majed.eddin.marvelcharacters.ui.viewModel.CharacterDetailsVM
import majed.eddin.marvelcharacters.utils.extentionUtils.toGone
import majed.eddin.marvelcharacters.utils.extentionUtils.toVisible
import majed.eddin.marvelcharacters.utils.recyclerUtils.OnEndless

class CharacterDetailsActivity : BaseCollapseActivity<CharacterDetailsVM>() {

    private lateinit var marvelCharacter: MarvelCharacter
    private lateinit var characterDetailsVM: CharacterDetailsVM
    private lateinit var binding: ActivityCharacterDetailsBinding

    private lateinit var comicsAdapter: ComicsAdapter
    private lateinit var eventsAdapter: ComicsAdapter
    private lateinit var seriesAdapter: ComicsAdapter
    private lateinit var storiesAdapter: ComicsAdapter

    private var comicsApiResponse: ApiResponse<ComicsData> = ApiResponse()
    private var eventsApiResponse: ApiResponse<ComicsData> = ApiResponse()
    private var seriesApiResponse: ApiResponse<ComicsData> = ApiResponse()
    private var storiesApiResponse: ApiResponse<ComicsData> = ApiResponse()


    override fun getViewModel(): Class<CharacterDetailsVM> = CharacterDetailsVM::class.java


    override fun viewModelInstance(viewModel: CharacterDetailsVM?) {
        characterDetailsVM = viewModel!!
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterDetailsBinding.inflate(layoutInflater, getBaseFrame(), true)
        setOnSwipeRefreshStatus(false)
        viewInit()
        updateView()

        characterDetailsVM.characterDetailsResult.observe(this, this::characterDetailsResult)
        characterDetailsVM.comicsIndexResult.observe(this, this::comicsIndexResult)
        characterDetailsVM.eventsIndexResult.observe(this, this::eventsIndexResult)
        characterDetailsVM.seriesIndexResult.observe(this, this::seriesIndexResult)
        characterDetailsVM.storiesIndexResult.observe(this, this::storiesIndexResult)

    }


    override fun updateView() {
        characterDetailsVM.getCharacterDetails(marvelCharacter.id.toString())
    }


    override fun viewInit() {
        marvelCharacter = intent.getParcelableExtra(CHARACTER)!!

        comicsAdapter = ComicsAdapter()
        binding.recyclerComics.adapter = comicsAdapter
        binding.recyclerComics.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        eventsAdapter = ComicsAdapter()
        binding.recyclerEvents.adapter = eventsAdapter
        binding.recyclerEvents.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        seriesAdapter = ComicsAdapter()
        binding.recyclerSeries.adapter = seriesAdapter
        binding.recyclerSeries.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        storiesAdapter = ComicsAdapter()
        binding.recyclerStories.adapter = storiesAdapter
        binding.recyclerStories.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

    }


    private fun characterDetailsResult(apiResponse: ApiResponse<CharactersData>) {
        getBaseFrame().isRefreshing = false
        handleApiResponse(apiResponse) { updateView() }
        if (apiResponse.apiStatus == ApiStatus.OnSuccess) {
            this.marvelCharacter = apiResponse.model!!.results[0]
            handelCharacterDetailsResult(apiResponse.model!!.results[0])
        }
    }


    private fun handelCharacterDetailsResult(marvelCharacter: MarvelCharacter) {
        title = marvelCharacter.name
        setBackgroundImage(marvelCharacter.getFullImagePath())
        setCollapseMedia(marvelCharacter.getFullImagePath())
        binding.txtCharacterName.text = marvelCharacter.name
        binding.txtDescription.text =
            if (marvelCharacter.description.isNotBlank()) marvelCharacter.description else getString(
                R.string.no_description
            )

        loadComics()

        loadEvents()

        loadSeries()

        loadStories()

    }


    private fun loadComics() {
        comicsApiResponse.model = ComicsData()
        comicsApiResponse.model!!.results.clear()
        comicsApiResponse.model!!.count = 1

        comicsAdapter.clear()

        val scrollListener = object : OnEndless(
            binding.recyclerComics.layoutManager as LinearLayoutManager,
            1
        ) {
            override fun onLoadMore(currentPage: Int) {
                if (currentPage <= comicsApiResponse.model!!.count) {
                    if (comicsApiResponse.model!!.count != 1)
                        loadComicsData(currentPage)
                }
            }
        }

        binding.recyclerComics.addOnScrollListener(scrollListener)
        if (comicsApiResponse.model!!.results.isEmpty()) loadComicsData(1)
    }


    private fun loadComicsData(currentPage: Int) {
        characterDetailsVM.getCharacterComicsIndex(marvelCharacter.id.toString(), currentPage)
    }


    private fun comicsIndexResult(apiResponse: ApiResponse<ComicsData>) {
        this.comicsApiResponse = apiResponse
        getBaseFrame().isRefreshing = false
        handleApiResponse(apiResponse) { updateView() }
        if (apiResponse.apiStatus == ApiStatus.OnSuccess)
            setComicsResponseResult(apiResponse.model!!.results)
    }


    private fun setComicsResponseResult(list: ArrayList<Comics>) {
        if (list.isNotEmpty()) {
            comicsAdapter.addAll(list)
            binding.layoutComics.toVisible()
        } else {
            binding.layoutComics.toGone()
            comicsApiResponse.model!!.results.clear()
            comicsAdapter.clear()
        }
    }


    private fun loadEvents() {
        eventsApiResponse.model = ComicsData()
        eventsApiResponse.model!!.results.clear()
        eventsApiResponse.model!!.count = 1

        eventsAdapter.clear()

        val scrollListener = object : OnEndless(
            binding.recyclerEvents.layoutManager as LinearLayoutManager,
            1
        ) {
            override fun onLoadMore(currentPage: Int) {
                if (currentPage <= eventsApiResponse.model!!.count) {
                    if (eventsApiResponse.model!!.count != 1)
                        loadEventsData(currentPage)
                }
            }
        }

        binding.recyclerEvents.addOnScrollListener(scrollListener)
        if (eventsApiResponse.model!!.results.isEmpty()) loadEventsData(1)
    }


    private fun loadEventsData(currentPage: Int) {
        characterDetailsVM.getCharacterEventsIndex(marvelCharacter.id.toString(), currentPage)
    }


    private fun eventsIndexResult(apiResponse: ApiResponse<ComicsData>) {
        this.eventsApiResponse = apiResponse
        getBaseFrame().isRefreshing = false
        handleApiResponse(apiResponse) { updateView() }
        if (apiResponse.apiStatus == ApiStatus.OnSuccess)
            setEventsResponseResult(apiResponse.model!!.results)
    }


    private fun setEventsResponseResult(list: ArrayList<Comics>) {
        if (list.isNotEmpty()) {
            eventsAdapter.addAll(list)
            binding.layoutEvents.toVisible()
        } else {
            binding.layoutEvents.toGone()
            eventsApiResponse.model!!.results.clear()
            eventsAdapter.clear()
        }
    }


    private fun loadSeries() {
        seriesApiResponse.model = ComicsData()
        seriesApiResponse.model!!.results.clear()
        seriesApiResponse.model!!.count = 1

        seriesAdapter.clear()

        val scrollListener = object : OnEndless(
            binding.recyclerSeries.layoutManager as LinearLayoutManager,
            1
        ) {
            override fun onLoadMore(currentPage: Int) {
                if (currentPage <= seriesApiResponse.model!!.count) {
                    if (seriesApiResponse.model!!.count != 1)
                        loadSeriesData(currentPage)
                }
            }
        }

        binding.recyclerSeries.addOnScrollListener(scrollListener)
        if (seriesApiResponse.model!!.results.isEmpty()) loadSeriesData(1)
    }


    private fun loadSeriesData(currentPage: Int) {
        characterDetailsVM.getCharacterSeriesIndex(marvelCharacter.id.toString(), currentPage)
    }


    private fun seriesIndexResult(apiResponse: ApiResponse<ComicsData>) {
        this.seriesApiResponse = apiResponse
        getBaseFrame().isRefreshing = false
        handleApiResponse(apiResponse) { updateView() }
        if (apiResponse.apiStatus == ApiStatus.OnSuccess)
            setSeriesResponseResult(apiResponse.model!!.results)
    }


    private fun setSeriesResponseResult(list: ArrayList<Comics>) {
        if (list.isNotEmpty()) {
            seriesAdapter.addAll(list)
            binding.layoutSeries.toVisible()
        } else {
            binding.layoutSeries.toGone()
            seriesApiResponse.model!!.results.clear()
            seriesAdapter.clear()
        }
    }


    private fun loadStories() {
        storiesApiResponse.model = ComicsData()
        storiesApiResponse.model!!.results.clear()
        storiesApiResponse.model!!.count = 1

        storiesAdapter.clear()

        val scrollListener = object : OnEndless(
            binding.recyclerStories.layoutManager as LinearLayoutManager,
            1
        ) {
            override fun onLoadMore(currentPage: Int) {
                if (currentPage <= storiesApiResponse.model!!.count) {
                    if (storiesApiResponse.model!!.count != 1)
                        loadStoriesData(currentPage)
                }
            }
        }

        binding.recyclerStories.addOnScrollListener(scrollListener)
        if (storiesApiResponse.model!!.results.isEmpty()) loadStoriesData(1)
    }


    private fun loadStoriesData(currentPage: Int) {
        characterDetailsVM.getCharacterStoriesIndex(marvelCharacter.id.toString(), currentPage)
    }


    private fun storiesIndexResult(apiResponse: ApiResponse<ComicsData>) {
        this.storiesApiResponse = apiResponse
        getBaseFrame().isRefreshing = false
        handleApiResponse(apiResponse) { updateView() }
        if (apiResponse.apiStatus == ApiStatus.OnSuccess)
            setStoriesResponseResult(apiResponse.model!!.results)
    }


    private fun setStoriesResponseResult(list: ArrayList<Comics>) {
        if (list.isNotEmpty()) {
            storiesAdapter.addAll(list)
            binding.layoutStories.toVisible()
        } else {
            binding.layoutStories.toGone()
            storiesApiResponse.model!!.results.clear()
            storiesAdapter.clear()
        }
    }

}