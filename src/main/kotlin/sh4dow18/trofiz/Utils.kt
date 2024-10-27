package sh4dow18.trofiz
// Get Platform Id using its own name
// Example: "Play Station 5" -> "play-station-5"
fun getPlatformId(name: String): String {
    return name.lowercase().replace(" ", "-")
}
// Get Platform Name using its own id
// Example: "play-station-5" -> "Play Station 5"
fun getPlatformName(id: String): String {
    return id.replace("-", " ").split(" ").joinToString(" ") {
        it.replaceFirstChar {
            char -> char.uppercaseChar()
        }
    }
}