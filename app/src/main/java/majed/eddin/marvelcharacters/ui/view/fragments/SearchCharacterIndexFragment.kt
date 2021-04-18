package majed.eddin.marvelcharacters.ui.view.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import majed.eddin.marvelcharacters.R
import majed.eddin.marvelcharacters.data.consts.Params.Companion.CHARACTER
import majed.eddin.marvelcharacters.data.model.service.CharactersData
import majed.eddin.marvelcharacters.data.model.service.MarvelCharacter
import majed.eddin.marvelcharacters.data.remote.ApiResponse
import majed.eddin.marvelcharacters.data.remote.ApiStatus
import majed.eddin.marvelcharacters.databinding.FragmentSearchCharacterIndexBinding
import majed.eddin.marvelcharacters.ui.base.BaseFragment
import majed.eddin.marvelcharacters.ui.view.activities.CharacterDetailsActivity
import majed.eddin.marvelcharacters.ui.view.adapters.SearchCharactersAdapter
import majed.eddin.marvelcharacters.ui.viewModel.CharactersIndexVM
import majed.eddin.marvelcharacters.utils.extentionUtils.hideKeyboard
import majed.eddin.marvelcharacters.utils.extentionUtils.intentWithSlideInOut
import majed.eddin.marvelcharacters.utils.extentionUtils.toGone
import majed.eddin.marvelcharacters.utils.extentionUtils.toVisible
import majed.eddin.marvelcharacters.utils.recyclerUtils.OnEndless

class SearchCharacterIndexFragment : BaseFragment<CharactersIndexVM>(), TextWatcher,
    SearchCharactersAdapter.CharactersCallBack, View.OnClickListener {

    private lateinit var charactersIndexVM: CharactersIndexVM

    private lateinit var adapter: SearchCharactersAdapter
    private var apiResponse: ApiResponse<CharactersData> = ApiResponse()

    private var name: String? = null

    private val binding get() = _binding!!
    private var _binding: FragmentSearchCharacterIndexBinding? = null


    override fun setLayout(): Int = R.layout.fragment_search_character_index


    override fun getViewModel(): Class<CharactersIndexVM> = CharactersIndexVM::class.java


    override fun viewModelInstance(viewModel: CharactersIndexVM?) {
        charactersIndexVM = viewModel!!
    }


    override fun onDestroyView() {
        super.onDestroyView()
        // calling this to avoid memory leak.
        _binding = null
    }


    companion object {
        fun getInstance() = SearchCharacterIndexFragment()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchCharacterIndexBinding.bind(view)
        stopOnSwipeRefreshStatus()
        viewInit()

        charactersIndexVM.charactersIndexResult.observe(
            viewLifecycleOwner,
            this::charactersIndexResult
        )
    }


    override fun updateView() {
        binding.progressBar.toVisible()
        apiResponse.model = CharactersData()
        apiResponse.model!!.results.clear()
        apiResponse.model!!.count = 1

        adapter.clear()

        val scrollListener = object : OnEndless(
            binding.recyclerMarvelCharacter.layoutManager as LinearLayoutManager,
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

        binding.recyclerMarvelCharacter.addOnScrollListener(scrollListener)
        if (apiResponse.model!!.results.isEmpty()) loadPageData(1)
    }


    private fun loadPageData(currentPage: Int) {
        charactersIndexVM.getCharactersIndex(name, currentPage)
    }


    private fun charactersIndexResult(apiResponse: ApiResponse<CharactersData>) {
        this.apiResponse = apiResponse
        getSwipeRefresh().isRefreshing = false
        handleApiResponse(apiResponse) { updateView() }
        if (apiResponse.apiStatus == ApiStatus.OnSuccess)
            setResponseResult(apiResponse.model!!.results)
    }


    private fun setResponseResult(list: ArrayList<MarvelCharacter>) {
        if (list.isNotEmpty()) {
            binding.layoutEmptyState.toGone()
            binding.recyclerMarvelCharacter.toVisible()
            adapter.addAll(name, list)
        } else {
            binding.recyclerMarvelCharacter.toGone()
            binding.progressBar.toGone()
            binding.layoutEmptyState.toVisible()
            apiResponse.model!!.results.clear()
            adapter.clear()
        }
        binding.progressBar.toGone()
    }


    override fun viewInit() {

        binding.txtEmptyTxt.text = getString(R.string.no_results)

        binding.editSearch.addTextChangedListener(this)
        binding.txtCancel.setOnClickListener(this)

        adapter = SearchCharactersAdapter(requireContext(), this)
        binding.recyclerMarvelCharacter.adapter = adapter
        binding.recyclerMarvelCharacter.layoutManager = LinearLayoutManager(requireContext())
    }


    override fun onDefaultClicked(marvelCharacter: MarvelCharacter) {
        val bundle = Bundle()
        bundle.putParcelable(CHARACTER, marvelCharacter)
        requireActivity().intentWithSlideInOut(
            CharacterDetailsActivity::class.java, bundle
        )
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {

            R.id.txt_cancel -> {
                getCustomView().hideKeyboard()
                setInputSearch(null)
                baseActivity.onBackPressed()
            }

        }

    }


    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }


    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        setInputSearch(p0.toString().trim())
    }


    override fun afterTextChanged(p0: Editable?) {

    }


    private fun setInputSearch(query: String?) {
        name = if (!query.isNullOrEmpty())
            query
        else {
            binding.editSearch.text!!.clear()
            null
        }
        updateView()

    }


}