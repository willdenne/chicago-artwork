package will.denne.artwork.util

import assertk.assertions.isEqualTo
import assertk.tableOf
import org.junit.Test

class ImageLinkGeneratorTest {
    @Test
    fun `generateImageUrl should return correct url`() {
        tableOf("imageId", "expectedUrl")
            .row("123" as String?, "https://www.artic.edu/iiif/2/123/full/843,/0/default.jpg" as String?)
            .row("9857859", "https://www.artic.edu/iiif/2/9857859/full/843,/0/default.jpg")
            .row("d0979087-dc1b-f259-a23f-169cdced27ee", "https://www.artic.edu/iiif/2/d0979087-dc1b-f259-a23f-169cdced27ee/full/843,/0/default.jpg")
            .row(null, null)
            .forAll { imageId, expectedUrl ->
                val result = ImageLinkGenerator.generateImageUrl(imageId)
                assertk.assertThat(result).isEqualTo(expectedUrl)
            }
    }
}
