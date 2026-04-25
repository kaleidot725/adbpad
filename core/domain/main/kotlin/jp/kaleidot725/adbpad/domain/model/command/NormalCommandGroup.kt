package jp.kaleidot725.adbpad.domain.model.command

data class NormalCommandGroup(
    val all: List<NormalCommand>,
    val communication: List<NormalCommand>,
    val navigation: List<NormalCommand>,
    val theme: List<NormalCommand>,
    val display: List<NormalCommand>,
    val device: List<NormalCommand>,
    val time: List<NormalCommand>,
    val favorite: List<NormalCommand> = emptyList(),
) {
    companion object {
        val Empty =
            NormalCommandGroup(
                all = emptyList(),
                communication = emptyList(),
                navigation = emptyList(),
                theme = emptyList(),
                display = emptyList(),
                device = emptyList(),
                time = emptyList(),
                favorite = emptyList(),
            )
    }
}
