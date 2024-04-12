plugins {
    java
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.onarandombox.com/content/groups/public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("com.onarandombox.multiversecore:multiverse-core:4.3.12")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}