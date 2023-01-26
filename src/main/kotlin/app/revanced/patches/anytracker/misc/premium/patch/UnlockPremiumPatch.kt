package app.revanced.patches.anytracker.misc.premium.patch

import app.revanced.extensions.toErrorResult
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patches.anytracker.misc.premium.annotations.UnlockPremiumCompatibility
import app.revanced.patches.anytracker.misc.premium.fingerprints.IsPurchasedFlowFingerprint
import app.revanced.patches.anytracker.misc.premium.fingerprints.IsPremiumFingerprint
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction

@Patch
@Name("unlock-premium")
@Description("Unlocks all premium features.")
@UnlockPremiumCompatibility
@Version("0.0.1")
class UnlockPremiumPatch : BytecodePatch(
    listOf(
        IsPurchasedFlowFingerprint,
        IsPremiumFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {

        try {
            val method = IsPurchasedFlowFingerprint.result!!.mutableMethod
            method.addInstructions(
                0,
                """
                	const/4 v0, 0x1
                	invoke-static {v0}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;
                	move-result-object v0
                	invoke-static {v0}, Lkotlinx/coroutines/flow/FlowKt;->flowOf(Ljava/lang/Object;)Lkotlinx/coroutines/flow/Flow;
                	move-result-object v1
                	const/4 v2, 0x0
                	const-wide/16 v3, 0x0
                	const/4 v5, 0x3
                	const/4 v6, 0x0
                	invoke-static/range {v1 .. v6}, Landroidx/lifecycle/FlowLiveDataConversions;->asLiveData${'$'}default(Lkotlinx/coroutines/flow/Flow;Lkotlin/coroutines/CoroutineContext;JILjava/lang/Object;)Landroidx/lifecycle/LiveData;
                	move-result-object v0
                	return-object v0
                """
            )
        } catch (_: Exception) {
            IsPremiumFingerprint.result?.let {
                with (it.mutableMethod) {
                        val insertIndex = it.scanResult.patternScanResult!!.endIndex
                        val register = (implementation!!.instructions[insertIndex - 1] as OneRegisterInstruction).registerA

                        addInstruction(
                            insertIndex,
                            "const/4 v$register, 0x1"
                        )
                }
            } ?: return IsPremiumFingerprint.toErrorResult()
        }

        return PatchResultSuccess()
    }
}
