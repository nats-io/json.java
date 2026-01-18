![NATS](src/main/javadoc/images/large-logo.png)

# JNATS JSON

This library is a JSON Parser built specifically for JNATS to avoid a 3rd party library dependency.

It has been extracted and repackaged from the JNATS library since it is also used by the [jwt.java](https://github.com/nats-io/jwt.java) library.

**Current Release**: 3.0.1 &nbsp; **Current Snapshot**: 3.0.2-SNAPSHOT

[![License Apache 2](https://img.shields.io/badge/License-Apache2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maven JDK 21](https://img.shields.io/maven-central/v/io.nats/jnats-json-jdk21)](https://mvnrepository.com/artifact/io.nats/jnats-json-jdk21)
[![Maven JDK 25](https://img.shields.io/maven-central/v/io.nats/jnats-json-jdk25)](https://mvnrepository.com/artifact/io.nats/jnats-json-jdk25)
[![Javadoc](http://javadoc.io/badge/io.nats/jnats-json.svg?branch=main)](http://javadoc.io/doc/io.nats/jnats-json?branch=main)
[![Coverage Status](https://coveralls.io/repos/github/nats-io/json.java/badge.svg?branch=main)](https://coveralls.io/github/nats-io/json.java?branch=main)
[![Build Main Badge](https://github.com/nats-io/json.java/actions/workflows/build-main.yml/badge.svg?event=push)](https://github.com/nats-io/json.java/actions/workflows/build-main.yml)
[![Release Badge](https://github.com/nats-io/json.java/actions/workflows/build-release.yml/badge.svg?event=release)](https://github.com/nats-io/json.java/actions/workflows/build-release.yml)

### JDK Version

This project uses Java 21 Language Level api, but builds with both Java 21 and Java 25, so creates two different artifacts. 
Both have the same group id `io.nats`, and the same version but have different artifact names. 

* The Java 21 artifact id is `jnats-json-jdk21`
* The Java 25 artifact id is `jnats-json-jdk25`

### Dependency Management

The NATS client is available in the Maven central repository, 
and can be imported as a standard dependency in your `build.gradle` or `pom.xml` file,
The examples shown use the jdk 21 version, to the jdk 25 version just change the artifact id. 

#### Gradle

```groovy
dependencies {
    implementation 'io.nats:jnats-json-jdk21:3.0.1'
}
```

If you need the latest and greatest before Maven central updates, you can use:

```groovy
repositories {
    mavenCentral()
    maven {
        url "https://repo1.maven.org/maven2/"
    }
}
```

If you need a snapshot version, you must add the url for the snapshots and change your dependency.

```groovy
repositories {
    mavenCentral()
    maven {
        url "https://central.sonatype.com/repository/maven-snapshots"
    }
}

dependencies {
   implementation 'io.nats:jnats-json-jdk21:3.0.2-SNAPSHOT'
}
```

#### Maven

```xml
<dependency>
    <groupId>io.nats</groupId>
    <artifactId>jnats-json-jdk21</artifactId>
    <version>3.0.1</version>
</dependency>
```

If you need the absolute latest, before it propagates to maven central, you can use the repository:

```xml
<repositories>
    <repository>
        <id>sonatype releases</id>
        <url>https://repo1.maven.org/maven2/</url>
        <releases>
           <enabled>true</enabled>
        </releases>
    </repository>
</repositories>
```

If you need a snapshot version, you must enable snapshots and change your dependency.

```xml
<repositories>
    <repository>
        <id>sonatype snapshots</id>
        <url>https://central.sonatype.com/repository/maven-snapshots</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>

<dependency>
    <groupId>io.nats</groupId>
    <artifactId>jnats-json-jdk21</artifactId>
    <version>3.0.2-SNAPSHOT</version>
</dependency>
```


## License

Unless otherwise noted, the NATS source files are distributed
under the Apache Version 2.0 license found in the LICENSE file.
