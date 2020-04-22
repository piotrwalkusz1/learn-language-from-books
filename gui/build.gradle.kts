plugins {
    application

    id("org.openjfx.javafxplugin") version "0.0.8"
}

dependencies {
    implementation(project(":core"))
    implementation("no.tornado:tornadofx:1.7.20")
}

javafx {
    version = "14"
    modules = listOf("javafx.controls")
}

application {
    mainClassName = "com.piotrwalkusz.learnlanguagefrombooks.gui.Application"
}
