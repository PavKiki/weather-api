dependencies {
    implementation(project(":core"))

    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("ch.qos.logback:logback-classic:1.4.11")

    api(project(":utils"))
}