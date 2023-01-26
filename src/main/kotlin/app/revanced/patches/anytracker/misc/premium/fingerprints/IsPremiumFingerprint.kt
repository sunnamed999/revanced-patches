package app.revanced.patches.anytracker.misc.premium.fingerprints

import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.Opcode

object IsPremiumFingerprint : MethodFingerprint(
    returnType = "V",
    access = AccessFlags.PUBLIC or AccessFlags.FINAL,
    parameters = listOf(),
    opcodes = listOf(
        Opcode.CONST_4,
        Opcode.INVOKE_STATIC
    ),
    strings = listOf("premium", "customerInfo"),
)
