import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.openbakery.xcode.Destination

plugins {
    id("org.openbakery.xcode-plugin")
}

xcodebuild {
    target = "App"
    scheme = "App"
    workspace = "App.xcworkspace"
    xcodeVersion = "12"
    setDestination(Destination("iPhone 12 Pro Max"))
}

tasks.create<Exec>("tuistGenerate") {
    commandLine("tuist", "generate")
}

tasks.named("xcodebuildConfig").dependsOn("tuistGenerate")
