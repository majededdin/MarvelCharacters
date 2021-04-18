package majed.eddin.marvelcharacters.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import majed.eddin.marvelcharacters.R
import majed.eddin.marvelcharacters.data.model.service.Comics
import majed.eddin.marvelcharacters.databinding.ItemComicBinding

class ComicsAdapter : RecyclerView.Adapter<ComicsAdapter.ViewHolder>() {

    private var items: ArrayList<Comics> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemComicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                if (getFullImagePath() != null)
                    binding.imgComic.loadImage(getFullImagePath())
                else
                    binding.imgComic.loadImage(R.drawable.ic_logo)

                binding.txtComicName.text = title
            }
        }
    }


    fun addAll(items: List<Comics>) {
        for (result in items) {
            add(result)
        }
    }


    private fun add(r: Comics) {
        items.add(r)
        notifyItemInserted(items.size - 1)
    }


    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = items.size


    inner class ViewHolder(val binding: ItemComicBinding) :
        RecyclerView.ViewHolder(binding.root)

}