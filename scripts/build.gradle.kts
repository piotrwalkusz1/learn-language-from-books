import com.piotrwalkusz.learnlanguagefrombooks.FreeDictEngPol
import de.undercouch.gradle.tasks.download.Download

val engPolDictionary = buildDir.resolve("dictionaries/eng-pol")
val engPolDictionaryLettersPath: String = engPolDictionary.resolve("letters").path

val downloadEngPolDictionary by tasks.registering(Download::class) {
    val path = "https://raw.githubusercontent.com/freedict/fd-dictionaries/master/eng-pol/letters/"
    val source = ('a'..'z').map { "$path$it.xml" }

    src(source)
    dest(engPolDictionaryLettersPath)
    onlyIfModified(true)
    quiet(true)
}

val createEngPolDictionary by tasks.registering {
    dependsOn(downloadEngPolDictionary)

    doLast {
        val parser = FreeDictEngPol()

        val header = "ENGLISH;POLISH"
        val dictionary = File(engPolDictionaryLettersPath).walk()
                .filter { it.isFile }
                .sorted()
                .joinToString("\n") { parser.parse(it) }

        engPolDictionary.resolve("dictionary.csv").writeText(header + "\n" + dictionary)
    }
}
