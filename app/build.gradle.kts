import java.io.FileInputStream
import java.util.Properties

val keystorePropertiesFile = rootProject.file("keystore/keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "1.9.0"
    id("com.google.devtools.ksp") version "1.9.0-1.0.12"
    id("com.joom.paranoid")
}

android {
    signingConfigs {
        create("release") {
            enableV1Signing = true
            enableV2Signing = true
            enableV3Signing = true
            enableV4Signing = null
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }
    compileSdk = 33
    namespace = "com.luckyzyx.luckytool"
    defaultConfig {
        applicationId = "com.luckyzyx.luckytool"
        minSdk = 30
        //noinspection ExpiredTargetSdkVersion
        targetSdk = 28
        versionCode = getVersionCode()
        versionName = "1.0.12_beta"
        buildConfigField("String", "APP_CENTER_SECRET", "\"${getAppCenterSecret()}\"")
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isDebuggable = true
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    buildFeatures {
        aidl = true
        viewBinding = true
        buildConfig = true
    }
    applicationVariants.all {
        val buildType = buildType.name
        println("buildType -> $buildType")
        outputs.all {
            @Suppress("DEPRECATION")
            if (this is com.android.build.gradle.api.ApkVariantOutput) {
                if (buildType == "release") outputFileName =
                    "LuckyTool_v${versionName}(${versionCode}).apk"
                if (buildType == "debug") outputFileName =
                    "LuckyTool_v${versionName}(${versionCode})_debug.apk"
                println("outputFileName -> $outputFileName")
            }
        }
    }
    androidResources.additionalParameters.addAll(
        arrayOf("--allow-reserved-package-id", "--package-id", "0x64")
    )
}

dependencies {
//    implementation(fileTree("libs").include("*.jar"))

    //Xposed基础
    compileOnly("de.robv.android.xposed:api:82")
    //YukiHookAPI ksp
    implementation("com.highcapable.yukihookapi:api:1.1.11")
    ksp("com.highcapable.yukihookapi:ksp-xposed:1.1.11")

//    implementation files("libs/yukihookapi-1.1.5-fix-pkgname-equals.jar")
//    ksp files("libs/yukihookapi-ksp-xposed-1.1.5-beta2.jar")

    //Material主题
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    //约束布局
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //快速创建Settings
    implementation("androidx.preference:preference-ktx:1.2.1")
    //下拉刷新控件
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    //Navigation
    //noinspection GradleDependency
    implementation("androidx.navigation:navigation-fragment-ktx:2.4.2")
    //noinspection GradleDependency
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    //一个强大并且灵活的RecyclerViewAdapter
//    implementation 'com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.11'
    // 权限请求框架
    implementation("com.github.getActivity:XXPermissions:18.2")
    //崩溃日志显示
    implementation("com.github.simplepeng.SpiderMan:spiderman:v1.1.9")
    // MoShi JSON ksp 注释处理
    val moshiVersion = "1.15.0"
    implementation("com.squareup.moshi:moshi:${moshiVersion}")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:${moshiVersion}")
    //Rikka
//    implementation "dev.rikka.rikkax.core:core:1.4.1"
    //kotlin协程
    val kotlinxCoroutinesVersion = "1.7.3"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${kotlinxCoroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${kotlinxCoroutinesVersion}")
    //Net OkHttp相关
    //noinspection GradleDependency
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.github.liangjingkanji:Net:3.5.8")
    //Apache DigestUtils md5 sha256
//    implementation 'com.google.firebase:firebase-crashlytics-buildtools:2.9.2'
    //libsu
    val libsuVersion = "5.0.5"
    implementation("com.github.topjohnwu.libsu:core:${libsuVersion}")
    implementation("com.github.topjohnwu.libsu:service:${libsuVersion}")
//    implementation "com.github.topjohnwu.libsu:nio:${libsuVersion}"

    //Microsoft AppCenter
    val appCenterSdkVersion = "5.0.2"
    implementation("com.microsoft.appcenter:appcenter-analytics:${appCenterSdkVersion}")
    implementation("com.microsoft.appcenter:appcenter-crashes:${appCenterSdkVersion}")
}

fun getVersionCode(): Int {
    val propsFile = file("version.properties")
    val properties = Properties()
    properties.load(FileInputStream(propsFile))
    var vCode = properties["versionCode"].toString().toInt()
    properties["versionCode"] = (++vCode).toString()
    properties.store(propsFile.writer(), null)
    return vCode
}

fun getAppCenterSecret(): String {
    var content = ""
    val file = rootProject.file(".secret/APP_CENTER_SECRET")
    if (file.exists()) file.forEachLine { content = it }
    return content
}