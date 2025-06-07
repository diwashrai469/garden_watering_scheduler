import kotlinx.coroutines.flow.Flow

class PlantRepository(private val plantDao: PlantDao) {
    val allPlants: Flow<List<Plant>> = plantDao.getAllPlants()

    suspend fun insertPlant(plant: Plant) {
        plantDao.insertPlant(plant)
    }
}
