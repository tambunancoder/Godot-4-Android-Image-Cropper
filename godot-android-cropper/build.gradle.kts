plugins {
    id("com.android.library") version "8.4.0" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

val cleanBuildDir by tasks.registering(Delete::class) {
    delete(rootProject.buildDir)
}

allprojects {
    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-Xlint:unchecked")
        options.compilerArgs.add("-Xlint:deprecation")
    }
}