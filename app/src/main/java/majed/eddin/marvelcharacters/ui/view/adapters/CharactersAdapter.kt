package majed.eddin.marvelcharacters.ui.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import majed.eddin.marvelcharacters.R
import majed.eddin.marvelcharacters.data.model.service.MarvelCharacter
import majed.eddin.marvelcharacters.databinding.ItemCharacterBinding
import majed.eddin.marvelcharacters.ui.base.ParallaxViewHolder

class CharactersAdapter(
    private val callback: CharactersCallBack
) : RecyclerView.Adapter<CharactersAdapter.ViewHolder>() {

    private var items: ArrayList<MarvelCharacter> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                Picasso.get().load(thumbnail.path.plus(".").plus(thumbnail.extension))
                    .into(binding.imgCharacter)

                binding.txtCharacterName.text = name
            }
        }
    }


    fun addAll(items: List<MarvelCharacter>) {
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


    inner class ViewHolder(val binding: ItemCharacterBinding) :
        ParallaxViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.imgCharacter.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val marvelCharacter = items[bindingAdapterPosition]
            callback.onDefaultClicked(marvelCharacter)
        }

        override fun getParallaxImageId(): Int {
            return R.id.img_character
        }
    }

    interface CharactersCallBack {
        fun onDefaultClicked(marvelCharacter: MarvelCharacter)
    }

}