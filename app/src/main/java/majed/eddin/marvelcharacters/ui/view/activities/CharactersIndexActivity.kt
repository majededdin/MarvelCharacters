package majed.eddin.marvelcharacters.ui.view.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import majed.eddin.marvelcharacters.R
import majed.eddin.marvelcharacters.data.model.service.Data
import majed.eddin.marvelcharacters.data.model.service.MarvelCharacter
import majed.eddin.marvelcharacters.data.remote.ApiResponse
import majed.eddin.marvelcharacters.data.remote.ApiStatus
import majed.eddin.marvelcharacters.databinding.LayoutGlobalListBinding
import majed.eddin.marvelcharacters.ui.base.BaseBarActivity
import majed.eddin.marvelcharacters.ui.view.adapters.CharactersAdapter
import majed.eddin.marvelcharacters.ui.view.fragments.SearchCharacterIndexFragment
import majed.eddin.marvelcharacters.ui.viewModel.CharactersIndexVM
import majed.eddin.marvelcharacters.utils.extentionUtils.toGone
import majed.eddin.marvelcharacters.utils.extentionUtils.toVisible
import majed.eddin.marvelcharacters.utils.recyclerUtils.OnEndless

class CharactersIndexActivity : BaseBarActivity<CharactersIndexVM>(),
    CharactersAdapter.CharactersCallBack, View.OnClickListener {

    private lateinit var charactersIndexVM: CharactersIndexVM
    private lateinit var binding: LayoutGlobalListBinding

    private lateinit var adapter: CharactersAdapter
    private var apiResponse: ApiResponse<Data> = ApiResponse()

    override fun getViewModel(): Class<CharactersIndexVM> = CharactersIndexVM::class.java

    override fun viewModelInstance(viewModel: CharactersIndexVM?) {
        charactersIndexVM = viewModel!!
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutGlobalListBinding.inflate(layoutInflater, getBaseFrame(), true)
        viewInit()
        updateView()

        charactersIndexVM.charactersIndexResult.observe(this, this::charactersIndexResult)

    }


    override fun updateView() {
        binding.progressBar.toVisible()
        apiResponse.model = Data()
        apiResponse.model!!.results.clear()
        apiResponse.model!!.count = 1

        adapter.clear()

        val scrollListener = object : OnEndless(
            binding.recyclerGlobalList.layoutManager as LinearLayoutManager,
            1
        ) {
            override fun onLoadMore(currentPage: Int) {
                if (currentPage <= apiResponse.model!!.count) {
                    if (apiResponse.model!!.count != 1) {
                        binding.progressBar.toVisible()
                        loadPageData(currentPage)
                    }
                }
            }
        }

        binding.recyclerGlobalList.addOnScrollListener(scrollListener)
        if (apiResponse.model!!.results.isEmpty()) loadPageData(1)
    }


    private fun loadPageData(currentPage: Int) {
        charactersIndexVM.getCharactersIndex(null, currentPage)
    }


    private fun charactersIndexResult(apiResponse: ApiResponse<Data>) {
        this.apiResponse = apiResponse
        getBaseFrame().isRefreshing = false
        handleApiResponse(apiResponse) { updateView() }
        if (apiResponse.apiStatus == ApiStatus.OnSuccess)
            setResponseResult(apiResponse.model!!.results)
    }


    private fun setResponseResult(list: ArrayList<MarvelCharacter>) {
        if (list.isNotEmpty()) {
            binding.layoutEmptyState.toGone()
            binding.recyclerGlobalList.toVisible()
            adapter.addAll(list)
        } else {
            binding.recyclerGlobalList.toGone()
            binding.layoutEmptyState.toVisible()
            apiResponse.listOfModel.clear()
            adapter.clear()
        }
        binding.progressBar.toGone()
    }


    override fun viewInit() {
        binding.txtEmptyTxt.text = getString(R.string.no_results)

        getSearchButton().setOnClickListener(this)

        adapter = CharactersAdapter(this)
        binding.recyclerGlobalList.adapter = adapter
        binding.recyclerGlobalList.layoutManager = LinearLayoutManager(this)
    }


    override fun onDefaultClicked(marvelCharacter: MarvelCharacter) {
//        val bundle = Bundle()
//        bundle.putParcelable(Params.CHARACTER, marvelCharacter)
//        intentResultWithSlideInOut(
//            CharacterDetailsActivity::class.java, bundle
//        )
    }

    override fun onClick(p0: View?) {
        if (p0!!.id == R.id.btn_search)
            loadSearch(SearchCharacterIndexFragment.getInstance())
    }

}