import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plants")
data class Plant(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: String,
    val scheduleType: String, // "daily" or "custom"
    val days: String, // comma-separated if scheduleType == "custom"
    val times: String, // comma-separated times like "07:00 AM,06:00 PM"
    val photoUri: String? // store URI as string
)
