plugins {
    `java-library`
}

dependencies {
    implementation("org.languagetool:language-en:4.8")
    implementation("org.apache.pdfbox:pdfbox:2.0.19")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}
