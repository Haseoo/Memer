import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.3"
	id("io.spring.dependency-management") version "1.0.13.RELEASE"
	id("jacoco")
	id("org.sonarqube") version "3.4.0.2513"
//	id("com.vaadin") version "23.1.7"
	war
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "com.github.haseoo"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	maven {
		url = uri("https://m2.dv8tion.net/releases")
		name = "m2-dv8tion"
	}
}

//extra["vaadinVersion"] = "23.1.7"

dependencies {
	//implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	//implementation("com.vaadin:vaadin-spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("net.dv8tion:JDA:4.4.0_352")
	implementation("io.lettuce:lettuce-core:6.2.0.RELEASE")
	implementation("org.springframework.boot:spring-boot-starter-tomcat")
	implementation("commons-validator:commons-validator:1.7")
	testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.amqp:spring-rabbit-test")
	testImplementation("it.ozimov:embedded-redis:0.7.3") {
		exclude(group = "org.slf4j", module = "slf4j-simple")
	}
	testImplementation("org.assertj:assertj-core:3.23.1")
	testImplementation("io.mockk:mockk:1.12.8")

	implementation("io.github.microutils:kotlin-logging-jvm:3.0.0")
}

dependencyManagement {
	imports {
//		mavenBom("com.vaadin:vaadin-bom:${property("vaadinVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		html.required.set(true)
		xml.required.set(true)
	}
}

tasks.war {
	archiveFileName.set("memer.war")
}

sonarqube{
	properties {
		property("sonar.dynamicAnalysis", "reuseReports")
		property("sonar.jacoco.reportPath", "$rootDir/target/jacoco.exe")
		property("sonar.language", "kotlin")
	}
}