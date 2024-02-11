package will.denne.artwork.data.model

fun artWorkDataBuilder(
    data: List<ArtworkListItemData> = listOf(artworkListBuilder()),
    pagination: Pagination = paginationBuilder()
): ArtworkData {
    return ArtworkData(
        data = data,
        pagination = pagination
    )
}

fun paginationBuilder(
    totalPages: Int = 1
): Pagination {
    return Pagination(
        totalPages = totalPages
    )
}

fun artworkListBuilder(
    id: Int = 1,
    title: String = "title",
    artist: String = "artist"
): ArtworkListItemData {
    return ArtworkListItemData(
        id = id,
        title = title,
        artist = artist
    )
}