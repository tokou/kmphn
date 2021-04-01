import ProjectDescription

// MARK: - Project

let name = "App"
let platform: Platform = .iOS

let frameworkPath = "../common/root/build/xcode-frameworks/Hackernews.framework"

let script = """
cd $SRCROOT/..
./gradlew :common:root:packForXCode -PXCODE_CONFIGURATION=${CONFIGURATION}
cd $SRCROOT
"""

let targetActions = [
    TargetAction.pre(
        script: script,
        name: "Build Hackernews framework",
        basedOnDependencyAnalysis: true
    )
]

let infoPlist: [String: InfoPlist.Value] = [
    "CFBundleShortVersionString": "1.0",
    "CFBundleVersion": "1",
    "UIMainStoryboardFile": "",
    "UILaunchStoryboardName": "LaunchScreen"
]

let organizationName = "com.github.tokou"
let bundleId = "\(organizationName).hackernews"

let mainTarget = Target(
    name: name,
    platform: platform,
    product: .app,
    bundleId: bundleId,
    infoPlist: .extendingDefault(with: infoPlist),
    sources: ["Targets/\(name)/Sources/**"],
    resources: ["Targets/\(name)/Resources/**"],
    actions: targetActions,
    dependencies: [
        .framework(path: .relativeToRoot(frameworkPath))
    ]
)

let testTarget = Target(
    name: "\(name)Tests",
    platform: platform,
    product: .unitTests,
    bundleId: "\(bundleId).tests",
    infoPlist: .default,
    sources: ["Targets/\(name)/Tests/**"],
    dependencies: [
        .target(name: "\(name)")
    ]
)

let targets = [mainTarget, testTarget]

let updatedSettings: SettingsDictionary = [
    "CLANG_WARN_QUOTED_INCLUDE_IN_FRAMEWORK_HEADER": .string("YES")
]
let settings = Settings(base: updatedSettings)

let project = Project(
    name: name,
    organizationName: organizationName,
    settings: settings,
    targets: targets
)
