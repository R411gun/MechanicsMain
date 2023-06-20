/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("me.deecaad.java-conventions")
}

repositories {
    maven {
        name = "lumine-repo"
        url = uri("http://mvn.lumine.io/repository/maven-public/")
        isAllowInsecureProtocol = true
    }
    mavenCentral()
}

dependencies {
    api("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    implementation(project(":MechanicsCore"))
    implementation("org.bstats:bstats-bukkit:3.0.1")
    implementation("me.cjcrafter:mechanicsautodownload:1.3.1")

    implementation("net.kyori:adventure-api:4.13.0")
    implementation("net.kyori:adventure-text-serializer-legacy:4.13.0")
    implementation("net.kyori:adventure-platform-bukkit:4.3.0")
    implementation("net.kyori:adventure-text-minimessage:4.13.0")

    compileOnly("com.comphenix.protocol:ProtocolLib:5.0.0")

    implementation("io.lumine:Mythic-Dist:5.0.1-SNAPSHOT")
    compileOnly(files(file("../lib/crackshot/CrackShotPlus.jar")))
    compileOnly(files(file("../lib/crackshot/CrackShot.jar")))
    compileOnly(files(file("../lib/vivecraft/Vivecraft_Spigot_Extensions.jar")))
}

tasks.test {
    useJUnitPlatform()
}

description = "WeaponMechanics"
