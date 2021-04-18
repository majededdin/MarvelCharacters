package majed.eddin.marvelcharacters.ui.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import majed.eddin.marvelcharacters.data.model.service.MarvelCharacter
import majed.eddin.marvelcharacters.databinding.ItemSearchCharacterBinding
import majed.eddin.marvelcharacters.utils.Utils.Companion.highlight

class SearchCharactersAdapter(
    private val context: Context,
    private val callback: CharactersCallBack
) : RecyclerView.Adapter<SearchCharactersAdapter.ViewHolder>() {

    private var searchString: String? = null
    private var items: ArrayList<MarvelCharacter> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSearchCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                Picasso.get().load(getFullImagePath())
                    .into(binding.imgCharacter)

                if (searchString != null)
                    binding.txtCharacterName.text = highlight(context, searchString!!, name)
                else
                    binding.txtCharacterName.text = name
            }
        }
    }


    fun addAll(searchString: String?, items: List<MarvelCharacter>) {
        this.searchString = searchString
        for (result in items) {
            add(result)
        }
    }


    private fun add(r: MarvelCharacter) {
        items.add(r)
        notifyItemInserted(items.size - 1)
    }


    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = items.size


    inner class ViewHolder(val binding: ItemSearchCharacterBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.imgCharacter.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val marvelCharacter = items[bindingAdapterPosition]
            callback.onDefaultClicked(marvelCharacter)
        }
    }

    interface CharactersCallBack {
        fun onDefaultClicked(marvelCharacter: MarvelCharacter)
    }

}