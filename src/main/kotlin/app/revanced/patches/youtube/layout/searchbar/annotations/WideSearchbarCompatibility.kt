package app.revanced.patches.youtube.layout.searchbar.annotations

import app.revanced.patcher.annotation.Compatibility
import app.revanced.patcher.annotation.Package

@Compatibility(
    [Package(
        "com.google.android.youtube", arrayOf("17.49.37", "18.03.42", "18.05.40")
    )]
)
@Target(AnnotationTarget.CLASS)
internal annotation class WideSearchbarCompatibility