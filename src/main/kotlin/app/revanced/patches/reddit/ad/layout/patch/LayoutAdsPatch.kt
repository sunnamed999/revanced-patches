package app.revanced.patches.reddit.ad.layout.patch

import app.revanced.extensions.doRecursively
import app.revanced.extensions.startsWithAny
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patches.reddit.ad.layout.annotations.LayoutAdsCompatibility
import org.w3c.dom.Element

@Patch
@Name("layout-reddit-ads")
@Description("Removes layout ads from the Reddit frontpage and subreddits.")
@LayoutAdsCompatibility
@Version("0.0.1")
class LayoutAdsPatch : ResourcePatch {
    private val resourceFileNames = arrayOf(
        "\$merge_listheader_link_detail"
    )

    private val replacements = arrayOf(
        "height",
        "width"
    )

    override fun execute(context: ResourceContext): PatchResult {
        context.forEach {

            if (!it.name.startsWithAny(*resourceFileNames)) return@forEach

            // for each file in the "layouts" directory replace all necessary attributes content
            context.xmlEditor[it.absolutePath].use { editor ->
                editor.file.doRecursively {
                    replacements.forEach replacement@{ replacement ->
                        if (it !is Element) return@replacement

                        it.getAttributeNode("android:layout_$replacement")?.let { attribute ->
                            attribute.textContent = "0.0dip"
                        }
                    }
                }
            }
        }

        return PatchResultSuccess()
    }
}

