buildscript {
    dependencies {
        classpath("com.joom.paranoid:paranoid-gradle-plugin:0.3.14")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
}

tasks {
    register("clean", Delete::class) {
        delete(buildDir)
    }
}