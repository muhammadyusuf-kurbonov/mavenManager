package uz.muhammadyusuf.kurbonov.maven.manager.model

data class Dependency(
        var groupName: String,
        var artifactID: String,
        var version: String
) {
    companion object {

        fun fromID(id: String): Dependency {
            val parts = id.trimEnd().split(":")
            if (parts.size != 3) throw IllegalArgumentException("Invalid artifact id $id")
            return Dependency(
                    groupName = parts[0],
                    artifactID = parts[1],
                    version = parts[2]
            )

        }
    }

    fun toID(): String = "$groupName:$artifactID:$version"
}