# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Data\Programs\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Retrolambda
-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*

# SLF4J, logback-android
-keep class ch.qos.** { *; }
-keep class org.slf4j.** { *; }
-keepattributes *Annotation*
-dontwarn ch.qos.logback.core.net.*

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# Okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**

