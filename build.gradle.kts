plugins {
    id("java")
}

subprojects {
    apply(plugin = "java-library")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.slf4j:slf4j-api:1.7.36")
        implementation("ch.qos.logback:logback-classic:1.4.11")

        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks.test {
        useJUnitPlatform()
    }
}