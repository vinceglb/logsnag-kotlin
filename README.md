<div align="center">
	<img src="https://logsnag.com/og-image.png" alt="LogSnag"/>
	<br>
	<h1>LogSnag Kotlin</h1>
	<p>Get notifications and track your project events in your Kotlin project.</p>
	<div>
		<img src="https://img.shields.io/maven-central/v/io.github.vinceglb/logsnag-kotlin" alt="LogSnag Kotlin" />
		<a href="https://discord.gg/dY3pRxgWua"><img src="https://img.shields.io/discord/922560704454750245?color=%237289DA&label=Discord" alt="Discord"></a>
		<a href="https://docs.logsnag.com"><img src="https://img.shields.io/badge/Docs-LogSnag" alt="Documentation"></a>
	</div>
	<div>
		<img src="https://img.shields.io/badge/Platform-Android-brightgreen.svg?logo=android" alt="Badge Android" />
		<img src="https://img.shields.io/badge/Platform-iOS-lightgrey.svg?logo=apple" alt="Badge iOS" />
		<img src="https://img.shields.io/badge/Platform-JVM-red.svg?logo=openjdk" alt="Badge JVM" />
	</div>
	<br>
	<br>
</div>

## 📦 Setup

1. Add the following to your `build.gradle.kts` file:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.vinceglb:logsnag-kotlin:0.1.0")
}
```

2. Choose and add to your dependencies one of [Ktor's engines](https://ktor.io/docs/http-client-engines.html). In case of Kotlin Multiplatform project, add an engine to each target.

## 🧑‍💻 Usage

### ⚡️ Initialize Client

Create an instance of `LogSnag` client:

```kotlin
val logSnag = LogSnag(
    token = "your-token",
    project = "your-project",
)
```

### 📝 Track Event

Send a track event to LogSnag:

```kotlin
logSnag.track(
    channel = "waitlist",
    event = "User Joined",
    description = "A user joined the waitlist",
    icon = "🎉",
    userId = "user_123",
    tags = mapOf("source" to "google"),
    notify = true,
)
```

### 👤 User Properties

Identify a user and set their properties:

```kotlin
logSnag.identify(
    userId = "user_123",
    properties = mapOf(
        "name" to "John Doe",
        "email" to "john@doe.com",
        "plan" to "premium",
    ),
)
```

### ✨ Track Insight

Send a insight track to LogSnag:

```kotlin
logSnag.insightTrack(
    title = "User Count",
    value = "100",
    icon = "👨",
)
```

### ➕ Increment Insight

Increment an insight track to LogSnag:

```kotlin
logSnag.insightIncrement(
    title = "User Count",
    value = 1,
    icon = "👨",
)
```

## 🤩 Contributing

To be able to work on the project locally, you need to update the `local.properties` file with your LogSnag token and project ID:

```properties
LOGSNAG_TOKEN=your-token
LOGSNAG_PROJECT=your-project
```
