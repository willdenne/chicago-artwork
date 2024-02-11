package will.denne.artwork.util

object ImageLinkGenerator {
    fun generateImageUrl(imageId: String?): String? {
        return if (imageId == null) {
            null
        } else {
            "https://www.artic.edu/iiif/2/${imageId}/full/843,/0/default.jpg"
        }
    }
}
