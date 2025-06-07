import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gradenwateringscheduler.R
import com.app.gradenwateringscheduler.databinding.ItemPlantBinding

class PlantsAdapter(private val plants: List<Plant>) :
    RecyclerView.Adapter<PlantsAdapter.PlantViewHolder>() {

    inner class PlantViewHolder(val binding: ItemPlantBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPlantBinding.inflate(inflater, parent, false)
        return PlantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plants[position]
        holder.binding.plantName.text = plant.name

        // Display schedule info (simple example)
        val scheduleInfo = when (plant.scheduleType) {
            "daily" -> "Daily at ${plant.times}"
            "custom" -> "On ${plant.days} at ${plant.times}"
            else -> "Schedule unknown"
        }
        holder.binding.plantSchedule.text = scheduleInfo

        // Load photo if URI available, else show placeholder
        if (!plant.photoUri.isNullOrEmpty()) {
            try {
                val uri = Uri.parse(plant.photoUri)
                holder.binding.plantPhoto.setImageURI(uri)
            } catch (e: Exception) {
                holder.binding.plantPhoto.setImageResource(R.drawable.flower_placeholder)
            }
        } else {
            holder.binding.plantPhoto.setImageResource(R.drawable.flower_placeholder)
        }
    }

    override fun getItemCount() = plants.size
}
