package sh4dow18.trofiz

import org.springframework.data.jpa.repository.JpaRepository
// Get Platform Id using its own name
// Example: "Play Station 5" -> "play-station-5"
fun getIdByName(name: String): String {
    return name.lowercase().replace(" ", "-")
}
// Help to Connect Entities with Many-to-Many Relationships using the requests list to get all entities
fun <T, U : NamedEntity> connectEntities(
    repository: JpaRepository<T, String>,
    requestsList: Set<U>,
): Set<T> {
    val idsList = requestsList.map { getIdByName(it.name) }
    val entitiesList = repository.findAllById(idsList)
    return repository.saveAll(entitiesList).toSet()
}
// Like Connect Entities but it is for tests
fun <T, U : NamedEntity> connectEntitiesTest(
    repository: JpaRepository<T, String>,
    requestsList: Set<U>,
): Set<T> {
    val idsList = requestsList.map { getIdByName(it.name) }
    val entitiesList = repository.findAllById(idsList)
    return entitiesList.toSet()
}
