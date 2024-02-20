package will.denne.artwork.data.model

import will.denne.artwork.data.NetworkResult

fun artWorkDetailDataBuilder(
    id: Int = 1,
    title: String = "title",
    artist: String = "artist",
    imageId: String = "imageId",
    thumbnail: String = "thumbnail",
    onLoanDisplay: String = "onLoanDisplay",
    dateDisplay: String = "dateDisplay",
    description: String = "description"
): ArtworkDetailData {
    return ArtworkDetailData(
        id = id,
        title = title,
        artist = artist,
        imageId = imageId,
        thumbnail = Thumbnail(thumbnail),
        onLoanDisplay = onLoanDisplay,
        dateDisplay = dateDisplay,
        description = description
    )
}