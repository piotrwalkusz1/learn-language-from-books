tasks.register<de.undercouch.gradle.tasks.download.Download>("downloadLetters") {
    val path = "https://raw.githubusercontent.com/freedict/fd-dictionaries/master/eng-pol/letters/"
    val source = ('a'..'z').map { "$path$it.xml" }

    src(source)
    dest(buildDir.resolve("letters"))
    onlyIfModified(true)
}
